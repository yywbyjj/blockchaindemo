package net.sppan.base.entity;

import net.sppan.base.entity.support.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "license")
public class License extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", unique = true, nullable = false, length = 32)
    private String id;

    private String licenseHash;

    private String ordererName;

    /**
     * 电子证照类型
     * @see net.sppan.base.entity.enu.MainExchangeCode
     */
    private String mainExchange;

    private String userId;

    private String photoUrl;

    /**
     * 审核状态码
     * @see net.sppan.base.entity.enu.LicenseCheckCode
     */
    private Integer checkCode;

    private Integer isDelete;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLicenseHash() {
        return licenseHash;
    }

    public void setLicenseHash(String licenseHash) {
        this.licenseHash = licenseHash;
    }

    public String getOrdererName() {
        return ordererName;
    }

    public void setOrdererName(String ordererName) {
        this.ordererName = ordererName;
    }

    public String getMainExchange() {
        return mainExchange;
    }

    public void setMainExchange(String mainExchange) {
        this.mainExchange = mainExchange;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Integer getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(Integer checkCode) {
        this.checkCode = checkCode;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }
}
