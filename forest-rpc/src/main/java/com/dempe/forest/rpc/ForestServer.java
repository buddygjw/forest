package com.dempe.forest.rpc;

import com.dempe.forest.register.ForestNameService;
import com.dempe.forest.rpc.core.ServerContext;
import com.dempe.forest.rpc.core.ServerHandlerInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/3/15
 * Time: 13:40
 * To change this template use File | Settings | File Templates.
 */
public class ForestServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(BootServer.class);
    ApplicationContext context;
    private BootServer bootServer;
    private AppConfig config;
    private ForestNameService forestNameService;

    public ForestServer(AppConfig config, ApplicationContext context) {
        this.config = config;
        this.context = context;
        ServerHandlerInitializer channelInitializer = new ServerHandlerInitializer(new ServerContext(config, context));
        bootServer = new BootServer(channelInitializer);
    }


    public ForestServer registerNameService() throws Exception {
        forestNameService = new ForestNameService();
        forestNameService.start();
        forestNameService.register();
        return this;
    }


    public void start() throws IOException {
        bootServer.start();
    }

    public void stop() throws IOException {
        bootServer.stop();
    }

    public ForestServer stopWithJVMShutdown() {
        bootServer.stopWithJVMShutdown();
        return this;
    }
}
