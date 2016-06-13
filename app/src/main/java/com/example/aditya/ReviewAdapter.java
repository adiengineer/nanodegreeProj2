package com.example.aditya.nanodegreeproj1;

/**
 * Created by Aditya on 03-06-2016.
 */
import android.content.Context;
import android.graphics.Typeface;
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
public class ReviewAdapter extends ArrayAdapter<Review> {
    Context context;

    public ReviewAdapter(Context c, List<Review> listofReviews) {
        super(c, R.layout.trailer_item_alone, listofReviews); //passed in the list, used res which has only image view.
        context = c;
    }



    public View getView(int position, View convertView, ViewGroup parent)
    {

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

       // textView.setMaxLines(10);
        textView.setText(getItem(position).getAuthor()+" says...\n"+getItem(position).getContent()); //logically correct
        textView.setTypeface(null,Typeface.BOLD_ITALIC);

        return textView;
    }


}