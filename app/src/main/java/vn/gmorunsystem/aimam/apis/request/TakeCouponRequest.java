package vn.gmorunsystem.aimam.apis.request;

import com.android.volley.Request;

import java.util.Map;

import vn.gmorunsystem.aimam.apis.response.TakeCouponResponse;
import vn.gmorunsystem.aimam.apis.volley.core.ObjectApiRequest;
import vn.gmorunsystem.aimam.constants.APIConstant;

/**
 * Created by HuongTrung on 6/7/2017.
 */

public class TakeCouponRequest extends ObjectApiRequest<TakeCouponResponse> {
    private int couponId;

    public TakeCouponRequest(int couponId) {
        this.couponId = couponId;
    }

    @Override
    public String getRequestURL() {
        return APIConstant.TAKE_COUPON_URL + couponId + APIConstant.TAKE_COUPON;
    }

    @Override
    public Map<String, String> getRequestParams() {
        return null;
    }

    @Override
    public Class<TakeCouponResponse> getResponseClass() {
        return TakeCouponResponse.class;
    }

    @Override
    public int getMethod() {
        return Request.Method.POST;
    }
}
