package vn.gmorunsystem.aimam.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Veteran Commander on 6/9/2017.
 */

public class ReviewBean {
    @SerializedName("id")
    public Integer id;
    @SerializedName("user_name")
    public String userName;
    @SerializedName("user_id")
    public Integer userId;
    @SerializedName("user_avatar_url")
    public String userAvartarUrl;
    @SerializedName("content")
    public String content;
    @SerializedName("vote")
    public Float vote;
    @SerializedName("useful")
    public Integer useful;
    @SerializedName("review_date")
    public String reviewDate;
    @SerializedName("voted_useful")
    public Integer votedUseful;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setUserAvartarUrl(String userAvartarUrl) {
        this.userAvartarUrl = userAvartarUrl;
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

    public void setReviewDate(String reviewDate) {
        this.reviewDate = reviewDate;
    }

    public void setVotedUseful(Integer votedUseful) {
        this.votedUseful = votedUseful;
    }
}
