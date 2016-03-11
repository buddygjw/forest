package com.dempe.forest.rpc;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/3/11
 * Time: 17:30
 * To change this template use File | Settings | File Templates.
 */
public class DummyAttachmentHandler implements AttachmentHandler {

    /* (non-Javadoc)
     * @see com.baidu.jprotobuf.pbrpc.AttachmentHandler#handleRequest(java.lang.String, java.lang.String, java.lang.Object[])
     */
    public byte[] handleRequest(String serviceName, String methodName, Object... params) {
        return null;
    }

}
