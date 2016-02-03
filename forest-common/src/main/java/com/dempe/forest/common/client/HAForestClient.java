package com.dempe.forest.common.client;

import com.dempe.forest.common.ha.HAProxy;
import com.dempe.forest.common.ha.ProxyHandler;
import com.dempe.forest.common.ha.ServerInstance;
import com.dempe.forest.common.ha.accessctr.AccessPolicy;
import com.dempe.forest.common.model.NodeDetails;
import com.dempe.forest.common.name.ForestNameService;
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
public class HAForestClient extends HAProxy<Client> {

    private final static Logger LOGGER = LoggerFactory.getLogger(HAForestClient.class);

    private ForestNameService forestNameService;

    private String name;

    /**
     * @param strategy
     * @param period
     */
    public HAForestClient(String name, Strategy strategy, long period) throws Exception {
        super(strategy, period);
        this.name = name;
        forestNameService = new ForestNameService();
        forestNameService.addServiceCacheListener(new ForestServiceCacheListener());
        forestNameService.start();
    }

    public HAForestClient(String name) throws Exception {
        this(name, Strategy.DEFAULT, 1000L);
    }

    @Override
    public List<ServerInstance> initServerInstanceList() throws Exception {
        List<ServerInstance> list = Lists.newArrayList();
        Collection<ServiceInstance<NodeDetails>> serviceInstances = forestNameService.listByName(name);
        for (ServiceInstance<NodeDetails> serviceInstance : serviceInstances) {
            ServerInstance serverInstance = new ServerInstance();
            serverInstance.setIp(serviceInstance.getAddress());
            serverInstance.setPort(serviceInstance.getPort());
        }
        return list;
    }


    @Override
    public Client createClient(ServerInstance serverInstance) throws Exception {
        /**
         *1s accessPolicy=5次发送失败则会自动切换client
         */
        AccessPolicy policy = new AccessPolicy(10, 1 * 1000, 5 * 1000 * 60, true);
        ForestClient forestClient = new ForestClient(serverInstance);
        Client client = (Client) ProxyHandler.getProxyInstance(forestClient, this, policy);
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

