package vn.gmorunsystem.aimam.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Veteran Commander on 6/20/2017.
 */

public class ImageInfoUploadBean {
    @SerializedName("item_id")
    public Integer itemId;
    @SerializedName("shop_id")
    public Integer shopId;
    @SerializedName("hashtags")
    public List<HashtagBean> hashtags;

    public ImageInfoUploadBean() {
        hashtags = new ArrayList<>();
    }
}
