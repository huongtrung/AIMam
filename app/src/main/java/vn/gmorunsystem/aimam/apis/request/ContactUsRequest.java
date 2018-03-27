package vn.gmorunsystem.aimam.apis.request;

import com.android.volley.Request;

import java.util.HashMap;
import java.util.Map;

import vn.gmorunsystem.aimam.apis.response.ContactUsResponse;
import vn.gmorunsystem.aimam.apis.volley.core.ObjectApiRequest;
import vn.gmorunsystem.aimam.constants.APIConstant;

/**
 * Created by HuongTrung on 6/8/2017.
 */

public class ContactUsRequest extends ObjectApiRequest<ContactUsResponse> {
    private String username;
    private String phoneNum;
    private int shopId;
    private String titleMess;
    private String contentMess;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public void setTitleMess(String titleMess) {
        this.titleMess = titleMess;
    }

    public void setContentMess(String contentMess) {
        this.contentMess = contentMess;
    }

    @Override
    public String getRequestURL() {
        return APIConstant.CONTACT_US_URL;
    }

    @Override
    public Map<String, String> getRequestParams() {
        Map<String, String> params = new HashMap<>();
        params.put(APIConstant.USER_NAME, username);
        params.put(APIConstant.USER_PHONE, phoneNum);
        params.put(APIConstant.SHOP_ID, String.valueOf(shopId));
        params.put(APIConstant.TITLE, titleMess);
        params.put(APIConstant.CONTENT, contentMess);
        return params;
    }

    @Override
    public Class<ContactUsResponse> getResponseClass() {
        return ContactUsResponse.class;
    }

    @Override
    public int getMethod() {
        return Request.Method.POST;
    }
}
