package com.medicus.medicus;

import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Appointments extends android.support.v4.app.Fragment {
    String[] mName;
    String[] mDetails;
    int[] mimage={R.drawable.bill,R.drawable.bill,R.drawable.bill,R.drawable.bill,R.drawable.bill,R.drawable.bill};
    ListView list;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_appointments, null);
        Resources res= getResources();
        mName = res.getStringArray(R.array.name);
        mDetails=res.getStringArray(R.array.details);
        list=view.findViewById(R.id.appointments_list);
        Adapter1 adapter1=new Adapter1(getContext(),mName,mimage,mDetails);
        list.setAdapter(adapter1);
        return view;
    }
}
class Adapter1 extends ArrayAdapter<String> {
    Context context;
    int[] images;
    String[] titleArray;
    String[] detailArray;

    Adapter1(Context c,String[] name,int[] imgs, String[] detail){
        super(c,R.layout.single_row2,R.id.appointment_name,name);
        this.context=c;
        this.images=imgs;
        this.titleArray=name;
        this.detailArray=detail;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row=inflater.inflate(R.layout.single_row2,parent,false);

        ImageView listImage=(ImageView) row.findViewById(R.id.appointment_image);
        TextView listName=(TextView)row.findViewById(R.id.appointment_name);
        TextView listDetail=(TextView)row.findViewById(R.id.appointment_detail);

        listImage.setImageResource(images[position]);
        listName.setText(titleArray[position]);
        listDetail.setText(detailArray[position]);

        return row;

    }
}

