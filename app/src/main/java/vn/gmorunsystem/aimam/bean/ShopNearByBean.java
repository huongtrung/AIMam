package vn.gmorunsystem.aimam.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Veteran Commander on 6/7/2017.
 */

public class ShopNearByBean {
    @SerializedName("id")
    public Integer id;
    @SerializedName("name")
    public String name;
    @SerializedName("address")
    public String address;
    @SerializedName("avatar_image_url")
    public String avatarImageUrl;
    @SerializedName("map_latitude")
    public Double mapLatitude;
    @SerializedName("map_longtitude")
    public Double mapLongtitude;
}
