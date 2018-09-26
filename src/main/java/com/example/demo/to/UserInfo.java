package com.example.demo.to;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class UserInfo implements Serializable {

    private String apiKey = "e34d444599e040a09955dde108356c70";

    @NotNull
    private String userId;

    private String groupId = "";

    private String userIdName = "";

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getUserIdName() {
        return userIdName;
    }

    public void setUserIdName(String userIdName) {
        this.userIdName = userIdName;
    }
}
