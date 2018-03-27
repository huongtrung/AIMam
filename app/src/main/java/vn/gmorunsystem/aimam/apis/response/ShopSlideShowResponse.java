package vn.gmorunsystem.aimam.apis.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import vn.gmorunsystem.aimam.bean.ShopSlideShowBean;

/**
 * Created by HuongTrung on 6/21/2017.
 */

public class ShopSlideShowResponse {
    @SerializedName("data")
    public List<ShopSlideShowBean> shopSlideShowBean;
}
