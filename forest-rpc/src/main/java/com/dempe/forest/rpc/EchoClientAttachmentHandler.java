package com.dempe.forest.rpc;

import junit.framework.Assert;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/3/11
 * Time: 17:28
 * To change this template use File | Settings | File Templates.
 */
public class EchoClientAttachmentHandler implements ClientAttachmentHandler {

    private byte[] attachment = EchoClientAttachmentHandler.class.getName().getBytes();

    /*
     * (non-Javadoc)
     *
     * @see
     * com.baidu.jprotobuf.pbrpc.AttachmentHandler#handleRequest(java.lang.String
     * , java.lang.String, java.lang.Object[])
     */
    public byte[] handleRequest(String serviceName, String methodName, Object... params) {
        return attachment;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.baidu.jprotobuf.pbrpc.ClientAttachmentHandler#handleResponse(byte[],
     * java.lang.String, java.lang.String, java.lang.Object[])
     */
    public void handleResponse(byte[] response, String serviceName, String methodName, Object... params) {
        Assert.assertEquals(EchoClientAttachmentHandler.class.getName(), new String(response));

    }

}
