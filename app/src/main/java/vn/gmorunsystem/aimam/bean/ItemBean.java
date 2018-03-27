package vn.gmorunsystem.aimam.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import vn.gmorunsystem.aimam.bean.data.ImageBean;

/**
 * Created by HuongTrung on 08/24/2017.
 */

public class ItemBean {
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
    @SerializedName("shop_id")
    public Integer shopId;
    @SerializedName("avatar_image_url")
    public String avatarImageUrl;
    @SerializedName("shop_name")
    public String shopName;
    @SerializedName("images")
    public List<ImageBean> images;

    public void setLike(Integer like) {
        this.like = like;
    }
}
