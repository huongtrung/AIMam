package vn.gmorunsystem.aimam.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by adm on 8/30/2017.
 */

public class ShopMenuItem {
    @SerializedName("id")
    public Integer id;
    @SerializedName("name")
    public String name;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
