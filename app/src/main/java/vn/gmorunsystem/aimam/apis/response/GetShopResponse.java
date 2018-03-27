package vn.gmorunsystem.aimam.apis.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HuongTrung on 6/6/2017.
 */

public class GetShopResponse {
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
    @SerializedName("description")
    public String description;
    @SerializedName("website")
    public String website;
    @SerializedName("phone")
    public String phone;
    @SerializedName("email")
    public String email;
    @SerializedName("is_subscribed")
    public Boolean isSubscribed;
    @SerializedName("opening_days")
    public String openingDays;
    @SerializedName("closing_days")
    public String closingDays;
    @SerializedName("opening_hours")
    public String openingHours;
    @SerializedName("closing_hours")
    public String closingHours;

}
