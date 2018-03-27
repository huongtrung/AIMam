package vn.gmorunsystem.aimam.apis.request;

import com.android.volley.Request;

import java.util.Map;

import vn.gmorunsystem.aimam.apis.response.CouponDetailResponse;
import vn.gmorunsystem.aimam.apis.volley.core.ObjectApiRequest;
import vn.gmorunsystem.aimam.constants.APIConstant;

/**
 * Created by Veteran Commander on 6/2/2017.
 */

public class CouponDetailRequest extends ObjectApiRequest<CouponDetailResponse> {

    private int couponId;

    public CouponDetailRequest(int id) {
        couponId = id;
    }

    @Override
    public String getRequestURL() {
        return APIConstant.COUPON_DETAIL_URL + couponId;
    }

    @Override
    public Map<String, String> getRequestParams() {
        return null;
    }

    @Override
    public Class<CouponDetailResponse> getResponseClass() {
        return CouponDetailResponse.class;
    }

    @Override
    public int getMethod() {
        return Request.Method.GET;
    }
}
