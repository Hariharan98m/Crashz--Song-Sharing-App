package io.hasura.crashz.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.silvestrpredko.dotprogressbar.DotProgressBar;
import com.github.silvestrpredko.dotprogressbar.DotProgressBarBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import io.hasura.crashz.R;
import io.hasura.crashz.fragment.GuysYouMayKnowFragment;
import io.hasura.crashz.fragment.NothingToShow;

import io.hasura.crashz.model.BitmapResponse;
import io.hasura.crashz.model.DPSelect;
import io.hasura.crashz.model.DataHandlingResponse;
import io.hasura.crashz.model.DpInsert;
import io.hasura.crashz.model.DrawerListItems;
import io.hasura.crashz.model.ErrorResponse;
import io.hasura.crashz.model.HomePageFriendsReqORConfirm;
import io.hasura.crashz.model.InsertIntoRequestORConfirm;
import io.hasura.crashz.model.SelectFriendsData;
import io.hasura.crashz.model.SongInsert;
import io.hasura.crashz.network.AuthApiManager;
import io.hasura.crashz.network.CustomResponseListener;
import io.hasura.crashz.network.DataApiManager;
import io.hasura.crashz.transformation.CircleTransform;
import io.hasura.crashz.transformation.DepthPageTransformer;
import io.hasura.crashz.transformation.ZoomOutPageTransformer;
import io.hasura.crashz.viewholder.FriendsListViewHolder;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class HomeActivity extends BaseActivity implements GuysYouMayKnowFragment.Helper{
    boolean shouldGoInvisible,setter;
    ImageButton left_arrow, right_arrow;
    ConstraintLayout cl;
    RecyclerView recyclerView;
    ViewPager viewPager;
    CardView card_view_list;
    GuysYouMayKnowAdapter pagerAdapter;
    FriendsListAdapter friendsListAdapter= new FriendsListAdapter();
    DotProgressBar dotProgressBar;
    AlertDialog AD;
    ListView listView;
    float mPreviousOffset;
    TextView song_name, link;
    EditText composer_name;
    EditText song_link;
    Button add_it;
    //DrawerLayout
    private Animator mCurrentAnimator;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    CoordinatorLayout coordinatorLayout;
    public static void startActivity(Activity startingActivity) {
        Intent intent = new Intent(startingActivity, HomeActivity.class);
        startingActivity.startActivity(intent);
        //To clear the stack, so that the user cannot go back to the authentication activity on hardware back press
        startingActivity.finish();
    }
    String pic_url;
    public int guys_you_may_know=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Home Page");
        coordinatorLayout=(CoordinatorLayout)findViewById(R.id.coordinatorLayout);
        cl=(ConstraintLayout) findViewById(R.id.con_holder);
        left_arrow= (ImageButton) findViewById(R.id.left_arrow);
        right_arrow= (ImageButton) findViewById(R.id.right_arrow);

        left_arrow.setVisibility(View.VISIBLE);
        right_arrow.setVisibility(View.VISIBLE);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        String[] tabs={"Edit Profile","Your Songs","Add a song","Update your DP","Logout"};
        ArrayList<DrawerListItems> items_list=new ArrayList<>();
        int i=0;
        for(String s: tabs){
            items_list.add(new DrawerListItems(s,i));
            i++;
        }
        listView = (ListView)findViewById(R.id.list_view);
        listView.setAdapter(new CustomAdapter(getBaseContext(),items_list));
        //card_view_list= (CardView) findViewById(R.id.card_view_list);

        listView.setOnItemClickListener(new DrawerItemClickListener());
        // Set the list's click listener
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open,R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                setTitle("Home Page");
                setter=false;
                shouldGoInvisible = false;
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                setTitle("What Next");
                setter=true;
                shouldGoInvisible = true;
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

        };
        mDrawerLayout.setDrawerListener(actionBarDrawerToggle);

        //Set a progressDialog
        dotProgressBar =(DotProgressBar) findViewById(R.id.dot_progress_bar);
        dotProgressBar= new DotProgressBarBuilder(this)
                .setDotAmount(4)
                .setAnimationDirection(DotProgressBar.RIGHT_DIRECTION)
                .build();

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dot_progress_bar, null);
        dialogBuilder.setView(dialogView);

        AD= dialogBuilder.create();
        AD.setCancelable(false);
        //Get the ViewPager
        viewPager = (ViewPager) findViewById(R.id.viewPagerFriends);
        viewPager.setPageTransformer(true,new ZoomOutPageTransformer());
        //Create the adapter linking the fragment
        pagerAdapter = new GuysYouMayKnowAdapter(getSupportFragmentManager());
        //Get the data for the adapter
        setGuys_you_may_knowList();
        //Set the adapter
        viewPager.setAdapter(pagerAdapter);

        //Recycler View
        recyclerView = (RecyclerView) findViewById(R.id.friendsList);
        if(!getRecyclerViewFriendsData(false)) {
            //Step 1--Set the adapter
            recyclerView.setAdapter(friendsListAdapter);

            //Step2 -- Set the layout
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }

    }

    public void left_click(View view){
        viewPager.setCurrentItem(viewPager.getCurrentItem()-1);

    }
    public void right_click(View view){
        viewPager.setCurrentItem(viewPager.getCurrentItem()+1);

    }
    NothingToShow NS1;
    int viewPagerSize=0;
    public void setGuys_you_may_knowList() {
        AD.show();
        new DataApiManager(getBaseContext()).getApiInterface().select(new SelectFriendsData("select","confirm_invitees_friend_data",GetUserId()))
                .enqueue(new CustomResponseListener<List<HomePageFriendsReqORConfirm>>() {
                    @Override

                    public void onSuccessfulResponse(List<HomePageFriendsReqORConfirm> res) {
                        AD.dismiss();
                        pagerAdapter.confirmList=res;
                        pagerAdapter.size=res.size();
                        pagerAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailureResponse(ErrorResponse errorResponse) {
                        showAlert("Error", errorResponse.getMessage());
                    }
                });
        AD.show();
        new DataApiManager(getBaseContext()).getApiInterface().select(new SelectFriendsData("select","send_an_invite_friend_data",GetUserId()))
                .enqueue(new CustomResponseListener<List<HomePageFriendsReqORConfirm>>() {

                    @Override
                    public void onSuccessfulResponse(List<HomePageFriendsReqORConfirm> resp) {
                        AD.dismiss();
                        Log.i("reqList",Integer.toString(resp.size()));
                        pagerAdapter.requestList= resp;
                        pagerAdapter.size+=resp.size();
                        viewPagerSize+=resp.size();
                        pagerAdapter.notifyDataSetChanged();
                        pagerAdapter.manageDataSet(false);
                    }

                    @Override
                    public void onFailureResponse(ErrorResponse errorResponse) {
                       showAlert("Error", errorResponse.getMessage());
                    }
                });

    }

    public boolean getRecyclerViewFriendsData(final boolean isWannaTuneTo){

        final boolean[] flag = new boolean[1];
        new DataApiManager(this).getApiInterface().select(new SelectFriendsData("select","your_friends_friend_data3",GetUserId()))
                .enqueue(new CustomResponseListener<List<HomePageFriendsReqORConfirm>>() {
                    @Override

                    public void onSuccessfulResponse(List<HomePageFriendsReqORConfirm> response) {

                        if(isWannaTuneTo&& NS1!=null)
                            getSupportFragmentManager().beginTransaction().remove(NS1).commit();
                        if(response.size()==0){
                            Bundle bundl= new Bundle();
                            bundl.putString("title","No Friends?");
                            bundl.putString("msg","Sorry, but you haven't got any friends\nGet started with invites");
                            bundl.putInt("mode",1);
                            NS1= new NothingToShow();
                            NS1.setArguments(bundl);
                            getSupportFragmentManager().beginTransaction()
                                    .add(R.id.nothing_to_show_container,NS1).commit();
                            flag[0] =true;
                        }else {
                            friendsListAdapter.setData(response, recyclerView);
                            flag[0] = false;
                        }
                    }

                    @Override
                    public void onFailureResponse(ErrorResponse errorResponse) {
                        showAlert("Error", errorResponse.getMessage());
                    }
                });
        Log.i("in get recyclerviewfriends data","check");

        return flag[0];

    }

    public Target target;

    MenuItem item;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout_button, menu);
        item=menu.findItem(R.id.edit_profile);
        target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                item.setIcon(drawable);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) { }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) { }
        };
        new DataApiManager(this).getApiInterface().select_dp_link(new DPSelect("select","profile_image",GetUserId()))
                .enqueue(new CustomResponseListener<List<BitmapResponse>>() {
                    @Override
                    public void onSuccessfulResponse(List<BitmapResponse> response) {

                        if(response.size()!=0) {
                            pic_url = response.get(0).getProfile_image_link();
                            Picasso.with(HomeActivity.this).load(pic_url).transform(new CircleTransform()).into(target);
                        }else{
                            item.setIcon(R.drawable.profile_dp);
                        }
                    }

                    @Override
                    public void onFailureResponse(ErrorResponse errorResponse) {

                    }
                });
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id= item.getItemId();
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (id){
            case R.id.edit_profile:
                EditProfileActivity.startActivity(HomeActivity.this,false);
                break;
            case R.id.logout:
                callLogOut();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void callLogOut(){
        progressDialog.setMessage("Logging out");
        showProgressDialog(true);

        new AuthApiManager(getBaseContext()).getApiInterface().logout()
                .enqueue(new CustomResponseListener<ErrorResponse>() {
                    @Override
                    public void onSuccessfulResponse(ErrorResponse response) {
                      showProgressDialog(false);
                        SharedPreferences hasura_id = getApplicationContext().getSharedPreferences("Hasura User ID",MODE_PRIVATE);
                        SharedPreferences.Editor editor= hasura_id.edit();
                        editor.clear().commit();
                        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().clear().commit();
                        SignInActivity.startActivity(HomeActivity.this,false);
                    }

                    @Override
                    public void onFailureResponse(ErrorResponse errorResponse) {

                    }
                });
    }


    @Override
    public void onButtonClick(String s, final int id){
        if(s=="Wanna Tune To Me"){
            AD.show();
            new DataApiManager(this).getApiInterface().insert_into_request_or_confirm(new InsertIntoRequestORConfirm("update","friend",id,GetUserId(),true,true))
                    .enqueue(new CustomResponseListener<DataHandlingResponse>() {
                        @Override
                        public void onSuccessfulResponse(DataHandlingResponse response) {
                            AD.dismiss();
                            Snackbar snackbar=Snackbar.make(coordinatorLayout,Html.fromHtml("<font color=\"#fafafb\"size=\"2\"><i>Friend added</i></font>"),Snackbar.LENGTH_INDEFINITE);
                            snackbar.setActionTextColor(Color.BLACK);
                            snackbar.getView().setBackgroundColor(ContextCompat.getColor(HomeActivity.this, R.color.blue));
                            snackbar.show();
                            for(int i=0;i<pagerAdapter.confirmList.size();i++)
                                if(pagerAdapter.confirmList.get(i).getYour_friend_id()==id)
                                    pagerAdapter.confirmList.remove(i);
                            TransitionManager.beginDelayedTransition(viewPager);
                            pagerAdapter.notifyDataSetChanged();
                            pagerAdapter.manageDataSet(false);
                            getRecyclerViewFriendsData(true);
                            TransitionManager.beginDelayedTransition(recyclerView);
                            friendsListAdapter.notifyDataSetChanged();


                        }

                        @Override
                        public void onFailureResponse(ErrorResponse errorResponse) {
                            showProgressDialog(false);
                            showAlert("Failed",errorResponse.getMessage());
                        }
                    });
        }
        else{
            AD.show();
            new DataApiManager(this).getApiInterface().insert_into_request_or_confirm(new InsertIntoRequestORConfirm("insert","friend",GetUserId(),id,true,false))
                    .enqueue(new CustomResponseListener<DataHandlingResponse>() {
                        @Override
                        public void onSuccessfulResponse(DataHandlingResponse response) {
                            AD.dismiss();
                            Snackbar snackbar=Snackbar.make(coordinatorLayout,Html.fromHtml("<font color=\"#fafafb\"size=\"2\"><i>Invitation sent</i></font>"),Snackbar.LENGTH_INDEFINITE);
                            snackbar.setActionTextColor(Color.BLACK);
                            snackbar.getView().setBackgroundColor(ContextCompat.getColor(HomeActivity.this, R.color.blue));
                            snackbar.show();
                            //HomeActivity.startActivity(HomeActivity.this);
                            for(int i=0;i<pagerAdapter.requestList.size();i++)
                                if(pagerAdapter.requestList.get(i).getYour_friend_id()==id)
                                    pagerAdapter.requestList.remove(i);
                            TransitionManager.beginDelayedTransition(viewPager);
                            pagerAdapter.notifyDataSetChanged();
                            pagerAdapter.manageDataSet(false);
                        }

                        @Override
                        public void onFailureResponse(ErrorResponse errorResponse) {
                            AD.dismiss();
                            showAlert("Failed",errorResponse.getMessage());
                        }
                    });
        }
    }
    boolean noElements=false;

    class GuysYouMayKnowAdapter extends FragmentStatePagerAdapter{

        List<HomePageFriendsReqORConfirm> confirmList= new ArrayList<HomePageFriendsReqORConfirm>();
        List<HomePageFriendsReqORConfirm> requestList= new ArrayList<HomePageFriendsReqORConfirm>();
        public int size;
        NothingToShow NS = new NothingToShow();
        public GuysYouMayKnowAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {

            noElements= true;
            Log.i("in getItem Viewpager", Integer.toString(getCount()));
            if (position >= (confirmList.size())) {
                Log.i("getItem viewpaegr","in req list");
                int pos = position - confirmList.size();
                Bundle bundle = new Bundle();
                bundle.putString("name", requestList.get(pos).getFriend_name());
                bundle.putString("desc", requestList.get(pos).getFriend_work() + ", <i>" + requestList.get(pos).getFriend_city() + "</i>");
                bundle.putString("add_or_remove_button", "Send an invite");
                bundle.putInt("friend_id",requestList.get(pos).getYour_friend_id());

                GuysYouMayKnowFragment requestFragment = new GuysYouMayKnowFragment();
                requestFragment.setArguments(bundle);
                return requestFragment;

            } else {
                Log.i("getItem viewpager","in confirm list");
                Bundle bundle = new Bundle();
                bundle.putString("name", confirmList.get(position).getFriend_name());
                bundle.putString("desc", confirmList.get(position).getFriend_work() + ", <i>" + confirmList.get(position).getFriend_city() + "</i>");
                bundle.putString("add_or_remove_button", "Wanna Tune To Me");
                bundle.putInt("friend_id",confirmList.get(position).getYour_friend_id());
                GuysYouMayKnowFragment confirmFragment = new GuysYouMayKnowFragment();
                confirmFragment.setArguments(bundle);
                return confirmFragment;
            }
        }

        @Override
        public int getCount() {
            return confirmList.size()+requestList.size();
        }

        private void manageDataSet(final boolean isLoadData) {
            final Handler handler = new Handler();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.i("Pagercount in manageDataSet", Integer.toString(pagerAdapter.getCount()));
                    //Do something after 100ms
                    Log.i("no req or confirm", "Nothing to show");
                    if(isLoadData|| (getCount()==0)) {

                        Bundle bundl = new Bundle();
                        bundl.putString("title", "Nothing to Show");
                        bundl.putString("msg", "You are connected with everybody on Crashz.\nEither invited or befriended!\n");
                        bundl.putInt("mode", 0);

                        NS.setArguments(bundl);
                        getSupportFragmentManager().beginTransaction()
                                .add(R.id.cardViewHolder, NS).commit();
                    }
                    if(getCount()==0||getCount()==1){
                        left_arrow.setVisibility(View.GONE);
                        right_arrow.setVisibility(View.GONE);

                    }

                }
            }, 0);
        }

    }

    public int GetUserId() {

        SharedPreferences hasura_id = getApplicationContext().getSharedPreferences("Hasura User ID",MODE_PRIVATE);
        return hasura_id.getInt("hasura_id",0);
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        //boolean drawerOpen = mDrawerLayout.isDrawerOpen(listView);
        if(shouldGoInvisible&&setter) {
            menu.findItem(R.id.edit_profile).setVisible(!shouldGoInvisible);
            menu.findItem(R.id.logout).setVisible(!shouldGoInvisible);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    public class CustomAdapter extends ArrayAdapter<DrawerListItems>{

        public CustomAdapter(@NonNull Context context, @NonNull List<DrawerListItems> objects) {
            super(context,0, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            DrawerListItems item= getItem(position);
            if(convertView==null){
                convertView= LayoutInflater.from(getContext()).inflate(R.layout.card_view,parent,false);
            }
            ImageButton button = (ImageButton) convertView.findViewById(R.id.action_icon);
            switch(item.getId()){
                case 0:
                    button.setImageResource(R.drawable.edit_prof);
                    break;
                case 1:
                    button.setImageResource(R.drawable.your_songs);
                    break;
                case 2:
                    button.setImageResource(R.drawable.song_add);
                    break;
                case 3:
                    button.setImageResource(R.drawable.insert_photo);
                    break;
                case 4:
                    button.setImageResource(R.drawable.logout);
                    break;
            }
            TextView text_msg=(TextView) convertView.findViewById(R.id.msg);
            text_msg.setText(item.getMsg());
            return convertView;
        }
    }


    private class DrawerItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.i("Item clicked","Clicked");
            selectItem(position);
        }
    }

    /** Swaps fragments in the main content view */
    private void selectItem(final int position) {
        listView.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(listView);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                switch(position){
                    case 0:
                        EditProfileActivity.startActivity(HomeActivity.this,false);
                        break;
                    case 1: SongActivity.startActivity(HomeActivity.this,true,GetUserId(),"Your Songs");
                        break;
                    case 2:
                        addASong();
                        break;
                    case 3:
                        updateDP();
                        break;
                    case 4:
                        callLogOut();
                        break;
                }
            }
        }, 300);

    }

    private void addASong() {
        //Inflate the layout
        LayoutInflater inflater= getLayoutInflater();
        View view= inflater.inflate(R.layout.add_a_song,null);
        link= (TextView) view.findViewById(R.id.link_text);
        link.setMovementMethod(LinkMovementMethod.getInstance());
        //Get the song details
        song_name= (EditText) view.findViewById(R.id.song_name);
        song_link= (EditText) view.findViewById(R.id.song_link);
        composer_name= (EditText) view.findViewById(R.id.composer_name);
        add_it= (Button) view.findViewById(R.id.add_it);
        AlertDialog.Builder alert= new AlertDialog.Builder(this)
                        .setTitle("Add a Song");
        alert.setView(view);
        final AlertDialog dialog=alert.create();
        dialog.show();
        add_it.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid(song_name.getText().toString().trim(), composer_name.getText().toString().trim(), song_link.getText().toString().trim())) {
                    dialog.dismiss();
                    makeInsertSongCall();
                }
            }
        });

    }

    public void updateDP(){
        //Inflate the layout
        final EditText  dp_link;
        Button take_a_snap, set_it;
        TextView text;
        LayoutInflater inflater= getLayoutInflater();
        View view= inflater.inflate(R.layout.add_a_photo,null);
        dp_link= (EditText) view.findViewById(R.id.dp_link);
        text= (TextView) view.findViewById(R.id.link_text);
        //Get the song details
        take_a_snap= (Button) view.findViewById(R.id.click_a_snap);
        set_it= (Button) view.findViewById(R.id.set_it);
        AlertDialog.Builder alert= new AlertDialog.Builder(this)
                .setTitle("Set a DP");
        alert.setView(view);
        final AlertDialog dialog=alert.create();
        dialog.show();
        text.setMovementMethod(LinkMovementMethod.getInstance());
        take_a_snap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start a camera intent
                if (ContextCompat.checkSelfPermission(HomeActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        Toast.makeText(HomeActivity.this, "Please add permissions to access data", Toast.LENGTH_SHORT).show();
                        ActivityCompat.requestPermissions(HomeActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.

                    } else {

                        // No explanation needed, we can request the permission.
                        Log.i("Request permission","asked");
                        ActivityCompat.requestPermissions(HomeActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                    }
                }
                else{
                    startCameraIntent();
                }
            }
        });
        set_it.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dp_link.getText().toString().trim().isEmpty())
                    Toast.makeText(getBaseContext(), "The link can't be empty", Toast.LENGTH_SHORT).show();
                else if(dp_link.getText().toString().startsWith("https://www.dropbox.com/") ) {
                    if (dp_link.getText().toString().contains(".jpg") || dp_link.getText().toString().contains(".png")) {

                        dialog.dismiss();
                        String mode;
                        insert_or_update_dp = getApplicationContext().getSharedPreferences("insert_or_update_dp" + Integer.toString(GetUserId()), MODE_PRIVATE);
                        if (!insert_or_update_dp.getBoolean("insert_dp", false)) {
                            mode = "insert";
                        } else
                            mode = "update";
                        makeInsertDPCall(dp_link.getText().toString().trim().substring(0, dp_link.length() - 5) + "?raw=1", mode);
                    }
                }
                    else {
                        Toast.makeText(getBaseContext(), "The link is not a Dropbox jpg/png link", Toast.LENGTH_SHORT).show();
                    }

        }
    });
    }

    private SharedPreferences insert_or_update_dp;
    private void makeInsertDPCall(String s, String iu) {
        new DataApiManager(this).getApiInterface().insert_dp(new DpInsert(iu,"profile_image",GetUserId(),s))
                .enqueue(new CustomResponseListener<DataHandlingResponse>() {
                    @Override
                    public void onSuccessfulResponse(DataHandlingResponse response) {
                        Snackbar snackbar=Snackbar.make(coordinatorLayout,Html.fromHtml("<font color=\"#fafafb\"size=\"2\"><i>DP set</i></font>"),Snackbar.LENGTH_INDEFINITE);
                        snackbar.setActionTextColor(Color.BLACK);
                        snackbar.getView().setBackgroundColor(ContextCompat.getColor(HomeActivity.this, R.color.blue));
                        snackbar.show();
                        insert_or_update_dp.edit().putBoolean("insert_dp",true).apply();
                        invalidateOptionsMenu();
                    }

                    @Override
                    public void onFailureResponse(ErrorResponse errorResponse) {
                        showAlert("Insert or update failed",errorResponse.getMessage());
                    }
                });
    }


    private void makeInsertSongCall() {
        AD.show();
        new DataApiManager(this).getApiInterface().insert_song(new SongInsert("insert","song",song_name.getText().toString().trim(), composer_name.getText().toString().trim(), song_link.getText().toString().trim().substring(0, song_link.length()-5)+"?raw=1",GetUserId()))
                .enqueue(new CustomResponseListener<DataHandlingResponse>() {
                    @Override
                    public void onSuccessfulResponse(DataHandlingResponse response) {
                        AD.dismiss();
                        Snackbar snackbar=Snackbar.make(coordinatorLayout,Html.fromHtml("<font color=\"#fafafb\"size=\"2\"><i>Song added to playlist</i></font>"),Snackbar.LENGTH_INDEFINITE);
                        snackbar.setActionTextColor(Color.BLACK);
                        snackbar.getView().setBackgroundColor(ContextCompat.getColor(HomeActivity.this, R.color.blue));
                        snackbar.show();
                    }

                    @Override
                    public void onFailureResponse(ErrorResponse errorResponse) {
                        showAlert("song insert failed",errorResponse.getMessage());
                    }
                });
    }


    public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListViewHolder>{
        List<HomePageFriendsReqORConfirm> h=new ArrayList<>();
        RecyclerView recyclerView;
        List<BitmapResponse> dp_links= new ArrayList<>();
        // Hold a reference to the current animator,
        // so that it can be canceled mid-way.
        private Animator mCurrentAnimator;

        // The system "short" animation time duration, in milliseconds. This
        // duration is ideal for subtle animations or animations that occur
        // very frequently.
        int mShortAnimationDuration;
        FragmentTransaction fm= getSupportFragmentManager().beginTransaction();

        @Override
        public FriendsListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //inflate the layout
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.friends_list, parent, false);
            return new FriendsListViewHolder(view);
        }

        private void zoomImageFromThumb(final View thumbView, String link) {
            // If there's an animation in progress, cancel it
            // immediately and proceed with this one.
            if (mCurrentAnimator != null) {
                mCurrentAnimator.cancel();
            }

            // Load the high-resolution "zoomed-in" image.
            final ImageView expandedImageView = (ImageView) findViewById(
                    R.id.image_view_holder);
            Picasso.with(getBaseContext()).load(link).into(expandedImageView);

            // Calculate the starting and ending bounds for the zoomed-in image.
            // This step involves lots of math. Yay, math.
            final Rect startBounds = new Rect();
            final Rect finalBounds = new Rect();
            final Point globalOffset = new Point();

            // The start bounds are the global visible rectangle of the thumbnail,
            // and the final bounds are the global visible rectangle of the container
            // view. Also set the container view's offset as the origin for the
            // bounds, since that's the origin for the positioning animation
            // properties (X, Y).
            thumbView.getGlobalVisibleRect(startBounds);
            findViewById(R.id.cardViewHolder)
                    .getGlobalVisibleRect(finalBounds, globalOffset);
            startBounds.offset(-globalOffset.x, -globalOffset.y);
            finalBounds.offset(-globalOffset.x, -globalOffset.y);

            // Adjust the start bounds to be the same aspect ratio as the final
            // bounds using the "center crop" technique. This prevents undesirable
            // stretching during the animation. Also calculate the start scaling
            // factor (the end scaling factor is always 1.0).
            float startScale;
            if ((float) finalBounds.width() / finalBounds.height()
                    > (float) startBounds.width() / startBounds.height()) {
                // Extend start bounds horizontally
                startScale = (float) startBounds.height() / finalBounds.height();
                float startWidth = startScale * finalBounds.width();
                float deltaWidth = (startWidth - startBounds.width()) / 2;
                startBounds.left -= deltaWidth;
                startBounds.right += deltaWidth;
            } else {
                // Extend start bounds vertically
                startScale = (float) startBounds.width() / finalBounds.width();
                float startHeight = startScale * finalBounds.height();
                float deltaHeight = (startHeight - startBounds.height()) / 2;
                startBounds.top -= deltaHeight;
                startBounds.bottom += deltaHeight;
            }

            // Hide the thumbnail and show the zoomed-in view. When the animation
            // begins, it will position the zoomed-in view in the place of the
            // thumbnail.
            thumbView.setAlpha(0f);
            expandedImageView.setVisibility(View.VISIBLE);

            // Set the pivot point for SCALE_X and SCALE_Y transformations
            // to the top-left corner of the zoomed-in view (the default
            // is the center of the view).
            expandedImageView.setPivotX(0f);
            expandedImageView.setPivotY(0f);

            // Construct and run the parallel animation of the four translation and
            // scale properties (X, Y, SCALE_X, and SCALE_Y).
            AnimatorSet set = new AnimatorSet();
            set
                    .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                            startBounds.left, finalBounds.left))
                    .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                            startBounds.top, finalBounds.top))
                    .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                            startScale, 1f)).with(ObjectAnimator.ofFloat(expandedImageView,
                    View.SCALE_Y, startScale, 1f));
            set.setDuration(mShortAnimationDuration);
            set.setInterpolator(new DecelerateInterpolator());
            set.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mCurrentAnimator = null;
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    mCurrentAnimator = null;
                }
            });
            set.start();
            mCurrentAnimator = set;

            // Upon clicking the zoomed-in image, it should zoom back down
            // to the original bounds and show the thumbnail instead of
            // the expanded image.
            final float startScaleFinal = startScale;
            cl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mCurrentAnimator != null) {
                        mCurrentAnimator.cancel();
                    }

                    // Animate the four positioning/sizing properties in parallel,
                    // back to their original values.
                    AnimatorSet set = new AnimatorSet();
                    set.play(ObjectAnimator
                            .ofFloat(expandedImageView, View.X, startBounds.left))
                            .with(ObjectAnimator
                                    .ofFloat(expandedImageView,
                                            View.Y,startBounds.top))
                            .with(ObjectAnimator
                                    .ofFloat(expandedImageView,
                                            View.SCALE_X, startScaleFinal))
                            .with(ObjectAnimator
                                    .ofFloat(expandedImageView,
                                            View.SCALE_Y, startScaleFinal));
                    set.setDuration(mShortAnimationDuration);
                    set.setInterpolator(new DecelerateInterpolator());
                    set.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            thumbView.setAlpha(1f);
                            if(flag){
                                NothingToShow NS= new NothingToShow();
                                Bundle bundl = new Bundle();
                                bundl.putString("title", "Nothing to Show");
                                bundl.putString("msg", "You are connected with everybody on Crashz.\nEither invited or befriended!\n");
                                bundl.putInt("mode", 0);

                                NS.setArguments(bundl);
                                getSupportFragmentManager().beginTransaction()
                                        .add(R.id.cardViewHolder, NS).commit();
                            }
                            expandedImageView.setVisibility(View.GONE);
                            viewPager.setVisibility(View.VISIBLE);
                            mCurrentAnimator = null;
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                            thumbView.setAlpha(1f);
                            if(flag){
                                NothingToShow NS= new NothingToShow();
                                Bundle bundl = new Bundle();
                                bundl.putString("title", "Nothing to Show");
                                bundl.putString("msg", "You are connected with everybody on Crashz.\nEither invited or befriended!\n");
                                bundl.putInt("mode", 0);

                                NS.setArguments(bundl);
                                getSupportFragmentManager().beginTransaction()
                                        .add(R.id.cardViewHolder, NS).commit();
                            }
                            expandedImageView.setVisibility(View.GONE);
                            viewPager.setVisibility(View.VISIBLE);
                            mCurrentAnimator = null;
                        }
                    });
                    set.start();
                    mCurrentAnimator = set;
                }
            });

        }

        public int mExpandedPosition =-1;
        public boolean flag=false;

        @Override
        public void onBindViewHolder(final FriendsListViewHolder holder, final int position) {
            //Set the data
            holder.friendName.setText(h.get(position).getFriend_name());
            holder.friendInfo.setText(Html.fromHtml(setString(position)));

            final boolean isExpanded = (position==mExpandedPosition);
            holder.friendInfo.setVisibility(isExpanded?View.VISIBLE:View.GONE);
            holder.itemView.setActivated(isExpanded);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mExpandedPosition = isExpanded ? -1:position;
                    Log.i("After on Click",Integer.toString(mExpandedPosition));
                    TransitionManager.beginDelayedTransition(recyclerView);
                    notifyDataSetChanged();
                }
            });
            holder.song_page.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SongActivity.startActivity(HomeActivity.this,false,h.get(position).getYour_friend_id(),h.get(position).getFriend_name());
                }
            });
            if(h.get(position).getProfile_image_link()!=null)
            Picasso.with(HomeActivity.this).load(h.get(position).getProfile_image_link()).transform(new CircleTransform()).into(holder.prof_image);

            final View thumb1View = holder.prof_image;

            thumb1View.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(h.get(position).getProfile_image_link()==null){

                    }else
                    {   if (getSupportFragmentManager().findFragmentById(R.id.cardViewHolder) != null) {
                        flag = true;
                        getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.cardViewHolder)).commit();
                        Log.i("Fragment Manager", "Removed");
                    }
                    viewPager.setVisibility(View.GONE);
                    zoomImageFromThumb(thumb1View, h.get(position).getProfile_image_link());
                }
                }
            });

            // Retrieve and cache the system's default "short" animation time.
            mShortAnimationDuration = getResources().getInteger(
                    android.R.integer.config_shortAnimTime);
        }

        @Override
        public int getItemCount() {
            return h.size();
        }

        public String setString(int pos){

            String s=h.get(pos).getFriend_work()+", <i>"+h.get(pos).getFriend_city()+"</i><br>";

            if(h.get(pos).getFriend_desc()!=null && (h.get(pos).getFriend_desc().length()!=0) )
                s+="<h6>About me</h6><p>"+h.get(pos).getFriend_desc()+"</p>";
            if(h.get(pos).getFriend_passion()!=null && (h.get(pos).getFriend_passion().length()!=0)) {
                String music="<b><i> "+h.get(pos).getFriend_music()+" </i></b>";
                if(h.get(pos).getFriend_music()==null || h.get(pos).getFriend_music().isEmpty()) music=" ";
                Log.i("music=",music);
                s += "<h6>Here's my" + music + "Music Story</h6>" + h.get(pos).getFriend_passion()+" <br>";
            }

            Log.i("s is ",s);


            return s;
        }
        public void setData(List<HomePageFriendsReqORConfirm> h, RecyclerView recyclerView) {
            this.h=h;
            this.recyclerView=recyclerView;

            notifyDataSetChanged();
        }


    }
    int REQUEST_IMAGE_CAPTURE=1;

    void startCameraIntent(){

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile=null;
            photoFile= createImageFile();

            if(photoFile!=null){
                Log.i("photoFile is ","not null");
                Uri photoURI= FileProvider.getUriForFile(this,
                        "io.hasura.android.fileprovider",
                        photoFile);
                if(photoFile.exists())
                    Log.i("photoURI",photoURI.toString());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }

    }

    private void addPicToGallery() {
        Log.i("add pic to gallery","i am here");
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Toast.makeText(this,"Saved to Pictures Album--Gallery",Toast.LENGTH_LONG).show();
            addPicToGallery();
        }
    }


    String mCurrentPhotoPath;
    final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE=1998;

    private File createImageFile() {
        // Create an image file name
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        Log.i("Permission check",Integer.toString(permissionCheck));
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            Log.i("Can't create a new file","don't know why");
        }

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        Log.i("Create image file",mCurrentPhotoPath);
        return image;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    startCameraIntent();
                    Log.i("permission granted","ok");

                } else {
                    Log.i("permission not granted","failed");
                    Toast.makeText(this,"Couldn't capture a pic or add to Gallery-- Permission denied",Toast.LENGTH_LONG).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }
}
