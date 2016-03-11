package com.dempe.forest.rpc;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/3/11
 * Time: 17:27
 * To change this template use File | Settings | File Templates.
 */

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ProtobufRPC {
    String serviceName();

    String methodName() default "";


    long onceTalkTimeout() default 0L;

    Class<? extends ClientAttachmentHandler> attachmentHandler() default DummyClientAttachmentHandler.class;

    CompressType compressType() default CompressType.NO;
}
