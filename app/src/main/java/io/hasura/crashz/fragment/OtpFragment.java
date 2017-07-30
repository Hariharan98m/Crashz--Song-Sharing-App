package io.hasura.crashz.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import io.hasura.crashz.R;

/**
 * Created by HARIHARAN on 27-06-2017.
 */

public class OtpFragment extends Fragment {
    public static EditText otp;
    Helper helper;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.otp,container,false);
        otp = (EditText) view.findViewById(R.id.otp);
        otp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                helper.onOtpEntered(new String(s.toString()));
            }
        });
        return view;
    }

    public interface Helper{
        public String onOtpEntered(String otp);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try{
            helper = (Helper) activity;
        }catch (ClassCastException e){
            throw new ClassCastException(activity.toString()+"must implement Helper");
        }
    }

}



