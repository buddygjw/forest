package com.dempe.forest.client;

import com.dempe.forest.common.NodeDetails;

/**
 * client工厂类
 * User: Dempe
 * Date: 2016/3/7
 * Time: 11:53
 * To change this template use File | Settings | File Templates.
 */
public interface ClientFactory {

    public Client makeClient(String host, int port);


    public Client makeClient(NodeDetails nodeDetails);

}
