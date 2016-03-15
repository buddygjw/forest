package com.dempe.forest.rpc.core;


import com.dempe.forest.rpc.handler.PacketDecoder;
import com.dempe.forest.rpc.handler.PacketEncoder;
import com.dempe.forest.rpc.handler.UnCompressHandler;
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
        pipeline.addLast("PacketDecoder", new PacketDecoder(300 * 1000))
                .addLast("PacketEncoder", new PacketEncoder())
                .addLast("UnCompressHandler", new UnCompressHandler())
                .addLast("ProcessorHandler", new ProcessorHandler(context));
    }

}
