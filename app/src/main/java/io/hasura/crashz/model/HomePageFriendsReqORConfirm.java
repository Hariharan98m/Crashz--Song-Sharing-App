package io.hasura.crashz.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HARIHARAN on 04-07-2017.
 */

public class HomePageFriendsReqORConfirm {
    @SerializedName("your_id")
    int your_id;

    @SerializedName("your_friend_id")
    int your_friend_id;

    @SerializedName("friend_name")
    String friend_name;

    @SerializedName("friend_work")
    String friend_work;

    @SerializedName("friend_city")
    String friend_city;

    @SerializedName("friend_music")
    String friend_music;

    @SerializedName("friend_desc")
    String friend_desc;

    @SerializedName("friend_passion")
    String friend_passion;

    @SerializedName("profile_image_link")
    String profile_image_link;

    public int getYour_id() {
        return your_id;
    }

    public String getProfile_image_link() {
        return profile_image_link;
    }

    public int getYour_friend_id() {
        return your_friend_id;
    }

    public String getFriend_name() {
        return friend_name;
    }

    public String getFriend_work() {
        return friend_work;
    }

    public String getFriend_city() {
        return friend_city;
    }

    public String getFriend_music() {
        return friend_music;
    }

    public String getFriend_desc() {
        return friend_desc;
    }

    public String getFriend_passion() {
        return friend_passion;
    }
}
