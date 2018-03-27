package vn.gmorunsystem.aimam.bean.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Veteran Commander on 6/9/2017.
 */

public class ImageBean {
    @SerializedName("id")
    public Integer id;
    @SerializedName("item_id")
    public Integer itemId;
    @SerializedName("shop_id")
    public Integer shopId;
    @SerializedName("avatar_image_url")
    public String avatarImageUrl;
}
