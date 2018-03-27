package vn.gmorunsystem.aimam.apis.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import vn.gmorunsystem.aimam.bean.ShopAdvBean;
import vn.gmorunsystem.aimam.bean.ShopItemBean;
import vn.gmorunsystem.aimam.bean.ShopSearchBean;
import vn.gmorunsystem.aimam.bean.ShopSuggestBean;

/**
 * Created by HuongTrung on 6/19/2017.
 */

public class ShopSearchResponse {
    @SerializedName("shop_search")
    public List<ShopSearchBean> shopSearch;
    @SerializedName("shop_suggest")
    public List<ShopSuggestBean> shopSuggest;
    @SerializedName("shop_advs")
    public List<ShopAdvBean> shopAdv;
    @SerializedName("shop_items")
    public List<ShopItemBean> shopItems;
}
