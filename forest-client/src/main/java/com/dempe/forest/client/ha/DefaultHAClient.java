package com.dempe.forest.client.ha;

import com.dempe.forest.client.CallbackClient;
import com.dempe.forest.client.Client;
import com.dempe.forest.common.NodeDetails;
import com.dempe.forest.common.cluster.HAProxy;
import com.dempe.forest.common.cluster.ProxyHandler;
import com.dempe.forest.common.cluster.accessctr.AccessPolicy;
import com.dempe.forest.register.ForestNameService;
import com.google.common.collect.Lists;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.ServiceCacheListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/2/2
 * Time: 11:01
 * To change this template use File | Settings | File Templates.
 */
public class DefaultHAClient extends HAProxy<Client> {

    private final static Logger LOGGER = LoggerFactory.getLogger(DefaultHAClient.class);

    private ForestNameService forestNameService;

    private String name;

    /**
     * @param strategy
     * @param period
     */
    public DefaultHAClient(String name, Strategy strategy, long period) throws Exception {
        super(strategy, name, period);
    }

    public DefaultHAClient(String name) throws Exception {
        this(name, Strategy.DEFAULT, 1000L);
    }


    @Override
    public List<NodeDetails> initServerInstanceList(String name) throws Exception {
        this.name = name;
        forestNameService = new ForestNameService();
        forestNameService.addServiceCacheListener(new ForestServiceCacheListener());
        forestNameService.start();
        List<NodeDetails> list = Lists.newArrayList();
        Collection<ServiceInstance<NodeDetails>> serviceInstances = forestNameService.listByName(name);
        for (ServiceInstance<NodeDetails> serviceInstance : serviceInstances) {
            NodeDetails serverInstance = new NodeDetails();
            serverInstance.setIp(serviceInstance.getAddress());
            serverInstance.setPort(serviceInstance.getPort());
            list.add(serverInstance);
        }
        return list;
    }


    @Override
    public Client createClient(NodeDetails serverInstance) throws Exception {
        /**
         *1s accessPolicy=5次发送失败则会自动切换client
         */
        AccessPolicy policy = new AccessPolicy(10, 1 * 1000, 5 * 1000 * 60, true);
        CallbackClient callbackClient = new CallbackClient(serverInstance.getIp(), serverInstance.getPort());
        Client client = (Client) ProxyHandler.getProxyInstance(callbackClient, this, policy);
        return client;
    }

    class ForestServiceCacheListener implements ServiceCacheListener {

        @Override
        public void cacheChanged() {
            LOGGER.info("cache changed");
        }

        @Override
        public void stateChanged(CuratorFramework client, ConnectionState newState) {

        }
    }


}

