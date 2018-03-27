package vn.gmorunsystem.aimam.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HuongTrung on 6/5/2017.
 */

public class ShopCouponBean {
    @SerializedName("id")
    public Integer id;
    @SerializedName("code")
    public String code;
    @SerializedName("title")
    public String title;
    @SerializedName("content")
    public String content;
    @SerializedName("expiry_date")
    public String expiryDate;
    @SerializedName("avatar_image_url")
    public String avatarImageUrl;
}
