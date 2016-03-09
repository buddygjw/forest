package com.dempe.forest.client;


import com.dempe.forest.common.protocol.Request;
import com.dempe.forest.common.protocol.Response;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/3/9
 * Time: 11:54
 * To change this template use File | Settings | File Templates.
 */
public class BlockingClient {

    private FutureClient futureClient;


    public BlockingClient(String host, int port) {
        futureClient = new FutureClient(host, port);
    }

    public Response send(Request request) throws Exception {
        Future<Response> future = futureClient.send(request);
        return future.await();
    }

    public void sendOnly(Request request) throws Exception {
        futureClient.sendOnly(request);
    }
}
