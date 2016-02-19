package com.dempe.forest.common.client;

import com.dempe.forest.common.protocol.Request;
import com.dempe.forest.common.protocol.Response;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/2/2
 * Time: 11:02
 * To change this template use File | Settings | File Templates.
 */
public interface Client {

    public void sendOnly(Request request) throws Exception;

    public Response sendAndWait(Request request) throws Exception;

    public Response sendAndWait(Request request, long timeOut) throws Exception;

    public void sendForward(ChannelHandlerContext ctx, Request request) throws Exception;

//
}
