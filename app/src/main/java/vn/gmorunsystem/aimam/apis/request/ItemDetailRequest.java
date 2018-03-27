package vn.gmorunsystem.aimam.apis.request;

import com.android.volley.Request;

import java.util.Map;

import vn.gmorunsystem.aimam.apis.response.ItemDetailResponse;
import vn.gmorunsystem.aimam.apis.volley.core.ObjectApiRequest;
import vn.gmorunsystem.aimam.constants.APIConstant;

/**
 * Created by Veteran Commander on 6/9/2017.
 */

public class ItemDetailRequest extends ObjectApiRequest<ItemDetailResponse> {
    private int itemId;
    private int page;

    public ItemDetailRequest(int itemId, int page) {
        this.itemId = itemId;
        this.page = page;
    }

    @Override
    public String getRequestURL() {
        return APIConstant.DETAIL_ITEM_URL + itemId + "?page=" + page;
    }

    @Override
    public Map<String, String> getRequestParams() {
        return null;
    }

    @Override
    public Class<ItemDetailResponse> getResponseClass() {
        return ItemDetailResponse.class;
    }

    @Override
    public int getMethod() {
        return Request.Method.GET;
    }
}
