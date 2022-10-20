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

    public TCollection(String transCode, String actualKg, String date, String saccoCode) {
        this.transCode = transCode;
        this.actualKg = actualKg;
        this.date = date;
        this.saccoCode = saccoCode;
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
}
