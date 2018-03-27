package vn.gmorunsystem.aimam.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Veteran Commander on 6/13/2017.
 */

public class ImageProfileBean {
    @SerializedName("id")
    public Integer imageId;
    @SerializedName("item_id")
    public Integer itemId;
    @SerializedName("shop_id")
    public Integer shopId;
    @SerializedName("url")
    public String imageAvartar;
}
