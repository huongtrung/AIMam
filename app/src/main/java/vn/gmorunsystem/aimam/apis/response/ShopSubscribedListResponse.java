package vn.gmorunsystem.aimam.apis.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import vn.gmorunsystem.aimam.bean.SubscribeBean;

/**
 * Created by Veteran Commander on 6/5/2017.
 */

public class ShopSubscribedListResponse {
    @SerializedName("subscribes")
    public List<SubscribeBean> subscribeBean;
}
