package io.hasura.crashz.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import io.hasura.crashz.R;
import io.hasura.crashz.fragment.OtpFragment;
import io.hasura.crashz.model.AuthenticationRequest;
import io.hasura.crashz.model.ErrorResponse;
import io.hasura.crashz.model.MessageResponse;
import io.hasura.crashz.network.AuthApiManager;
import io.hasura.crashz.network.CustomResponseListener;


public class RegisterActivity extends BaseActivity implements OtpFragment.Helper {
    CoordinatorLayout coordinatorLayout;
    EditText username, password, mobile, otp;
    Boolean flag = false, secondSubmitClick = false;
    OtpFragment otpFragment = new OtpFragment();

    public static void startActivity(Activity startingActivity) {
        Intent intent = new Intent(startingActivity, RegisterActivity.class);
        startingActivity.startActivity(intent);
        //To clear the stack, so that the user cannot go back to the authentication activity on hardware back press
        startingActivity.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        coordinatorLayout= (CoordinatorLayout) findViewById(R.id.coordinatorLayout3);
        username = (EditText) findViewById(R.id.username_reg);
        password = (EditText) findViewById(R.id.password_reg);
        mobile = (EditText) findViewById(R.id.mobile);
        getSupportActionBar().show();
        setTitle("Register");
    }


    public void showNavigateAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNeutralButton("Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            SignInActivity.startActivity(RegisterActivity.this,true);
            }
        });
        builder.show();
    }

    public void onClick(View view) {
        //Make a post request
        if (!flag) {
            if (IsFormValid()) {
                showProgressDialog(true);
                new AuthApiManager(getBaseContext()).getApiInterface().register(new AuthenticationRequest(username.getText().toString().trim(), password.getText().toString().trim(), mobile.getText().toString().trim()))
                        .enqueue(new CustomResponseListener<MessageResponse>() {
                            @Override
                            public void onSuccessfulResponse(MessageResponse response) {
                                showProgressDialog(false);
                                flag = true;
                                showOTP();

                            }

                            @Override
                            public void onFailureResponse(ErrorResponse errorResponse) {
                                showProgressDialog(false);
                                showAlert("Error", errorResponse.getMessage());
                                flag = false;
                            }
                        });
            }
            flag = true;
        }
        else{
            String otp=otpFragment.otp.getText().toString();
            if(otp.length()!=6){
                Toast.makeText(this,"Otp should be 6 digits",Toast.LENGTH_SHORT).show();
            }
            else{
                callMobConfirm(otpFragment.otp.getText().toString());
            }
        }
    }

    @Override
    public String onOtpEntered(String otp) {
        return otp;
    }

    private void callMobConfirm(String s){
        progressDialog.setMessage("Confirming mobile");
        showProgressDialog(true);
        new AuthApiManager(getBaseContext()).getApiInterface().mconfirm(new AuthenticationRequest(username.getText().toString().trim(), password.getText().toString().trim(), mobile.getText().toString().trim(), s))
                .enqueue(new OtpConfirmListener<MessageResponse>());
    }

    private class OtpConfirmListener<T> extends CustomResponseListener<T>{
        @Override
        public void onSuccessfulResponse(T response) {
            showProgressDialog(false);
            Log.i("Otp Confirm","Success");
            Snackbar snackbar=Snackbar.make(coordinatorLayout, Html.fromHtml("<font color=\"#fafafb\"size=\"2\"><i>OTP verified</i></font>"), Snackbar.LENGTH_INDEFINITE);
            snackbar.setActionTextColor(Color.BLACK);
            snackbar.getView().setBackgroundColor(ContextCompat.getColor(RegisterActivity.this, R.color.blue));
            snackbar.show();
            SignInActivity.startActivity(RegisterActivity.this,true);
        }

        @Override
        public void onFailureResponse(ErrorResponse errorResponse) {
            showProgressDialog(false);
            showAlert("Error", errorResponse.getMessage());
        }
    }

    private Boolean IsFormValid(){
        if(username.getText().toString().trim().isEmpty()){
            Toast.makeText(this,"Username cannot be left empty",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(password.getText().toString().trim().isEmpty()){
            Toast.makeText(this,"Password cannot be empty",Toast.LENGTH_SHORT).show();
            return false;
        }

        if(mobile.getText().toString().trim().isEmpty()){
            Toast.makeText(this,"Mobile cannot be left empty",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void showOTP(){
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentcontainer,otpFragment)
                .commit();
    }
}
