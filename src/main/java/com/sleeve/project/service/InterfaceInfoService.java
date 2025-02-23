package com.sleeve.project.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sleeve.project.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import com.sleeve.project.model.entity.InterfaceInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sleeve.project.model.vo.InterfaceInfoVO;

/**
* @author 散落星河
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2025-01-13 00:03:04
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    /**
     * 获取接口的封装
     * @param interfaceInfo 接口信息
     * @return 封装
     */
    InterfaceInfoVO getInterfaceInfoVO(InterfaceInfo interfaceInfo);


    /**
     * 获取查询条件
     * @param interfaceInfoQueryRequest 请求
     * @return
     */
    QueryWrapper<InterfaceInfo> getQueryWrapper(InterfaceInfoQueryRequest interfaceInfoQueryRequest);


    /**
     * 获取分页结果(封装)
     * @param interfaceInfoPage 分页原生
     * @return
     */
    Page<InterfaceInfoVO> getInterfaceInfoVOPage(Page<InterfaceInfo> interfaceInfoPage);

}
