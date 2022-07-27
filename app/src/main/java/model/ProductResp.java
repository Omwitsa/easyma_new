package model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductResp {
    @SerializedName("Success")
    private boolean success;
    @SerializedName("Message")
    private String Message;
    @SerializedName("Data")
    private List<String> Products;

    public ProductResp(boolean success, String message, List<String> products) {
        this.success = success;
        Message = message;
        Products = products;
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

    public List<String> getProducts() {
        return Products;
    }

    public void setProducts(List<String> products) {
        Products = products;
    }
}
