package net.sppan.base.service.impl;

import com.alibaba.fastjson.JSON;
import net.sppan.base.common.utils.HttpClientResult;
import net.sppan.base.common.utils.HttpClientUtils;
import net.sppan.base.common.utils.MD5Utils;
import net.sppan.base.common.utils.PWD;
import net.sppan.base.dao.IUserDao;
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

    @Autowired
    private IUserDao userDao;

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
        license.setLicenseHash(license.getPhotoUrl());
        license.setCheckCode(LicenseCheckCode.CHECKED);
        licenseDao.save(license);
        String idCardNumber = userDao.findOne(license.getUserId()).getIdNumber();
        String owner = null;
        if (license.getMainExchange().equals(MainExchangeCode.PUBLICSECURITYBUREAU)){
            owner = "8998";
        }else if (license.getMainExchange().equals(MainExchangeCode.CIVILAFFAIRSBUREAU)){
            owner = "9819";
        }
        Map<String,String> params = new HashMap<>();
        params.put("$class","org.example.mynetwork.License");
        params.put("id",license.getId());
        params.put("licenseHash",license.getLicenseHash());
        params.put("idCardNumber",idCardNumber);
        params.put("mainExchange", license.getMainExchange().toString());
        params.put("owner","resource:org.example.mynetwork.Order#"+owner);
        System.out.println(JSON.toJSONString(params));
        HttpClientResult result = HttpClientUtils.doPost("http://119.29.174.167:3000/api/License", params);
        if (result.getCode() != StateCode.SUCCESSCODE){
            throw new NoClientBlockChainException("连接到区块链出错");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteLicense(String id) throws Exception{
        License license = licenseDao.findOne(id);
        license.setIsDelete(1);
        licenseDao.save(license);
        HttpClientResult result = HttpClientUtils.doDelete("http://119.29.174.167:3000/api/License/"+id);
        if (result.getCode() != StateCode.DELSUCCESSCODE){
            throw new NoClientBlockChainException("连接到区块链出错");
        }
    }
}
