package io.hasura.crashz.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.hasura.crashz.R;

/**
 * Created by HARIHARAN on 12-07-2017.
 */

public class TextViewFragment extends android.support.v4.app.Fragment{
    TextView comment_text;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.text_view,container,false);
        comment_text=(TextView) view.findViewById(R.id.put_text);

        Bundle bundle= getArguments();
        String user_name= bundle.getString("user_name");
        String comment= bundle.getString("comment_text");

        String display="<font color='#4787ed'><i>"+user_name+": </i></font>"+comment+"<br><br>";
        comment_text.setText(Html.fromHtml(display));
        Log.i("TextViewFragment",display);
        return view;
    }
}
