package vn.gmorunsystem.aimam.bean;

import com.google.gson.annotations.SerializedName;

import vn.gmorunsystem.aimam.bean.data.ShopLocationBean;

/**
 * Created by HuongTrung on 09/01/2017.
 */

public class NewFeedBean {
    @SerializedName("shop_id")
    public Integer shopId;
    @SerializedName("shop_name")
    public String shopName;
    @SerializedName("shop_address")
    public String shopAddress;
    @SerializedName("type")
    public Integer type;
    @SerializedName("shop_location")
    public ShopLocationBean shopLocation;
    @SerializedName("image_url")
    public String imageUrl;
    @SerializedName("item_id")
    public Integer itemId;
    @SerializedName("item_name")
    public String itemName;
    @SerializedName("description")
    public String description;
    @SerializedName("coupon_id")
    public Integer couponId;
    @SerializedName("coupon_name")
    public String couponName;
}
