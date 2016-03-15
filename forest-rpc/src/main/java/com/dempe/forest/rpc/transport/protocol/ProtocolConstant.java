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

import java.nio.charset.Charset;

/**
 * Protocol constant definition.
 *
 * @author xiemalin
 * @since 1.0
 */
public class ProtocolConstant {

    /**
     * default magic code
     */
    public static final String MAGIC_CODE = "PRPC";

    /**
     * default charset
     */
    public static Charset CHARSET = Charset.forName("utf-8");

    /**
     * get the cHARSET
     *
     * @return the cHARSET
     */
    public static Charset getCharSet() {
        return CHARSET;
    }

    /**
     * set charSet value to CHARSET
     *
     * @param charSet the CHARSET to set
     */
    public static void setCharset(Charset charSet) {
        CHARSET = charSet;
    }


}
