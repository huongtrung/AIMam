package vn.gmorunsystem.aimam.apis.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import vn.gmorunsystem.aimam.bean.ItemBean;
import vn.gmorunsystem.aimam.bean.ReviewBean;

/**
 * Created by Veteran Commander on 6/9/2017.
 */

public class ItemDetailResponse {
    @SerializedName("item")
    public ItemBean item;
    @SerializedName("item_reviews")
    public List<ReviewBean> itemReviews;
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
}
