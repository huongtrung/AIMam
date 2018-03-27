package vn.gmorunsystem.aimam.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HuongTrung on 6/19/2017.
 */

public class ShopItemBean {
    @SerializedName("id")
    public Integer id;
    @SerializedName("name")
    public String name;
    @SerializedName("vote")
    public Float vote;
    @SerializedName("avatar_image_url")
    public String avatarImageUrl;
    @SerializedName("inserted_date")
    public String insertedDate;
}
