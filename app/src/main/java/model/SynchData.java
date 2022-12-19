package model;

import com.google.gson.annotations.SerializedName;

public class SynchData {
    @SerializedName("sup")
    private String sup;
    @SerializedName("qty")
    private String qty;
    @SerializedName("branchhh")
    private String branchhh;
    @SerializedName("dates")
    private String dates;
    @SerializedName("auditid")
    private String auditid;
    @SerializedName("product")
    private String product;
    @SerializedName("saccoCode")
    private String saccoCode;
    @SerializedName("shift")
    private String shift;

    public SynchData(String sup, String qty, String branchhh, String dates, String auditid, String product, String saccoCode, String shift) {
        this.sup = sup;
        this.qty = qty;
        this.branchhh = branchhh;
        this.dates = dates;
        this.auditid = auditid;
        this.product = product;
        this.saccoCode = saccoCode;
        this.shift = shift;
    }

    public String getSup() {
        return sup;
    }

    public void setSup(String sup) {
        this.sup = sup;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getBranchhh() {
        return branchhh;
    }

    public void setBranchhh(String branchhh) {
        this.branchhh = branchhh;
    }

    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }

    public String getAuditid() {
        return auditid;
    }

    public void setAuditid(String auditid) {
        this.auditid = auditid;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getSaccoCode() {
        return saccoCode;
    }

    public void setSaccoCode(String saccoCode) {
        this.saccoCode = saccoCode;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }
}
