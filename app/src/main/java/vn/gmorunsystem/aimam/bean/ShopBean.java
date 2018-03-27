package vn.gmorunsystem.aimam.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Veteran Commander on 6/1/2017.
 */

public class ShopBean implements Parcelable {
    @SerializedName("name")
    public String name;
    @SerializedName("logo")
    public String logo;
    @SerializedName("address")
    public String address;

    protected ShopBean(Parcel in) {
        name = in.readString();
        logo = in.readString();
        address = in.readString();
    }

    public static final Creator<ShopBean> CREATOR = new Creator<ShopBean>() {
        @Override
        public ShopBean createFromParcel(Parcel in) {
            return new ShopBean(in);
        }

        @Override
        public ShopBean[] newArray(int size) {
            return new ShopBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(logo);
        parcel.writeString(address);
    }
}
