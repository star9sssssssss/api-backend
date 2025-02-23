package com.sleeve.project.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sleeve.project.annotation.AuthCheck;
import com.sleeve.project.constant.UserConstant;
import com.sleeve.project.exception.BusinessException;
import com.sleeve.project.exception.ThrowUtils;
import com.sleeve.project.model.dto.userInterfaceInfo.UserInterfaceInfoAddRequest;
import com.sleeve.project.model.dto.userInterfaceInfo.UserInterfaceInfoQueryRequest;
import com.sleeve.project.model.dto.userInterfaceInfo.UserInterfaceInfoUpdateRequest;
import com.sleeve.project.model.entity.User;
import com.sleeve.project.model.entity.UserInterfaceInfo;
import com.sleeve.project.service.UserInterfaceInfoService;
import com.sleeve.project.service.UserService;
import com.sleeve.project.common.BaseResponse;
import com.sleeve.project.common.DeleteRequest;
import com.sleeve.project.common.ErrorCode;
import com.sleeve.project.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 维护用户与接口关系(调用次数)
 */
@RestController
@RequestMapping("/userInterfaceinfo")
@Slf4j
public class UserInterfaceInfoController {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Resource
    private UserService userService;

    /**
     * 创建
     *
     * @param userInterfaceInfoAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addUserInterfaceInfo(@RequestBody UserInterfaceInfoAddRequest userInterfaceInfoAddRequest, HttpServletRequest request) {
        if (userInterfaceInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        BeanUtils.copyProperties(userInterfaceInfoAddRequest, userInterfaceInfo);
        User loginUser = userService.getLoginUser(request);
        userInterfaceInfo.setUserId(loginUser.getId());
        boolean save = userInterfaceInfoService.save(userInterfaceInfo);
        ThrowUtils.throwIf(!save, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(userInterfaceInfo.getId());
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteUserInterfaceInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        UserInterfaceInfo oldUserInterface = userInterfaceInfoService.getById(id);
        ThrowUtils.throwIf(oldUserInterface == null, ErrorCode.NOT_FOUND_ERROR);
        boolean b = userInterfaceInfoService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新（仅管理员）
     *
     * @param userInterfaceInfoUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateUserInterfaceInfo(@RequestBody UserInterfaceInfoUpdateRequest userInterfaceInfoUpdateRequest) {
        if (userInterfaceInfoUpdateRequest == null || userInterfaceInfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = userInterfaceInfoUpdateRequest.getId();
        // 判断是否存在
        UserInterfaceInfo oldUserInterface = userInterfaceInfoService.getById(id);
        ThrowUtils.throwIf(oldUserInterface == null, ErrorCode.NOT_FOUND_ERROR);
        BeanUtils.copyProperties(userInterfaceInfoUpdateRequest, oldUserInterface);
        boolean result = userInterfaceInfoService.updateById(oldUserInterface);
        return ResultUtils.success(result);
    }


    /**
     * 分页获取列表（仅管理员）
     *
     * @param userInterfaceInfoQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<UserInterfaceInfo>> listUserInterfaceInfoByPage(@RequestBody UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest) {
        long current = userInterfaceInfoQueryRequest.getCurrent();
        long size = userInterfaceInfoQueryRequest.getPageSize();
        Page<UserInterfaceInfo> interfaceinfoPage = userInterfaceInfoService.page(new Page<>(current, size),
                userInterfaceInfoService.getQueryWrapper(userInterfaceInfoQueryRequest));
        return ResultUtils.success(interfaceinfoPage);
    }

}
