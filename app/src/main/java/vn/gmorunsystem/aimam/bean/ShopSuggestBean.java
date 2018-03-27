package vn.gmorunsystem.aimam.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Veteran Commander on 6/14/2017.
 */

public class ShopSuggestBean implements Comparable<ShopSuggestBean>, Parcelable {
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
    @SerializedName("matching_rate")
    public Double matchingRate;

    protected ShopSuggestBean(Parcel in) {
        id = in.readInt();
        name = in.readString();
        address = in.readString();
        avatarImageUrl = in.readString();
        mapLatitude = in.readDouble();
        mapLongtitude = in.readDouble();
        matchingRate = in.readDouble();

    }

    public static final Creator<ShopSuggestBean> CREATOR = new Creator<ShopSuggestBean>() {
        @Override
        public ShopSuggestBean createFromParcel(Parcel in) {
            return new ShopSuggestBean(in);
        }

        @Override
        public ShopSuggestBean[] newArray(int size) {
            return new ShopSuggestBean[size];
        }
    };

    @Override
    public int compareTo(@NonNull ShopSuggestBean shopSuggestBean) {
        Double otherRating = shopSuggestBean.matchingRate;
        if (matchingRate == otherRating)
            return 0;
        else if (matchingRate > otherRating)
            return -1;
        else
            return 1;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(avatarImageUrl);
        parcel.writeString(address);
        parcel.writeDouble(mapLatitude);
        parcel.writeDouble(mapLongtitude);
        parcel.writeDouble(matchingRate);

    }
}

