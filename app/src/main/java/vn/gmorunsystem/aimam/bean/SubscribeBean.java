package vn.gmorunsystem.aimam.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Veteran Commander on 6/5/2017.
 */

public class SubscribeBean {
    @SerializedName("id")
    public Integer id;
    @SerializedName("shop_id")
    public Integer shopId;
    @SerializedName("name")
    public String name;
    @SerializedName("avatar_image_url")
    public String avatarImageUrl;
    @SerializedName("like")
    public Integer like;
    @SerializedName("view")
    public Integer view;
    @SerializedName("created_at")
    public String createdAt;
    @SerializedName("slideshows")
    public List<ShopSlideShowBean> slideshows;
}
