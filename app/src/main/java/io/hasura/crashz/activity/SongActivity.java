package io.hasura.crashz.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.transition.AutoTransition;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;
import io.hasura.crashz.R;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.hasura.crashz.R;
import io.hasura.crashz.adapter.TestPagerAdapter;
import io.hasura.crashz.fragment.NothingToShow;
import io.hasura.crashz.fragment.TextViewFragment;
import io.hasura.crashz.model.CommentTextListRequest;
import io.hasura.crashz.model.DataHandlingResponse;
import io.hasura.crashz.model.ErrorResponse;
import io.hasura.crashz.model.FriendSongsList;
import io.hasura.crashz.model.InsertLikeModelRequest;
import io.hasura.crashz.model.SelectFriendsSongsRequest;
import io.hasura.crashz.model.SongComments;
import io.hasura.crashz.network.CustomResponseListener;
import io.hasura.crashz.network.DataApiManager;
import io.hasura.crashz.viewholder.SongsListViewHolder;

import static android.R.attr.id;
import static io.hasura.crashz.R.id.comments_view_pager;
import static io.hasura.crashz.R.id.coordinatorLayout;
import static io.hasura.crashz.R.id.song;
import static java.sql.Types.NULL;

public class SongActivity extends BaseActivity implements View.OnTouchListener, MediaPlayer.OnPreparedListener, View.OnClickListener {
    CoordinatorLayout song_coordinator;
    RecyclerView recyclerView;
    WifiManager.WifiLock wifiLock;
    Timer timer;
    int page = 1;
    AlertDialog AD;
    private boolean first_time= true;
    private ImageButton buttonPlayPause, back_button;
    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    android.os.Handler handler= new android.os.Handler();
    private int mediaFileLengthInMilliseconds; // this value contains the song duration in milliseconds. Look at getDuration() method in MediaPlayer class
    private ConstraintLayout no_songs_holder;

    public static void startActivity(Activity startingActivity, boolean isUser, int friend_id, String friend_name) {
        Intent intent = new Intent(startingActivity, SongActivity.class);
        intent.putExtra("isUser",isUser);
        intent.putExtra("friend_id",friend_id);
        intent.putExtra("friend_name",friend_name);
        startingActivity.startActivity(intent);
        //To clear the stack, so that the user cannot go back to the authentication activity on hardware back press
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.home) {
            Log.i("I am here","home button clicked");
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song);
        no_songs_holder= (ConstraintLayout) findViewById(R.id.no_songs_holder);
        song_coordinator= (CoordinatorLayout) findViewById(R.id.coordinatorLayout2);
        recyclerView = (RecyclerView) findViewById(R.id.songs_list);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dot_progress_bar, null);
        TextView setText= (TextView) dialogView.findViewById(R.id.progress_text);
        setText.setText("In a jiffy");
        dialogBuilder.setView(dialogView);

        AD= dialogBuilder.create();
        AD.setCancelable(false);

        Log.i("I am here","song activity");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(getIntent()!=null){
            setTitle(getIntent().getStringExtra("friend_name"));
            progressDialog.setMessage("Fetching Songs");
            showProgressDialog(true);
            final boolean isUser= getIntent().getBooleanExtra("isUser",false);
            new DataApiManager(this).getApiInterface().get_songs_for_this_friend(new SelectFriendsSongsRequest("select","song_likes_comments_info",getIntent().getIntExtra("friend_id",0)))
                    .enqueue(new CustomResponseListener<List<FriendSongsList>>() {
                        @Override
                        public void onSuccessfulResponse(List<FriendSongsList> response) {
                            showProgressDialog(false);
                            //Recycler View
                            //Step 1--Set the adapter
                            Log.i("I am here","song activity");
                            if(response.size()==0){
                                Bundle bundl = new Bundle();
                                bundl.putString("title","No Songs");
                                if(isUser) {
                                    bundl.putString("msg", "You haven't added any Songs yet\nGet started with Dropbox & Crashz Media Library");
                                    bundl.putInt("mode", 2);
                                }else{
                                    bundl.putString("msg", "Your friend hasn't added any Songs yet\nEndorse your friends with Crashz Media Library");
                                    bundl.putInt("mode", 3);
                                }
                                NothingToShow NS = new NothingToShow();
                                NS.setArguments(bundl);
                                getSupportFragmentManager().beginTransaction()
                                        .add(R.id.no_songs_holder,NS).commit();
                            }
                            else {

                                recyclerView.setAdapter(new SongsListAdapter(response));
                                //Step2 -- Set the layout
                                recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                            }
                        }

                        @Override
                        public void onFailureResponse(ErrorResponse errorResponse) {
                            showAlert("Failed",errorResponse.getMessage());

                        }
                    });
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        SeekBar sb = (SeekBar)v;
        int playPositionInMillisecconds = (mediaFileLengthInMilliseconds / 100) * sb.getProgress();
        mediaPlayer.seekTo(playPositionInMillisecconds);
        return false;
    }

    private void primarySeekBarProgressUpdater() {
        if (mediaPlayer!=null && mediaPlayer.isPlaying()) {
        seekBar.setProgress((int)(((float)mediaPlayer.getCurrentPosition()/mediaFileLengthInMilliseconds)*100)); // This math construction give a percentage of "was playing"/"song length"
            Runnable notification = new Runnable() {
                public void run() {
                    primarySeekBarProgressUpdater();
                }
            };
            handler.postDelayed(notification,1000);
        }
    }


    @Override
    public void onPrepared(MediaPlayer mp) {
        AD.dismiss();
        Log.i("OnPrepared", "Prepare over");
        mediaPlayer.start();
        buttonPlayPause.setImageResource(R.drawable.pause_button);
        first_time=false;
        primarySeekBarProgressUpdater();
        mediaFileLengthInMilliseconds=mediaPlayer.getDuration();
    }

    @Override
    public void onClick(View v) {
        if(mediaPlayer.isPlaying() && !first_time){
            buttonPlayPause.setImageResource(R.drawable.play_button);
            mediaPlayer.pause();
        }
        else if(!mediaPlayer.isPlaying() && !first_time){
            buttonPlayPause.setImageResource(R.drawable.pause_button);
            mediaPlayer.start();
        }
    }


    public class SongsListAdapter extends RecyclerView.Adapter<SongsListViewHolder> {
        private List<FriendSongsList> songs = new ArrayList<>();
        //private List<FragmentViewAdapter> comment_adapter= new ArrayList<>();

        public SongsListAdapter(List<FriendSongsList> songs) {
            this.songs = songs;

            Log.i("I am here", "songlist adapter");
        }

        public int mExpandedPosition = -1;

        @Override
        public SongsListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_plus_comments, parent, false);


            return new SongsListViewHolder(view);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public void onBindViewHolder(final SongsListViewHolder holder, final int position) {

            final SharedPreferences user_id_song_id_like = getApplicationContext().getSharedPreferences(Integer.toString(GetUserId()) + Integer.toString(songs.get(position).getSong_id()), MODE_PRIVATE);
            holder.composer_name.setText(songs.get(position).getComposer_name());
            holder.song_name.setText(songs.get(position).getSong_name());

            /*holder.song_tab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //play the song
                }
            });

            */

            holder.send_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Check if the comments tab is empty
                    if (holder.user_comment_enter.getText().toString().trim().isEmpty()) {
                        Toast.makeText(getBaseContext(), "Can't post empty comments", Toast.LENGTH_SHORT).show();
                    }
                    //insert the comment and increment the comment count
                    else {
                        SharedPreferences user_profile = getBaseContext().getSharedPreferences(Integer.toString(GetUserId()), getBaseContext().MODE_PRIVATE);
                        Log.i("user_profile name",user_profile.getString("prof_name", ""));
                        new DataApiManager(getBaseContext()).getApiInterface().insert_comments_for_this_song(new CommentTextListRequest("insert", "comments", songs.get(position).getSong_id(), GetUserId(), holder.user_comment_enter.getText().toString().trim(), user_profile.getString("prof_name", "")))
                                .enqueue(new CustomResponseListener<DataHandlingResponse>() {
                                    @Override
                                    public void onSuccessfulResponse(DataHandlingResponse response) {
                                        Snackbar snackbar=Snackbar.make(song_coordinator,Html.fromHtml("<font color=\"#fafafb\"size=\"2\"><i>Cool, it's a comment</i></font>"),Snackbar.LENGTH_INDEFINITE);
                                        snackbar.setActionTextColor(Color.BLACK);
                                        snackbar.getView().setBackgroundColor(ContextCompat.getColor(SongActivity.this, R.color.colorAccent));
                                        snackbar.show();
                                        holder.num_of_comments.setText(Integer.toString(songs.get(position).getNum_of_comments()+1));
                                    }

                                    @Override
                                    public void onFailureResponse(ErrorResponse errorResponse) {
                                        showAlert("Comment insert", "Failed");
                                        //Toast.makeText(getBaseContext(), "Can't post the same comments", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }
            });

            if (!user_id_song_id_like.getBoolean("like", false))
                holder.add_like.setImageResource(R.drawable.before_like);
            else
                holder.add_like.setImageResource(R.drawable.after_like);

            holder.add_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //insert the like.
                    if (!user_id_song_id_like.getBoolean("like", false)) {
                        holder.add_like.setImageResource(R.drawable.after_like);
                        new DataApiManager(getBaseContext()).getApiInterface().insert_like(new InsertLikeModelRequest("insert", "likes", GetUserId(), songs.get(position).getSong_id(), true))
                                .enqueue(new CustomResponseListener<DataHandlingResponse>() {
                                    @Override
                                    public void onSuccessfulResponse(DataHandlingResponse response) {
                                        songs.get(position).setNum_of_likes(songs.get(position).getNum_of_likes() + 1);
                                        notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onFailureResponse(ErrorResponse errorResponse) {
                                        showAlert("Like insert failed", errorResponse.getMessage());
                                        Toast.makeText(getBaseContext(), "You have already liked the song", Toast.LENGTH_LONG).show();
                                    }
                                });


                        notifyDataSetChanged();
                        user_id_song_id_like.edit().putBoolean("like", true).apply();

                    } else {
                        holder.add_like.setImageResource(R.drawable.before_like);
                        Like like = new Like();
                        like.execute(0, GetUserId(), songs.get(position).getSong_id());
                        if (songs.get(position).getNum_of_likes() != 0)
                            songs.get(position).setNum_of_likes(songs.get(position).getNum_of_likes() - 1);
                        notifyDataSetChanged();
                        user_id_song_id_like.edit().putBoolean("like", false).apply();
                    }
                }
            });

            if (songs.get(position).getDate_created() != null)
                holder.date.setText(num_to_month(songs.get(position).getDate_created().getMonth()) + ", " + Integer.toString(songs.get(position).getDate_created().getDate()));
            if (songs.get(position).getNum_of_comments() != NULL) {
                holder.num_of_comments.setText(Integer.toString(songs.get(position).getNum_of_comments()));
            } else
                holder.num_of_comments.setText(Integer.toString(0));
            if (songs.get(position).getNum_of_likes() != NULL)
                holder.num_of_likes.setText(Integer.toString(songs.get(position).getNum_of_likes()));
            else
                holder.num_of_likes.setText(Integer.toString(0));

            holder.comment_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(songs.get(position).getNum_of_comments()>0) {
                        Dialog dialog = new Dialog(SongActivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.comment_box_layout);
                        TestPagerAdapter adapter = new TestPagerAdapter(getBaseContext(), songs.get(position).getSong_id());
                        AutoScrollViewPager pager = (AutoScrollViewPager) dialog.findViewById(R.id.comments_view_pager);
                        pager.startAutoScroll(3000);
                        pager.setInterval(3500);
                        pager.setScrollDurationFactor(6);
                        pager.setAdapter(adapter);

                        dialog.show();
                    }
                    else{
                        Toast.makeText(SongActivity.this,"No comments",Toast.LENGTH_SHORT);
                    }
                }
            });

            holder.play_song_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        play_song(songs.get(position).getSong_link());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return songs.size();
        }

        String num_to_month(int num) {
            switch (num) {
                case 1:
                    return "Jan";
                case 2:
                    return "Feb";
                case 3:
                    return "Mar";
                case 4:
                    return "Apr";
                case 6:
                    return "June";
                case 7:
                    return "July";
                case 8:
                    return "Aug";
                case 5:
                    return "May";
                case 9:
                    return "Sept";
                case 10:
                    return "Oct";
                case 11:
                    return "Nov";
                case 12:
                    return "Dec";
            }
            return "already returned";
        }

        class Like extends AsyncTask<Integer, Void, Boolean> {
            public boolean flag = false;

            @Override
            protected Boolean doInBackground(Integer... params) {
                if (params[0] == 1)
                    new DataApiManager(getBaseContext()).getApiInterface().insert_like(new InsertLikeModelRequest("insert", "likes", params[1], params[2], true))
                            .enqueue(new CustomResponseListener<DataHandlingResponse>() {
                                @Override
                                public void onSuccessfulResponse(DataHandlingResponse response) {
                                    //showAlert("like inserted","success");
                                    flag = true;
                                }

                                @Override
                                public void onFailureResponse(ErrorResponse errorResponse) {
                                    //showAlert("Like insert failed", errorResponse.getMessage());
                                    //Toast.makeText(getBaseContext(), "You have already liked the song", Toast.LENGTH_LONG).show();
                                }
                            });

                if (params[0] == 0)
                    new DataApiManager(getBaseContext()).getApiInterface().delete_like(new InsertLikeModelRequest("delete", "likes", params[1], params[2], false))
                            .enqueue(new CustomResponseListener<DataHandlingResponse>() {
                                @Override
                                public void onSuccessfulResponse(DataHandlingResponse response) {
                                    flag = true;
                                }

                                @Override
                                public void onFailureResponse(ErrorResponse errorResponse) {
                                }
                            });
                return flag;
            }
        }
    }
    public int GetUserId() {

        SharedPreferences hasura_id = getApplicationContext().getSharedPreferences("Hasura User ID",MODE_PRIVATE);
        return hasura_id.getInt("hasura_id",0);
    }

    public void play_song(String url) throws IOException {
        View view= getLayoutInflater().inflate(R.layout.play_music,null);
        Log.i("play_song",url);

        wifiLock = ((WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE))
                .createWifiLock(WifiManager.WIFI_MODE_FULL, "mylock");

        wifiLock.acquire();
        final AlertDialog.Builder dialog= new AlertDialog.Builder(this);
        dialog.setView(view);
        dialog.setCancelable(false);
        final AlertDialog alert= dialog.create();
        alert.show();

        back_button= (ImageButton) view.findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                mediaPlayer.release();
                mediaPlayer=null;
            }
        });

        Log.i("i am here","after wifi lock");
        seekBar= (SeekBar) view.findViewById(R.id.seekBar);
        seekBar.setMax(99);
        seekBar.setOnTouchListener(this);

        buttonPlayPause= (ImageButton) view.findViewById(R.id.play_pause);

        mediaPlayer= new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setDataSource(url);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);

        AD.show();
        mediaPlayer.prepareAsync();
        buttonPlayPause.setOnClickListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) mediaPlayer.release();
        if(wifiLock!=null) wifiLock.release();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
