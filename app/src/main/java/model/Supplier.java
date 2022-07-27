package model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Supplier {
    @SerializedName("SNo")
    private int SNo;
    @SerializedName("Names")
    private String Names;
    @SerializedName("IdNo")
    private String IdNo;
    @SerializedName("PhoneNo")
    private String PhoneNo;
    @SerializedName("Email")
    private String Email;
    @SerializedName("Branch")
    private String Branch;
    @SerializedName("BBranch")
    private String BBranch;
    @SerializedName("Address")
    private String Address;
    @SerializedName("Town")
    private String Town;
    @SerializedName("District")
    private String District;
    @SerializedName("Division")
    private String Division;
    @SerializedName("Location")
    private String Location;
    @SerializedName("Village")
    private String Village;
    @SerializedName("dob")
    private Date dob;
    @SerializedName("Regdate")
    private Date Regdate;

    public Supplier(int SNo, String names, String idNo, String phoneNo, String email, String branch, String BBranch, String address, String town, String district, String division, String location, String village, Date dob, Date regdate) {
        this.SNo = SNo;
        Names = names;
        IdNo = idNo;
        PhoneNo = phoneNo;
        Email = email;
        Branch = branch;
        this.BBranch = BBranch;
        Address = address;
        Town = town;
        District = district;
        Division = division;
        Location = location;
        Village = village;
        this.dob = dob;
        Regdate = regdate;
    }

    public int getSNo() {
        return SNo;
    }

    public void setSNo(int SNo) {
        this.SNo = SNo;
    }

    public String getNames() {
        return Names;
    }

    public void setNames(String names) {
        Names = names;
    }

    public String getIdNo() {
        return IdNo;
    }

    public void setIdNo(String idNo) {
        IdNo = idNo;
    }

    public String getPhoneNo() {
        return PhoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        PhoneNo = phoneNo;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getBranch() {
        return Branch;
    }

    public void setBranch(String branch) {
        Branch = branch;
    }

    public String getBBranch() {
        return BBranch;
    }

    public void setBBranch(String BBranch) {
        this.BBranch = BBranch;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getTown() {
        return Town;
    }

    public void setTown(String town) {
        Town = town;
    }

    public String getDistrict() {
        return District;
    }

    public void setDistrict(String district) {
        District = district;
    }

    public String getDivision() {
        return Division;
    }

    public void setDivision(String division) {
        Division = division;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getVillage() {
        return Village;
    }

    public void setVillage(String village) {
        Village = village;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public Date getRegdate() {
        return Regdate;
    }

    public void setRegdate(Date regdate) {
        Regdate = regdate;
    }
}
