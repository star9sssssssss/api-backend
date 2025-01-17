package com.sleeve.project.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sleeve.project.model.dto.userInterfaceInfo.UserInterfaceInfoQueryRequest;
import com.sleeve.project.model.entity.UserInterfaceInfo;

/**
* @author 散落星河
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service
* @createDate 2025-01-15 22:13:28
*/
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {

    /**
     * 获取查询条件
     * @param userInterfaceInfoQueryRequest
     * @return
     */
    QueryWrapper<UserInterfaceInfo> getQueryWrapper(UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest);

    /**
     * 记录用户调用次数
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    boolean invokeCount(long interfaceInfoId, long userId);
}
