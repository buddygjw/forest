package com.dempe.forest.bus;

import com.dempe.forest.register.ForestNameService;
import com.dempe.forest.rpc.BootServer;
import com.dempe.forest.rpc.handler.PacketDecoder;
import com.dempe.forest.rpc.handler.PacketEncoder;
import com.dempe.forest.rpc.handler.UnCompressHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/3/15
 * Time: 15:13
 * To change this template use File | Settings | File Templates.
 */
public class BusServer {

    private BootServer bootServer;

    private ForestNameService forestNameService;

    public BusServer() {
        this.bootServer = new BootServer(new ChannelInitializer() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast("PacketDecoder", new PacketDecoder(300 * 1000))
                        .addLast("PacketEncoder", new PacketEncoder())
                        .addLast("UnCompressHandler", new UnCompressHandler())
                        .addLast("ProcessorHandler", new DispatcherHandler());
            }
        });
    }


    public void start() throws IOException {
        bootServer.start();
    }

    public BusServer registerNameService() throws Exception {
        forestNameService = new ForestNameService();
        forestNameService.start();
        forestNameService.register();
        return this;
    }


    public BusServer stopWithJVMShutdown() {
        bootServer.stopWithJVMShutdown();
        return this;
    }

    public static void main(String[] args) throws Exception {
        new BusServer().registerNameService()
                .start();
    }
}
