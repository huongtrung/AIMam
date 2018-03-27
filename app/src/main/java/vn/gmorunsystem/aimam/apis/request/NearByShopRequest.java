package vn.gmorunsystem.aimam.apis.request;

import com.android.volley.Request;
import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.Map;

import vn.gmorunsystem.aimam.apis.response.NearByShopResponse;
import vn.gmorunsystem.aimam.apis.volley.core.ObjectApiRequest;
import vn.gmorunsystem.aimam.constants.APIConstant;

/**
 * Created by Veteran Commander on 6/6/2017.
 */

public class NearByShopRequest extends ObjectApiRequest<NearByShopResponse> {
    private LatLng topLeftLatLng;
    private LatLng botRightLatLng;

    public NearByShopRequest(LatLng topLeft, LatLng botRight) {
        topLeftLatLng = topLeft;
        botRightLatLng = botRight;
    }

    @Override
    public String getRequestURL() {
        return APIConstant.SHOP_NEAR_URL;
    }

    @Override
    public Map<String, String> getRequestParams() {
        Map<String, String> params = new HashMap<>();
        params.put(APIConstant.TOP_LEFT_LAT, String.valueOf(topLeftLatLng.latitude));
        params.put(APIConstant.TOP_LEFT_LONG, String.valueOf(topLeftLatLng.longitude));
        params.put(APIConstant.BOT_RIGHT_LAT, String.valueOf(botRightLatLng.latitude));
        params.put(APIConstant.BOT_RIGHT_LONG, String.valueOf(botRightLatLng.longitude));
        return params;
    }

    @Override
    public Class<NearByShopResponse> getResponseClass() {
        return NearByShopResponse.class;
    }

    @Override
    public int getMethod() {
        return Request.Method.POST;
    }
}
