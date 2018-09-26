package com.example.demo.to;

import com.example.demo.to.eum.ReqType;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class TulingRequestTo implements Serializable {

    private int reqType;

    @NotNull
    private Perception perception;

    @NotNull
    private UserInfo userInfo;

    public int getReqType() {
        return reqType;
    }

    public void setReqType(int reqType) {
        this.reqType = reqType;
    }

    public Perception getPerception() {
        return perception;
    }

    public void setPerception(Perception perception) {
        this.perception = perception;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
