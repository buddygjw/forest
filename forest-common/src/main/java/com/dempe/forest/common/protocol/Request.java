package com.dempe.forest.common.protocol;

import com.dempe.forest.common.pack.MarshallUtils;
import com.dempe.forest.common.pack.Marshallable;
import com.dempe.forest.common.pack.Pack;
import com.dempe.forest.common.pack.Unpack;

import java.io.IOException;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/2/2
 * Time: 11:27
 * To change this template use File | Settings | File Templates.
 */
public class Request implements Marshallable {

    private int seqId;

    private String name;

    private String uri;

    private Map<String, String> paramMap;

    public int getSeqId() {
        return seqId;
    }

    public void setSeqId(int seqId) {
        this.seqId = seqId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Map<String, String> getParamMap() {
        return paramMap;
    }

    public void setParamMap(Map<String, String> paramMap) {
        this.paramMap = paramMap;
    }

    @Override
    public Pack marshal(Pack pack) {
        pack.putInt(seqId);
        pack.putVarstr(name);
        pack.putVarstr(uri);
        MarshallUtils.packMap(pack, paramMap, String.class, String.class);
        return pack;
    }

    @Override
    public Request unmarshal(Unpack unpack) throws IOException {
        seqId = unpack.popInt();
        name = unpack.popVarstr();
        uri = unpack.popVarstr();
        paramMap = MarshallUtils.unpackMap(unpack, String.class, String.class, false);
        return this;
    }

    @Override
    public String toString() {
        return "Request{" +
                "seqId=" + seqId +
                ", name='" + name + '\'' +
                ", uri='" + uri + '\'' +
                ", paramMap=" + paramMap +
                '}';
    }
}
