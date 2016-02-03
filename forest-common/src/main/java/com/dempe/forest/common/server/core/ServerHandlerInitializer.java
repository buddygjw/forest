package com.dempe.forest.common.server.core;


import com.dempe.forest.common.proto.codec.DefaultEncoder;
import com.dempe.forest.common.proto.codec.RequestDecoder;
import com.dempe.forest.common.server.ServerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/11/3
 * Time: 14:58
 * To change this template use File | Settings | File Templates.
 */
public class ServerHandlerInitializer extends ChannelInitializer<SocketChannel> {

    private ServerContext context;

    public ServerHandlerInitializer(ServerContext context) {
        this.context = context;
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("RequestDecoder", new RequestDecoder())
                .addLast("ResponseEncoder", new DefaultEncoder())
                .addLast("dispatcher", new ProcessorHandler(context))
        ;
    }
}
