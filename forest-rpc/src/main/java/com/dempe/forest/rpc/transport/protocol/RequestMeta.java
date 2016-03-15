/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dempe.forest.rpc.transport.protocol;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

import java.io.IOException;
import java.util.Arrays;

/**
 * 请求包的元数据主要描述了需要调用的RPC方法信息
 *
 * @author xiemalin
 * @see RpcMeta
 * @since 1.0
 */
public class RequestMeta implements Readable, Writerable {


    /**
     * default encode and decode handler
     */
    private static final Codec<RequestMeta> CODEC = ProtobufProxy.create(RequestMeta.class);

    /**
     * 服务名
     */
    @Protobuf(required = true)
    private String serviceName;

    /**
     * 方法名
     */
    @Protobuf(required = true)
    private String methodName;

    /**
     * 用于打印日志。可用于存放BFE_LOGID。该参数可选。
     */
    @Protobuf
    private Long logId;

    /**
     * 非PbRpc规范，用于传输额外的参数
     */
    @Protobuf(fieldType = FieldType.BYTES)
    private byte[] extraParam;

    /**
     * get the serivceName
     *
     * @return the serivceName
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     * set serivceName value to serivceName
     *
     * @param serviceName the serivceName to set
     */
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    /**
     * get the methodName
     *
     * @return the methodName
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * set methodName value to methodName
     *
     * @param methodName the methodName to set
     */
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    /**
     * get the logId
     *
     * @return the logId
     */
    public Long getLogId() {
        return logId;
    }

    /**
     * set logId value to logId
     *
     * @param logId the logId to set
     */
    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public void read(byte[] bytes) {
        if (bytes == null) {
            throw new IllegalArgumentException("param 'bytes' is null.");
        }

        try {
            RequestMeta meta = CODEC.decode(bytes);
            copy(meta);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * copy {@link RequestMeta}
     *
     * @param meta
     */
    private void copy(RequestMeta meta) {
        if (meta == null) {
            return;
        }
        setLogId(meta.getLogId());
        setMethodName(meta.getMethodName());
        setServiceName(meta.getServiceName());
        setExtraParam(meta.getExtraParam());
    }

    public byte[] write() {
        try {
            return CODEC.encode(this);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * get the extraParam
     *
     * @return the extraParam
     */
    public byte[] getExtraParam() {
        return extraParam;
    }

    /**
     * set extraParam value to extraParam
     *
     * @param extraParam the extraParam to set
     */
    public void setExtraParam(byte[] extraParam) {
        this.extraParam = extraParam;
    }

    /**
     * @return
     */
    public RequestMeta copy() {
        RequestMeta rpcRequestMeta = new RequestMeta();
        rpcRequestMeta.copy(this);
        return rpcRequestMeta;
    }

    @Override
    public String toString() {
        return "RequestMeta{" +
                "serviceName='" + serviceName + '\'' +
                ", methodName='" + methodName + '\'' +
                ", logId=" + logId +
                ", extraParam=" + Arrays.toString(extraParam) +
                '}';
    }
}
