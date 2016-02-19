package com.dempe.forest.common.server;


import com.dempe.forest.common.AppConfig;
import com.dempe.forest.common.name.ForestNameService;
import com.dempe.forest.common.server.core.ServerHandlerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.io.IOException;

/**
 * 框架启动基类
 * User: Dempe
 * Date: 2015/10/15
 * Time: 16:41
 * To change this template use File | Settings | File Templates.
 */

public class BootServer implements Server {

    private static final Logger LOGGER = LoggerFactory.getLogger(BootServer.class);

    ApplicationContext context;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private ServerBootstrap b;
    private DefaultEventExecutorGroup executorGroup;
    private AppConfig config;
    private ServerContext Servercontext;
    private ForestNameService forestNameService;
    private ChannelInitializer channelInitializer;


    public BootServer(AppConfig config, ApplicationContext context) {
        this.config = config;
        this.context = context;
        Servercontext = new ServerContext(config, context);
        init();
    }

    public BootServer(AppConfig config, ApplicationContext context, ChannelInitializer channelInitializer) {
        this.config = config;
        this.context = context;
        Servercontext = new ServerContext(config, context);
        this.channelInitializer = channelInitializer;
        init();
    }


    public void init() {
        executorGroup = new DefaultEventExecutorGroup(4, new DefaultThreadFactory("decode-worker-thread-pool"));
        if (channelInitializer == null) {
            channelInitializer = new ServerHandlerInitializer(Servercontext);
        }
        init(channelInitializer);

    }

    public BootServer registerNameService() throws Exception {
        forestNameService = new ForestNameService();
        forestNameService.start();
        forestNameService.register();
        return this;
    }


    public void start() throws IOException {
        try {
            ChannelFuture f = b.bind(config.port()).sync();
            LOGGER.info("server start:{}", config.port());
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            stop();
        }
    }

    private void init(ChannelInitializer channelInitializer) {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 2);
        b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, config.tcpNoDelay())
                .option(ChannelOption.SO_KEEPALIVE, config.soKeepAlive())
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(channelInitializer);
    }


    public void stop() throws IOException {
        if (bossGroup != null)
            bossGroup.shutdownGracefully();
        if (workerGroup != null)
            workerGroup.shutdownGracefully();
        if (forestNameService != null) {
            forestNameService.close();
        }
    }

    public BootServer stopWithJVMShutdown() {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    stop();
                } catch (IOException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }));
        return this;
    }

}
