package com.dempe.forest.client;

import com.dempe.forest.common.NodeDetails;
import com.dempe.forest.common.protocol.Request;
import com.dempe.forest.common.protocol.Response;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/3/7
 * Time: 11:51
 * To change this template use File | Settings | File Templates.
 */
public class BlockingClient extends FutureClient {

    public BlockingClient(NodeDetails nodeDetails) {
        super(nodeDetails);
    }

    public BlockingClient(String host, int port) {
        super(host, port);
    }

    /**
     * 发送消息，等消息返回
     *
     * @param request 请求消息
     * @return
     */
    public Response sendAndWaitRequest(Request request) throws Exception {
        ReplyFuture future = sendRequest(request);
        return future.getReply();
    }

    public Response sendAndWaitRequest(Request request, long timeOut) throws Exception {
        ReplyFuture future = sendRequest(request);
        future.setReadTimeoutMillis(timeOut);
        return future.getReply();
    }


}
