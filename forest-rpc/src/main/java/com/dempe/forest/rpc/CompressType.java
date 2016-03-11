package com.dempe.forest.rpc;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/3/11
 * Time: 17:28
 * To change this template use File | Settings | File Templates.
 */
public enum CompressType {

    /**
     * No compress
     */
    NO(0),

    /**
     * Snappy compress
     */
    Snappy(1),

    /**
     * GZIP compress
     */
    GZIP(2);

    private final int value;

    CompressType(int value) { this.value = value; }

    public int value() { return this.value; }


}
