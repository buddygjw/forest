package com.dempe.forest.service.handler;

import com.dempe.forest.common.NodeDetails;
import com.dempe.forest.name.ForestNameService;
import com.dempe.forest.name.NameService;
import com.dempe.forest.service.proto.Message;
import org.apache.curator.x.discovery.ServiceInstance;

import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/1/29
 * Time: 17:42
 * To change this template use File | Settings | File Templates.
 */
public class Dispatcher {

    private NameService nameService;

    public Dispatcher() throws Exception {
        nameService = new ForestNameService();
    }


    public void dispatcher(String key, String name, Message message) throws Exception {


        Collection<ServiceInstance<NodeDetails>> serviceInstances = nameService.listByName(name);
        if (serviceInstances.size() < 1) {
            return;
        }
        ServiceInstance<NodeDetails> instance = serviceInstances.iterator().next();
        send();


    }

    private void send() {

    }
}
