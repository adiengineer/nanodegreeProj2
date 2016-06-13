package com.example.aditya.nanodegreeproj1;


import android.app.ActionBar;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Aditya on 11-06-2016.
 */
public class DetailsFragment extends Fragment implements View.OnClickListener
{

    private String base_url="http://image.tmdb.org/t/p/";
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
    //private Intent launcher; //the intent responsible for launching the activity.
    TextView title=null;
    ImageView poster=null;
    TextView plot=null;
    TextView rating=null;
    ImageButton favouriteButton=null;
    ImageButton deleteButton=null;
    TextView t=null;
    ImageView v=null;
    RefreshInterface ri=null;
    private ContentValues dataValues; //temp variable to hold the incoming Content values.
    private Intent launcher;
    private ProgressDialog loading=null;
    //implement set details method which the parent mainActiviry will call
    //before that implement the onCreate view of this fragment to setup its layout

    //interface definition to instruct grid to update itself
    public interface RefreshInterface{
        public void refreshgrid();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
         ri = (RefreshInterface)getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " must implement OnArticleSelectedListener");
        }
    }



    //inflate layout for the fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.details_fragment_layout,container,false); //if does not work try true ie attach to root


    }

    //set up the layout, instantiate the layout views
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        trailerListView=(ListView)view.findViewById(R.id.trailerdisplay);
        reviewListView=(ListView)view.findViewById(R.id.reviewdisplay);
         poster=(ImageView)view.findViewById(R.id.poster); //moved here from setupLayout, as findview valid here
        plot=(TextView)view.findViewById(R.id.plot);
         rating=(TextView)view.findViewById(R.id.rating);
        favouriteButton=(ImageButton)view.findViewById(R.id.favourite_button);
         deleteButton=(ImageButton)view.findViewById(R.id.delete_button);
         t=(TextView)view.findViewById(R.id.plot);
       //  title=(TextView)view.findViewById(R.id.title);
       //  v=(ImageView)view.findViewById(R.id.imageView);

        //set common listener for both which distinguishes based on id
         favouriteButton.setOnClickListener(this);
         deleteButton.setOnClickListener(this);

        Log.i("devicetype","testing device type");
        //if handset then will have to display the details right now
        if (getResources().getBoolean(R.bool.isTablet)==false) //if handset
        {
            Log.i("devicetype","feltwashandset");
            //then call
              launcher=getActivity().getIntent();

            ContentValues contentValues = new ContentValues();

            //workaround to use same methods
            contentValues.put("movTitle", launcher.getStringExtra("movtitle"));
            contentValues.put("movdate", launcher.getStringExtra("movdate"));
            contentValues.put("movavg", launcher.getDoubleExtra("movavg",0));
            contentValues.put("movplot", launcher.getStringExtra("movplot")); //info as extras
            contentValues.put("posterpath", launcher.getStringExtra("posterpath"));
            contentValues.put("movieid", launcher.getIntExtra("movieid",0));

            setDetails(contentValues);
        }
    }


    @Override
    public void onClick(View v)
    {
     //v is view being cliked
        if(v.getId()==R.id.favourite_button)
        {
            //add movie to db
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

            Log.i("oncreatecall", "aftercallingwd");
            ContentValues values = new ContentValues(); //contentValues to hold the movie info



            values.put(DatabaseContract.tableDefinition.COLUMN_NAME_ID,dataValues.getAsString("movieid"));
            values.put(DatabaseContract.tableDefinition.COLUMN_NAME_POSTERPATH,dataValues.getAsString("posterpath"));
            values.put(DatabaseContract.tableDefinition.COLUMN_NAME_OVERVIEW,dataValues.getAsString("movplot"));
            values.put(DatabaseContract.tableDefinition.COLUMN_NAME_RELEASEDATE,dataValues.getAsString("movdate"));
            values.put(DatabaseContract.tableDefinition.COLUMN_NAME_TITLE, dataValues.getAsString("movtitle"));
            values.put(DatabaseContract.tableDefinition.COLUMN_NAME_VOTEAVERAGE, dataValues.getAsDouble("movavg"));


            long retval=db.insert(DatabaseContract.tableDefinition.TABLE_NAME, null, values); //row added
            Long l=retval;
            Log.i("inserted?",l.toString());
            values.clear();

            //try closing db once updated,

            db.close();

            //testing if queries are correct or not
            //     this.loadFromDb(); //check if db is correct or not.


            Toast.makeText(getActivity(), "Added to database", Toast.LENGTH_SHORT).show();

            if (getResources().getBoolean(R.bool.isTablet)==true && ((MainActivity)getActivity()).isFavouriteSelected==true)
            ri.refreshgrid();
        }
        else
        {
            //otherwise delete movie from db
            //delete the row entry corrsponding to the movie
            db = mDbHelper.getWritableDatabase(); //use db instan in add. try
            Integer movieId=dataValues.getAsInteger("movieid");
            String WHEREClause=DatabaseContract.tableDefinition.COLUMN_NAME_ID+"="+movieId.toString()+";";

            int delrows=db.delete(DatabaseContract.tableDefinition.TABLE_NAME,WHEREClause,null);
            db.close();

            if (delrows!=0)
            Toast.makeText(getActivity(), "Deleted from database", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getActivity(),"Not present in database",Toast.LENGTH_SHORT).show();

            if (getResources().getBoolean(R.bool.isTablet)==true)
            ri.refreshgrid();
        }
    }

    public void setDetails(ContentValues contentValues)
        {
            Log.i("devicetype","setDetailscalled");
         dataValues = contentValues;
            getTrialers();
            setupLayout();
        }

    //will use retrofit
    private void getTrialers()
    {
         loading = ProgressDialog.show(getActivity(), "Fetching Data", "Please wait...", false, false);
        //While the app fetched data we are displaying a progress dialog
        //final ProgressDialog loading = ProgressDialog.show(this, "Fetching Data", "Please wait...", false, false);

        //movieId=getIntent().getIntExtra("movieid",5);
          movieId=dataValues.getAsInteger("movieid"); //change
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
                        trialerAdapter = new TrialerAdapter(getActivity(), trialerList); //hopefully context works
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
                                if (intent.resolveActivity(getActivity().getPackageManager()) == null) { //change
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
                    loading.dismiss();
                    Toast.makeText(getActivity(),"Reviews not available in offline mode",Toast.LENGTH_SHORT).show();
            }
        });

        //mimic the call for the reviews
        rev_call.enqueue(new Callback<ReviewList>() {
            @Override
            public void onResponse(Response<ReviewList> response, Retrofit retrofit) {

                //one doubt: why is response a list of results each containing movies list?
                try {

                    reviewList = response.body().getReviews();
          //          loading.dismiss();


                    //if not already made initialize and set.
                    if (reviewAdapter == null) //will happen onCreate.
                    {
                        reviewAdapter = new ReviewAdapter(getActivity().getApplicationContext(),reviewList);
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
                        });*/

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

                Toast.makeText(getActivity(),"Trailers and Reviews not available in offline mode",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupLayout()
    {
        //launcher=getIntent();
        //ImageView poster=(ImageView)view.findViewById(R.id.poster); //moved to onViewCreated, as recommended
        String poster_path= dataValues.getAsString("posterpath");
        String modified_poster_path=base_url+imageSize+poster_path;


        //call picasso again.
        Picasso.with(getActivity()).load(modified_poster_path).into(poster); //context ok?

        //TODO what kind of data can we get back from the imageview once poster has been loaded into it?
        // we don't need to store the image itself, its posterpath  will do. And I will store only
        //that data from the movie object which is required.

        //        setTitle(launcher.getStringExtra("movtitle"));



        //TextView releasedate=(TextView)findViewById(R.id.releasedate);
        //TextView plot=(TextView)findViewById(R.id.plot);
        //TextView rating=(TextView)findViewById(R.id.rating); ,moved to onViewCreated


        // releasedate.setText(launcher.getStringExtra("movdate"));
        plot.setText(dataValues.getAsString("movplot"));
        //title.setText(dataValues.getAsString("movTitle"));
        Double rat=dataValues.getAsDouble("movavg");
        rating.setText(rat.toString() + "/10" + "  " + dataValues.getAsString("movdate"));

        ActionBar ab=getActivity().getActionBar();
        ab.setTitle(dataValues.getAsString("movTitle"));

//        getTrialers();
    }

    public void expandPlot(View view)
    {
        //showAll.setVisibility(View.INVISIBLE);



        if(t.getMaxLines()==2) {
            t.setMaxLines(Integer.MAX_VALUE);
            v.setVisibility(View.INVISIBLE);
        }
        else {
            t.setMaxLines(2);
            v.setVisibility(View.VISIBLE);
        }

    }
}
