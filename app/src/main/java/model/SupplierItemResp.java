package model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SupplierItemResp {
    @SerializedName("Success")
    private boolean success;
    @SerializedName("Message")
    private String Message;
    @SerializedName("Data")
    private List<SupplierItem> supplierItems;

    public SupplierItemResp(boolean success, String message, List<SupplierItem> supplierItems) {
        this.success = success;
        Message = message;
        this.supplierItems = supplierItems;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public List<SupplierItem> getSupplierItems() {
        return supplierItems;
    }

    public void setSupplierItems(List<SupplierItem> supplierItems) {
        this.supplierItems = supplierItems;
    }
}
