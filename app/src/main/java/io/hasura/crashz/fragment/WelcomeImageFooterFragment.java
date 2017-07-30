package io.hasura.crashz.fragment;

import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import io.hasura.crashz.R;

/**
 * Created by HARIHARAN on 27-06-2017.
 */

public class WelcomeImageFooterFragment extends Fragment {
    ConstraintLayout contain;
    ImageView image;
    TextView text;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_welcome_image_footer,container, false);

        contain=(ConstraintLayout) itemView.findViewById(R.id.container);
        text= (TextView) itemView.findViewById(R.id.welcomeText);
        image = (ImageView) itemView.findViewById(R.id.welcomeImage);

        Bundle bundle= getArguments();
        text.setText(Html.fromHtml(bundle.getString("welcomeText")));
        image.setImageResource(bundle.getInt("welcomeImage"));

        return itemView;
    }
}
