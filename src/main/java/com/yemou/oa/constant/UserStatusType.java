package com.yemou.oa.constant;

import com.fasterxml.jackson.annotation.JsonValue;

public enum UserStatusType {
    实习("实习"),
    在职("在职"),
    已离职("已离职");

    private final String value;
    UserStatusType(String value) {
        this.value = value;
    }
    @JsonValue
    public String getValue() {
        return value;
    }
}
