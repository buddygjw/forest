/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dempe.forest.rpc.handler;


import com.dempe.forest.rpc.transport.protocol.HeadMeta;
import com.dempe.forest.rpc.transport.protocol.PacketData;
import com.dempe.forest.rpc.transport.protocol.ProtocolConstant;
import com.google.common.collect.Maps;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Decode RpcDataPackage from received bytes
 *
 * @author xiemalin
 * @see
 * @since 1.0
 */
public class PacketDecoder extends ByteToMessageDecoder {

    /**
     * Default chunk package wait time out check interval
     */
    private static final int DEFAULT_CLEANUP_INTERVAL = 1000;

    private static Logger LOG = Logger.getLogger(PacketDecoder.class.getName());

    private static final Map<Long, PacketData> tempTrunkPackages = new ConcurrentHashMap<Long, PacketData>();

    private static final AtomicBoolean startChunkPackageCleanUp = new AtomicBoolean(false);

    private ExecutorService es;

    private boolean stopChunkPackageTimeoutClean = false;


    /**
     * @param chunkPackageTimeout
     */
    public PacketDecoder(final int chunkPackageTimeout) {
        if (chunkPackageTimeout <= 0) {
            return;
        }

        // only start once OK
        if (startChunkPackageCleanUp.compareAndSet(false, true)) {
            es = Executors.newSingleThreadExecutor();
            es.execute(new Runnable() {

                public void run() {
                    while (!stopChunkPackageTimeoutClean) {

                        if (!tempTrunkPackages.isEmpty()) {

                            Map<Long, PacketData> currentCheckPackage;
                            currentCheckPackage = Maps.newHashMap(tempTrunkPackages);

                            Iterator<Entry<Long, PacketData>> iter = currentCheckPackage.entrySet().iterator();
                            while (iter.hasNext()) {
                                Entry<Long, PacketData> entry = iter.next();

                                if (entry.getValue().getTimeStamp() + chunkPackageTimeout > System.currentTimeMillis()) {
                                    // get time out chunk package, do clean action
                                    tempTrunkPackages.remove(entry.getValue());
                                    LOG.log(Level.SEVERE, "Found chunk package time out long than " + chunkPackageTimeout
                                            + "(ms) will clean up correlationId:"
                                            + entry.getValue().getRpcMeta().getCorrelationId());
                                }
                            }
                        }

                        try {
                            Thread.sleep(DEFAULT_CLEANUP_INTERVAL);
                        } catch (Exception e) {
                            LOG.log(Level.SEVERE, e.getMessage(), e);
                        }

                    }

                }
            });
        }

    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in,
                          List<Object> out) throws Exception {
        Object decoded = decode(ctx, in);
        if (decoded != null) {
            out.add(decoded);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.jboss.netty.handler.codec.frame.FrameDecoder#decode(org.jboss.netty
     * .channel.ChannelHandlerContext, org.jboss.netty.channel.Channel,
     * org.jboss.netty.buffer.ChannelBuffer)
     */
    protected Object decode(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {

        // Make sure if the length field was received.
        if (buf.readableBytes() < HeadMeta.SIZE) {
            // The length field was not received yet - return null.
            // This method will be invoked again when more packets are
            // received and appended to the buffer.
            return null;
        }

        // The length field is in the buffer.

        // Mark the current buffer position before reading the length field
        // because the whole frame might not be in the buffer yet.
        // We will reset the buffer position to the marked position if
        // there's not enough bytes in the buffer.
        buf.markReaderIndex();

        // Read the RPC head
        long rpcMessageDecoderStart = System.nanoTime();
        ByteBuffer buffer = buf.nioBuffer(buf.readerIndex(), HeadMeta.SIZE);

        buffer.order(ByteOrder.LITTLE_ENDIAN);
        byte[] bytes = new byte[HeadMeta.SIZE];
        buffer.get(bytes);

        HeadMeta headMeta = new HeadMeta();
        headMeta.read(bytes);

        // get total message size
        int messageSize = headMeta.getMsgSize() + HeadMeta.SIZE;

        // Make sure if there's enough bytes in the buffer.
        if (buf.readableBytes() < messageSize) {
            // The whole bytes were not received yet - return null.
            // This method will be invoked again when more packets are
            // received and appended to the buffer.

            // Reset to the marked position to read the length field again
            // next time.
            buf.resetReaderIndex();

            return null;
        }

        // check magic code
        String magicCode = headMeta.getSignAsString();
        if (!ProtocolConstant.MAGIC_CODE.equals(magicCode)) {
            throw new Exception("Error magic code:" + magicCode);
        }
        // There's enough bytes in the buffer. Read it.
        byte[] totalBytes = new byte[messageSize];
        buf.readBytes(totalBytes, 0, messageSize);

        PacketData rpcDataPackage = new PacketData();
        rpcDataPackage.setTimeStamp(System.currentTimeMillis());
        rpcDataPackage.read(totalBytes);

        // check if a chunk package
        if (rpcDataPackage.isChunkPackage()) {

            Long chunkStreamId = rpcDataPackage.getChunkStreamId();

            PacketData chunkDataPackage = tempTrunkPackages.get(chunkStreamId);
            if (chunkDataPackage == null) {
                chunkDataPackage = rpcDataPackage;
                tempTrunkPackages.put(chunkStreamId, rpcDataPackage);
            } else {
                chunkDataPackage.mergeData(rpcDataPackage.getData());
            }

            if (rpcDataPackage.isFinalPackage()) {
                chunkDataPackage.chunkInfo(chunkStreamId, -1);
                tempTrunkPackages.remove(chunkStreamId);

                return chunkDataPackage;
            }

            return null;
        }

        long rpcMessageDecoderEnd = System.nanoTime();
        LOG.log(Level.FINE, "[profiling] nshead decode cost : " + (rpcMessageDecoderEnd - rpcMessageDecoderStart)
                / 1000);

        return rpcDataPackage;
    }

    /**
     *
     */
    public void close() {
        stopChunkPackageTimeoutClean = true;
        if (es != null) {
            es.shutdown();
        }

    }


}
