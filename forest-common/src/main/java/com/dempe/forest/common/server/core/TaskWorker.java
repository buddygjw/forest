package com.dempe.forest.common.server.core;

import com.dempe.forest.common.proto.Request;
import com.dempe.forest.common.proto.Response;
import com.dempe.forest.common.server.ServerContext;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;

/**
 * 业务处理线程类
 * User: Dempe
 * Date: 2015/12/11
 * Time: 10:01
 * To change this template use File | Settings | File Templates.
 */
public class TaskWorker implements Runnable {

    private final static Logger LOGGER = LoggerFactory.getLogger(TaskWorker.class);

    private ChannelHandlerContext ctx;
    private ServerContext context;
    private Request request;

    public TaskWorker(ChannelHandlerContext ctx, ServerContext context, Request request) {
        this.ctx = ctx;
        this.context = context;
        this.request = request;
    }

    @Override
    public void run() {
        try {
            // set执行上下文环境
            context.setLocalContext(request, ctx);
            ActionTake tack = new ActionTake(context);
            Response act = tack.act(request);
            if (act != null) {
                ctx.writeAndFlush(act);
            }
        } catch (InvocationTargetException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (InstantiationException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            // remove执行上下文环境
            context.removeLocalContext();
        }

    }
}
