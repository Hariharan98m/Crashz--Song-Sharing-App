package io.hasura.crashz.activity;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.TableLayout;

import java.util.HashSet;

import io.hasura.crashz.R;
import io.hasura.crashz.fragment.GetFragmentClass;
import io.hasura.crashz.fragment.WelcomeImageFooterFragment;
import io.hasura.crashz.interceptor.AddCookiesInterceptor;
import io.hasura.crashz.model.ErrorResponse;
import io.hasura.crashz.model.MessageResponse;
import io.hasura.crashz.network.AuthApiManager;
import io.hasura.crashz.network.CustomResponseListener;
import io.hasura.crashz.network.NetworkURL;
import io.hasura.crashz.transformation.DepthPageTransformer;

public class WelcomeActivity extends BaseActivity {

    private ViewPager viewPager;
    PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        //PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().clear().commit();

        if(!(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getStringSet("REF_COOKIES", new HashSet<String>()).isEmpty())) {

            Log.i("In WelcomeActivity", "Cookie found");

            SharedPreferences user_profile_edited_flag= getApplicationContext().getSharedPreferences("user_profile_edited"+Integer.toString(getApplicationContext().getSharedPreferences("Hasura User ID",MODE_PRIVATE).getInt("hasura_id",0)),MODE_PRIVATE);
            if(!user_profile_edited_flag.getBoolean("user_profile_edited_flag",false)) {
                Log.i("In WelcomeAct--to EditProfile", "univ_flag is false, so insert data");
                EditProfileActivity.startActivity(WelcomeActivity.this, true);
            }
            else {
                Log.i("In WelcomeActivity", "Go to home activity as user_prof_flag is true and profile is edited");
                HomeActivity.startActivity(WelcomeActivity.this);
            }
        }

        getSupportActionBar().hide();

        viewPager= (ViewPager) findViewById(R.id.viewPager);
        viewPager.setPageTransformer(true,new DepthPageTransformer());
        pagerAdapter= new FragmentViewAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_dots);
        tabLayout.setupWithViewPager(viewPager, true);

    }

    @Override
    public void onBackPressed() {
        if(viewPager.getCurrentItem()==0)
        super.onBackPressed();
        else
            viewPager.setCurrentItem(viewPager.getCurrentItem()-1);
    }

    class FragmentViewAdapter extends FragmentStatePagerAdapter{
        //List<WelcomeImageFooterFragment> welcomeImageFooterFragment= new ArrayList<>();
        String[] text={"<font color=\"#07c\" size=\"20\"><b>Welcome to Crashz</b></font><br><br> A Place to stash your Songs in the Cloud<br><br> Tune them Anytime",
            "<font color=\"#07c\"><b>A Forum to Socialize | New Way of Making Friends</b></font> <br><br>Find out what your friend listens to <br><br> Comment on their Song Collections<br>&<br> Read Out yours",
        "Over"
        };

        public FragmentViewAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position==2)
                return new GetFragmentClass();
            Bundle bundl =new Bundle();
            bundl.putString("welcomeText",text[position]);
            if(position==0)
            bundl.putInt("welcomeImage",R.drawable.open);
            if(position==1)
                bundl.putInt("welcomeImage",R.drawable.socialise);
            WelcomeImageFooterFragment welcomeImageFooterFragment =new WelcomeImageFooterFragment();
            welcomeImageFooterFragment.setArguments(bundl);
            return welcomeImageFooterFragment;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    public void startLogin(View view){
        SignInActivity.startActivity(WelcomeActivity.this,false);
    }

    public void startRegister(View view){
        RegisterActivity.startActivity(WelcomeActivity.this);
    }
}
