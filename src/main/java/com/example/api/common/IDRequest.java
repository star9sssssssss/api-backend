package com.example.api.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 接口修改与删除请求
 *
 
 */
@Data
public class IDRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}