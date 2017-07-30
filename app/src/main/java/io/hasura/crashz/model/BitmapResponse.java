package io.hasura.crashz.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HARIHARAN on 16-07-2017.
 */

public class BitmapResponse {
    @SerializedName("user_id")
    String user_id;

    @SerializedName("profile_image_link")
    String profile_image_link;

    public BitmapResponse(String user_id, String profile_image_link) {
        this.user_id = user_id;
        this.profile_image_link = profile_image_link;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getProfile_image_link() {
        return profile_image_link;
    }
}
