package vn.gmorunsystem.aimam.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Veteran Commander on 6/1/2017.
 */

public class CouponDetailBean implements Parcelable {
    @SerializedName("id")
    public Integer id;
    @SerializedName("title")
    public String title;
    @SerializedName("content")
    public String content;
    @SerializedName("shop")
    public ShopBean shopBean;

    protected CouponDetailBean(Parcel in) {
        id = in.readInt();
        title = in.readString();
        content = in.readString();
        shopBean = ShopBean.CREATOR.createFromParcel(in);
    }

    public static final Creator<CouponDetailBean> CREATOR = new Creator<CouponDetailBean>() {
        @Override
        public CouponDetailBean createFromParcel(Parcel in) {
            return new CouponDetailBean(in);
        }

        @Override
        public CouponDetailBean[] newArray(int size) {
            return new CouponDetailBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(content);
        parcel.writeParcelable(shopBean, i);
    }
}
