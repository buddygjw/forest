package com.dempe.forest.rpc;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/3/11
 * Time: 17:29
 * To change this template use File | Settings | File Templates.
 */
public interface ClientAttachmentHandler extends AttachmentHandler {

    /**
     * @param response
     * @param serviceName
     * @param methodName
     * @param params
     */
    void handleResponse(byte[] response, String serviceName, String methodName, Object... params);
}
