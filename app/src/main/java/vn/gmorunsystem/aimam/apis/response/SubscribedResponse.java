package vn.gmorunsystem.aimam.apis.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HuongTrung on 6/8/2017.
 */

public class SubscribedResponse {
    @SerializedName("id")
    public Integer id;
    @SerializedName("user_id")
    public Integer userId;
    @SerializedName("shop_id")
    public Integer shopId;
}
