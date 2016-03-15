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

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

import java.io.IOException;
import java.util.Arrays;

/**
 * RPC meta data
 *
 * @author xiemalin
 * @see RequestMeta
 * @see ResponseMeta
 * @since 1.0
 */
public class RpcMeta implements Readable, Writerable, Cloneable {

    public static final int COMPRESS_NO = 0;
    public static final int COMPRESS_SNAPPY = 1;
    public static final int COMPRESS_GZIP = 2;

    /**
     * Decode and encode handler
     */


    /**
     * 请求包元数据
     */
    @Protobuf(fieldType = FieldType.OBJECT)
    private RequestMeta request;

    /**
     * 响应包元数据
     */
    @Protobuf(fieldType = FieldType.OBJECT)
    private ResponseMeta response;

    /**
     * 0 不压缩
     * 1 使用Snappy 1.0.5
     * 2 使用gzip
     */
    @Protobuf
    private Integer compressType;

    /**
     * 请求包中的该域由请求方设置，用于唯一标识一个RPC请求。<br>
     * 请求方有义务保证其唯一性，协议本身对此不做任何检查。<br>
     * 响应方需要在对应的响应包里面将correlation_id设为同样的值。
     */
    @Protobuf
    private Long correlationId;

    /**
     * 附件大小
     */
    @Protobuf
    private Integer attachmentSize;

    /**
     * Chunk模式本质上是将一个大的数据流拆分成一个个小的Chunk包按序进行发送。如何拆分还原由通信双方确定
     */
    @Protobuf
    private ChunkInfo chunkInfo;

    /**
     * 用于存放身份认证相关信息
     */
    @Protobuf(fieldType = FieldType.BYTES)
    private byte[] authenticationData;

    /**
     * get the request
     *
     * @return the request
     */
    public RequestMeta getRequest() {
        return request;
    }

    /**
     * set request value to request
     *
     * @param request the request to set
     */
    public void setRequest(RequestMeta request) {
        this.request = request;
    }

    /**
     * get the response
     *
     * @return the response
     */
    public ResponseMeta getResponse() {
        return response;
    }

    /**
     * set response value to response
     *
     * @param response the response to set
     */
    public void setResponse(ResponseMeta response) {
        this.response = response;
    }

    /**
     * get the compressType
     *
     * @return the compressType
     */
    public Integer getCompressType() {
        if (compressType == null) {
            compressType = 0;
        }
        return compressType;
    }

    /**
     * set compressType value to compressType
     *
     * @param compressType the compressType to set
     */
    public void setCompressType(Integer compressType) {
        this.compressType = compressType;
    }

    /**
     * get the correlationId
     *
     * @return the correlationId
     */
    public Long getCorrelationId() {
        if (correlationId == null) {
            correlationId = 0L;
        }
        return correlationId;
    }

    /**
     * set correlationId value to correlationId
     *
     * @param correlationId the correlationId to set
     */
    public void setCorrelationId(Long correlationId) {
        this.correlationId = correlationId;
    }

    /**
     * get the attachmentSize
     *
     * @return the attachmentSize
     */
    public Integer getAttachmentSize() {
        if (attachmentSize == null) {
            return 0;
        }
        return attachmentSize;
    }

    /**
     * set attachmentSize value to attachmentSize
     *
     * @param attachmentSize the attachmentSize to set
     */
    public void setAttachmentSize(Integer attachmentSize) {
        this.attachmentSize = attachmentSize;
    }

    /**
     * get the authenticationData
     *
     * @return the authenticationData
     */
    public byte[] getAuthenticationData() {
        return authenticationData;
    }

    /**
     * set authenticationData value to authenticationData
     *
     * @param authenticationData the authenticationData to set
     */
    public void setAuthenticationData(byte[] authenticationData) {
        this.authenticationData = authenticationData;
    }


    public byte[] write() {
        try {
            return ProtobufProxy.create(RpcMeta.class).encode(this);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    public void read(byte[] bytes) {
        if (bytes == null) {
            throw new IllegalArgumentException("param 'bytes' is null.");
        }
        try {
            RpcMeta meta = ProtobufProxy.create(RpcMeta.class).decode(bytes);
            copyReference(meta);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * copy {@link RpcMeta}
     *
     * @param meta
     */
    private void copyReference(RpcMeta meta) {
        if (meta == null) {
            return;
        }
        setRequest(meta.getRequest());
        setResponse(meta.getResponse());
        setAttachmentSize(meta.getAttachmentSize());
        setAuthenticationData(meta.getAuthenticationData());
        setCompressType(meta.getCompressType());
        setCorrelationId(meta.getCorrelationId());
        setChunkInfo(meta.getChunkInfo());
    }

    public RpcMeta copy() {
        RpcMeta rpcMeta = new RpcMeta();

        if (chunkInfo != null) {
            rpcMeta.setChunkInfo(chunkInfo.copy());
        }
        if (request != null) {
            rpcMeta.setRequest(request.copy());
        }
        if (response != null) {
            rpcMeta.setResponse(response.copy());
        }
        rpcMeta.setAttachmentSize(attachmentSize);
        rpcMeta.setAuthenticationData(authenticationData);
        rpcMeta.setCompressType(compressType);
        rpcMeta.setCorrelationId(correlationId);


        return rpcMeta;
    }

    /**
     * get the chunkInfo
     *
     * @return the chunkInfo
     */
    public ChunkInfo getChunkInfo() {
        return chunkInfo;
    }

    /**
     * set chunkInfo value to chunkInfo
     *
     * @param chunkInfo the chunkInfo to set
     */
    public void setChunkInfo(ChunkInfo chunkInfo) {
        this.chunkInfo = chunkInfo;
    }


    @Override
    public String toString() {
        return "RpcMeta{" +
                "request=" + request +
                ", response=" + response +
                ", compressType=" + compressType +
                ", correlationId=" + correlationId +
                ", attachmentSize=" + attachmentSize +
                ", chunkInfo=" + chunkInfo +
                ", authenticationData=" + Arrays.toString(authenticationData) +
                '}';
    }
}
