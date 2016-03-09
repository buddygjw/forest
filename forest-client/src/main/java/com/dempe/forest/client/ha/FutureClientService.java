package com.dempe.forest.client.ha;

import com.dempe.forest.client.Future;
import com.dempe.forest.client.Promise;
import com.dempe.forest.common.cluster.HAProxy;
import com.dempe.forest.common.protocol.Request;
import com.dempe.forest.common.protocol.Response;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/3/9
 * Time: 16:21
 * To change this template use File | Settings | File Templates.
 */
public class FutureClientService {

    private static CallbackClientService callbackClientService;

    public FutureClientService(String name) throws Exception {
        callbackClientService = new CallbackClientService(name);
    }

    public FutureClientService(String name, HAProxy.Strategy strategy, long period) throws Exception {
        callbackClientService = new CallbackClientService(name, strategy, period);
    }

    public Future<Response> send(Request request) throws Exception {
        Promise<Response> future = new Promise<Response>();
        callbackClientService.send(request, future);
        return future;
    }

}
