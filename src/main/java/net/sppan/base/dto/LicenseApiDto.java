package net.sppan.base.dto;

public class LicenseApiDto {

    private String $class;
    private String id;
    private String licenseHash;
    private String idCardNumber;
    private String mainExchange;
    private String owner;

    public LicenseApiDto(){}

    public LicenseApiDto(String $class, String id, String licenseHash, String idCardNumber, String mainExchange, String owner) {
        this.$class = $class;
        this.id = id;
        this.licenseHash = licenseHash;
        this.idCardNumber = idCardNumber;
        this.mainExchange = mainExchange;
        this.owner = owner;
    }

    public String get$class() {
        return $class;
    }

    public void set$class(String $class) {
        this.$class = $class;
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

    public String getIdCardNumber() {
        return idCardNumber;
    }

    public void setIdCardNumber(String idCardNumber) {
        this.idCardNumber = idCardNumber;
    }

    public String getMainExchange() {
        return mainExchange;
    }

    public void setMainExchange(String mainExchange) {
        this.mainExchange = mainExchange;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
