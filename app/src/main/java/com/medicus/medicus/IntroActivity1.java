package com.medicus.medicus;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Paint;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity1 extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
//    private IntroActivity1.SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private final Handler mHideHandler = new Handler();
    private ViewPager mViewPager;
    String str_device;
    MyPageAdapter obj_adapter;

    RelativeLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro1);

        init();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        Button button = (Button) findViewById(R.id.button5);
        button.setOnClickListener(new View.OnClickListener()   {
            public void onClick(View v)  {
                try {
                    changeInstance();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });






    }

    public void changeInstance(){
//        Intent main = new Intent(IntroActivity1.this,
//                ScreenSlidePagerActivity.class);
//        IntroActivity1.this.startActivity(main);
//        IntroActivity1.this.finish();
    }




    private void init() {
        mViewPager = (ViewPager) findViewById(R.id.container);
//        differentDensityAndScreenSize(getApplicationContext());
        List<Fragment> fragments = getFragments();
        mViewPager.setAdapter(obj_adapter);
        mViewPager.setClipToPadding(false);



        obj_adapter = new MyPageAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setPageTransformer(true, new ExpandingViewPagerTransformer());
        mViewPager.setAdapter(obj_adapter);
    }

    class MyPageAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments;


        public MyPageAdapter(FragmentManager fm, List<Fragment> fragments) {

            super(fm);

            this.fragments = fragments;

        }

        @Override

        public Fragment getItem(int position) {

            return this.fragments.get(position);

        }

        @Override

        public int getCount() {

            return this.fragments.size();

        }

    }

    private List<Fragment> getFragments() {

        List<Fragment> fList = new ArrayList<Fragment>();

        fList.add(MyFragment.newInstance("Need a Doctor?","Search doctor using the app and find the best doctor near you.", R.mipmap.ic_launcher_round));
        fList.add(MyFragment.newInstance("Make & Manage Appointment","Make and Manage appointments in a single app.", R.mipmap.ic_launcher_round));
        fList.add(MyFragment.newInstance("Meet your Doctor", "Meet your doctor & never miss your appointment.",R.mipmap.ic_launcher_round));

        return fList;

    }


}
