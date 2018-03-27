package vn.gmorunsystem.aimam.apis.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Veteran Commander on 6/19/2017.
 */

public class FavouriteResponse {
    @SerializedName("favourite_id")
    public Integer favouriteId;
    @SerializedName("user_id")
    public Integer userId;
    @SerializedName("item_id")
    public Integer itemId;
}
