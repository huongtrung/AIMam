package vn.gmorunsystem.aimam.apis.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import vn.gmorunsystem.aimam.bean.ShopBean;

/**
 * Created by Veteran Commander on 6/2/2017.
 */

public class CouponDetailResponse implements Parcelable {
    @SerializedName("id")
    public Integer id;
    @SerializedName("title")
    public String title;
    @SerializedName("content")
    public String content;
    @SerializedName("code")
    public String code;
    @SerializedName("expiry_date")
    public String expiryDate;
    @SerializedName("avatar_image_url")
    public String avatarImageUrl;
    @SerializedName("is_took")
    public Boolean isTook;
    @SerializedName("shop_id")
    public Integer shopId;
    @SerializedName("shop")
    public ShopBean shop;

    private CouponDetailResponse(Parcel in) {
        id = in.readInt();
        title = in.readString();
        content = in.readString();
        code = in.readString();
        expiryDate = in.readString();
        avatarImageUrl = in.readString();
        shop = in.readParcelable(ShopBean.class.getClassLoader());
    }

    public static final Creator<CouponDetailResponse> CREATOR = new Creator<CouponDetailResponse>() {
        @Override
        public CouponDetailResponse createFromParcel(Parcel in) {
            return new CouponDetailResponse(in);
        }

        @Override
        public CouponDetailResponse[] newArray(int size) {
            return new CouponDetailResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(content);
        parcel.writeString(code);
        parcel.writeString(expiryDate);
        parcel.writeString(avatarImageUrl);
        parcel.writeParcelable(shop, i);
    }
}
