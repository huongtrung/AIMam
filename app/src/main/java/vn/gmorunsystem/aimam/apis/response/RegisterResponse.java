package vn.gmorunsystem.aimam.apis.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Veteran Commander on 5/29/2017.
 */

public class RegisterResponse {
    @SerializedName("id")
    public int userId;
    @SerializedName("full_name")
    public String userName;

}
