package com.dempe.forest.client;

import com.dempe.forest.common.protocol.Response;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/21
 * Time: 17:22
 * To change this template use File | Settings | File Templates.
 */
public class ReplyFuture {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReplyFuture.class);

    private int messageId;

    private long readTimeoutMillis = 120000;

    private Response message;

    private ChannelHandlerContext ctx;


    public ReplyFuture(int messageId) {
        this.messageId = messageId;
    }

    public ReplyFuture(ChannelHandlerContext ctx, int messageId) {
        this.messageId = messageId;
        this.ctx = ctx;
    }


    public long getReadTimeoutMillis() {
        return readTimeoutMillis;
    }

    public void setReadTimeoutMillis(long readTimeoutMillis) {
        this.readTimeoutMillis = readTimeoutMillis;
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public synchronized void await() {
        await(readTimeoutMillis);
    }

    public synchronized void await(long millis) {
        try {
            if (message == null) {
                this.wait(millis);
            }
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public synchronized void onReceivedReply(final Response message) {
        this.message = message;
        if (ctx != null) {
            LOGGER.info("future write msg {}", message);
            ctx.executor().execute(new Runnable() {
                @Override
                public void run() {
                    ctx.writeAndFlush(message);
                }
            });

        }
        this.notifyAll();
    }

    public Response getReply() {
        if (this.message == null) {
            await();
        }
        if (this.message == null) {
            LOGGER.error("message is null");
        }
        return message;
    }
}
