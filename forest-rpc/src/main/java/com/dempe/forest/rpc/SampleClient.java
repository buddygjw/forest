package com.dempe.forest.rpc;

import com.dempe.forest.client.CommonClient;
import com.dempe.forest.rpc.handler.PacketDecoder;
import com.dempe.forest.rpc.handler.PacketEncoder;
import com.dempe.forest.rpc.protocol.PacketData;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/3/11
 * Time: 17:11
 * To change this template use File | Settings | File Templates.
 */
public class SampleClient extends CommonClient {


    public static void main(String[] args) throws Exception {
        SampleClient client = new SampleClient("localhost", 8888);
        PojoRpcMethodInfo methodInfo = new PojoRpcMethodInfo();
        PacketData packetData = PacketData.buildRpcDataPackage(null, new Object[]{});
        client.writeAndFlush(packetData);
    }

    public SampleClient(String host, int port) {
        super(host, port);
    }

    @Override
    public void initClientChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("RequestEncoder", new PacketEncoder())
                .addLast("ResponseDecoder", new PacketDecoder(60000))
                .addLast("ClientHandler", new ChannelHandlerAdapter() {
                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        Long id = 0L;
                        PacketData resp = (PacketData) msg;
                        id = resp.getRpcMeta().getCorrelationId();
                        Context context = contextMap.remove(id);
                        if (context == null) {
                            LOGGER.debug("messageID:{}, take Context null", id);
                            return;
                        }
                        context.cb.onReceive(resp);
                    }
                });
    }
}
