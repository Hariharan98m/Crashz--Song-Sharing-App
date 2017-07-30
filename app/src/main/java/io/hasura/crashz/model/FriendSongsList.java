package io.hasura.crashz.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by HARIHARAN on 11-07-2017.
 */

public class FriendSongsList {

    @SerializedName("song_id")
    int song_id;

    @SerializedName("composer_name")
    String composer_name;

    @SerializedName("song_name")
    String song_name;

    @SerializedName("song_link")
    String song_link;

    @SerializedName("num_of_comments")
    int num_of_comments;

    @SerializedName("num_of_likes")
    int num_of_likes;

    @SerializedName("created")
    Date date_created;

    @SerializedName("user_id")
    int user_id;

    public int getSong_id() {
        return song_id;
    }

    public String getComposer_name() {
        return composer_name;
    }

    public String getSong_name() {
        return song_name;
    }

    public String getSong_link() {
        return song_link;
    }

    public int getNum_of_comments() {
        return num_of_comments;
    }

    public int getNum_of_likes() {
        return num_of_likes;
    }

    public Date getDate_created() {
        return date_created;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setNum_of_comments(int num_of_comments) {
        this.num_of_comments = num_of_comments;
    }

    public void setNum_of_likes(int num_of_likes) {
        this.num_of_likes = num_of_likes;
    }
}
