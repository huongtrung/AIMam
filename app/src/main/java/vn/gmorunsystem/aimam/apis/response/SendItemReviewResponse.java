package vn.gmorunsystem.aimam.apis.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Veteran Commander on 6/12/2017.
 */

public class SendItemReviewResponse {
    @SerializedName("id")
    public Integer id;
    @SerializedName("user_name")
    public String userName;
    @SerializedName("user_id")
    public Integer userId;
    @SerializedName("content")
    public String content;
    @SerializedName("vote")
    public Float vote;
    @SerializedName("useful")
    public Integer useful;
    @SerializedName("item_id")
    public Integer itemId;
}
