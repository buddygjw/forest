package com.dempe.forest.leaf.simulator;

import com.alibaba.fastjson.JSONObject;
import com.dempe.forest.common.Constants;
import com.dempe.forest.common.client.ForestClient;
import com.dempe.forest.common.client.ha.HAClientService;
import com.dempe.forest.common.client.ha.HAForestClient;
import com.dempe.forest.common.proto.Request;
import com.dempe.forest.common.proto.Response;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/2/3
 * Time: 12:55
 * To change this template use File | Settings | File Templates.
 */
public class LeafSimulator {

    public static void main(String[] args) throws Exception {
        HAClientService clientService = new HAClientService(Constants.FOREST_BUS_NAME);
        Request request = buildReq();
        Response response = clientService.sendAndWait(request);
        System.out.println(response);
    }

    public static Request buildReq() {
        Request request = new Request();
        request.setName(Constants.FOREST_LEAF_NAME);
        request.setUri("/sample/hello");
        JSONObject param = new JSONObject();
        param.put("name", "dempe");
        request.putParaJSON(param);
        return request;
    }
}
