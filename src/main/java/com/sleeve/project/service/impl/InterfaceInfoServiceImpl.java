package com.sleeve.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sleeve.project.constant.CommonConstant;
import com.sleeve.project.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import com.sleeve.project.model.entity.InterfaceInfo;
import com.sleeve.project.model.vo.InterfaceInfoVO;
import com.sleeve.project.service.InterfaceInfoService;
import com.sleeve.project.mapper.InterfaceInfoMapper;
import com.sleeve.project.utils.SqlUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author 散落星河
* @description 针对表【interface_info(接口信息)】的数据库操作Service实现
* @createDate 2025-01-13 00:03:04
*/
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
    implements InterfaceInfoService{


    @Override
    public InterfaceInfoVO getInterfaceInfoVO(InterfaceInfo interfaceInfo) {
        if (interfaceInfo == null) {
            return null;
        }
        return InterfaceInfoVO.objToVo(interfaceInfo);
    }

    @Override
    public QueryWrapper<InterfaceInfo> getQueryWrapper(InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        if (interfaceInfoQueryRequest == null) {
            return queryWrapper;
        }
        Long id = interfaceInfoQueryRequest.getId();
        String name = interfaceInfoQueryRequest.getName();
        String description = interfaceInfoQueryRequest.getDescription();
        String url = interfaceInfoQueryRequest.getUrl();
        String requestHeader = interfaceInfoQueryRequest.getRequestHeader();
        String responseHeader = interfaceInfoQueryRequest.getResponseHeader();
        String method = interfaceInfoQueryRequest.getMethod();
        Long userId = interfaceInfoQueryRequest.getUserId();
        String sortField = interfaceInfoQueryRequest.getSortField();
        String sortOrder = interfaceInfoQueryRequest.getSortOrder();

        // 拼接查询条件
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name);
        queryWrapper.like(StringUtils.isNotBlank(description), "description", description);
        queryWrapper.like(StringUtils.isNotBlank(url), "url", url);
        queryWrapper.like(StringUtils.isNotBlank(requestHeader), "requestHeader", requestHeader);
        queryWrapper.like(StringUtils.isNotBlank(responseHeader), "responseHeader", responseHeader);
        queryWrapper.eq(StringUtils.isNotBlank(method), "method", method);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public Page<InterfaceInfoVO> getInterfaceInfoVOPage(Page<InterfaceInfo> interfaceInfoPage) {
        long current = interfaceInfoPage.getCurrent();
        long size = interfaceInfoPage.getSize();
        long total = interfaceInfoPage.getTotal();
        Page<InterfaceInfoVO> infoVOPage = new Page<>(current, size, total);
        List<InterfaceInfoVO> collect = interfaceInfoPage.getRecords().stream().map(this::getInterfaceInfoVO).collect(Collectors.toList());
        infoVOPage.setRecords(collect);
        return infoVOPage;
    }
}




