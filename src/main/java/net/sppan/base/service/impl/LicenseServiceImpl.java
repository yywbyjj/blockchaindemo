package net.sppan.base.service.impl;

import net.sppan.base.common.utils.HttpClientResult;
import net.sppan.base.common.utils.HttpClientUtils;
import net.sppan.base.common.utils.MD5Utils;
import net.sppan.base.dao.LicenseDao;
import net.sppan.base.dao.support.IBaseDao;
import net.sppan.base.entity.License;
import net.sppan.base.entity.enu.LicenseCheckCode;
import net.sppan.base.entity.enu.MainExchangeCode;
import net.sppan.base.entity.enu.StateCode;
import net.sppan.base.service.LicenseService;
import net.sppan.base.service.NoClientBlockChainException;
import net.sppan.base.service.support.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class LicenseServiceImpl extends BaseServiceImpl<License,String> implements LicenseService {

    @Autowired
    private LicenseDao licenseDao;

    @Override
    public void updateCheckCode(Integer checkCode,String id) {
        licenseDao.updateCheckById(checkCode,id);
    }

    @Override
    public IBaseDao<License, String> getBaseDao() {
        return this.licenseDao;
    }

    @Override
    public String findUrlById(String id) {
        return licenseDao.findUrlById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void checkPassLicense(License license) throws Exception{
        license.setLicenseHash(MD5Utils.md5(license.getPhotoUrl()));
        license.setCheckCode(LicenseCheckCode.CHECKED);
        licenseDao.save(license);
        Map<String,String> params = new HashMap<>();
        params.put("$class","org.example.mynetwork.Commodity");
        params.put("licenseHash",license.getLicenseHash());
        params.put("ordererName",license.getOrdererName());
        params.put("mainExchange", "身份证");
        params.put("quantity","0");
        params.put("owner","resource:org.example.mynetwork.Trader#"+license.getUserId());
        HttpClientUtils.doPost("http://193.112.47.47:3000/api/Commodity",params);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteLicense(String id) throws Exception{
        String licenseHash = licenseDao.findOne(id).getLicenseHash();
        licenseDao.delete(id);
        System.out.println(licenseHash);
        HttpClientResult result = HttpClientUtils.doDelete("http://193.112.47.47:3000/api/Commodity/"+licenseHash);
        System.out.println(result.getCode());
        if (result.getCode() != StateCode.DELSUCCESSCODE){
            throw new NoClientBlockChainException("连接到区块链出错");
        }
    }
}
