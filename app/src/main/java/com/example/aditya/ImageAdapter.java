package com.example.aditya.nanodegreeproj1;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aditya on 02-04-2016.
 */
public class ImageAdapter extends ArrayAdapter<Result>
{
    //it has an inbuilt arraylist of movies which we will modify.

    private Context mContext;
    private int number_of_Images=20; //later add this in values folder.
    private String base_url="http://image.tmdb.org/t/p/";
    private String imageSize="w185";

    //constructor using const of arrayadapter
    public ImageAdapter(Context c,List<Result> listofMovies)
    {
        super(c,R.layout.image_item_alone,listofMovies); //passed in the list, used res which has only image view.
        mContext = c;

    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.i("mainActcalledgetview","debugFlag");

        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);



            imageView.setLayoutParams(new LayoutParams(200,300)); //image will be resized if necessary, check how does it look
            //imageView.setLayoutParams(new LayoutParams(null));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(0,0,0,0);
        } else {
            imageView = (ImageView) convertView;
        }

        //now i need to invoke picasso, to retrieve image from the given url, urls will be stored in a public static String
        //array in the main activity.
          //String url=MainActivity.getCompletePath(position); //obtained path since info will be collected in mainActivity
        String url=getCompletePath(position);
        Picasso.with(mContext).load(url).fit().into(imageView); //hopefully mcontext gets initialized
        // imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }

    private String getCompletePath(int position)
    {
        String modified_poster_path= this.getItem(position).getPosterPath();  //relative_poster_paths[position].substring(1); //need to remove the annoying backslash from each poster path.

        //StringBuilder sb=new StringBuilder();
        //sb.
        return base_url+imageSize+modified_poster_path; //i have not put / after w185, check

    }
}
