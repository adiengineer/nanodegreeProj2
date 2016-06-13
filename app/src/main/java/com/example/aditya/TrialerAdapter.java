package com.example.aditya.nanodegreeproj1;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by Aditya on 01-06-2016.
 */
public class TrialerAdapter extends ArrayAdapter<Trailer> {
    Context context;

    public TrialerAdapter(Context c, List<Trailer> listofTrailers) {
        super(c, R.layout.trailer_item_alone, listofTrailers); //passed in the list, used res which has only image view.
        context = c;
    }



    public View getView(int position, View convertView, ViewGroup parent)
    {
        Log.i("inside get view","debugflag");
        TextView textView;
        if (convertView == null) {
// This a new view we inflate the new layout
       textView=new TextView(context);
    }
        else
        {
            textView=(TextView)convertView;
        }
// Now we can fill the layout with the right values

        textView.setText(getItem(position).getName()); //logically correct


        return textView;
    }


}