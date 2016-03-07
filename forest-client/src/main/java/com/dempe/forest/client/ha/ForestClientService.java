package com.dempe.forest.client.ha;

import com.dempe.forest.client.Client;
import com.dempe.forest.client.ForestClient;
import com.dempe.forest.client.ReplyFuture;
import com.dempe.forest.common.protocol.Request;
import com.dempe.forest.common.protocol.Response;
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
public class ForestClientService {

    private final static Logger LOGGER = LoggerFactory.getLogger(ForestClientService.class);

    private static HAForestClient haForestClient;

    public ForestClientService(String name) throws Exception {
        if (haForestClient == null) {
            synchronized (ForestClientService.class) {
                if (haForestClient == null) {
                    haForestClient = new HAForestClient(name);
                }
            }
        }
    }

    public ReplyFuture sendAndWait(Request request) throws Exception {
        ForestClient client = (ForestClient) haForestClient.getClient();
        if (client == null) {
            LOGGER.warn("no available node for request:{}", request);
            return null;
        }
        return client.sendRequest(request);
    }

    public Response sendAndWaitRequest(Request request) throws Exception {
        ForestClient client = (ForestClient) haForestClient.getClient();
        if (client == null) {
            LOGGER.warn("no available node for request:{}", request);
            return null;
        }
        return client.sendAndWaitRequest(request);
    }

    public Response sendAndWaitRequest(Request request, long timeOut) throws Exception {
        ForestClient client = (ForestClient) haForestClient.getClient();
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
