package com.dempe.forest.bus.handler;

import com.dempe.forest.client.ha.FutureClientService;
import com.dempe.forest.common.protocol.Request;
import com.google.common.collect.Maps;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * netty handler是串行，故无需考虑并发问题
 * User: Dempe
 * Date: 2016/1/29
 * Time: 17:42
 * To change this template use File | Settings | File Templates.
 */
public class BusDispatcherHandler extends ChannelHandlerAdapter {

    public static final Logger LOGGER = LoggerFactory.getLogger(BusDispatcherHandler.class);

    private final static Map<String, FutureClientService> nameClientMap = Maps.newConcurrentMap();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (msg instanceof Request) {
            Request request = (Request) msg;
            LOGGER.info("dispatcher request = {}", request);
            FutureClientService clientService = getClientServiceByName(request.getName());
            clientService.sendAndWrite(ctx, request);
        }


    }

    /**
     * 根据节点名称获取对应的HAClientService
     * HAClientService 会选择路由策略选择合适的业务进程，将消息透传
     *
     * @param name
     * @return
     * @throws Exception
     */
    private FutureClientService getClientServiceByName(String name) throws Exception {
        FutureClientService clientService = nameClientMap.get(name);
        if (clientService == null) {
            clientService = new FutureClientService(name);
            nameClientMap.put(name, clientService);
        }
        return clientService;
    }


}
