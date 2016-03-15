package com.dempe.forest.bus;

import com.dempe.forest.rpc.core.BetterExecutorService;
import com.dempe.forest.rpc.core.ServerContext;
import com.dempe.forest.rpc.core.TaskWorker;
import com.dempe.forest.rpc.transport.protocol.PacketData;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/3/15
 * Time: 15:22
 * To change this template use File | Settings | File Templates.
 */
public class DispatcherHandler extends ChannelHandlerAdapter {

    public static final Logger LOGGER = LoggerFactory.getLogger(DispatcherHandler.class);

    // 业务逻辑线程池(业务逻辑最好跟netty io线程分开处理，线程切换虽会带来一定的性能损耗，但可以防止业务逻辑阻塞io线程)
    private final static BetterExecutorService threadPool = new BetterExecutorService(100);


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof PacketData) {

        }
    }


}
