package com.dempe.forest.client;

import com.dempe.forest.common.NodeDetails;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/3/7
 * Time: 12:35
 * To change this template use File | Settings | File Templates.
 */
public class FutureClientFactory implements ClientFactory {
    @Override
    public FutureClient makeClient(String host, int port) {
        return new FutureClient(host, port);
    }


    @Override
    public Client makeClient(NodeDetails nodeDetails) {
        return new FutureClient(nodeDetails);
    }
}
