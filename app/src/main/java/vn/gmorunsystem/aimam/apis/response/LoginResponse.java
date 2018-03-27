package vn.gmorunsystem.aimam.apis.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HuongTrung on 5/26/2017.
 */

public class LoginResponse {
    @SerializedName("id")
    public Integer userId;
    @SerializedName("full_name")
    public String fullName;
    @SerializedName("avatar_image_url")
    public String avatarImageUrl;
    @SerializedName("coupon")
    public Integer coupon;
    @SerializedName("message")
    public Integer message;
}
