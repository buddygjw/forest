package com.dempe.forest.client.ha;

import com.dempe.forest.client.Client;
import com.dempe.forest.client.FutureClient;
import com.dempe.forest.client.ReplyFuture;
import com.dempe.forest.common.protocol.Request;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/2/2
 * Time: 11:18
 * To change this template use File | Settings | File Templates.
 */
public class FutureClientService {

    private final static Logger LOGGER = LoggerFactory.getLogger(FutureClientService.class);

    private static HAForestClient haForestClient;

    public FutureClientService(String name) throws Exception {
        if (haForestClient == null) {
            synchronized (FutureClientService.class) {
                if (haForestClient == null) {
                    haForestClient = new HAForestClient(name);
                }
            }
        }
    }

    public ReplyFuture sendAndWait(Request request) throws Exception {
        FutureClient client = (FutureClient) haForestClient.getClient();
        if (client == null) {
            LOGGER.warn("no available node for request:{}", request);
            return null;
        }
        return client.sendRequest(request);
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
