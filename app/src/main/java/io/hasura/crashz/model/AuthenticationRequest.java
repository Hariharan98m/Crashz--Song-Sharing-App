package io.hasura.crashz.model;

import com.google.gson.annotations.SerializedName;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by HARIHARAN on 27-06-2017.
 */

public class AuthenticationRequest {
    @SerializedName("username")
    String username;

    @SerializedName("password")
    String password;

    @SerializedName("mobile")
    String mobile;

    @SerializedName("otp")
    String otp;

    public AuthenticationRequest(String username, String password, String mobile, String otp){
        this.username= username;
        this.password=password;
        this.mobile=mobile;
        this.otp=otp;
    }


    public AuthenticationRequest(String username, String password, String mobile) {
        this.username = username;
        this.password = password;
        this.mobile = mobile;
    }

    public AuthenticationRequest(String id, String password) {

        Pattern pattern = Pattern.compile("[7-9][0-9]{9}");
        Matcher matcher = pattern.matcher(id);
        if (matcher.matches() && id.length()==10) {
            this.mobile=id;
        }
        else
            this.username = id;
        this.password = password;
    }


}
