package com.dempe.forest.leaf.handler;

import com.dempe.forest.common.client.ha.HAClientService;
import com.dempe.forest.common.proto.Request;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/1/29
 * Time: 17:42
 * To change this template use File | Settings | File Templates.
 */
public class Dispatcher {

    private final static Map<String, HAClientService> nameClientMap = Maps.newConcurrentMap();

    public void dispatcher(Request request) throws Exception {
        HAClientService clientService = getClientServiceByName(request.getName());
        clientService.sendAndWait(request);
    }

    private HAClientService getClientServiceByName(String name) throws Exception {
        HAClientService clientService = nameClientMap.get(name);
        if (clientService == null) {
            clientService = new HAClientService(name);
            nameClientMap.put(name, clientService);
        }
        return clientService;
    }

}
