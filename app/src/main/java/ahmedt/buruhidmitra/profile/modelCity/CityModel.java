package ahmedt.buruhidmitra.profile.modelCity;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class CityModel {

    @SerializedName("code")
    private int code;

    @SerializedName("data")
    private List<DataItem> data;

    @SerializedName("success")
    private boolean success;

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setData(List<DataItem> data) {
        this.data = data;
    }

    public List<DataItem> getData() {
        return data;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    @Override
    public String toString() {
        return
                "CityModel{" +
                        "code = '" + code + '\'' +
                        ",data = '" + data + '\'' +
                        ",success = '" + success + '\'' +
                        "}";
    }
}