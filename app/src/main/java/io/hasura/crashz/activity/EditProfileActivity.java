package io.hasura.crashz.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import io.hasura.crashz.R;
import io.hasura.crashz.model.DataHandlingResponse;
import io.hasura.crashz.model.ErrorResponse;
import io.hasura.crashz.model.InsertORUpdateIntoUserTableBuilder;
import io.hasura.crashz.network.CustomResponseListener;
import io.hasura.crashz.network.DataApiManager;

public class EditProfileActivity extends BaseActivity {
    public EditText name, work, city, music_style, passion, description;

    public static void startActivity(Activity startingActivity, boolean isFirstTime) {
        Intent intent = new Intent(startingActivity, EditProfileActivity.class);
        intent.putExtra("isFirstTime",isFirstTime);
        startingActivity.startActivity(intent);
        if(isFirstTime){
            startingActivity.finish();
        }
        //To clear the stack, so that the user cannot go back to the authentication activity on hardware back press
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.home:
                ultimate_check=true;
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        setTitle("Edit Profile");


        Log.i("In EditProfile","Before parsing the intent");
        name= (EditText) findViewById(R.id.prof_name);
        work= (EditText) findViewById(R.id.work_info);
        city= (EditText) findViewById(R.id.city_input);
        music_style= (EditText) findViewById(R.id.music_style_input);
        passion= (EditText) findViewById(R.id.passion_with_music);
        description= (EditText) findViewById(R.id.description);

        if (getIntent() != null) {
            boolean aBoolean = getIntent().getBooleanExtra("isFirstTime", false);
            if (!aBoolean) {
                Log.i("In EditProfile","firstTime is false");
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                setData();
            }
            else{
                Log.i("In EditProfile","firstTime is true");
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            }
        }
    }

    public void insertOrUpdate(View view){
        Log.i("Inside insertorUpdate","I am here-- Save button clicked");
        if (getIntent() != null) {
            boolean aBoolean = getIntent().getBooleanExtra(SignInActivity.KEY,false);
            if(aBoolean) {
                if (IsFormValid()) {
                    //Insert the data
                    Log.i("Inside insertorUpdate", "aBoolean is true-- first Time / Insert now");
                    SharedPreferences user_profile_edited_flag= getApplicationContext().getSharedPreferences("user_profile_edited"+Integer.toString(getApplicationContext().getSharedPreferences("Hasura User ID",MODE_PRIVATE).getInt("hasura_id",0)),MODE_PRIVATE);
                    user_profile_edited_flag.edit().putBoolean("user_profile_edited_flag",true).commit();
                    Log.i("Inside insertorUpdate","user_edited_flag is set to true");
                    insertOrUpdateData("insert");
                }
            }
             else {
                if(IsFormValid()) {
                    //Update the data
                    Log.i("In EditProfile", "aBoolean is false-- Update now");
                    insertOrUpdateData("update");
                }
             }
        }
    }


    private void setData() {
        Log.i("sharedPreferences-- setData","Inside Setdata");

        SharedPreferences user_profile = getBaseContext().getSharedPreferences(Integer.toString(GetUserId()), getBaseContext().MODE_PRIVATE);
        Log.i("name in SP is",user_profile.getString("prof_name", ""));

            name.setText(user_profile.getString("prof_name", ""));
            work.setText(user_profile.getString("work", ""));
            city.setText(user_profile.getString("city", ""));
            music_style.setText(user_profile.getString("music_style", ""));
            passion.setText(user_profile.getString("passion", ""));
            description.setText(user_profile.getString("description", ""));
    }

    private void insertOrUpdateData(String s) {
        int id= GetUserId();
        Log.i("sharedPreferences-- setData","Inside Setdata");
        saveToSharedPreferences();

        Log.i("In InsertOrUpdateData mode",s);
        //put it in an array of Args.Objects
        progressDialog.setMessage("Saving data");
        showProgressDialog(true);
        new DataApiManager(getBaseContext()).getApiInterface().insert_or_update(new InsertORUpdateIntoUserTableBuilder()
                .setTypeTable(s,"user")
                .setId(id)
                .setCity(city.getText().toString().trim())
                .setDescription(description.getText().toString().trim())
                .setWork(work.getText().toString().trim())
                .setPassion_with_music(passion.getText().toString().trim())
                .setProf_name(name.getText().toString().trim())
                .setMusic(music_style.getText().toString().trim())
                .build())
            .enqueue(new DataSavedAlert<DataHandlingResponse>());
    }

    class DataSavedAlert<DataHandlingResponse> extends CustomResponseListener<DataHandlingResponse>{

        @Override
        public void onSuccessfulResponse(DataHandlingResponse response) {
            showProgressDialog(false);
            //showAlert("Success","Data saved Successfully");
            boolean aBoolean = getIntent().getBooleanExtra("isFirstTime", false);
            if(aBoolean)
                HomeActivity.startActivity(EditProfileActivity.this);
            else
                finish();
        }

        @Override
        public void onFailureResponse(ErrorResponse errorResponse) {
            showProgressDialog(false);
            showAlert("Failed",errorResponse.getMessage());
        }
    }

    private void saveToSharedPreferences() {

        //Put the user_id in that user's edit_profile
        SharedPreferences user_profile = getBaseContext().getSharedPreferences(Integer.toString(GetUserId()), getBaseContext().MODE_PRIVATE);
        SharedPreferences.Editor editor= user_profile.edit();

        editor.putString("prof_name",name.getText().toString().trim());
        editor.putString("work",work.getText().toString().trim());
        editor.putString("city",city.getText().toString().trim());
        editor.putString("music_style",music_style.getText().toString().trim());
        editor.putString("passion",passion.getText().toString().trim());
        editor.putString("description",description.getText().toString().trim());

        editor.commit();
    }

    private boolean IsFormValid() {
        Log.i("Inside isFormValid","true");
        if(name.getText().toString().trim().isEmpty()){
            Log.i("Inside isFormValid","name is empty");
            Toast.makeText(this,"Profilename cannot be left empty",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(work.getText().toString().trim().isEmpty()){
            Toast.makeText(this,"Work cannot be blank",Toast.LENGTH_SHORT).show();
            return false;
        }

        if(city.getText().toString().trim().isEmpty()){
            Toast.makeText(this,"City field cannot be left empty",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public int GetUserId() {

        SharedPreferences hasura_id = getApplicationContext().getSharedPreferences("Hasura User ID",MODE_PRIVATE);
        return hasura_id.getInt("hasura_id",0);
    }
}
