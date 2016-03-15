package com.dempe.forest.rpc.client.proxy;


import com.dempe.forest.rpc.client.Future;
import com.dempe.forest.rpc.core.ForestContext;
import com.dempe.forest.rpc.transport.protocol.PacketData;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ObjectProxy<T> extends BaseObjectProxy<T> implements InvocationHandler {
    public ObjectProxy(String host, int port) {
        super(host, port);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        if (Object.class == method.getDeclaringClass()) {
            String name = method.getName();
            if ("equals".equals(name)) {
                return proxy == args[0];
            } else if ("hashCode".equals(name)) {
                return System.identityHashCode(proxy);
            } else if ("toString".equals(name)) {
                return proxy.getClass().getName() + "@" +
                        Integer.toHexString(System.identityHashCode(proxy)) +
                        ", with InvocationHandler " + this;
            } else {
                throw new IllegalStateException(String.valueOf(method));
            }
        }

        ForestContext rpcCtx = createPacket(method, args);
        Future<PacketData> send = send(rpcCtx.getPacketData());
        PacketData packetData = send.await();
        LOGGER.info(">>>>>>>>>>>>>>>>pkdata:{}", packetData);
        LOGGER.info(">>>>>>>>data:{}", new String(packetData.getData()));

        byte[] data = packetData.getData();
        return new String(data);
    }


}
