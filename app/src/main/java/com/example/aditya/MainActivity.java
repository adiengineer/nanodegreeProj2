package com.example.aditya.nanodegreeproj1;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;

import retrofit.Response;
import retrofit.Retrofit;



public class MainActivity extends Activity implements GridViewFragment.GridFragmentInterface,DetailsFragment.RefreshInterface {

  /*  private static int number_of_posters = 20; //later put val in values
    //public static String[] relative_poster_paths=new String[number_of_posters]; //this will hold the rel poster paths
    // static private String base_url="http://image.tmdb.org/t/p/";
    // static private String imageSize="w185"; //guide has a slash after this as well w185/

    //retrofit has been added to the project files

    //Root URL of our web service
    public static final String ROOT_URL = "http://api.themoviedb.org/"; //a little confused as to where to break the given url, try out.

    //end point string to be set acc to user choice, by default pop movies, modify this in a method set endpoint url
    private String endpointUrl = ROOT_URL + "3/movie/popular?api_key=PLEASEGENERATEYOUROWNKEY"; //TODO:api key has been removed as instructed, please generate your own key to use this code
    private String endpointopt1 = ROOT_URL + "3/movie/popular?api_key=PLEASEGENERATEYOUROWNKEY";
    private String endpointopt2 = ROOT_URL + "3/movie/top_rated?api_key=PLEASEGENERATEYOUROWNKEY";

    private SQLiteDatabase db; //temporary reference to a db.
    private static MyDBhelper mDbHelper;


    //List of type books this list will store type Book which is our data model
    private List<Result> movies; //this is where the processed json data will be stored. we have called its class as result
    private ImageAdapter imageAdapter = null;
    private GridView gridview;
    static float density;
  */   //these references haeve been moved to the fragment999

    public static final String ROOT_URL = "http://api.themoviedb.org/"; //a little confused as to where to break the given url, try out.

    //end point string to be set acc to user choice, by default pop movies, modify this in a method set endpoint url
    private String endpointUrl = ROOT_URL + "3/movie/popular?api_key=PLEASEGENERATEYOUROWNKEY"; //TODO:api key has been removed as instructed, please generate your own key to use this code
    private String endpointopt1 = ROOT_URL + "3/movie/popular?api_key=PLEASEGENERATEYOUROWNKEY";
    private String endpointopt2 = ROOT_URL + "3/movie/top_rated?api_key=PLEASEGENERATEYOUROWNKEY";


    private DetailsFragment detailsFragment=null; //to reger to the detail's fragment
    public static boolean isFavouriteSelected=false;
    //taken till here


    @Override
    public  void refreshgrid()
    {
       //call load from db
        GridViewFragment gf=(GridViewFragment)getFragmentManager().findFragmentById(R.id.gridFragment);
        gf.loadFromDb();
    }
    @Override //fragment interface implementation to share data between fragments
    public void sendDataToDetails(ContentValues values)
    {
        Log.i("devicetype","senddatacalled");
        DetailsFragment df=(DetailsFragment)getFragmentManager().findFragmentById(R.id.detailsFragment);
        df.setDetails(values); //hopefully this gives DetailsFragment data to update itself
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // hopefully layout selected according to screen size
          // tentatively, make put diff layouts with same name in different res folders. Then setContentView
          // will automatically pick the correct one.

        //TODO: add code to get the JSON input, parse that to get create an arraylist of Movie objects, pass that to imageAdapter.
        //TODO: while implement refresh of grid in the listener change the list of arrayAdapter and can nottifydatasetchange

/*  the rest of the code has been moved to the fragment
        detailsFragment=(DetailsFragment)getFragmentManager().findFragmentById(R.id.detailsFragment);
         if (detailsFragment.mDbHelper==null)   //changed to access fragment variable
              detailsFragment.mDbHelper= new MyDBhelper(this); //is context right


        //function to use retrofi
        // t

        //try calling async task before this, so that info will be available
        //we will need to pass in the movie arraylist to the adapter.

        //call getmovies to initialize the list movies
        //initialize the static reference only once.

        //  if (mDbHelper==null) //anyway activities are sharing the instance
        // mDbHelper = new MyDBhelper(this);   //will database helper give access to same database across two activi
        //is the context okay

        getmovies();


        //create an array adapter

        //
        //if (movies==null)
        // {
        //    Log.i("msg","damn its null!");
//
        //     }


        gridview = (GridView) findViewById(R.id.gridView);

        //attach the adapter to the gridcview
        */

    }

    @Override
    protected void onResume() {
        super.onResume();
        //do its usual stuff, then choose what to display
        if (isFavouriteSelected==true && ( getResources().getBoolean(R.bool.isTablet)==false )) //useful only for handset
        {
           GridViewFragment gf=(GridViewFragment)getFragmentManager().findFragmentById(R.id.gridFragment);
            gf.loadFromDb();
            //will refresh on coming back
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //TODO: check if tablet, if tablet then inflate menu with action bars
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        GridViewFragment gf=(GridViewFragment)getFragmentManager().findFragmentById(R.id.gridFragment);

        //TODO use switch statement instead of if else
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.most_popular) //if user selects this action
        {

              //check what is being currenly displayed. check last value of endpoint url
            if (gf.endpointUrl == gf.endpointopt1 && isFavouriteSelected==false) {

                return true; //alreadt displayed so no need to change anything
            }
                else {
                isFavouriteSelected=false;


                gf.setEndpointURL(this.endpointopt1); //changed to top rated, call get movies again
                gf.getmovies(); //
                return true; //return after finishing
            }
        } else if (id == R.id.top_rated) //if user selects this action
        {
            //check what is being currenly displayed. check last value of endpoint url

            if (gf.endpointUrl == gf.endpointopt2 && isFavouriteSelected==false) {

                return true; //alreadt displayed so no need to change anything
            }
            else {
                isFavouriteSelected= false;
               // this.setEndpointURL(this.endpointopt2); //changed to top rated, call get movies again
               // GridViewFragment gf=(GridViewFragment)getFragmentManager().findFragmentById(R.id.gridFragment);
                gf.setEndpointURL(this.endpointopt2);
                gf.getmovies(); //
                return true; //return after finishing
            }
        } else if (id == R.id.favourite) //if fav is selected populate the gridview from the database.
        {

            //populate gridView from db.
          //  GridViewFragment gf=(GridViewFragment)getFragmentManager().findFragmentById(R.id.gridFragment);
            gf.loadFromDb();
            isFavouriteSelected=true;
        }
        //add code to handle the action clicks
        return super.onOptionsItemSelected(item);
    }


    //will use retrofit
   /* private void getmovies() {
        //While the app fetched data we are displaying a progress dialog
        final ProgressDialog loading = ProgressDialog.show(this, "Fetching Data", "Please wait...", false, false);

        // i have used the retrofit library to simplify fetching json data.
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(this.ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        //TODO: remove after check test call to modifying the url, actually will have to be accroding to user selectio in the menu
        // this.setEndpointURL(this.endpointopt2);

        MovieAPI service = retrofit.create(MovieAPI.class);


        Call<Movie> call = service.getMovieResults(this.endpointUrl); //called by default using pop endpoint.

        //Executing Call
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Response<Movie> response, Retrofit retrofit) {

                //one doubt: why is response a list of results each containing movies list?
                try {

                    movies = response.body().getResults();

                    //Log.i(movies.get(0).getTitle(),"debugFlag");
                    //     Result temp=movies.get(0);
                    //   Log.i(temp.getTitle(),"moviename");
                    loading.dismiss();

                    //if not already made initialize and set.
                    if (imageAdapter == null) //will happen onCreate.
                    {
                        imageAdapter = new ImageAdapter(getApplicationContext(), movies);


                        gridview.setAdapter(imageAdapter);
                        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                //when a particular poster is clicked, launch the info activity and bundle info for the other activity

                               //TODO: here include a check if fragment B is present or not
                                //if it is present then call its method telling
                                // mainact to update its contents.
                                Intent i = new Intent(MainActivity.this, MovieInfoAct.class);
                                Result currMov = (Result) parent.getAdapter().getItem(position); // i know it is of type movie so cast is safe.
                                i.putExtra("movtitle", currMov.getTitle());
                                i.putExtra("movdate", currMov.getReleaseDate());
                                i.putExtra("movavg", currMov.getVoteAverage());
                                i.putExtra("movplot", currMov.getOverview()); //info as extras
                                i.putExtra("posterpath", currMov.getPosterPath());
                                i.putExtra("movieid", currMov.getId());
                                // Log.i(currMov.getId().toString(),"debugFlag");
                                startActivity(i);
                            }
                        });
                    } else //if we have alrady displayed some data, then change that data and notify
                    {
                        //remove all elemnets from the list as we have fresh data which has come in.
                        imageAdapter.clear();
                        imageAdapter.addAll(movies); //hopefully adds the list
                        imageAdapter.notifyDataSetChanged(); //tells gridview to refresh itself.
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
    }*/

    //to modify endpoint url if option item is selected accordingly.
   /* private void setEndpointURL(String endpointURL) {
        this.endpointUrl = endpointURL;
    }*/


    //TODO: later handle the tab case in which gridview is updated instantaneosly. need fragment communication.
   /*
    private void loadFromDb() {
        List<Result> moviesList = new ArrayList<Result>();

        //rss list is empty then retrieve information from the database.
        //we will try to get cursor if cursor is empty then db is empty

        db = MovieInfoAct.mDbHelper.getWritableDatabase(); //will it know which db to get? as the db has been init in the Movieinfact
        //db= MovieInfoAct.db;   //stack people say each call refers to the same db, let us see.
        String query = "SELECT * FROM " + DatabaseContract.tableDefinition.TABLE_NAME + " WHERE 1"; //query syntax to retrive the entire database.
        // it must not be null terminated.

        Cursor c = db.query(DatabaseContract.tableDefinition.TABLE_NAME, null, null, null, null, null, null);
        // Cursor c =db.rawQuery(query,null);

        if (c.moveToFirst() == false) {
            Toast.makeText(this, "No favourites", Toast.LENGTH_SHORT).show();
        } else

        {
            String posterPath = null;
            String overview = null;
            String releasedate = null;
            int id = 0;
            String title = null;
            double voteAverage = 0;

            while (!c.isAfterLast()) {

                if (c.getString(c.getColumnIndex("posterPath")) != null) {
                    posterPath = c.getString(c.getColumnIndex("posterPath"));
                }

                if (c.getString(c.getColumnIndex("overview")) != null) {
                    overview = c.getString(c.getColumnIndex("overview"));
                }

                if (c.getString(c.getColumnIndex("releasedate")) != null) {
                    releasedate = c.getString(c.getColumnIndex("releasedate"));
                }

                //no check known for empty int
                id = c.getInt(c.getColumnIndex("id"));

                if (c.getString(c.getColumnIndex("title")) != null) {
                    title = c.getString(c.getColumnIndex("title"));
                }

                //no emptyness test for double known
              //  voteAverage = c.getDouble(c.getColumnIndex("voteaverage"));
                 voteAverage=1.1;

                moviesList.add(new Result(posterPath, overview, releasedate, id, title, voteAverage));
                c.moveToNext();
            }

            db.close();
            imageAdapter.clear();
            imageAdapter.addAll(moviesList); //hopefully adds the list
            imageAdapter.notifyDataSetChanged();


            //the method assumes that imageAdapter is the adapter in service of gridView, this is ok
            //as by default when the app starts the gridview is populated after the call to
            // popular movies endpoint.


        }

    }
    */
}