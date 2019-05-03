package net.sppan.base.controller.admin.system;

import net.sppan.base.common.JsonResult;
import net.sppan.base.controller.BaseController;
import net.sppan.base.dao.LicenseDao;
import net.sppan.base.dto.LicenseDto;
import net.sppan.base.entity.License;
import net.sppan.base.entity.User;
import net.sppan.base.entity.enu.LicenseCheckCode;
import net.sppan.base.service.IUserService;
import net.sppan.base.service.LicenseService;
import net.sppan.base.service.NoClientBlockChainException;
import net.sppan.base.service.specification.SimpleSpecificationBuilder;
import net.sppan.base.service.specification.SpecificationOperator;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
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
    public JsonResult checkPass(String id) throws Exception{
        License license = licenseService.find(id);
        licenseService.checkPassLicense(license);
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
