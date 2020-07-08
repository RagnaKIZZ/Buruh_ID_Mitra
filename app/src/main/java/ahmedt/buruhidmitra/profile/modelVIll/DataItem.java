package ahmedt.buruhidmitra.profile.modelVIll;


import com.google.gson.annotations.SerializedName;


public class DataItem {

    @SerializedName("name")
    private String name;

    @SerializedName("id")
    private long id;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return
                "DataItem{" +
                        "name = '" + name + '\'' +
                        ",id = '" + id + '\'' +
                        "}";
    }
}