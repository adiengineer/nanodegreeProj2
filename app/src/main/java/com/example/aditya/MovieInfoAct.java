package com.example.aditya.nanodegreeproj1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;


public class MovieInfoAct extends Activity implements  DetailsFragment.RefreshInterface{

  //used only for a handset

 /*   private String base_url="http://image.tmdb.org/t/p/";
    private String imageSize="w185";
    private Integer movieId;
    private String API_TRAILERCALL;
    private String API_REVIEWCALL;
    private List<Trailer> trialerList=null;
    private List<Review> reviewList=null;
    private TrialerAdapter trialerAdapter=null;
    private ReviewAdapter reviewAdapter=null;
    private ListView trailerListView;
    private ListView reviewListView;
    public static MyDBhelper mDbHelper=null;
    private SQLiteDatabase db; //associate with the class, so that I can reference it from the mainAct, try
    private Intent launcher; //the intent responsible for launching the activity.
*/

    @Override
    public void refreshgrid()
    {
        //ideally this will never be called, so do nothing
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mov_info_activity); //our layout is inflated.

       /* trailerListView=(ListView)findViewById(R.id.trailerdisplay);
        reviewListView=(ListView)findViewById(R.id.reviewdisplay);

      //  if (mDbHelper==null)  //do this in main Activity instead, since that starts first.
      //  mDbHelper= new MyDBhelper(this); //is context right


        getTrialers();

        setupLayout(); */


        //test
        //TextView debug=(TextView)findViewById(R.id.debug);
        //debug.setText("Can you see me?");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movie_info, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement TODO use switch case instead of if-else
        if (id == R.id.action_settings) {
            return true;
        }
     /*   else if (id==R.id.add_button) //moved to buttons of details frag
        {
           // add movie to the database.

               Log.i("oncreatecall","beforecallingwd");
            try
            {
                db = mDbHelper.getWritableDatabase();
            }
            catch (SQLException e)
            {
                Log.i("oncreatecall",e.getMessage());
            }

            Log.i("oncreatecall","aftercallingwd");
            ContentValues values = new ContentValues(); //contentValues to hold the movie info



                values.put(DatabaseContract.tableDefinition.COLUMN_NAME_ID,launcher.getIntExtra("movieid", 1));
            values.put(DatabaseContract.tableDefinition.COLUMN_NAME_POSTERPATH,launcher.getStringExtra("posterpath"));
            values.put(DatabaseContract.tableDefinition.COLUMN_NAME_OVERVIEW,launcher.getStringExtra("movplot"));
            values.put(DatabaseContract.tableDefinition.COLUMN_NAME_RELEASEDATE,launcher.getStringExtra("movdate"));
                values.put(DatabaseContract.tableDefinition.COLUMN_NAME_TITLE, launcher.getStringExtra("movtitle"));
           //     values.put(DatabaseContract.tableDefinition.COLUMN_NAME_VOTEAVERAGE, launcher.getDoubleExtra("movavg", 1.1));


               long retval=db.insert(DatabaseContract.tableDefinition.TABLE_NAME, null, values); //row added
             Long l=retval;
            Log.i("inserted?",l.toString());
            values.clear();

               //try closing db once updated,

            db.close();

            //testing if queries are correct or not
         //     this.loadFromDb(); //check if db is correct or not.

             Toast.makeText(this,"Added to database",Toast.LENGTH_SHORT).show();
        }
        else if (id==R.id.delete_button)
        {
            //delete the row entry corrsponding to the movie
            db = mDbHelper.getWritableDatabase(); //use db instan in add. try
             Integer movieId=launcher.getIntExtra("movieid",0);
            String WHEREClause=DatabaseContract.tableDefinition.COLUMN_NAME_ID+"="+movieId.toString()+";";

            db.delete(DatabaseContract.tableDefinition.TABLE_NAME,WHEREClause,null);
            db.close();
            Toast.makeText(this, "Deleted from database", Toast.LENGTH_SHORT).show();

            //later try closing only when we go back from movieact
        } */

        return super.onOptionsItemSelected(item);
    }

   /*private void setupLayout()
    {
        launcher=getIntent();
        ImageView poster=(ImageView)findViewById(R.id.poster);
        String poster_path= launcher.getStringExtra("posterpath");
        String modified_poster_path=base_url+imageSize+poster_path;


        //call picasso again.
        Picasso.with(this).load(modified_poster_path).into(poster);

        //TODO what kind of data can we get back from the imageview once poster has been loaded into it?
        // we don't need to store the image itself, its posterpath  will do. And I will store only
        //that data from the movie object which is required.

        //        setTitle(launcher.getStringExtra("movtitle"));



        //TextView releasedate=(TextView)findViewById(R.id.releasedate);
        TextView plot=(TextView)findViewById(R.id.plot);
        TextView rating=(TextView)findViewById(R.id.rating);


       // releasedate.setText(launcher.getStringExtra("movdate"));
        plot.setText(launcher.getStringExtra("movplot"));
        Double rat=launcher.getDoubleExtra("movavg", 0);
        rating.setText(rat.toString() + "/10" + "  " + launcher.getStringExtra("movdate"));
//        getTrialers();
    }*/

    //will use retrofit
   /* private void getTrialers()
    {
        final ProgressDialog loading = ProgressDialog.show(this, "Fetching Data", "Please wait...", false, false);
        //While the app fetched data we are displaying a progress dialog
        //final ProgressDialog loading = ProgressDialog.show(this, "Fetching Data", "Please wait...", false, false);

        movieId=getIntent().getIntExtra("movieid",5);
        API_TRAILERCALL="http://api.themoviedb.org/3/movie/"+movieId.toString()+"/videos?api_key=PLEASEGENERATEYOUROWNKEY";
        API_REVIEWCALL= "http://api.themoviedb.org/3/movie/"+movieId.toString()+"/reviews?api_key=PLEASEGENERATEYOUROWNKEY";

        // i have used the retrofit library to simplify fetching json data.
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.themoviedb.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Retrofit rev_retrofit = new Retrofit.Builder()
                .baseUrl("http://api.themoviedb.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        TrailerInterface service = retrofit.create(TrailerInterface.class);
        ReviewInterface rev_service=rev_retrofit.create(ReviewInterface.class);

        Call<TrailerList> call = service.getTrailerResults(this.API_TRAILERCALL); //called by default using pop endpoint.
        Call<ReviewList> rev_call = rev_service.getReviewResults(this.API_REVIEWCALL);
        //Executing Call
        call.enqueue(new Callback<TrailerList>() {
            @Override
            public void onResponse(Response<TrailerList> response, Retrofit retrofit) {

                //one doubt: why is response a list of results each containing movies list?
                try {
                    Log.i("justenteredonResponse", "debugFlag");
                    trialerList = response.body().getTrailers();
                    loading.dismiss();

                    //Log.i("afternavghene", "debugFlag");
                    Log.i(trialerList.get(0).getName(), "debugFlag");
                    Log.i("afternavghene", "debugFlag");
                    //the container was not null I checked.


                    // TextView debug=(TextView)findViewById(R.id.debug);
                    // Trailer temp=(Trailer)trailerListView.getItemAtPosition(0);
                    //Log.i(temp.getName(), "trailername");
                    //    if(temp.getName()==null)
                    //  debug.setText("it is null");

                    // debug.setText(temp.getName());

                    //if not already made initialize and set.
                    if (trialerAdapter == null) //will happen onCreate.
                    {
                        trialerAdapter = new TrialerAdapter(getApplicationContext(), trialerList);
                        trailerListView.setAdapter(trialerAdapter);
                        Log.i("adaptersetapparently", "debugFlag");


                        trailerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                //when a particular poster is clicked, launch the info activity and bundle info for the other activity
                                //Intent i = new Intent(MovieInfoAct.this, MovieInfoAct.class);
                                Trailer currTrailer = (Trailer) parent.getAdapter().getItem(position); // i know it is of type movie so cast is safe.

                                //send a broadcast for videos
                                Intent intent = new Intent(Intent.ACTION_VIEW);

                                String videoURL = "https://www.youtube.com/watch?v=" + currTrailer.getKey();
                                intent.setData(Uri.parse(videoURL));

                                // Check there is an activity that can handle this intent
                                if (intent.resolveActivity(getPackageManager()) == null) {
                                    // TODO No activity available. Do something else.
                                } else {
                                    startActivity(intent);
                                }

                            }
                        });

                        //try
                        trailerListView.setOnTouchListener(new View.OnTouchListener() {
                            // Setting on Touch Listener for handling the touch inside ScrollView
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                // Disallow the touch request for parent scroll on touch of child view
                                v.getParent().requestDisallowInterceptTouchEvent(true);
                                //  v.scro
                                return false;
                            }
                        });
                        //   setupLayout();
                        // trialerAdapter.notifyDataSetChanged(); //try
                    } else //if we have alrady displayed some data, then change that data and notify
                    {
                        //remove all elemnets from the list as we have fresh data which has come in.
                        trialerAdapter.clear();
                        trialerAdapter.addAll(trialerList); //hopefully adds the list
                        trialerAdapter.notifyDataSetChanged(); //tells gridview to refresh itself.
                    }
                    // placeAdapter();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

        //mimic the call for the reviews
        rev_call.enqueue(new Callback<ReviewList>() {
            @Override
            public void onResponse(Response<ReviewList> response, Retrofit retrofit) {

                //one doubt: why is response a list of results each containing movies list?
                try {

                    reviewList = response.body().getReviews();
                    loading.dismiss();


                    //if not already made initialize and set.
                    if (reviewAdapter == null) //will happen onCreate.
                    {
                        reviewAdapter = new ReviewAdapter(getApplicationContext(),reviewList);
                        reviewListView.setAdapter(reviewAdapter); //is using trailers xml is it okay?



                 /*      reviewListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            //listener for dealing with individual review items, collapsible behaviour
                                TextView t=(TextView)parent.getChildAt(position);
                                //ImageView v=(ImageView)findViewById(R.id.imageView);

                                if(t.getMaxLines()==3) {
                                    t.setMaxLines(Integer.MAX_VALUE);
                                    //v.setVisibility(View.INVISIBLE);
                                }
                                else {
                                    t.setMaxLines(3);
                                   // v.setVisibility(View.VISIBLE);
                                }
                            }
                        });

                        //try
                       reviewListView.setOnTouchListener(new View.OnTouchListener() {
                            // Setting on Touch Listener for handling the touch inside ScrollView
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                // Disallow the touch request for parent scroll on touch of child view
                                v.getParent().requestDisallowInterceptTouchEvent(true);

                                return false;
                            }
                        });

                        //   setupLayout();
                        // trialerAdapter.notifyDataSetChanged(); //try
                    } else //if we have alrady displayed some data, then change that data and notify
                    {
                        //remove all elemnets from the list as we have fresh data which has come in.
                        reviewAdapter.clear();
                        reviewAdapter.addAll(reviewList); //hopefully adds the list
                        reviewAdapter.notifyDataSetChanged(); //tells gridview to refresh itself.
                    }
                    // placeAdapter();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }
*/
   /* public void expandPlot(View view)
    {
        //showAll.setVisibility(View.INVISIBLE);

        TextView t=(TextView)findViewById(R.id.plot);
       // ImageView v=(ImageView)findViewById(R.id.imageView);

        if(t.getMaxLines()==2) {
            t.setMaxLines(Integer.MAX_VALUE);
           // v.setVisibility(View.INVISIBLE);
        }
        else {
            t.setMaxLines(2);
           // v.setVisibility(View.VISIBLE);
        }

    }*/


}
