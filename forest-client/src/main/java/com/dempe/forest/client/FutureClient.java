package com.dempe.forest.client;


import com.dempe.forest.common.protocol.Request;
import com.dempe.forest.common.protocol.Response;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/3/9
 * Time: 10:51
 * To change this template use File | Settings | File Templates.
 */
public class FutureClient {

    private CallbackClient client;

    public FutureClient(String host, int port) {
        client = new CallbackClient(host, port);
    }

    public Future<Response> send(Request request) throws Exception {
        Promise<Response> future = new Promise<Response>();
        client.send(request, future);
        return future;
    }

    public void sendOnly(Request request) throws Exception {
        client.sendOnly(request);
    }
}
