package vn.gmorunsystem.aimam.bean.data;

import android.os.Parcel;
import android.os.Parcelable;

import vn.gmorunsystem.aimam.apis.response.ProfileResponse;

/**
 * Created by adm on 7/10/2017.
 */

public class ProfileData implements Parcelable {
    public String fullName;
    public String website;
    public String intro;
    public String birthday;
    public Integer gender;
    public String address;
    public String work;

    public ProfileData(ProfileResponse data) {
        this.fullName = data.fullName;
        this.website = data.website;
        this.intro = data.description;
        this.birthday = data.birthday;
        this.gender = data.sex;
        this.address = data.address;
        this.work = data.job;
    }

    protected ProfileData(Parcel in) {
        fullName = in.readString();
        website = in.readString();
        intro = in.readString();
        birthday = in.readString();
        address = in.readString();
        work = in.readString();
        gender = in.readInt();
    }

    public static final Creator<ProfileData> CREATOR = new Creator<ProfileData>() {
        @Override
        public ProfileData createFromParcel(Parcel in) {
            return new ProfileData(in);
        }

        @Override
        public ProfileData[] newArray(int size) {
            return new ProfileData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(fullName);
        parcel.writeString(website);
        parcel.writeString(intro);
        parcel.writeString(birthday);
        parcel.writeString(address);
        parcel.writeString(work);
        parcel.writeInt(gender);
    }
}
