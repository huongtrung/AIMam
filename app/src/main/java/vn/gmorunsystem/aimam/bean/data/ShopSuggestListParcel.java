package vn.gmorunsystem.aimam.bean.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import vn.gmorunsystem.aimam.bean.ShopSuggestBean;

/**
 * Created by Veteran Commander on 6/16/2017.
 */

public class ShopSuggestListParcel implements Parcelable {
    public List<ShopSuggestBean> beanList;

    protected ShopSuggestListParcel(Parcel in) {
        beanList = in.createTypedArrayList(ShopSuggestBean.CREATOR);
    }

    public ShopSuggestListParcel() {
        beanList = new ArrayList<>();
    }

    public void add(ShopSuggestBean shopSuggestBean) {
        if (beanList != null) {
            beanList.add(shopSuggestBean);
        }
    }

    public ShopSuggestListParcel(List<ShopSuggestBean> list) {
        beanList = list;
    }

    public static final Creator<ShopSuggestListParcel> CREATOR = new Creator<ShopSuggestListParcel>() {
        @Override
        public ShopSuggestListParcel createFromParcel(Parcel in) {
            return new ShopSuggestListParcel(in);
        }

        @Override
        public ShopSuggestListParcel[] newArray(int size) {
            return new ShopSuggestListParcel[size];
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
