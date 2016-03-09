package com.dempe.forest.client.ha;

import com.dempe.forest.client.Callback;
import com.dempe.forest.client.Client;
import com.dempe.forest.common.protocol.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/3/9
 * Time: 16:21
 * To change this template use File | Settings | File Templates.
 */
public class CallbackClientService {

    private final static Logger LOGGER = LoggerFactory.getLogger(CallbackClientService.class);

    private static HACallbackClient haCallbackClient;

    public CallbackClientService(String name) throws Exception {
        if (haCallbackClient == null) {
            synchronized (CallbackClientService.class) {
                if (haCallbackClient == null) {
                    haCallbackClient = new HACallbackClient(name);
                }
            }
        }
    }

    public Callback send(Request request, Callback callback) throws Exception {
        Client client = haCallbackClient.getClient();
        if (client == null) {
            LOGGER.warn("no available node for request:{}", request);
            return null;
        }
        return client.send(request, callback);
    }

}
