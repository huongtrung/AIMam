package vn.gmorunsystem.aimam.apis.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import vn.gmorunsystem.aimam.bean.ShopAdvBean;
import vn.gmorunsystem.aimam.bean.ShopSuggestBean;

/**
 * Created by Veteran Commander on 6/6/2017.
 */

public class NearByShopResponse {
    @SerializedName("shop_suggests")
    public List<ShopSuggestBean> shopSuggests;
    @SerializedName("shop_advs")
    public List<ShopAdvBean> shopAdvs;
}
