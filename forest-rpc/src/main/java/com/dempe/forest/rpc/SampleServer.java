package com.dempe.forest.rpc;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/3/11
 * Time: 17:09
 * To change this template use File | Settings | File Templates.
 */
public class SampleServer {
    public static void main(String[] args) throws IOException {
        BootServer server = new BootServer();
        server.start();
    }
}
