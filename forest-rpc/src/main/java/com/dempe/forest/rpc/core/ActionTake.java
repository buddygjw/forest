package com.dempe.forest.rpc.core;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dempe.forest.rpc.transport.protocol.PacketData;
import com.dempe.forest.rpc.utils.PathUtil;
import com.dempe.forest.rpc.utils.Unpack;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * 业务处理快照类
 * User: Dempe
 * Date: 2015/11/4
 * Time: 10:17
 * To change this template use File | Settings | File Templates.
 */
public class ActionTake implements Take<PacketData, PacketData> {

    private final static Logger LOGGER = LoggerFactory.getLogger(ActionTake.class);

    private ServerContext context;

    public ActionTake(ServerContext context) {
        this.context = context;
    }

    /**
     * @param packetData 请求消息
     * @return PacketData 返回消息
     * @throws java.lang.reflect.InvocationTargetException
     * @throws IllegalAccessException
     */
    public PacketData act(PacketData packetData) throws InvocationTargetException, IllegalAccessException,
            ClassNotFoundException, InstantiationException, UnsupportedEncodingException {
        String uri = PathUtil.buildPath(packetData);
        // 通过Request uri找到对应的ActionMethod
        ActionMethod actionMethod = context.tackAction(uri);
        if (actionMethod == null) {
            LOGGER.warn("[dispatcher]:not find jsonURI {}", uri);
            return null;
        }
        Method method = actionMethod.getMethod();
        byte[] data = packetData.getData();
        Unpack unpack = new Unpack(data);
        Type[] type = method.getGenericParameterTypes();
        Object[] args = new Object[type.length];
        for (int i = 0; i < type.length; i++) {
            if (Integer.class == type[i] || StringUtils.equals(type[i].toString(), "int")) {
                args[i] = unpack.popInt();
            } else if (String.class == type[i]) {
                args[i] = unpack.popVarstr();
            } else if (Boolean.class == type[i] || StringUtils.equals(type[i].toString(), "boolean")) {
                args[i] = unpack.popBoolean();
            } else if (Long.class == type[i] || StringUtils.equals(type[i].toString(), "long")) {
                args[i] = unpack.popLong();
            } else if (Short.class == type[i] || StringUtils.equals(type[i].toString(), "short")) {
                args[i] = unpack.popShort();
            } else if (Double.class == type[i] || StringUtils.equals(type[i].toString(), "double")) {
                args[i] = unpack.popDouble();
            } else if (Float.class == type[i] || StringUtils.equals(type[i].toString(), "float")) {
                args[i] = unpack.popFloat();
            }

        }
        // 获取方法参数
        Object result = actionMethod.call(args);
        if (result == null) {
            // 当action method 返回是void的时候，不返回任何消息
            LOGGER.debug("actionMethod:{} return void.", actionMethod);
            return null;
        }
        if (result instanceof String) {
            packetData.data(((String) result).getBytes());
        } else if (result instanceof JSONObject) {
            packetData.data(JSON.toJSONBytes(result));
        }
        return packetData;


    }


}
