package com.dempe.forest.bus.handler;

import com.dempe.forest.client.Callback;
import com.dempe.forest.client.ha.CallbackClientService;
import com.dempe.forest.common.protocol.Request;
import com.dempe.forest.common.protocol.Response;
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

    private final static Map<String, CallbackClientService> nameClientMap = Maps.newConcurrentMap();

    @Override
    public void channelRead(final ChannelHandlerContext ctx, Object msg) throws Exception {

        if (msg instanceof Request) {
            Request request = (Request) msg;
            LOGGER.info("dispatcher request = {}", request);
            CallbackClientService clientService = getClientServiceByName(request.getName());
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
     * HAClientService 会选择路由策略选择合适的业务进程，将消息透传
     *
     * @param name
     * @return
     * @throws Exception
     */
    private CallbackClientService getClientServiceByName(String name) throws Exception {
        CallbackClientService clientService = nameClientMap.get(name);
        if (clientService == null) {
            clientService = new CallbackClientService(name);
            nameClientMap.put(name, clientService);
        }
        return clientService;
    }


}
