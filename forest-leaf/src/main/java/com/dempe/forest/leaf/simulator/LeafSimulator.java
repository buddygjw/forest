package com.dempe.forest.leaf.simulator;

import com.alibaba.fastjson.JSONObject;
import com.dempe.forest.client.ha.HAClientService;
import com.dempe.forest.common.Constants;
import com.dempe.forest.common.protocol.Request;
import com.dempe.forest.common.protocol.Response;

import java.util.concurrent.TimeUnit;

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
        for (int i = 0; i < 10000; i++) {

            Request request = buildReq();
            Response response = clientService.sendAndWait(request);
            System.out.println(response);
            if (i % 10 == 0) {
                TimeUnit.SECONDS.sleep(1);
            }
        }
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
