package com.sleeve.project.model.enums;

import lombok.Getter;

/**
 * 接口状态枚举
 *
 
 */
@Getter
public enum InterfaceInfoStatusEnum {

    OFFLINE("下线", 0),
    ONLINE("上线", 1);

    private final String text;

    private final Integer value;

    InterfaceInfoStatusEnum(String text, Integer value) {
        this.text = text;
        this.value = value;
    }


}
