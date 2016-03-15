package com.dempe.forest.leaf.simulator;

import com.dempe.forest.leaf.controller.SampleController;
import com.dempe.forest.rpc.client.RPCClient;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/2/3
 * Time: 12:55
 * To change this template use File | Settings | File Templates.
 */
public class LeafSimulator {
    public static void main(String[] args) {
        SampleController client = RPCClient.proxyBuilder(SampleController.class)
                .withServerNode("127.0.0.1", 8888)
                .build();
        String hello = client.hello("hello world!");
        System.out.println(">>>>>>>>>>>>>>>>" + hello);
    }


}
