package io.hasura.crashz.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import io.hasura.crashz.R;

/**
 * Created by HARIHARAN on 08-07-2017.
 */

public class NothingToShow extends Fragment {
    TextView msg, nothing_text;
    ImageView nothing_image;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.nothing_to_show,container,false);
        msg= (TextView) view.findViewById(R.id.msg);
        nothing_text= (TextView) view.findViewById(R.id.nothing_text);
        nothing_image=(ImageView) view.findViewById(R.id.nothing_image);
        nothing_text.setText(getArguments().getString("title"));
        msg.setText(getArguments().getString("msg"));
        switch (getArguments().getInt("mode")) {
            case 0:   nothing_image.setImageResource(R.drawable.penguin);
                break;

            case 1: nothing_image.setImageResource(R.drawable.butterfly);break;

            case 2: nothing_image.setImageResource(R.drawable.ice_cream);break;

            case 3: nothing_image.setImageResource(R.drawable.candy);break;
        }
        return view;
    }

}
