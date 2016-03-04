package com.dempe.forest.core;

import com.dempe.forest.common.protocol.Request;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * 业务逻辑处理handler
 * User: Dempe
 * Date: 2015/12/10
 * Time: 17:36
 * To change this template use File | Settings | File Templates.
 */
public class ProcessorHandler extends ChannelHandlerAdapter {

    public static final Logger LOGGER = LoggerFactory.getLogger(ProcessorHandler.class);

    // 业务逻辑线程池(业务逻辑最好跟netty io线程分开处理，线程切换虽会带来一定的性能损耗，但可以防止业务逻辑阻塞io线程)
    private final static ExecutorService workerThreadService = newBlockingExecutorsUseCallerRun(100);

    private ServerContext context;

    public ProcessorHandler(ServerContext context) {
        this.context = context;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof Request) {
            workerThreadService.submit(new TaskWorker(ctx, context, (Request) msg));
        }
    }

    /**
     * 阻塞的ExecutorService
     * @param size
     * @return
     */
    public static ExecutorService newBlockingExecutorsUseCallerRun(int size) {
        return new ThreadPoolExecutor(size, size, 0L, TimeUnit.MILLISECONDS, new SynchronousQueue<Runnable>(),
                new RejectedExecutionHandler() {
                    @Override
                    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                        try {
                            executor.getQueue().put(r);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
    }



}
