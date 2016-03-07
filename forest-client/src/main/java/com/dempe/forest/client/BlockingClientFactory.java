package com.dempe.forest.client;

import com.dempe.forest.common.NodeDetails;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/3/7
 * Time: 12:36
 * To change this template use File | Settings | File Templates.
 */
public class BlockingClientFactory implements ClientFactory {
    @Override
    public BlockingClient makeClient(String host, int port) {
        return new BlockingClient(host, port);
    }

    @Override
    public Client makeClient(NodeDetails nodeDetails) {
        return new BlockingClient(nodeDetails);
    }
}
