package ahmedt.buruhidmitra.login.modellogin;

import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("tukang_id")
    private String tukangId;

    @SerializedName("anggota")
    private String anggota;

    @SerializedName("nama")
    private String nama;

    @SerializedName("foto")
    private String foto;

    @SerializedName("telepon")
    private String telepon;

    @SerializedName("rating")
    private String rating;

    @SerializedName("token_login")
    private String tokenLogin;

    @SerializedName("login")
    private String login;

    @SerializedName("aktivasi")
    private String aktivasi;

    @SerializedName("email")
    private String email;

    @SerializedName("aktif")
    private String aktif;

    @SerializedName("alamat")
    private String alamat;

    public String getTukangId() {
        return tukangId;
    }

    public String getAnggota() {
        return anggota;
    }

    public String getNama() {
        return nama;
    }

    public String getFoto() {
        return foto;
    }

    public String getTelepon() {
        return telepon;
    }

    public String getRating() {
        return rating;
    }

    public String getTokenLogin() {
        return tokenLogin;
    }

    public String getLogin() {
        return login;
    }

    public String getAktivasi() {
        return aktivasi;
    }

    public String getEmail() {
        return email;
    }

    public String getAktif() {
        return aktif;
    }

    public String getAlamat() {
        return alamat;
    }
}