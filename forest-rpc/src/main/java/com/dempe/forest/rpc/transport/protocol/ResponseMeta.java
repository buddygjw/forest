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
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

import java.io.IOException;

/**
 * 响应包的元数据是对返回结果的描述。如果出现任何异常，错误也会放在元数据中。
 *
 * @author xiemalin
 * @see RpcMeta
 * @since 1.0
 */
public class ResponseMeta implements Readable, Writerable {

    /**
     * Decode and encode hanlder
     */
    private static final Codec<ResponseMeta> CODEC = ProtobufProxy.create(ResponseMeta.class);

    /**
     * default constrctor
     */
    public ResponseMeta() {
    }

    /**
     * constructor with errorCode and errorText
     *
     * @param errorCode
     * @param errorText
     */
    public ResponseMeta(Integer errorCode, String errorText) {
        super();
        this.errorCode = errorCode;
        this.errorText = errorText;
    }

    /**
     * 发生错误时的错误号，0表示正常，非0表示错误。具体含义由应用方自行定义。
     */
    @Protobuf
    private Integer errorCode;

    /**
     * 错误的文本描述
     */
    @Protobuf
    private String errorText;

    /**
     * get the errorCode
     *
     * @return the errorCode
     */
    public Integer getErrorCode() {
        if (errorCode == null) {
            errorCode = 0;
        }
        return errorCode;
    }

    /**
     * set errorCode value to errorCode
     *
     * @param errorCode the errorCode to set
     */
    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * get the errorText
     *
     * @return the errorText
     */
    public String getErrorText() {
        return errorText;
    }

    /**
     * set errorText value to errorText
     *
     * @param errorText the errorText to set
     */
    public void setErrorText(String errorText) {
        this.errorText = errorText;
    }

    /* (non-Javadoc)
     * @see com.baidu.jprotobuf.remoting.pbrpc.Writerable#write()
     */
    public byte[] write() {
        try {
            return CODEC.encode(this);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * copy {@link ResponseMeta}
     *
     * @param meta
     */
    private void copy(ResponseMeta meta) {
        if (meta == null) {
            return;
        }
        setErrorCode(meta.getErrorCode());
        setErrorText(meta.getErrorText());
    }

    /* (non-Javadoc)
     * @see com.baidu.jprotobuf.remoting.pbrpc.Readable#read(byte[])
     */
    public void read(byte[] bytes) {
        if (bytes == null) {
            throw new IllegalArgumentException("param 'bytes' is null.");
        }

        try {
            ResponseMeta meta = CODEC.decode(bytes);
            copy(meta);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * @return
     */
    public ResponseMeta copy() {
        ResponseMeta rpcResponseMeta = new ResponseMeta();
        rpcResponseMeta.copy(this);
        return rpcResponseMeta;
    }


}
