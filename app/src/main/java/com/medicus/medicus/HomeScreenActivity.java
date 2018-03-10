package com.medicus.medicus;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        nav_view = (NavigationView) findViewById(R.id.nav_view);

        this.postRequest();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            // Handle the camera action
        } else if (id == R.id.nav_appointment) {

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
                        checkLogin();
                    } catch (final JSONException e) {
                        Log.e("JSON", "Json parsing error: " + e.getMessage());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),
                                        "Json parsing error: " + e.getMessage(),
                                        Toast.LENGTH_LONG)
                                        .show();
                            }
                        });
                    }
                }
            }
        };
        async.execute();
    }

}
