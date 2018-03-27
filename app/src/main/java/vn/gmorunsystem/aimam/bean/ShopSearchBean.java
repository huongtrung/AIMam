package vn.gmorunsystem.aimam.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HuongTrung on 6/19/2017.
 */

public class ShopSearchBean {
    @SerializedName("id")
    public Integer id;
    @SerializedName("name")
    public String name;
    @SerializedName("address")
    public String address;
    @SerializedName("avatar_image_url")
    public String avatarImageUrl;
    @SerializedName("map_latitude")
    public Float mapLatitude;
    @SerializedName("map_longtitude")
    public Float mapLongtitude;


    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getAvatarImageUrl() {
        return avatarImageUrl;
    }

    public Float getMapLatitude() {
        return mapLatitude;
    }

    public Float getMapLongtitude() {
        return mapLongtitude;
    }
}
