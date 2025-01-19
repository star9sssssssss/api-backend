package com.sleeve.project.provider;


public interface DemoService {
    String sayHello(String name);

    /**
     * 查询accessKey是否存在对应的用户，并返回用户id
     * @param accessKey ak
     * @return is
     */
    Long hasUser(String accessKey);


    /**
     * 根据accessKey获取对应的secretKey，如果无对应用户，返回 ""
     * @param accessKey ak
     * @return sk
     */
    String getSecretKey(String accessKey);


    /**
     * 根据interfaceInfoId确定是否存在对应的接口
     * @param interfaceInfoId 接口Id
     * @return is
     */
    Boolean hasInterface(Long interfaceInfoId);

    /**
     * 实现调用接口后记录次数
     * @param interfaceInfoId 接口Id
     * @param userId 用户Id
     * @return is
     */
    Boolean doInvokeCount(Long interfaceInfoId, Long userId);
}



/**
 *
 * 1.从数据库中查询ak和sk
 * 2.需要判断请求接口是否存在
 * 3.调用接口成功后进行相应的操作 (增加调用次数)
 *
 */