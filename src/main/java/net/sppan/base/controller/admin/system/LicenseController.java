package net.sppan.base.controller.admin.system;

import net.sppan.base.common.JsonResult;
import net.sppan.base.common.utils.MD5Utils;
import net.sppan.base.controller.BaseController;
import net.sppan.base.entity.License;
import net.sppan.base.entity.User;
import net.sppan.base.entity.enu.LicenseCheckCode;
import net.sppan.base.service.LicenseService;
import net.sppan.base.service.specification.SimpleSpecificationBuilder;
import net.sppan.base.service.specification.SpecificationOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

/**
 * @author yangkj
 * @since 2019/3/28
 */
@Controller
@RequestMapping("/admin/license")
public class LicenseController extends BaseController {

    @Autowired
    private LicenseService licenseService;

    @RequestMapping(value = { "/", "/index" })
    public String index() {
        return "admin/license/index";
    }

    @RequestMapping(value = { "/list" })
    @ResponseBody
    public Page<License> list() {
        SimpleSpecificationBuilder<License> builder = new SimpleSpecificationBuilder<License>();
        String searchText = request.getParameter("searchText");
        if(StringUtils.isNotBlank(searchText)){
            builder.add("ordererName", SpecificationOperator.Operator.likeAll.name(), searchText);
        }
        Page<License> page = licenseService.findAll(builder.generateSpecification(), getPageRequest());
        return page;
    }

    @GetMapping(value = "/check/{id}")
    public String checkLicense(@PathVariable String id, ModelMap map){
        License license = licenseService.find(id);
        map.put("license",license);
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
}