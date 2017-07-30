package io.hasura.crashz.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HARIHARAN on 27-06-2017.
 */

public class MessageResponse {
    @SerializedName("auth_token")
    String auth_token;

    @SerializedName("hasura_roles")
    String[] hasura_roles;

    @SerializedName("hasura_id")
    int hasura_id;

    public int getHasura_id() {
        return hasura_id;
    }
}
