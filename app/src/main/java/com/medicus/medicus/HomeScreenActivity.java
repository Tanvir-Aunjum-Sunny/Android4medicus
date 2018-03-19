package com.medicus.medicus;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

public class HomeScreenActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String verify = "false";
    NavigationView nav_view;
    ViewPager viewPager;
    User_Profile user_profile;
    Appointments appointments;
    ContentHomeScreen contentHomeScreen;
    Favourite favourite;
    BottomNavigationView navigation;
    MenuItem prevMenuItem;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        nav_view = (NavigationView) findViewById(R.id.nav_view);
        viewPager = findViewById(R.id.viewpager);
        this.postRequest();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        mTextMessage = (TextView) findViewById(R.id.message);
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.disableShiftMode(navigation);

        viewPager.addOnPageChangeListener(onPageChangeListener);
        setupViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        user_profile = new User_Profile();
        appointments = new Appointments();
        contentHomeScreen = new ContentHomeScreen();
        favourite = new Favourite();
        viewPagerAdapter.addFragment(contentHomeScreen);
        viewPagerAdapter.addFragment(appointments);
        viewPagerAdapter.addFragment(favourite);
        viewPagerAdapter.addFragment(user_profile);
        viewPager.setAdapter(viewPagerAdapter);
    }

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener(){
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            if (prevMenuItem != null) {
                prevMenuItem.setChecked(false);
            } else {
                navigation.getMenu().getItem(0).setChecked(false);
            }
            Log.d("page", "onPageSelected: " + position);
            navigation.getMenu().getItem(position).setChecked(true);
            prevMenuItem = navigation.getMenu().getItem(position);

        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    private boolean loadFragment(android.support.v4.app.Fragment fragment){

        if(fragment!=null){
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.fragment_layout,fragment)
//                    .commit();
            return true;
        }
        return false;
    }

    private void checkLogin() {
        if (verify.equals("false")) {
            nav_view.getMenu().setGroupVisible(R.id.main_group, false);
            nav_view.getMenu().findItem(R.id.nav_logout).setVisible(false);
            nav_view.getMenu().findItem(R.id.nav_login).setVisible(true);
        } else if(verify.equals("true")){
            nav_view.getMenu().setGroupVisible(R.id.main_group, true);
            nav_view.getMenu().findItem(R.id.nav_logout).setVisible(true);
            nav_view.getMenu().findItem(R.id.nav_login).setVisible(false);
        }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

        private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            android.support.v4.app.Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
//                    fragment = new ContentHomeScreen();
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.appointments:
//                    fragment = new Appointments();
                    viewPager.setCurrentItem(1);
                    break;
                case R.id.likes:
                    viewPager.setCurrentItem(2);
                    return true;
                case R.id.profile:
//                    fragment = new User_Profile();
                    viewPager.setCurrentItem(3);
                    break;
            }
            return true;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.notification_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.notification) {
            Toast.makeText(getApplicationContext(),"Notification",Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.navigation_home){
            startActivity(new Intent(HomeScreenActivity.this,User_Profile.class));
//            Toast.makeText(getApplicationContext(),"Hello BC",Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        android.support.v4.app.Fragment fragment = null;
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            // Handle the camera action
//            fragment = new User_Profile();
            viewPager.setCurrentItem(3);
//            startActivity(new Intent(HomeScreenActivity.this,User_Profile.class));

        } else if (id == R.id.nav_appointment) {
//            fragment = new Appointments();
            viewPager.setCurrentItem(1);
//            startActivity(new Intent(HomeScreenActivity.this,Appointments.class));

        } else if (id == R.id.nav_reminder) {

        } else if (id == R.id.nav_setting) {

        } else if (id == R.id.nav_TOU) {

        } else if (id == R.id.nav_about) {

        } else if (id == R.id.nav_logout) {
            //Save the __TOKEN__ first and change the instance
            SharedPreferences share = getSharedPreferences("PREFS", MODE_PRIVATE);
            SharedPreferences.Editor editor;
            editor = share.edit();
            editor.putString("token",null);
            editor.apply();

            Intent main = new Intent(HomeScreenActivity.this,
                    LoginActivity.class);
            HomeScreenActivity.this.startActivity(main);
            HomeScreenActivity.this.finish();
        } else if (id == R.id.nav_login) {
            //Save the __TOKEN__ first and change the instance
            SharedPreferences share = getSharedPreferences("PREFS", MODE_PRIVATE);
            SharedPreferences.Editor editor;
            editor = share.edit();
            editor.putString("token",null);
            editor.putInt("isIntroDone",0);
            editor.apply();

            Intent main = new Intent(HomeScreenActivity.this,
                    IntroActivity1.class);
            HomeScreenActivity.this.startActivity(main);
            HomeScreenActivity.this.finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




    //check if the user is logged in or out
    private void postRequest() {

        SharedPreferences share = getSharedPreferences("PREFS", MODE_PRIVATE);
        final String verify_token = share.getString("token","");

        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, String> async = new  AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {

                // CASE 2: For JSONObject parameter
                String url = "http://192.168.0.102:3000/api/v1/login/verify";
                JSONObject jsonBody;
                String requestBody;
                HttpURLConnection urlConnection = null;
                try {
                    jsonBody = new JSONObject();
                    jsonBody.put("JWT", verify_token);
                    requestBody = Utils.buildPostParameters(jsonBody);
                    urlConnection = (HttpURLConnection) Utils.makeRequest("POST", url, null, "application/json", requestBody);

                    // the same logic to case #1
                    InputStream inputStream;
                    // get stream
                    if (urlConnection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                        inputStream = urlConnection.getInputStream();
                    } else {
                        inputStream = urlConnection.getErrorStream();
                    }
                    // parse stream
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String temp,response = "";
                    while ((temp = bufferedReader.readLine()) != null) {
                        response += temp;
                    }
                    return response;
                } catch (JSONException | IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Cannot Connect To Internet",
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
                    e.printStackTrace();
                    return e.toString();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            }

            @Override
            protected void onPostExecute(String response) {
                super.onPostExecute(response);
                // do something...
                // Verify the user
                if (response != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        String verification = jsonObj.get("status").toString();
                        verify = verification;
                    } catch (final JSONException e) {
                        Log.e("JSON", "Json parsing error: " + e.getMessage());
                    }
                }
                checkLogin();
            }
        };
        async.execute();
    }

}
