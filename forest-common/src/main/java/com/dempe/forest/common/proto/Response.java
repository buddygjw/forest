package com.dempe.forest.common.proto;

import com.dempe.forest.common.uitls.pack.Marshallable;
import com.dempe.forest.common.uitls.pack.Pack;
import com.dempe.forest.common.uitls.pack.Unpack;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/2/2
 * Time: 11:27
 * To change this template use File | Settings | File Templates.
 */
public class Response implements Marshallable {

    private int seqId;

    private String result;


    public int getSeqId() {
        return seqId;
    }

    public void setSeqId(int seqId) {
        this.seqId = seqId;
    }


    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public Pack marshal(Pack pack) {
        pack.putInt(seqId);
        pack.putVarstr(result);
        return pack;
    }

    @Override
    public Response unmarshal(Unpack unpack) throws IOException {
        seqId = unpack.popInt();
        result = unpack.popVarstr();
        return this;
    }

    @Override
    public String toString() {
        return "Response{" +
                "seqId=" + seqId +
                ", result='" + result + '\'' +
                '}';
    }
}
