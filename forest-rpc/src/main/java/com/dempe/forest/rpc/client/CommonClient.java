package com.dempe.forest.rpc.client;


import com.dempe.forest.rpc.handler.CompressHandler;
import com.dempe.forest.rpc.handler.PacketDecoder;
import com.dempe.forest.rpc.handler.PacketEncoder;
import com.dempe.forest.rpc.transport.protocol.PacketData;
import com.google.common.collect.Maps;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/2/2
 * Time: 11:02
 * To change this template use File | Settings | File Templates.
 */
public class CommonClient {

    protected static final Logger LOGGER = LoggerFactory.getLogger(CommonClient.class);

    protected Bootstrap b;

    protected ChannelFuture f;

    // TODO 改用channelPool
    protected Channel channel;

    protected EventLoopGroup group;
    protected Map<Long, Context> contextMap = Maps.newConcurrentMap();
    private DefaultEventExecutorGroup executorGroup;
    private String host;
    private int port;
    private long connectTimeout = 5000L;


    public CommonClient(String host, int port) {
        this.host = host;
        this.port = port;
        init();
    }

    private void init() {
        b = new Bootstrap();
        group = new NioEventLoopGroup(4);
        executorGroup = new DefaultEventExecutorGroup(4,
                new DefaultThreadFactory("worker group"));
        b.group(group)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        initClientChannel(ch);
                    }
                });

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    closeSync();
                } catch (IOException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }));
        connect(host, port);
    }

    public void initClientChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("RequestEncoder", new PacketEncoder())
                .addLast("ResponseDecoder", new PacketDecoder(500000))
                .addLast("CompressHandler", new CompressHandler())
                .addLast("ClientHandler", new ChannelHandlerAdapter() {
                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        long id = 0;
                        PacketData packetData = (PacketData) msg;
                        id = packetData.getRpcMeta().getCorrelationId();

                        Context context = contextMap.remove(id);
                        if (context == null) {
                            LOGGER.debug("messageID:{}, take Context null", id);
                            return;
                        }
                        context.cb.onReceive(packetData);
                    }
                });
    }

    public void connect(final String host, final int port) {
        try {
            f = b.connect(host, port).sync();
            channel = f.channel();
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public void closeSync() throws IOException {
        try {
            f.channel().close().sync();
            group.shutdownGracefully();

        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public void close() {
        if (isConnected()) {
            f.channel().close();
        }
    }

    public boolean reconnect() throws Exception {
        close();
        LOGGER.info("start reconnect to server.");
        f = b.connect(host, port);// 异步建立长连接
        f.get(connectTimeout, TimeUnit.MILLISECONDS); // 最多等待5秒，如连接建立成功立即返回
        LOGGER.info("end reconnect to server result:" + isConnected());
        return isConnected();
    }

    public boolean isConnected() {
        return f != null && f.channel().isActive();
    }

    public void writeAndFlush(Object request) throws Exception {
        if (!isConnected()) {
            reconnect();
        }
        f.channel().writeAndFlush(request);
    }

    public static class Context {
        final PacketData packetData;
        public final Callback cb;
        private final long id;

        Context(long id, PacketData packetData, Callback cb) {
            this.id = id;
            this.cb = cb;
            this.packetData = packetData;
        }
    }


}
