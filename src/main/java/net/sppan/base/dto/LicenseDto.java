package net.sppan.base.dto;

public class LicenseDto {

    private String id;
    private String licenseHash;
    private String ordererName;
    private String mainExchange;
    private String userId;
    private String photoUrl;
    private Integer checkCode;
    private Integer isDelete;
    private String userName;

    public LicenseDto(String id, String licenseHash, String ordererName, String mainExchange, String userId, String photoUrl, Integer checkCode, Integer isDelete, String userName) {
        this.id = id;
        this.licenseHash = licenseHash;
        this.ordererName = ordererName;
        this.mainExchange = mainExchange;
        this.userId = userId;
        this.photoUrl = photoUrl;
        this.checkCode = checkCode;
        this.isDelete = isDelete;
        this.userName = userName;
    }

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
