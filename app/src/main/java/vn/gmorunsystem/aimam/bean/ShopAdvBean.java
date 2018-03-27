package vn.gmorunsystem.aimam.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Veteran Commander on 6/14/2017.
 */

public class ShopAdvBean implements Parcelable {
    @SerializedName("id")
    public Integer id;
    @SerializedName("name")
    public String name;
    @SerializedName("address")
    public String address;
    @SerializedName("avatar_image_url")
    public String avatarImageUrl;
    @SerializedName("map_latitude")
    public Double mapLatitude;
    @SerializedName("map_longtitude")
    public Double mapLongtitude;

    protected ShopAdvBean(Parcel in) {
        id = in.readInt();
        name = in.readString();
        address = in.readString();
        avatarImageUrl = in.readString();
        mapLatitude = in.readDouble();
        mapLongtitude = in.readDouble();
    }

    public static final Creator<ShopAdvBean> CREATOR = new Creator<ShopAdvBean>() {
        @Override
        public ShopAdvBean createFromParcel(Parcel in) {
            return new ShopAdvBean(in);
        }

        @Override
        public ShopAdvBean[] newArray(int size) {
            return new ShopAdvBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(address);
        parcel.writeString(avatarImageUrl);
        parcel.writeDouble(mapLatitude);
        parcel.writeDouble(mapLongtitude);
    }
}
