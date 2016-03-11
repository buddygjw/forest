package com.dempe.forest.rpc.protocol;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/3/11
 * Time: 17:31
 * To change this template use File | Settings | File Templates.
 */
public interface LogIDGenerator {

    /**
     * To generate log id for each RPC method invoke
     *
     * @param serviceName the service name
     * @param methodName  the method name
     * @param params      method params
     * @return new created log id
     */
    long generate(String serviceName, String methodName, Object... params);
}
