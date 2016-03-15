package com.dempe.forest.rpc.client.proxy;

import com.dempe.forest.rpc.client.DefaultClient;
import com.dempe.forest.rpc.core.ForestContext;
import com.dempe.forest.rpc.core.RPCExporter;
import com.dempe.forest.rpc.core.RPCService;
import com.dempe.forest.rpc.transport.compress.CompressType;
import com.dempe.forest.rpc.transport.protocol.PacketData;
import com.dempe.forest.rpc.transport.protocol.ProtocolConstant;
import com.dempe.forest.rpc.transport.protocol.RequestMeta;
import com.dempe.forest.rpc.transport.protocol.RpcMeta;
import com.dempe.forest.rpc.utils.Pack;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.Map;

public class BaseObjectProxy<T> extends DefaultClient {

    protected Class<T> clazz;

    protected String serviceName;

    private Map<String, PacketData> packetCache = Maps.newHashMap();

    public BaseObjectProxy(String host, int port) {
        super(host, port);
    }

    public void setClazz(Class<T> clazz) {
        this.clazz = clazz;
        RPCExporter rpcExporter = clazz.getAnnotation(RPCExporter.class);
        if (rpcExporter == null) {
            new IllegalArgumentException("clazz :" + clazz.getSimpleName() + " ,RPCExporter is null");
        }
        this.serviceName = rpcExporter.serviceName();
        if (StringUtils.isBlank(serviceName)) {
            serviceName = clazz.getSimpleName();
        }
    }

    ForestContext createPacket(Method method, Object[] args) {
        if (serviceName == null) {
            new IllegalArgumentException("method:" + method.getName() + "serviceName is null,please setClazz first");
        }
        String serviceKey = serviceName + "|" + method.getName();
        PacketData packetData = packetCache.get(serviceKey);
        if (packetData == null) {
            RPCService rpcService = method.getAnnotation(RPCService.class);
            if (rpcService == null) {
                new IllegalArgumentException("method:" + method.getName() + "RPCService is null");
            }
            String funcName = rpcService.methodName();
            if (StringUtils.isBlank(funcName)) {
                funcName = method.getName();
            }
            CompressType compressType = rpcService.compressType();
            packetData = new PacketData();
            packetData.magicCode(ProtocolConstant.MAGIC_CODE);
            packetData.compressType(0);
            RpcMeta rpcMeta = new RpcMeta();
            rpcMeta.setCompressType(compressType.value());
            RequestMeta request = new RequestMeta();
            request.setMethodName(funcName);
            request.setServiceName(serviceName);
            rpcMeta.setRequest(request);
            packetData.setRpcMeta(rpcMeta);
            packetCache.put(serviceKey, packetData.copy());
        }

        if (args != null && args.length > 0) {
            byte[] bytes = encodeArgs(args);
            if (bytes != null) {
                packetData.data(bytes);
            }
        }
        ForestContext rpcCtx = new ForestContext();
        rpcCtx.setPacketData(packetData);
        return rpcCtx;
    }


    private byte[] encodeArgs(Object[] args) {
        Pack pack = new Pack();
        for (Object arg : args) {
            if (arg instanceof String) {
                String value = (String) arg;
                pack.putVarstr(value);
            } else if (arg instanceof Integer) {
                Integer value = (Integer) arg;
                pack.putInt(value);
            } else if (arg instanceof Short) {
                Short value = (Short) arg;
                pack.putShort(value);
            } else if (arg instanceof Long) {
                Long value = (Long) arg;
                pack.putLong(value);
            } else if (arg instanceof Double) {
                Double value = (Double) arg;
                pack.putDouble(value);
            } else if (arg instanceof Float) {
                Float value = (Float) arg;
                pack.putFloat(value);
            }
        }
        // encode args
        byte data[] = pack.getOriBuffer().array();
        return data;

    }

}
