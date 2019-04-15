package net.sppan.base.service;

import net.sppan.base.entity.License;
import net.sppan.base.service.support.IBaseService;

/**
 * @author yangkj
 * @since 2019/3/28
 */
public interface LicenseService extends IBaseService<License,String> {

    /**
     *更具id查找证照的url
     */
    String findUrlById(String id);

    /**
     * 审核证照
     */
    void updateCheckCode(Integer checkCode,String id);

    /**
     * 审核通过并且将数据记录到区块链中
     */
    void checkPassLicense(License license) throws Exception;
}
