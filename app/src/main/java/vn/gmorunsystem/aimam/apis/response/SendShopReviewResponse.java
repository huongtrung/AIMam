package vn.gmorunsystem.aimam.apis.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by adm on 6/28/2017.
 */

public class SendShopReviewResponse {
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
    @SerializedName("shop_id")
    public Integer shopId;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setVote(Float vote) {
        this.vote = vote;
    }

    public void setUseful(Integer useful) {
        this.useful = useful;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }
}
