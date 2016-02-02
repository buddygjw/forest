package com.dempe.forest.client;

import com.dempe.forest.common.ha.ServerInstance;
import com.dempe.forest.common.proto.Request;
import com.dempe.forest.common.proto.Response;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/2/2
 * Time: 11:02
 * To change this template use File | Settings | File Templates.
 */
public class ForestClient implements Client {

    private String host;

    private int port;

    public ForestClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public ForestClient(ServerInstance serverInstance) {
        this(serverInstance.getIp(), serverInstance.getPort());
    }

    @Override
    public void sendOnly(Request request) {

    }

    @Override
    public Response sendAndWait(Request request) {
        return null;
    }

    @Override
    public Response sendAndWait(Request request, long timeOut) {
        return null;
    }

    public void reconnect() {

    }

    public void isConnect() {

    }

    public void close() throws IOException {

    }
}
