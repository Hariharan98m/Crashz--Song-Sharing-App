package io.hasura.crashz.viewholder;

import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;
import io.hasura.crashz.R;

/**
 * Created by HARIHARAN on 11-07-2017.
 */

public class SongsListViewHolder extends RecyclerView.ViewHolder{
    public TextView song_name;
    public TextView composer_name;
    public TextView date;
    public TextView num_of_comments;
    public TextView num_of_likes;
    public ConstraintLayout viewPager_comments;
    public CardView song_tab;
    public EditText user_comment_enter;
    public ImageButton add_like, send_comment;
    public AutoScrollViewPager comments_view_pager;
    public ConstraintLayout view_pager_comments;
    public ImageButton play_song_button;
    public ImageButton comment_button;
    public SongsListViewHolder(View itemView) {
        super(itemView);
        song_name= (TextView) itemView.findViewById(R.id.song_name);
        composer_name=(TextView) itemView.findViewById(R.id.composer_name);
        date= (TextView) itemView.findViewById(R.id.date_created);
        num_of_comments= (TextView) itemView.findViewById(R.id.num_of_comments);
        num_of_likes= (TextView) itemView.findViewById(R.id.num_of_likes);
        song_tab=(CardView) itemView.findViewById(R.id.song);
        add_like=(ImageButton) itemView.findViewById(R.id.like_button);
        add_like.setImageResource(R.drawable.before_like);
        user_comment_enter=(EditText) itemView.findViewById(R.id.enter_comment);
        send_comment=(ImageButton) itemView.findViewById(R.id.send_comment_button);
        comments_view_pager= (AutoScrollViewPager) itemView.findViewById(R.id.comments_view_pager);
        view_pager_comments=(ConstraintLayout) itemView.findViewById(R.id.viewpager_comments);
        play_song_button= (ImageButton) itemView.findViewById(R.id.play_song_button);
        comment_button=(ImageButton) itemView.findViewById(R.id.song_page);
        //viewPager_comments= (ConstraintLayout) itemView.findViewById(R.id.viewpager_comments);
    }
}