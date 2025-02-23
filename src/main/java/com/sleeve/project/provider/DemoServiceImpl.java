package com.sleeve.project.provider;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sleeve.project.mapper.InterfaceInfoMapper;
import com.sleeve.project.mapper.UserMapper;
import com.sleeve.project.model.entity.InterfaceInfo;
import com.sleeve.project.model.entity.User;
import com.sleeve.project.service.UserInterfaceInfoService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class DemoServiceImpl implements DemoService{

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Resource
    private InterfaceInfoMapper interfaceInfoMapper;

    @Override
    public String sayHello(String name) {
        System.out.println("正在调用api-backend的sayHello 参数是 " + name);
        return "sleeve" + name;
    }

    @Override
    public Long hasUser(String accessKey) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("accessKey", accessKey);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            return -1L;
        }
        return user.getId();
    }


    @Override
    public String getSecretKey(String accessKey) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("accessKey", accessKey);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            return "";
        }
        return user.getSecretKey();
    }

    @Override
    public Boolean hasInterface(Long interfaceInfoId) {
        InterfaceInfo interfaceInfo = interfaceInfoMapper.selectById(interfaceInfoId);
        return interfaceInfo != null;
    }

    @Override
    public Boolean doInvokeCount(Long interfaceInfoId, Long userId) {
        return userInterfaceInfoService.invokeCount(interfaceInfoId, userId);
    }
}
