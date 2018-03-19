package com.medicus.medicus;

import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by kushbhardwaj on 17/03/18.
 */

public class ContentHomeScreen extends android.support.v4.app.Fragment {

    //variable for Layout search page
    String[] mTitle;
    String[] mDescription;
    String[] mDesignation;
    int[] mimage={R.drawable.bill,R.drawable.bill,R.drawable.bill,R.drawable.bill,R.drawable.bill,R.drawable.bill};
    ListView list;
    TextView searchText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.content_home_screen, null);

        //for Layout search page
        Resources res= getResources();
        mTitle = res.getStringArray(R.array.title);
        mDesignation = res.getStringArray(R.array.designation);
        mDescription = res.getStringArray(R.array.description);
        list = view.findViewById(R.id.listView);
        Adapter adapter = new Adapter(getContext(),mTitle,mimage,mDesignation,mDescription);
        list.setAdapter(adapter);

        //implement search bar
        searchText=view.findViewById(R.id.search_src_text);
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.v("Before", ""+s+"-"+start+"-"+count+"-"+after+"-");
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    // reset listview
                }
                else{
                    // perform search
//                    Toast.makeText(getActivity(),s,Toast.LENGTH_SHORT).show();
                    Log.v("onTextChanged", ""+s+"-"+start+"-"+before+"-"+count+"-");
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                Log.v("Before", ""+s);
            }
        });


        return view;
    }
}

class Adapter extends ArrayAdapter<String> {
    Context context;
    int[] images;
    String[] titleArray;
    String[] descriptionArray;
    String[] designationArray;

    Adapter(Context c,String[] title,int[] imgs, String[] desig, String[] desc){
        super(c,R.layout.single_row,R.id.listName,title);
        this.context=c;
        this.images=imgs;
        this.titleArray=title;
        this.designationArray=desig;
        this.descriptionArray=desc;
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

        listImage.setImageResource(images[position]);
        listName.setText(titleArray[position]);
        listDesignation.setText(designationArray[position]);
        listDescription.setText(descriptionArray[position]);

        return row;
    }
}
