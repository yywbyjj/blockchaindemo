package net.sppan.base.dao;

import net.sppan.base.dao.support.IBaseDao;
import net.sppan.base.entity.License;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author yangkj
 * @since 2019/3/28
 */
@Repository
public interface LicenseDao extends IBaseDao<License,String> {

    @Query(value = "select photoUrl from License where id=?1")
    String findUrlById(String id);

    @Modifying
    @Query(value = "update License set checkCode=?1 where id=?2")
    void updateCheckById(Integer checkCode,String id);
}
