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
package com.dempe.forest.rpc.core;

import com.dempe.forest.rpc.transport.compress.CompressType;

import java.lang.annotation.*;


/**
 * 暴露service方法
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RPCService {

    String methodName() default "";

    // 用于后台显示
    String description() default "";

    // 默认压缩方式
    CompressType compressType() default CompressType.NO;

    // 默认timeout
    long timeout() default 0;


}
