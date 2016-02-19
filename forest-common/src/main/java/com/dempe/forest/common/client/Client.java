package com.dempe.forest.common.client;

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

    public void sendOnly(Request request);

    public Response sendAndWait(Request request);

    public Response sendAndWait(Request request, long timeOut);

    public void sendForward(ChannelHandlerContext ctx, Request request);

//
}
