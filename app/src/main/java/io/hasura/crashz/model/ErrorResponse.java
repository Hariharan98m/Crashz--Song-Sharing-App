package io.hasura.crashz.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HARIHARAN on 27-06-2017.
 */

public class ErrorResponse {
    @SerializedName("message")
    String message;

    public ErrorResponse(String message){
        this.message=message;
    }
    public String getMessage(){
        return message;
    }

}
