package com.dempe.forest.rpc;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/3/11
 * Time: 17:29
 * To change this template use File | Settings | File Templates.
 */
public interface AttachmentHandler {

    /**
     * To add attachment for each RPC method invoke
     *
     * @param serviceName the service name
     * @param methodName  the method name
     * @param params      method params
     * @return attachment byte array to send
     */
    byte[] handleRequest(String serviceName, String methodName, Object... params);
}
