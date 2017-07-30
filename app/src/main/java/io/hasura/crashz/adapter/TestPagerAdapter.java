package io.hasura.crashz.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.hasura.crashz.R;
import io.hasura.crashz.fragment.TextViewFragment;
import io.hasura.crashz.model.CommentTextListRequest;
import io.hasura.crashz.model.ErrorResponse;
import io.hasura.crashz.model.InsertORUpdateIntoUserTable;
import io.hasura.crashz.model.SongComments;
import io.hasura.crashz.network.CustomResponseListener;
import io.hasura.crashz.network.DataApiManager;

/**
 * Created by HARIHARAN on 22-07-2017.
 */

public class TestPagerAdapter extends PagerAdapter{
        int song_id;
        Context context;
        LayoutInflater mLayoutInflator;

        public List<SongComments> list = new ArrayList<>();

        public TestPagerAdapter(Context context, int id) {
        mLayoutInflator=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        new DataApiManager(context).getApiInterface().get_comments_for_this_song(new CommentTextListRequest("select", "comments", id, 0, "", ""))
                    .enqueue(new CustomResponseListener<List<SongComments>>() {

                        @Override
                        public void onSuccessfulResponse(List<SongComments> response) {
                            list = response;
                            notifyDataSetChanged();
                        }

                        @Override
                        public void onFailureResponse(ErrorResponse errorResponse) {

                        }
                    });
        }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view= mLayoutInflator.inflate(R.layout.text_view,container,false);
        TextView textView= (TextView)view.findViewById(R.id.put_text);

        view.setTag(position);

        ((ViewPager) container).addView(view);
        String user_name= list.get(position).user_name;
        String comment= list.get(position).comment_text;

        textView.setText(Html.fromHtml("<font color='#4787ed'><i>"+user_name+": </i></font>"+comment+"<br><br>"));

        return ((Object) view);
    }

    @Override
     public int getCount() {
            return list.size();
        }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==(View) object;
    }
}

