package vn.gmorunsystem.aimam.apis.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import vn.gmorunsystem.aimam.bean.ShopStampBean;

public class ShopStampResponse {
    @SerializedName("stamp_count")
    public Integer stampCount;
    @SerializedName("stamp_number_limit")
    public Integer stampLimit;
    @SerializedName("stamps")
    public List<ShopStampBean> stamps;
}
