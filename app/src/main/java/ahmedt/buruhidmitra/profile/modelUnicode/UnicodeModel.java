package ahmedt.buruhidmitra.profile.modelUnicode;


import com.google.gson.annotations.SerializedName;


public class UnicodeModel {

    @SerializedName("code")
    private int code;

    @SerializedName("success")
    private boolean success;

    @SerializedName("token")
    private String token;

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    @Override
    public String toString() {
        return
                "UnicodeModel{" +
                        "code = '" + code + '\'' +
                        ",success = '" + success + '\'' +
                        ",token = '" + token + '\'' +
                        "}";
    }
}