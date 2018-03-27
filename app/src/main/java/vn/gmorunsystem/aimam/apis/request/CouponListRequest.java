package vn.gmorunsystem.aimam.apis.request;

import com.android.volley.Request;

import java.util.Map;

import vn.gmorunsystem.aimam.apis.response.CouponListResponse;
import vn.gmorunsystem.aimam.apis.volley.core.ObjectApiRequest;
import vn.gmorunsystem.aimam.constants.APIConstant;

/**
 * Created by Veteran Commander on 6/1/2017.
 */

public class CouponListRequest extends ObjectApiRequest<CouponListResponse> {

    private int pageNum = 1;

    public CouponListRequest(int pageNum) {
        this.pageNum = pageNum;
    }

    @Override
    public String getRequestURL() {
        return APIConstant.COUPON_LIST_URL + pageNum;
    }

    @Override
    public Map<String, String> getRequestParams() {
        return null;
    }

    @Override
    public Class<CouponListResponse> getResponseClass() {
        return CouponListResponse.class;
    }

    @Override
    public int getMethod() {
        return Request.Method.GET;
    }
}
