package com.dempe.forest.client;

import com.dempe.forest.common.NodeDetails;
import com.dempe.forest.common.protocol.Request;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/3/7
 * Time: 11:46
 * To change this template use File | Settings | File Templates.
 */
public class FutureClient extends ForestClient {
    public FutureClient(NodeDetails nodeDetails) {
        super(nodeDetails);
    }

    public FutureClient(String host, int port) {
        super(host, port);
    }

    /**
     * 发送消息，等消息返回
     *
     * @param request 请求消息
     * @return
     */
    public ReplyFuture sendRequest(Request request) throws Exception {
        int id = request.getSeqId();
        if (id == 0) {
            id = idMaker.incrementAndGet();
            request.setSeqId(id);
        }
        ReplyFuture future = new ReplyFuture(id);
        replyQueue.add(future);
        send(request);
        return future;
    }


    /**
     * 发送消息，并将返回消息写到ctx中
     *
     * @param ctx 上下文，用于将response写入对应的channel中
     * @param request 请求消息
     */
    public void sendForward(ChannelHandlerContext ctx, Request request) throws Exception {
        int id = request.getSeqId();
        if (id == 0) {
            id = idMaker.incrementAndGet();
            request.setSeqId(id);
        }
        ReplyFuture future = new ReplyFuture(ctx, id);
        replyQueue.add(future);
        send(request);
    }


}
