package vn.gmorunsystem.aimam.apis.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import vn.gmorunsystem.aimam.bean.HomeFavoriteBean;
import vn.gmorunsystem.aimam.bean.ShopAdvBean;
import vn.gmorunsystem.aimam.bean.ShopSuggestBean;

/**
 * Created by Veteran Commander on 6/14/2017.
 */

public class GetHomeResponse {
    @SerializedName("shop_suggests")
    public List<ShopSuggestBean> shopSuggests;
    @SerializedName("shop_advs")
    public List<ShopAdvBean> shopAdvs;
    @SerializedName("favourite_items")
    public List<HomeFavoriteBean> favouriteItems;
}
