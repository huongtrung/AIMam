package vn.gmorunsystem.aimam.apis.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import vn.gmorunsystem.aimam.bean.CouponDetailBean;

/**
 * Created by Veteran Commander on 6/1/2017.
 */

public class CouponListResponse {
    @SerializedName("data")
    public List<CouponDetailBean> couponDetailList;
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
