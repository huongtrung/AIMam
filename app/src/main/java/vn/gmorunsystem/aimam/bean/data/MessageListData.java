package vn.gmorunsystem.aimam.bean.data;

import com.google.gson.annotations.SerializedName;

import vn.gmorunsystem.aimam.bean.ShopBean;

/**
 * Created by HuongTrung on 6/12/2017.
 */

public class MessageListData {
    @SerializedName("id")
    public Integer id;
    @SerializedName("title")
    public String title;
    @SerializedName("content")
    public String content;
    @SerializedName("shop")
    public ShopBean shop;
    @SerializedName("sent_date")
    public String sentDate;
}
