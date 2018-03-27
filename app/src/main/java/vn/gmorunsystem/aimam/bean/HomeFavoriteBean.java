package vn.gmorunsystem.aimam.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Veteran Commander on 6/14/2017.
 */

public class HomeFavoriteBean {
    @SerializedName("id")
    public Integer id;
    @SerializedName("name")
    public String name;
    @SerializedName("avatar_image_url")
    public String avatarImageUrl;
    @SerializedName("inserted_date")
    public String insertedDate;
}
