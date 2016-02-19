package com.dempe.forest.common.protocol;

import com.alibaba.fastjson.JSONObject;
import com.dempe.forest.common.uitls.pack.Marshallable;
import com.dempe.forest.common.uitls.pack.Pack;
import com.dempe.forest.common.uitls.pack.Unpack;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

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

    private String param;

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

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public JSONObject paramJSON() {
        if (StringUtils.isBlank(param)) {
            return new JSONObject();
        }
        return JSONObject.parseObject(param);
    }

    public void putParaJSON(JSONObject paramJSON) {
        this.param = paramJSON.toJSONString();
    }

    @Override
    public Pack marshal(Pack pack) {
        pack.putInt(seqId);
        pack.putVarstr(name);
        pack.putVarstr(uri);
        pack.putVarstr(param);
        return pack;
    }

    @Override
    public Request unmarshal(Unpack unpack) throws IOException {
        seqId = unpack.popInt();
        name = unpack.popVarstr();
        uri = unpack.popVarstr();
        param = unpack.popVarstr();
        return this;
    }

    @Override
    public String toString() {
        return "Request{" +
                "seqId=" + seqId +
                ", name='" + name + '\'' +
                ", uri='" + uri + '\'' +
                ", param='" + param + '\'' +
                '}';
    }
}
