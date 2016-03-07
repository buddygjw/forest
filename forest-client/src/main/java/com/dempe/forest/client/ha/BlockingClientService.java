package com.dempe.forest.client.ha;

import com.dempe.forest.client.BlockingClient;
import com.dempe.forest.client.BlockingClientFactory;
import com.dempe.forest.client.Client;
import com.dempe.forest.common.protocol.Request;
import com.dempe.forest.common.protocol.Response;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/3/7
 * Time: 13:00
 * To change this template use File | Settings | File Templates.
 */
public class BlockingClientService {
    private final static Logger LOGGER = LoggerFactory.getLogger(BlockingClientService.class);

    private static HAForestClient haForestClient;

    public BlockingClientService(String name) throws Exception {
        if (haForestClient == null) {
            synchronized (FutureClientService.class) {
                if (haForestClient == null) {
                    haForestClient = new HAForestClient(new BlockingClientFactory(), name);
                }
            }
        }
    }

    public Response sendAndWait(Request request) throws Exception {
        BlockingClient client = (BlockingClient) haForestClient.getClient();
        if (client == null) {
            LOGGER.warn("no available node for request:{}", request);
            return null;
        }
        return client.sendAndWaitRequest(request);
    }

    public Response sendAndWait(Request request, long timeOut) throws Exception {
        BlockingClient client = (BlockingClient) haForestClient.getClient();
        if (client == null) {
            LOGGER.warn("no available node for request:{}", request);
            return null;
        }
        return client.sendAndWaitRequest(request, timeOut);
    }

    public void sendAndWrite(ChannelHandlerContext ctx, Request request) throws Exception {
        Client client = haForestClient.getClient();
        if (client == null) {
            LOGGER.warn("no available node for request:{}", request);
            return;
        }
        client.sendForward(ctx, request);
    }


}
