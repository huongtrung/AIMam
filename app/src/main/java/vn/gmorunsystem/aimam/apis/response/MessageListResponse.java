package vn.gmorunsystem.aimam.apis.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import vn.gmorunsystem.aimam.bean.data.MessageListData;

/**
 * Created by HuongTrung on 6/12/2017.
 */

public class MessageListResponse {
    @SerializedName("data")
    public List<MessageListData> data;
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
