package vn.gmorunsystem.aimam.apis.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import vn.gmorunsystem.aimam.bean.ReviewBean;

/**
 * Created by tiena on 6/27/2017.
 */

public class ShopReviewResponse {
    @SerializedName("data")
    public List<ReviewBean> reviews;
    @SerializedName("shop_id")
    public Integer shopId;
    @SerializedName("name")
    public String name;
    @SerializedName("description")
    public String description;
    @SerializedName("vote")
    public Float vote;
    @SerializedName("like")
    public Integer like;
    @SerializedName("view")
    public Integer view;
    @SerializedName("avatar_image_url")
    public String avatarImageUrl;
    @SerializedName("total")
    public Integer total;
    @SerializedName("per_page")
    public Integer perPage;
    @SerializedName("total_pages")
    public Integer totalPages;
    @SerializedName("current_page")
    public Integer currentPage;
    @SerializedName("last_page")
    public Integer lastPage;
    @SerializedName("from")
    public Integer from;
    @SerializedName("to")
    public Integer to;


    public void setReviews(List<ReviewBean> reviews) {
        this.reviews = reviews;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setVote(Float vote) {
        this.vote = vote;
    }

    public void setLike(Integer like) {
        this.like = like;
    }

    public void setView(Integer view) {
        this.view = view;
    }

    public void setAvatarImageUrl(String avatarImageUrl) {
        this.avatarImageUrl = avatarImageUrl;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public void setPerPage(Integer perPage) {
        this.perPage = perPage;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public void setLastPage(Integer lastPage) {
        this.lastPage = lastPage;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public void setTo(Integer to) {
        this.to = to;
    }
}
