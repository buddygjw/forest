package com.dempe.forest.rpc.client;


import com.dempe.forest.rpc.client.proxy.ObjectProxy;
import com.dempe.forest.rpc.core.BetterExecutorService;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.logging.Slf4JLoggerFactory;

import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class RPCClient {
    static {
        InternalLoggerFactory.setDefaultFactory(new Slf4JLoggerFactory());
    }

    private Config conf;

    private BetterExecutorService threadPool;

    EventLoopGroup eventLoopGroup;

    static RPCClient instance;

    public RPCClient() {
        conf = ConfigFactory.load();
        eventLoopGroup = new NioEventLoopGroup(conf.getInt("client.ioThreadNum"));

        LinkedBlockingDeque<Runnable> linkedBlockingDeque = new LinkedBlockingDeque<Runnable>();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 600L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
        threadPool = new BetterExecutorService(linkedBlockingDeque, executor, "Client async thread pool", getConfig().getInt("client.asyncThreadPoolSize"));

    }

    public static RPCClient getInstance() {
        if (instance == null) {
            synchronized (RPCClient.class) {
                if (instance == null) {
                    instance = new RPCClient();
                }
            }
        }
        return instance;
    }


    public static <T> ObjProxyBuilder<T> proxyBuilder(Class<T> clazz) {
        return new ObjProxyBuilder<T>(clazz);
    }

    public static class ObjProxyBuilder<T> {
        private Class<T> clazz;
        private String host;
        private int port;
        private List<InetSocketAddress> serverNodes;
        private boolean enableRegistry;

        public ObjProxyBuilder(Class<T> clazz) {
            this.clazz = clazz;
        }

        public ObjProxyBuilder<T> withServerNode(String host, int port) {
            this.host = host;
            this.port = port;
            return this;
        }

        public ObjProxyBuilder<T> withServerNodes(List<InetSocketAddress> serverNodes) {
            this.serverNodes = serverNodes;
            return this;
        }

        public ObjProxyBuilder<T> enableRegistry() {
            this.enableRegistry = true;
            return this;
        }

        public T build() {
            ArrayList<InetSocketAddress> serverNodes = new ArrayList<InetSocketAddress>();
            serverNodes.add(new InetSocketAddress(host, port));
            ObjectProxy<T> h = new ObjectProxy<T>(host, port);
            h.setClazz(clazz);
            T t = (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, h);
            return t;
        }


    }


    public Config getConfig() {
        return conf;
    }

    public EventLoopGroup getEventLoopGroup() {
        return eventLoopGroup;
    }

    public void shutdown() {
        eventLoopGroup.shutdownGracefully();
        threadPool.shutdown();
    }
}


