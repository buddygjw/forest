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


import com.dempe.forest.rpc.transport.compress.Compress;
import com.dempe.forest.rpc.transport.compress.GZipCompress;
import com.dempe.forest.rpc.transport.compress.SnappyCompress;
import com.dempe.forest.rpc.transport.protocol.PacketData;
import com.dempe.forest.rpc.transport.protocol.RpcMeta;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * Do data compress handler
 *
 * @author xiemalin
 * @since 1.4
 */
@Sharable
public class UnCompressHandler extends MessageToMessageDecoder<PacketData> {

    @Override
    protected void decode(ChannelHandlerContext ctx, PacketData msg, List<Object> out) throws Exception {

        // if select compress type should do compress here
        PacketData dataPackage = (PacketData) msg;

        try {
            // check if do compress
            Integer compressType = dataPackage.getRpcMeta().getCompressType();
            Compress compress = null;
            if (compressType == RpcMeta.COMPRESS_GZIP) {
                compress = new GZipCompress();
            } else if (compressType == RpcMeta.COMPRESS_SNAPPY) {
                compress = new SnappyCompress();
            }

            if (compress != null) {
                byte[] data = dataPackage.getData();
                data = compress.unCompress(data);
                dataPackage.data(data);
            }
            out.add(dataPackage);
        } catch (Exception e) {
            // TODO
            throw new Exception("");
        }

    }

}
