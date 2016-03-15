package com.dempe.forest.leaf.controller;

import com.dempe.forest.rpc.core.RPCExporter;
import com.dempe.forest.rpc.core.RPCService;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/3/15
 * Time: 14:38
 * To change this template use File | Settings | File Templates.
 */
@RPCExporter
public interface SampleController {
    @RPCService
    public String hello(String name);
}
