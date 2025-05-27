package com.yemou.oa.constant;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PositionType {
    员工("员工"),
    实习生("实习生"),
    组长("组长"),
    工程师("工程师"),
    总监("总监"),
    部长("部长");

    private final String value;

    PositionType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
