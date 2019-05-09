package net.sppan.base.dao;

import net.sppan.base.dto.LicenseDto;
import net.sppan.base.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LicenseDaoImpl {

    @PersistenceContext
    private EntityManager entityManager;

    private static String admin = "00000000000000000000000000000000";

    public Page<LicenseDto> findAllLicense(List<String> names,String searchText,Pageable pageable){
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        String sql = "select license.id,license_hash,orderer_name,main_exchange,user_id,photo_url,check_code," +
                "is_delete,user_name from license left join tb_user on user_id = tb_user.id where is_delete='0'";
        String countSql = "select count(*) from license left join tb_user on user_id = tb_user.id where is_delete='0'";
        if (!admin.equals(user.getId())&&!names.isEmpty()){
            sql += " and orderer_name in ('" + StringUtils.join(names,"','") + "')";
        }
        if (searchText!=null){
            sql += " and user_name like '" +searchText+ "%'";
        }
        sql += " order by check_code asc ";
        System.out.println(sql);
        Query query = entityManager.createNativeQuery(sql);
        Query countQuery = entityManager.createNativeQuery(countSql);
        //设置分页
        query.setFirstResult(pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        Integer count = Integer.valueOf( countQuery.getSingleResult().toString()).intValue();
        Long total = count.longValue();
        List<Object[]> licenseDtos = query.getResultList();
        List<LicenseDto> result = getLicenseDtos(licenseDtos);
        return new PageImpl<LicenseDto>(result, pageable, total);
    }

    private List<LicenseDto> getLicenseDtos(List<Object[]> licenseDtos) {
        return licenseDtos.stream().map(x -> {
                LicenseDto licenseDto = new LicenseDto();
                licenseDto.setId(x[0].toString());
                if (x[1] != null) {
                    licenseDto.setLicenseHash(x[1].toString());
                }
                if (x[2] != null) {
                    licenseDto.setOrdererName(x[2].toString());
                }
                if (x[3] != null) {
                    licenseDto.setMainExchange(x[3].toString());
                }
                if (x[4] != null) {
                    licenseDto.setUserId(x[4].toString());
                }
                if (x[5] != null) {
                    licenseDto.setPhotoUrl(x[5].toString());
                }
                if (x[6] != null) {
                    licenseDto.setCheckCode(Integer.parseInt(x[6].toString()));
                }
                if (x[7] != null) {
                    licenseDto.setIsDelete(Integer.parseInt(x[7].toString()));
                }
                if (x[8] != null) {
                    licenseDto.setUserName(x[8].toString());
                }
                return licenseDto;
            }).collect(Collectors.toList());
    }
}
