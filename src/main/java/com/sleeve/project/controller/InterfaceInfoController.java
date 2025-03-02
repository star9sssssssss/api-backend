package com.sleeve.project.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sleeve.project.annotation.AuthCheck;
import com.sleeve.project.constant.UserConstant;
import com.sleeve.project.exception.BusinessException;
import com.sleeve.project.exception.ThrowUtils;
import com.sleeve.project.model.dto.interfaceinfo.InterfaceInfoAddRequest;
import com.sleeve.project.model.dto.interfaceinfo.InterfaceInfoInvokeRequest;
import com.sleeve.project.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import com.sleeve.project.model.dto.interfaceinfo.InterfaceInfoUpdateRequest;
import com.sleeve.project.model.entity.InterfaceInfo;
import com.sleeve.project.model.entity.User;
import com.sleeve.project.model.enums.InterfaceInfoStatusEnum;
import com.sleeve.project.model.vo.InterfaceInfoVO;
import com.sleeve.project.service.InterfaceInfoService;
import com.sleeve.project.service.UserService;
import com.sleeve.project.common.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.example.apiclientsdk.client.ApiClient;
import org.example.apiclientsdk.common.ApiException;
import org.example.apiclientsdk.common.BaseRequest;
import org.example.apiclientsdk.common.CurrencyRequest;
import org.example.apiclientsdk.model.params.NameParam;
import org.example.apiclientsdk.model.request.NameRequest;
import org.example.apiclientsdk.model.response.NameResponse;
import org.example.apiclientsdk.service.ApiService;
import org.example.apiclientsdk.service.impl.ApiServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 帖子接口
 *
 
 */
@RestController
@RequestMapping("/interfaceinfo")
@Slf4j
public class InterfaceInfoController {

    @Resource
    private InterfaceInfoService interfaceinfoService;

    @Resource
    private UserService userService;

    /**
     * 创建
     *
     * @param interfaceinfoAddRequest 添加请求
     * @param request http
     * @return 接口id
     */
    @PostMapping("/add")
    public BaseResponse<Long> addInterfaceInfo(@RequestBody InterfaceInfoAddRequest interfaceinfoAddRequest, HttpServletRequest request) {
        if (interfaceinfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceinfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceinfoAddRequest, interfaceinfo);
        User loginUser = userService.getLoginUser(request);
        interfaceinfo.setUserId(loginUser.getId());
        boolean result = interfaceinfoService.save(interfaceinfo);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newInterfaceInfoId = interfaceinfo.getId();
        // TODO id 是否更新
        return ResultUtils.success(newInterfaceInfoId);
    }

    /**
     * 删除
     *
     * @param deleteRequest 删除请求
     * @param request http
     * @return 是否成功
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteInterfaceInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceinfoService.getById(id);
        ThrowUtils.throwIf(oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = interfaceinfoService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新（仅管理员）
     *
     * @param interfaceinfoUpdateRequest 更新请求
     * @return 更新结果
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateInterfaceInfo(@RequestBody InterfaceInfoUpdateRequest interfaceinfoUpdateRequest) {
        if (interfaceinfoUpdateRequest == null || interfaceinfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = interfaceinfoUpdateRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceinfoService.getById(id);
        ThrowUtils.throwIf(oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        BeanUtils.copyProperties(interfaceinfoUpdateRequest, oldInterfaceInfo);
        oldInterfaceInfo.setUpdateTime(DateTime.now());
        boolean result = interfaceinfoService.updateById(oldInterfaceInfo);
        return ResultUtils.success(result);
    }


    /**
     * 上线接口
     * @param idRequest id 接口id
     * @return 上线结果
     */
    @PostMapping("/online")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> onlineInterfaceInfo(@RequestBody IDRequest idRequest) {
        if (idRequest == null || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = idRequest.getId();
        // 1. 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceinfoService.getById(id);
        ThrowUtils.throwIf(oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        // 2. 验证接口的可行性 TODO
        // 3. 上线接口
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setId(id);
        interfaceInfo.setStatus(InterfaceInfoStatusEnum.ONLINE.getValue());
        boolean result = interfaceinfoService.updateById(interfaceInfo);
        return ResultUtils.success(result);
    }

    /**
     * 下线接口
     * @param idRequest id 接口id
     * @return 下线结果
     */
    @PostMapping("/offline")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> offlineInterfaceInfo(@RequestBody IDRequest idRequest) {
        if (idRequest == null || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = idRequest.getId();
        // 1. 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceinfoService.getById(id);
        ThrowUtils.throwIf(oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        // 3. 下线接口
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setId(id);
        interfaceInfo.setStatus(InterfaceInfoStatusEnum.OFFLINE.getValue());
        boolean result = interfaceinfoService.updateById(interfaceInfo);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id id
     * @return 接口
     */
    @GetMapping("/get/vo")
    public BaseResponse<InterfaceInfoVO> getInterfaceInfoVOById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceinfo = interfaceinfoService.getById(id);
        if (interfaceinfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(interfaceinfoService.getInterfaceInfoVO(interfaceinfo));
    }

    /**
     * 测试调用
     *
     * @param invokeRequest 调用请求
     * @param request http
     * @return 调用结果
     */
    @PostMapping("/invoke")
    // 这里给它新封装一个参数InterfaceInfoInvokeRequest
    // 返回结果把对象发出去就好了，因为不确定接口的返回值到底是什么
    public BaseResponse<Object> invokeInterfaceInfo(@RequestBody InterfaceInfoInvokeRequest invokeRequest,
                                                    HttpServletRequest request) {
        // 检查请求对象是否为空或者接口id是否小于等于0
        if (invokeRequest == null || invokeRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 获取接口id
        long id = invokeRequest.getId();
        // 获取用户请求参数
        String userRequestParams = invokeRequest.getUserRequestParams();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceinfoService.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 检查接口状态是否为下线状态
        if (oldInterfaceInfo.getStatus().equals(InterfaceInfoStatusEnum.OFFLINE.getValue())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口已关闭");
        }
        // 调用接口
        User loginUser = userService.getLoginUser(request);
        String accessKey = loginUser.getAccessKey();
        String secretKey = loginUser.getSecretKey();
        // 创建调用接口的客户端，给定ak，sk
        ApiClient apiClient = new ApiClient(accessKey, secretKey, id);
        ApiServiceImpl apiService = new ApiServiceImpl();
        apiService.setApiClient(apiClient);
        // 设置本次请求的方式，url，和参数
        CurrencyRequest currencyRequest = new CurrencyRequest();
        currencyRequest.setMethod(oldInterfaceInfo.getMethod());
        currencyRequest.setPath(oldInterfaceInfo.getUrl());
        currencyRequest.setRequestParams(userRequestParams);
        try {
            // 获得响应结果
            org.example.apiclientsdk.common.BaseResponse response = apiService.reuqest(apiClient, currencyRequest);
            return ResultUtils.success(response);
        } catch (ApiException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, e.getMessage());
        }
    }



    /**
     * 分页获取列表（仅管理员）
     *
     * @param interfaceinfoQueryRequest 分页请求
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<InterfaceInfo>> listInterfaceInfoByPage(@RequestBody InterfaceInfoQueryRequest interfaceinfoQueryRequest) {
        long current = interfaceinfoQueryRequest.getCurrent();
        long size = interfaceinfoQueryRequest.getPageSize();
        Page<InterfaceInfo> interfaceinfoPage = interfaceinfoService.page(new Page<>(current, size),
                interfaceinfoService.getQueryWrapper(interfaceinfoQueryRequest));
        return ResultUtils.success(interfaceinfoPage);
    }

    /**
     * 分页获取列表（封装类）
     *
     * @param interfaceinfoQueryRequest 分页请求
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<InterfaceInfoVO>> listInterfaceInfoVOByPage(@RequestBody InterfaceInfoQueryRequest interfaceinfoQueryRequest) {
        long current = interfaceinfoQueryRequest.getCurrent();
        long size = interfaceinfoQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<InterfaceInfo> interfaceinfoPage = interfaceinfoService.page(new Page<>(current, size),
                interfaceinfoService.getQueryWrapper(interfaceinfoQueryRequest));
        return ResultUtils.success(interfaceinfoService.getInterfaceInfoVOPage(interfaceinfoPage));
    }

    /**
     * 分页获取当前用户创建的资源列表
     *
     * @param interfaceinfoQueryRequest 分页请求
     * @param request http
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<InterfaceInfoVO>> listMyInterfaceInfoVOByPage(@RequestBody InterfaceInfoQueryRequest interfaceinfoQueryRequest,
            HttpServletRequest request) {
        if (interfaceinfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        interfaceinfoQueryRequest.setUserId(loginUser.getId());
        long current = interfaceinfoQueryRequest.getCurrent();
        long size = interfaceinfoQueryRequest.getPageSize();
        Page<InterfaceInfo> interfaceinfoPage = interfaceinfoService.page(new Page<>(current, size),
                interfaceinfoService.getQueryWrapper(interfaceinfoQueryRequest));
        return ResultUtils.success(interfaceinfoService.getInterfaceInfoVOPage(interfaceinfoPage));
    }




//    /**
//     * 编辑（用户）
//     *
//     * @param interfaceinfoEditRequest
//     * @param request
//     * @return
//     */
//    @PostMapping("/edit")
//    public BaseResponse<Boolean> editInterfaceInfo(@RequestBody InterfaceInfoEditRequest interfaceinfoEditRequest, HttpServletRequest request) {
//        if (interfaceinfoEditRequest == null || interfaceinfoEditRequest.getId() <= 0) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR);
//        }
//        InterfaceInfo interfaceinfo = new InterfaceInfo();
//        BeanUtils.copyProperties(interfaceinfoEditRequest, interfaceinfo);
//        List<String> tags = interfaceinfoEditRequest.getTags();
//        if (tags != null) {
//            interfaceinfo.setTags(JSONUtil.toJsonStr(tags));
//        }
//        // 参数校验
//        interfaceinfoService.validInterfaceInfo(interfaceinfo, false);
//        User loginUser = userService.getLoginUser(request);
//        long id = interfaceinfoEditRequest.getId();
//        // 判断是否存在
//        InterfaceInfo oldInterfaceInfo = interfaceinfoService.getById(id);
//        ThrowUtils.throwIf(oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
//        // 仅本人或管理员可编辑
//        if (!oldInterfaceInfo.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
//            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
//        }
//        boolean result = interfaceinfoService.updateById(interfaceinfo);
//        return ResultUtils.success(result);
//    }

}
