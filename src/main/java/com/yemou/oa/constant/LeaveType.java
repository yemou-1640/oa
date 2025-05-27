package com.yemou.oa.constant;


import com.fasterxml.jackson.annotation.JsonValue;

public enum LeaveType {
    年假("年假"),
    病假("病假"),
    事假("事假");

    private final String value;

    LeaveType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
