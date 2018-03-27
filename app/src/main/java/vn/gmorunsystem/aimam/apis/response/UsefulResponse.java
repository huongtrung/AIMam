package vn.gmorunsystem.aimam.apis.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by adm on 7/4/2017.
 */

public class UsefulResponse {
    @SerializedName("id")
    public Integer id;
    @SerializedName("user_id")
    public Integer userId;
    @SerializedName("review_id")
    public Integer reviewId;
}
