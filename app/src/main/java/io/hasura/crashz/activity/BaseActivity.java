package io.hasura.crashz.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.regex.Pattern;

import io.hasura.crashz.R;

import static io.hasura.crashz.R.id.put_text;

/**
 * Created by HARIHARAN on 27-06-2017.
 */

public class BaseActivity extends AppCompatActivity {

    public ProgressDialog progressDialog;
    public boolean ultimate_check=false;
    public TextView alertMessage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressDialog= new ProgressDialog(this);
        progressDialog.setIndeterminate(false);
        progressDialog.setMessage("Please wait");
        progressDialog.setCancelable(false);
    }

    public void showProgressDialog(Boolean shouldShow) {
        if (shouldShow) {
            progressDialog.show();
        } else {
            progressDialog.dismiss();
        }
    }

    public void showAlert(String title, String message) {
        View view=getLayoutInflater().inflate(R.layout.text_view2,null);
        alertMessage=(TextView) view.findViewById(R.id.put_text2);
        alertMessage.setText(message);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setView(view);
        builder.setNeutralButton("Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    public boolean isValid(String song_name, String composer_name, String song_link){
            Log.i("Inside isFormValid","true");
            Pattern pattern= Pattern.compile("[https://www.dropbox.com/]");
            if(song_name.isEmpty()){
                Log.i("Inside isFormValid","name is empty");
                Toast.makeText(this,"Song name can't be left empty",Toast.LENGTH_SHORT).show();
                return false;
            }
            if(composer_name.isEmpty()){
                Toast.makeText(this,"Composer can't be blank",Toast.LENGTH_SHORT).show();
                return false;
            }

            if(song_link.isEmpty()){
                Toast.makeText(this,"Song link can't be left blank",Toast.LENGTH_SHORT).show();
                return false;
            }
            if(!song_link.startsWith("https://www.dropbox.com/")&& !song_link.contains(".mp3")){
                Toast.makeText(this,"The link is not a Dropbox mp3 link",Toast.LENGTH_SHORT).show();
                return false;
            }
                return true;
        }
}