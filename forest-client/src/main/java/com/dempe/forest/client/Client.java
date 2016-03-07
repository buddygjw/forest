package com.dempe.forest.client;

import com.dempe.forest.common.protocol.Request;
import com.dempe.forest.common.protocol.Response;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/2/2
 * Time: 11:02
 * To change this template use File | Settings | File Templates.
 */
public interface Client {

    public ReplyFuture sendRequest(Request request) throws Exception;


    public Response sendAndWaitRequest(Request request, long timeOut) throws Exception;

    public Response sendAndWaitRequest(Request request) throws Exception;

    public void sendForward(ChannelHandlerContext ctx, Request request) throws Exception;


}
