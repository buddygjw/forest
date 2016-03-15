package com.dempe.forest.leaf.simulator;

import com.dempe.forest.register.ForestNameService;
import com.dempe.forest.register.NodeDetails;
import org.apache.curator.x.discovery.ServiceInstance;

import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/2/3
 * Time: 13:23
 * To change this template use File | Settings | File Templates.
 */
public class NameServiceSimulator {
    public static void main(String[] args) throws Exception {
        ForestNameService nameService = new ForestNameService();
        nameService.start();
        List<Collection<ServiceInstance<NodeDetails>>> list = nameService.list();
        for (Collection<ServiceInstance<NodeDetails>> serviceInstances : list) {
            for (ServiceInstance<NodeDetails> serviceInstance : serviceInstances) {
                System.out.println(serviceInstance.getName());
            }
        }
    }
}
