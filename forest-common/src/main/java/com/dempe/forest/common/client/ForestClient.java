package com.dempe.forest.common.client;

import com.dempe.forest.common.NodeDetails;
import com.dempe.forest.common.protocol.Request;
import com.dempe.forest.common.protocol.Response;
import com.dempe.forest.common.protocol.codec.DefaultEncoder;
import com.dempe.forest.common.protocol.codec.ResponseDecoder;
import com.dempe.forest.common.client.core.ClientHandler;
import com.dempe.forest.common.client.core.ReplyFuture;
import com.dempe.forest.common.client.core.ReplyWaitQueue;
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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/2/2
 * Time: 11:02
 * To change this template use File | Settings | File Templates.
 */
public class ForestClient implements Client {

    private static final Logger LOGGER = LoggerFactory.getLogger(ForestClient.class);

    protected Bootstrap b;

    protected ChannelFuture f;

    protected Channel channel;

    protected EventLoopGroup group;

    private DefaultEventExecutorGroup executorGroup;

    private String host;

    private int port;

    private long connectTimeout = 5000L;

    // 消息id生成器，消息id用户标识消息，标识sendAndWait方法的返回
    private static AtomicInteger idMaker = new AtomicInteger(0);

    // 消息返回会被装入到队列里
    private ReplyWaitQueue replyQueue = new ReplyWaitQueue();


    public ForestClient(NodeDetails nodeDetails) {
        this(nodeDetails.getIp(), nodeDetails.getPort());

    }

    public ForestClient(String host, int port) {
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
        pipeline.addLast("RequestEncoder", new DefaultEncoder())
                .addLast("ResponseDecoder", new ResponseDecoder())
                .addLast("ClientHandler", new ClientHandler());
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

    public void send(Request request) throws Exception {
        if(!isConnected()){
            reconnect();
        }
        f.channel().writeAndFlush(request);
    }

    /**
     * 仅仅发现消息，不关心返回
     * @param request 请求消息
     */
    @Override
    public void sendOnly(Request request) throws Exception {
        send(request);
    }

    /**
     * 发送消息，等消息返回
     * @param request 请求消息
     * @return
     */
    @Override
    public Response sendAndWait(Request request) throws Exception {
        int id = request.getSeqId();
        if (id == 0) {
            id = idMaker.incrementAndGet();
            request.setSeqId(id);
        }
        ReplyFuture future = new ReplyFuture(id);
        replyQueue.add(future);
        send(request);
        return future.getReply();


    }

    /**
     * 发送消息并等待返回
     * @param request 请求消息
     * @param timeOut 请求超时时间
     * @return
     */
    @Override
    public Response sendAndWait(Request request, long timeOut) throws Exception {
        int id = request.getSeqId();
        if (id == 0) {
            id = idMaker.incrementAndGet();
            request.setSeqId(id);
        }
        ReplyFuture future = new ReplyFuture(id);
        replyQueue.add(future);
        future.setReadTimeoutMillis(timeOut);
        send(request);
        return future.getReply();

    }

    /**
     * 发送消息，并将返回消息写到ctx中
     *
     * @param ctx 上下文，用于将response写入对应的channel中
     * @param request 请求消息
     */
    public void sendForward(ChannelHandlerContext ctx, Request request) throws Exception {
        int id = request.getSeqId();
        if (id == 0) {
            id = idMaker.incrementAndGet();
            request.setSeqId(id);
        }
        ReplyFuture future = new ReplyFuture(ctx, id);
        replyQueue.add(future);
        send(request);
    }
}
