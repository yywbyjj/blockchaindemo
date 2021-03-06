package net.sppan.base.controller.admin.system;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import net.sppan.base.common.JsonResult;
import net.sppan.base.common.utils.HttpClientResult;
import net.sppan.base.common.utils.HttpClientUtils;
import net.sppan.base.common.utils.MD5Utils;
import net.sppan.base.common.utils.PWD;
import net.sppan.base.controller.BaseController;
import net.sppan.base.dao.LicenseDao;
import net.sppan.base.dto.LicenseApiDto;
import net.sppan.base.dto.LicenseDto;
import net.sppan.base.entity.License;
import net.sppan.base.entity.User;
import net.sppan.base.entity.enu.LicenseCheckCode;
import net.sppan.base.entity.enu.StateCode;
import net.sppan.base.service.IUserService;
import net.sppan.base.service.LicenseService;
import net.sppan.base.service.NoClientBlockChainException;
import net.sppan.base.service.specification.SimpleSpecificationBuilder;
import net.sppan.base.service.specification.SpecificationOperator;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import sun.security.provider.MD5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author yangkj
 * @since 2019/3/28
 */
@Controller
@RequestMapping("/admin/license")
public class LicenseController extends BaseController {

    @Autowired
    private LicenseService licenseService;

    @Autowired
    private LicenseDao licenseDao;

    @Autowired
    private IUserService userService;

    @RequestMapping(value = { "/", "/index" })
    public String index() {
        return "admin/license/index";
    }

    @RequestMapping(value = { "/list" })
    @ResponseBody
    public Page<LicenseDto> list() {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        List<String> names = new ArrayList<>();
        if (!user.getRoles().isEmpty()){
            names = user.getRoles().stream().map(x -> {
                String name = x.getName();
                return name;
            }).collect(Collectors.toList());
        }
        String searchText = request.getParameter("searchText");
        Page<LicenseDto> page = licenseDao.findAllLicense(names,searchText,getPageRequest());
        return page;
    }

    @RequestMapping(value = "/search")
    @ResponseBody
    public JsonResult searchList(String idNumber,ModelMap map) throws Exception{
        HttpClientResult result = HttpClientUtils.doGet("http://119.29.174.167:3000/api/queries/selectLicensesByIdCardNumber?idCardNumber="+idNumber);
        List<LicenseApiDto> licenseApiDtos = JSONObject.parseArray(result.getContent(), LicenseApiDto.class);
        List<String> collect = licenseApiDtos.stream().map(x -> {
            String url = x.getLicenseHash();
            return url;
        }).collect(Collectors.toList());
        return JsonResult.success("查询成功",collect);
    }

    @GetMapping(value = "/check/{id}")
    public String checkLicense(@PathVariable String id, ModelMap map){
        License license = licenseService.find(id);
        String userId = license.getUserId();
        User user = userService.find(userId);
        map.put("license",license);
        map.put("user",user);
        return "admin/license/form";
    }

    @PostMapping(value = "/checking")
    @ResponseBody
    public JsonResult checking(String id){
        licenseService.updateCheckCode(LicenseCheckCode.CHECK,id);
        return JsonResult.success();
    }

    @PostMapping(value = "/checkPass")
    @ResponseBody
    public JsonResult checkPass(String id){
        License license = licenseService.find(id);
        try {
            licenseService.checkPassLicense(license);
        }catch (NoClientBlockChainException e){
            return JsonResult.failure(e.getMessage());
        }catch (Exception e){
            return JsonResult.failure("未知的错误");
        }
        return JsonResult.success();
    }

    @PostMapping(value = "/deleteLicense")
    @ResponseBody
    public JsonResult deleteLicense(String id){
        try {
            licenseService.deleteLicense(id);
        }catch (NoClientBlockChainException e){
            return JsonResult.failure(e.getMessage());
        }catch (Exception e){
            return JsonResult.failure("未知的错误");
        }
        return JsonResult.success();
    }
}
