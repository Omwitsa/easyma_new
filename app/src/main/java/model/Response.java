package model;

import com.google.gson.annotations.SerializedName;

public class Response {
    @SerializedName("Success")
    private boolean success;
    @SerializedName("Message")
    private String Message;
    @SerializedName("Data")
    private String Data;

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
        this.Message = message;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        this.Data = data;
    }

    public Response(boolean success, String message, String data) {
        this.success = success;
        this.Message = message;
        this.Data = data;
    }
}
