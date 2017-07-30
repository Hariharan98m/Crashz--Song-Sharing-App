package io.hasura.crashz.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HARIHARAN on 11-07-2017.
 */

public class SongComments {
    @SerializedName("comment_text")
    public String comment_text;

    @SerializedName("user_name")
    public String user_name;
}
