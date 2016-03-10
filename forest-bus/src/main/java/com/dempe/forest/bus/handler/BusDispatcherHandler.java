package com.dempe.forest.bus.handler;

import com.dempe.forest.bus.StrategyConfig;
import com.dempe.forest.client.Callback;
import com.dempe.forest.client.ha.DefaultClientService;
import com.dempe.forest.common.protocol.Request;
import com.dempe.forest.common.protocol.Response;
import com.google.common.collect.Maps;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.aeonbits.owner.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * User: Dempe
 * Date: 2016/1/29
 * Time: 17:42
 * To change this template use File | Settings | File Templates.
 */
public class BusDispatcherHandler extends ChannelHandlerAdapter {

    public static final Logger LOGGER = LoggerFactory.getLogger(BusDispatcherHandler.class);

    private final static Map<String, DefaultClientService> nameClientMap = Maps.newConcurrentMap();
    private static StrategyConfig strategyConfig = ConfigFactory.create(StrategyConfig.class);

    @Override
    public void channelRead(final ChannelHandlerContext ctx, Object msg) throws Exception {

        if (msg instanceof Request) {
            Request request = (Request) msg;
            LOGGER.info("dispatcher request = {}", request);
            DefaultClientService clientService = getClientServiceByName(request.getName());
            clientService.send(request, new Callback() {
                @Override
                public void onReceive(final Object message) {
                    if (message instanceof Response) {
                        ctx.executor().execute(new Runnable() {
                            @Override
                            public void run() {
                                ctx.writeAndFlush(message);
                            }
                        });
                    }

                }
            });
        }


    }

    /**
     * 根据节点名称获取对应的HAClientService
     * netty handler是串行，故无需考虑并发问题
     * HAClientService 会选择路由策略选择合适的业务进程，将消息透传
     *
     * @param name
     * @return
     * @throws Exception
     */
    private DefaultClientService getClientServiceByName(String name) throws Exception {
        DefaultClientService clientService = nameClientMap.get(name);
        if (clientService == null) {
            clientService = new DefaultClientService(name, strategyConfig.loadStrategy(), strategyConfig.period());
            nameClientMap.put(name, clientService);
        }
        return clientService;
    }


}
