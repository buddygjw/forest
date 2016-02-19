package com.dempe.forest.bus;

import com.dempe.forest.bus.handler.BusDispatcherHandler;
import com.dempe.forest.common.AppConfig;
import com.dempe.forest.common.protocol.codec.DefaultEncoder;
import com.dempe.forest.common.protocol.codec.RequestDecoder;
import com.dempe.forest.common.server.BootServer;
import com.dempe.forest.common.uitls.DefConfigFactory;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


/**
 * 服务总线启动类
 * User: Dempe
 * Date: 2016/1/29
 * Time: 17:40
 * To change this template use File | Settings | File Templates.
 */
public class ForestBusServer {


    public static void main(String[] args) throws Exception {
        // 启动spring容器
        final ApplicationContext context = new AnnotationConfigApplicationContext(ForestBusServer.class);
        // 生成开发环境的配置
        final AppConfig devConfig = DefConfigFactory.createDEVConfig();
        ChannelInitializer<SocketChannel> channelInitializer = new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast("RequestDecoder", new RequestDecoder())
                        .addLast("DefaultEncoder", new DefaultEncoder())
                        .addLast("BusDispatcherHandler", new BusDispatcherHandler());
            }
        };
        BootServer server = new BootServer(devConfig, context, channelInitializer);
        server.registerNameService()
                .start();
    }


}
