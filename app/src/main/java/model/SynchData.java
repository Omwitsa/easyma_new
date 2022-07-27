package model;

import com.google.gson.annotations.SerializedName;

public class SynchData {

    @SerializedName("sup")
    private String sup;
    @SerializedName("qty")
    private String qty;
    @SerializedName("dates")
    private String dates;
    @SerializedName("shift")
    private String shift;
    @SerializedName("auditid")
    private String auditid;
    @SerializedName("branchhh")
    private String branchhh;
    @SerializedName("product")
    private String product;

    public SynchData(String sup, String qty, String dates, String shift, String auditid, String branchhh, String product) {
        this.sup = sup;
        this.qty = qty;
        this.dates = dates;
        this.shift = shift;
        this.auditid = auditid;
        this.branchhh = branchhh;
        this.product = product;
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

    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public String getAuditid() {
        return auditid;
    }

    public void setAuditid(String auditid) {
        this.auditid = auditid;
    }

    public String getBranchhh() {
        return branchhh;
    }

    public void setBranchhh(String branchhh) {
        this.branchhh = branchhh;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }
}
