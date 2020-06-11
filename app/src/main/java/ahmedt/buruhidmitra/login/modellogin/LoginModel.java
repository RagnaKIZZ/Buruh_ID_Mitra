package ahmedt.buruhidmitra.login.modellogin;

import com.google.gson.annotations.SerializedName;

public class LoginModel {

    @SerializedName("msg")
    private String msg;

    @SerializedName("code")
    private int code;

    @SerializedName("data")
    private Data data;

    public String getMsg() {
        return msg;
    }

    public int getCode() {
        return code;
    }

    public Data getData() {
        return data;
    }
}