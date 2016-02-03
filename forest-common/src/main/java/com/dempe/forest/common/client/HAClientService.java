package com.dempe.forest.common.client;

import com.dempe.forest.common.proto.Request;
import com.dempe.forest.common.proto.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/2/2
 * Time: 11:18
 * To change this template use File | Settings | File Templates.
 */
public class HAClientService {

    private final static Logger LOGGER = LoggerFactory.getLogger(HAClientService.class);

    private static HAForestClient haForestClient;

    public HAClientService(String name) throws Exception {
        if (haForestClient == null) {
            synchronized (HAClientService.class) {
                if (haForestClient == null) {
                    haForestClient = new HAForestClient(name);
                }
            }
        }
    }

    public void sendOnly(Request request) {
        haForestClient.getClient().sendOnly(request);
    }

    public Response sendAndWait(Request request) {
        return haForestClient.getClient().sendAndWait(request);
    }

    public Response sendAndWait(Request request, long timeOut) {
        return haForestClient.getClient().sendAndWait(request, timeOut);
    }


}
