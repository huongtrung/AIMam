package vn.gmorunsystem.aimam.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HuongTrung on 6/5/2017.
 */

public class ShopNewBean {
    @SerializedName("name")
    public String name;
    @SerializedName("description")
    public String description;
    @SerializedName("like")
    public Integer like;
    @SerializedName("view")
    public Integer view;
    @SerializedName("vote")
    public Float vote;
    @SerializedName("id")
    public Integer id;
    @SerializedName("avatar_image_url")
    public String avatarImageUrl;
    @SerializedName("price")
    public Float price;
}
