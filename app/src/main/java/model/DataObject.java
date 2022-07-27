package model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by alex on 1/16/2018.
 */

public class DataObject {
    @SerializedName("plant_name")
    private String plant_name;
    public DataObject(){}

    public DataObject(String plant_name) {
        this.plant_name = plant_name;
    }

    public String getPlant_name() {
        return plant_name;
    }

    public void setPlant_name(String plant_name) {
        this.plant_name = plant_name;
    }
}
