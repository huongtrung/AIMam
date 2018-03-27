package vn.gmorunsystem.aimam.apis.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import vn.gmorunsystem.aimam.bean.ShopNewBean;
import vn.gmorunsystem.aimam.bean.ShopCouponBean;
import vn.gmorunsystem.aimam.bean.ShopSpecialBean;

/**
 * Created by HuongTrung on 6/5/2017.
 */

public class ShopHotResponse {
    @SerializedName("coupons")
    public List<ShopCouponBean> coupons;
    @SerializedName("special_items")
    public List<ShopSpecialBean> specialItems;
    @SerializedName("new_items")
    public List<ShopNewBean> newItems;
}
