package vn.gmorunsystem.aimam.apis.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import vn.gmorunsystem.aimam.bean.HashtagBean;
import vn.gmorunsystem.aimam.bean.ImageProfileBean;

/**
 * Created by Veteran Commander on 6/8/2017.
 */

public class ProfileResponse {
    @SerializedName("full_name")
    public String fullName;
    @SerializedName("avatar_image_url")
    public String avatarImageUrl;
    @SerializedName("post")
    public Integer post;
    @SerializedName("useful")
    public Integer useful;
    @SerializedName("sex")
    public Integer sex;
    @SerializedName("job")
    public String job;
    @SerializedName("address")
    public String address;
    @SerializedName("website")
    public String website;
    @SerializedName("description")
    public String description;
    @SerializedName("birthday")
    public String birthday;
    @SerializedName("hashtags")
    public List<HashtagBean> hashtags;
    @SerializedName("images")
    public List<ImageProfileBean> images;
}
