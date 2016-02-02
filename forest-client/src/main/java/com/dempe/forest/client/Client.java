package com.dempe.forest.client;

import com.dempe.forest.common.proto.Request;
import com.dempe.forest.common.proto.Response;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/2/2
 * Time: 11:02
 * To change this template use File | Settings | File Templates.
 */
public interface Client {

    public void sendOnly(Request request);

    public Response sendAndWait(Request request);

    public Response sendAndWait(Request request, long timeOut);

//
}
