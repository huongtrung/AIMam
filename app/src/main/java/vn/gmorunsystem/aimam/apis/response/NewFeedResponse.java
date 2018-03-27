package vn.gmorunsystem.aimam.apis.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import vn.gmorunsystem.aimam.bean.NewFeedBean;

/**
 * Created by HuongTrung on 09/01/2017.
 */

public class NewFeedResponse {
    @SerializedName("data")
    public List<NewFeedBean> data;
}
