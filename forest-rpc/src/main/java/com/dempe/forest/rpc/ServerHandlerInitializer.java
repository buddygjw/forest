package com.dempe.forest.rpc;


import com.dempe.forest.rpc.handler.PacketDecoder;
import com.dempe.forest.rpc.handler.PacketEncoder;
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


    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("RequestDecoder", new PacketDecoder(300 * 1000))
                .addLast("ResponseEncoder", new PacketEncoder())
                .addLast("ProcessorHandler", new ProcessorHandler());
    }
}
