package com.example.api.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.api.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import com.example.api.model.entity.InterfaceInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.api.model.vo.InterfaceInfoVO;

/**
* @author 散落星河
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2025-01-13 00:03:04
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    /**
     * 获取接口的封装
     * @param interfaceInfo
     * @return
     */
    InterfaceInfoVO getInterfaceInfoVO(InterfaceInfo interfaceInfo);


    /**
     * 获取查询条件
     * @param interfaceInfoQueryRequest
     * @return
     */
    QueryWrapper<InterfaceInfo> getQueryWrapper(InterfaceInfoQueryRequest interfaceInfoQueryRequest);


    Page<InterfaceInfoVO> getInterfaceInfoVOPage(Page<InterfaceInfo> interfaceInfoPage);

}
