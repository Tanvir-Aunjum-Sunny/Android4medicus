package com.medicus.medicus;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by kushbhardwaj on 17/03/18.
 */

public class ContentHomeScreen extends android.support.v4.app.Fragment{

    //variable for Layout search page
    ArrayList<String> mTitle = new ArrayList<>();
    ArrayList<String> mDescription = new ArrayList<>();
    ArrayList<String> mDesignation = new ArrayList<>();
    ArrayList<String> mimage = new ArrayList<>();
    AutoCompleteTextView autoCompleteTextView;
    ListView list;
    SharedPreferences share;
    private String verify_token = null;

    private String Specialization;
    ArrayList<String> ids = new ArrayList<>();
    ArrayList<String> specialization =new ArrayList<>();
    String doc_response;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.content_home_screen, null);

        list = view.findViewById(R.id.listView);
        //implement search bar
//        searchText=(AutoCompleteTextView)view.findViewById(R.id.search_text);

        // AutoComplete the EditText
        autoCompleteTextView = view.findViewById(R.id.search_text);
        // Get the string array

        try {
            JSONArray jsonArray_special = new JSONArray(Specialization);
            for(int i = 0; i < jsonArray_special.length(); i++){
                JSONObject jObj = jsonArray_special.getJSONObject(i);
                ids.add(jObj.getString("id"));
                specialization.add(jObj.getString("specialization_name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Create the adapter and set it to the AutoCompleteTextView
        ArrayAdapter<String> adapter_specialization =
                new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1, specialization);

        autoCompleteTextView.setAdapter(adapter_specialization);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {
                postRequest(pos);
            }
        });

        return view;
    }


    // TODO: get a better way to solve the problem
    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        share = activity.getSharedPreferences("PREFS", 0);
        Specialization = share.getString("AllSpecialization","");
        verify_token = share.getString("token","");
    }

    private void postRequest(final int id) {
        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, String> async = new  AsyncTask<Void, Void, String>() {
            JSONObject jObj;
            JSONArray jsonArray_special;
            ArrayList<String> arr = new ArrayList<>();
            @Override
            protected String doInBackground(Void... params) {

                // CASE 2: For JSONObject parameter
                String doc_id = ids.get(id);
                String url = "http://192.168.0.102:3000/api/v1/search";
                JSONObject jsonBody;
                String requestBody;
                HttpURLConnection urlConnection = null;

                try {
                    jsonBody = new JSONObject();
                    jsonBody.put("specialization_id", doc_id);
                    requestBody = Utils.buildPostParameters(jsonBody);
                    urlConnection = (HttpURLConnection) Utils.makeRequest("POST", url, verify_token, "application/json", requestBody);
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
                    doc_response = response;
                    Log.d("Doc",doc_response);
                    jsonArray_special = new JSONArray(doc_response);
                    for(int i = 0; i < jsonArray_special.length(); i++) {
                        jObj = jsonArray_special.getJSONObject(i);
                        arr.add(jObj.getString("profile_image_url"));
//                        URL url2 = new URL(jObj.getString("profile_image_url"));
//                        mimage.add(BitmapFactory.decodeStream(url2.openConnection().getInputStream()).toString());
//                        mTitle.add(jObj.getString("first_name")+" "+jObj.getString("last_name"));
//                        mDesignation.add(jObj.getString("qualification_name"));
//                        mDescription.add(jObj.getString("professional_statement"));
                    }

                    return response;
                } catch (JSONException | IOException e) {
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
                //for Layout search page
                try {
                    for(int i = 0; i < jsonArray_special.length(); i++){
                        jObj = jsonArray_special.getJSONObject(i);
                        mTitle.add(jObj.getString("first_name")+" "+jObj.getString("last_name"));
                        mDesignation.add(jObj.getString("qualification_name"));
                        mDescription.add(jObj.getString("professional_statement"));
//                        getImage(arr);
//                        Log.d("imagesBitmap",arr.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                Activity activity = getActivity();
//                Adapter adapter=new LazyImageLoadAdapter(activity, arr.toArray(new String[arr.size()]));
                Adapter adapter = new Adapter(getContext(),arr.toArray(new String[arr.size()]),mTitle.toArray(new String[mTitle.size()]),mDesignation.toArray(new String[mDesignation.size()]),mDescription.toArray(new String[mDescription.size()]));
                list.setAdapter(adapter);
            }
        };
        async.execute();
    }


}

class Adapter extends ArrayAdapter<String> {
    Context context;
    String[] images;
    String[] titleArray;
    String[] descriptionArray;
    String[] designationArray;

    Adapter(Context c,String[] imgs,String[] title, String[] desig, String[] desc){
        super(c,R.layout.single_row,R.id.listName,title);
        this.context=c;
        this.titleArray=title;
        this.designationArray=desig;
        this.descriptionArray=desc;
        this.images=imgs;
    }

    Adapter(Context c,String[] imgs){
        super(c,R.layout.single_row,R.id.listName);
        this.context = c;
        this.images=imgs;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row=inflater.inflate(R.layout.single_row,parent,false);

        ImageView listImage=(ImageView) row.findViewById(R.id.listImage);
        TextView listName=(TextView)row.findViewById(R.id.listName);
        TextView listDesignation=(TextView)row.findViewById(R.id.listDesignation);
        TextView listDescription=(TextView)row.findViewById(R.id.listDescription);

        Bitmap bitmap = null;
        try {
            byte[] encodeByte = Base64.decode(images[position], Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
                    encodeByte.length);
        } catch (Exception e) {
            e.getMessage();
        }

        listName.setText(titleArray[position]);
        listDesignation.setText(designationArray[position]);
        listDescription.setText(descriptionArray[position]);

//        listImage.setImageBitmap(BitmapFactory.decodeFile(images[position]));

//        for (int i=0; i<images.length;i++){
        if(position<images.length) {
            getImage(listImage, images[position], position);
        } else{
            getImage(listImage, images[position-1], position);
        }
//        }
//        getImage(listImage,position);
//        listImage.setImageBitmap(BitmapFactory.decodeFile());

        return row;
    }

    private void getImage(final ImageView imageView,final String imageUrl,final int position) {
//        final String[] bitmap = new String[1];
        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, Bitmap> async = new  AsyncTask<Void, Void, Bitmap>() {
            JSONObject jObj;
            Bitmap imageBitmap;
            //            ArrayList<String> arr = new ArrayList<>();
            @Override
            protected Bitmap doInBackground(Void... params) {
                // CASE 2: For JSONObject parameter
                try {
//                    jsonArray_special = new JSONArray(docs);
//                    for(int i = 0; i < images.length; i++) {
//                        jObj = jsonArray_special.getJSONObject(i);
//                        arr.add(jObj.getString("profile_image_url"));
                        URL url2 = new URL(imageUrl);
//                        images[i] = (BitmapFactory.decodeStream(url2.openConnection().getInputStream()).toString());
                        imageBitmap = BitmapFactory.decodeStream(url2.openConnection().getInputStream());
//                        bitmap[0] = mimage.get(position).toString();

//                        Log.d("DocImages",mimage.toString());
                        //                        mTitle.add(jObj.getString("first_name")+" "+jObj.getString("last_name"));
                        //                        mDesignation.add(jObj.getString("qualification_name"));
                        //                        mDescription.add(jObj.getString("professional_statement"));

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return imageBitmap;
            }

            @Override
            protected void onPostExecute(Bitmap response) {
                super.onPostExecute(response);
                imageView.setImageBitmap(response);
                //for Layout search page
//                try {
//                    for(int i = 0; i < jsonArray_special.length(); i++){
//                        jObj = jsonArray_special.getJSONObject(i);
//                        mTitle.add(jObj.getString("first_name")+" "+jObj.getString("last_name"));
//                        mDesignation.add(jObj.getString("qualification_name"));
//                        mDescription.add(jObj.getString("professional_statement"));
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                Activity activity = getActivity();
////                Adapter adapter=new LazyImageLoadAdapter(activity, arr.toArray(new String[arr.size()]));
//                Adapter adapter = new Adapter(getContext(),mimage.toArray(new String[mimage.size()]));
//                list.setAdapter(adapter);
            }
        };
        async.execute();
//        return bitmap[0];
    }
}
