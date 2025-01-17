package com.sleeve.project.model.enums;

/**
 * 接口状态枚举
 *
 
 */
public enum InterfaceInfoStatusEnum {

    OFFLINE("下线", 0),
    ONLINE("上线", 1);

    private final String text;

    private final Integer value;

    InterfaceInfoStatusEnum(String text, Integer value) {
        this.text = text;
        this.value = value;
    }


    public Integer getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}
