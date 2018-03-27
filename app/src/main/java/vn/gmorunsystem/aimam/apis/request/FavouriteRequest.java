package vn.gmorunsystem.aimam.apis.request;

import com.android.volley.Request;

import java.util.HashMap;
import java.util.Map;

import vn.gmorunsystem.aimam.apis.response.FavouriteResponse;
import vn.gmorunsystem.aimam.apis.volley.core.ObjectApiRequest;
import vn.gmorunsystem.aimam.constants.APIConstant;

/**
 * Created by Veteran Commander on 6/19/2017.
 */

public class FavouriteRequest extends ObjectApiRequest<FavouriteResponse> {
    int itemId;

    public FavouriteRequest(int itemId) {
        this.itemId = itemId;
    }

    @Override
    public String getRequestURL() {
        return APIConstant.FAVOURTIE_URL;
    }

    @Override
    public Map<String, String> getRequestParams() {
        Map<String, String> params = new HashMap<>();
        params.put(APIConstant.ITEM_ID, String.valueOf(itemId));
        return params;
    }

    @Override
    public Class<FavouriteResponse> getResponseClass() {
        return FavouriteResponse.class;
    }

    @Override
    public int getMethod() {
        return Request.Method.POST;
    }
}
