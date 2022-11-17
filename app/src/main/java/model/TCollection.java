package model;

import com.google.gson.annotations.SerializedName;

public class TCollection {
    @SerializedName("transCode")
    private String transCode;
    @SerializedName("actualKg")
    private String actualKg;
    @SerializedName("date")
    private String date;
    @SerializedName("saccoCode")
    private String saccoCode;
    @SerializedName("branch")
    private String branch;
    @SerializedName("auditDate")
    private String auditDate;

    public TCollection(String transCode, String actualKg, String date, String saccoCode, String branch, String auditDate) {
        this.transCode = transCode;
        this.actualKg = actualKg;
        this.date = date;
        this.saccoCode = saccoCode;
        this.branch = branch;
        this.auditDate = auditDate;
    }

    public String getTransCode() {
        return transCode;
    }

    public void setTransCode(String transCode) {
        this.transCode = transCode;
    }

    public String getActualKg() {
        return actualKg;
    }

    public void setActualKg(String actualKg) {
        this.actualKg = actualKg;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSaccoCode() {
        return saccoCode;
    }

    public void setSaccoCode(String saccoCode) {
        this.saccoCode = saccoCode;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(String auditDate) {
        this.auditDate = auditDate;
    }
}
