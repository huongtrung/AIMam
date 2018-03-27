package vn.gmorunsystem.aimam.bean.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import vn.gmorunsystem.aimam.bean.ShopAdvBean;

/**
 * Created by Veteran Commander on 6/16/2017.
 */

public class ShopAdvListParcel implements Parcelable {
    public List<ShopAdvBean> beanList;

    protected ShopAdvListParcel(Parcel in) {
        beanList = in.createTypedArrayList(ShopAdvBean.CREATOR);
    }

    public ShopAdvListParcel() {
        beanList = new ArrayList<>();
    }

    public ShopAdvListParcel(List<ShopAdvBean> list) {
        beanList = list;
    }

    public void add(ShopAdvBean shopAdvBean) {
        if (beanList != null) {
            beanList.add(shopAdvBean);
        }
    }

    public static final Creator<ShopAdvListParcel> CREATOR = new Creator<ShopAdvListParcel>() {
        @Override
        public ShopAdvListParcel createFromParcel(Parcel in) {
            return new ShopAdvListParcel(in);
        }

        @Override
        public ShopAdvListParcel[] newArray(int size) {
            return new ShopAdvListParcel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(beanList);
    }
}
