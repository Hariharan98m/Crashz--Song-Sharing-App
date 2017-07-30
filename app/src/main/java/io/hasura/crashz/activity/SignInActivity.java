package io.hasura.crashz.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.util.SortedList;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;

import io.hasura.crashz.R;
import io.hasura.crashz.interceptor.AddCookiesInterceptor;
import io.hasura.crashz.model.AuthenticationRequest;
import io.hasura.crashz.model.ErrorResponse;
import io.hasura.crashz.model.MessageResponse;
import io.hasura.crashz.network.AuthApiManager;
import io.hasura.crashz.network.CustomResponseListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends BaseActivity {
    EditText username, password;
    Button login;
    Boolean aBoolean;
    public static final String KEY = "isFirstTime";

    public static void startActivity(Activity startingActivity, boolean isFirstTime) {
        Intent intent = new Intent(startingActivity, SignInActivity.class);
        intent.putExtra(KEY, isFirstTime);
        startingActivity.startActivity(intent);


        //To clear the stack, so that the user cannot go back to the authentication activity on hardware back press
        startingActivity.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //Set username, password, button
        setTitle("SignIn");

        if (getIntent() != null)
            aBoolean = getIntent().getBooleanExtra(KEY, false);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
    }

    public void performSignIn(View view) {
        if(IsFormValid()) {
            progressDialog.setMessage("Verifying credentials");
            showProgressDialog(true);
            new AuthApiManager(getBaseContext()).getApiInterface().login(new AuthenticationRequest(username.getText().toString().trim(), password.getText().toString().trim()))
                    .enqueue(new Callback<MessageResponse>() {
                        public void onSuccessfulResponse(MessageResponse response) {
                            showProgressDialog(false);
                            if (aBoolean) {
                                EditProfileActivity.startActivity(SignInActivity.this, true);
                            }
                            else
                                HomeActivity.startActivity(SignInActivity.this);
                        }

                        public void onFailureResponse(ErrorResponse errorResponse) {
                            showProgressDialog(false);
                            showAlert("Error", errorResponse.getMessage());
                        }

                        @Override
                        public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                            if (response.isSuccessful()) {
                                SharedPreferences hasura_id = getApplicationContext().getSharedPreferences("Hasura User ID", MODE_PRIVATE);
                                SharedPreferences.Editor editor = hasura_id.edit();
                                editor.putInt("hasura_id", response.body().getHasura_id()).commit();
                                onSuccessfulResponse(response.body());
                            } else {
                                try {
                                    String errorMessage = response.errorBody().string();
                                    try {
                                        ErrorResponse errorResponse = new Gson().fromJson(errorMessage, ErrorResponse.class);
                                        onFailureResponse(errorResponse);
                                    } catch (JsonSyntaxException jsonSyntaxException) {
                                        jsonSyntaxException.printStackTrace();
                                        onFailureResponse(new ErrorResponse("JSON Syntax Exception"));
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    onFailureResponse(new ErrorResponse("IOException caught"));
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<MessageResponse> call, Throwable t) {
                            onFailureResponse(new ErrorResponse("You are not connected to the Internet"));
                        }

                    });
        }
    }

    private Boolean IsFormValid() {
        if (username.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Username/Mobile cannot be left empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public void showNavigateAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNeutralButton("Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //CookieManager cookieManager= CookieManager.getInstance();
                    if (aBoolean) {
                        EditProfileActivity.startActivity(SignInActivity.this, true);
                    }
                    else
                        HomeActivity.startActivity(SignInActivity.this);
            }
        });
        builder.show();
    }
}