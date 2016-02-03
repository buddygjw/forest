package com.dempe.forest.leaf.handler;

import com.dempe.forest.common.client.ha.HAClientService;
import com.dempe.forest.common.proto.Request;
import com.google.common.collect.Maps;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/1/29
 * Time: 17:42
 * To change this template use File | Settings | File Templates.
 */
public class BusDispatcherHandler extends ChannelHandlerAdapter {

    public static final Logger LOGGER = LoggerFactory.getLogger(BusDispatcherHandler.class);

    // 业务逻辑线程池(业务逻辑最好跟netty io线程分开处理，线程切换虽会带来一定的性能损耗，但可以防止业务逻辑阻塞io线程)
    private final static ExecutorService workerThreadService = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors(), new DefaultThreadFactory("workerThread"));

    private final static Map<String, HAClientService> nameClientMap = Maps.newConcurrentMap();


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (msg instanceof Request) {
            Request request = (Request) msg;
            LOGGER.info("dispatcher request = {}", request);
            HAClientService clientService = getClientServiceByName(request.getName());
            clientService.sendAndWrite(ctx, request);
        }


    }

    private HAClientService getClientServiceByName(String name) throws Exception {
        HAClientService clientService = nameClientMap.get(name);
        if (clientService == null) {
            clientService = new HAClientService(name);
            nameClientMap.put(name, clientService);
        }
        return clientService;
    }


}
