package com.example.aditya.nanodegreeproj1;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Aditya on 11-06-2016.
 */
public class GridViewFragment extends Fragment
{
    private static int number_of_posters = 20; //later put val in values
    //public static String[] relative_poster_paths=new String[number_of_posters]; //this will hold the rel poster paths
    // static private String base_url="http://image.tmdb.org/t/p/";
    // static private String imageSize="w185"; //guide has a slash after this as well w185/

    //retrofit has been added to the project files

    //Root URL of our web service
    public static final String ROOT_URL = "http://api.themoviedb.org/"; //a little confused as to where to break the given url, try out.

    //end point string to be set acc to user choice, by default pop movies, modify this in a method set endpoint url
    public static String endpointUrl = ROOT_URL + "3/movie/popular?api_key=PLEASEGENERATEYOUROWNKEY"; //TODO:api key has been removed as instructed, please generate your own key to use this code
    public static String endpointopt1 = ROOT_URL + "3/movie/popular?api_key=PLEASEGENERATEYOUROWNKEY";
    public static String endpointopt2 = ROOT_URL + "3/movie/top_rated?api_key=PLEASEGENERATEYOUROWNKEY";

    private SQLiteDatabase db; //temporary reference to a db.
    private static MyDBhelper mDbHelper;
    private boolean isFavouriteSelected=false;

    //List of type books this list will store type Book which is our data model
    private List<Result> movies; //this is where the processed json data will be stored. we have called its class as result
    private ImageAdapter imageAdapter = null;
    private GridView gridview;
    static float density;

    ProgressDialog loading=null;

  GridFragmentInterface gridFragmentInterface=null; //tell parent to execute its implentation via this reference

    //the interface for sending data to the other fragment, the method will be implemented by mainActivity.
    public interface GridFragmentInterface{
        public void sendDataToDetails(ContentValues values);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.grid_fragment_layout,container,false); //if does not work try true ie attach to root
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

         //do work here
        if (DetailsFragment.mDbHelper==null)
            DetailsFragment.mDbHelper= new MyDBhelper(getActivity());

        gridview = (GridView)view.findViewById(R.id.gridView);

        //now get the movies
        getmovies();
    }

    @Override
   /*public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            Activity activity=(Activity)context; //assume context passed is activity
            gridFragmentInterface = (GridFragmentInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(" must implement OnHeadlineSelectedListener");
        }
    } */


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            gridFragmentInterface = (GridFragmentInterface)getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " must implement OnArticleSelectedListener");
        }
    }

    //will use retrofit
    public void getmovies() {
        //While the app fetched data we are displaying a progress dialog
         loading = ProgressDialog.show(getActivity(), "Fetching Data", "Please wait...", false, false);

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
                        //to make the call same as that made in mainActivity earlier
                        imageAdapter = new ImageAdapter(getActivity().getApplication().getApplicationContext(), movies);


                        gridview.setAdapter(imageAdapter);
                        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                //when a particular poster is clicked, launch the info activity and bundle info for the other activity

                                //TODO: here include a check if fragment B is present or not
                                //if it is present then call its method telling
                                // mainact to update its contents.

                                //add condition here, if tablet then ask parent to update the other
                                //fragment's contentl, if handset then launch the other activity.

                                if (getResources().getBoolean(R.bool.isTablet) == true) {
                                    //ask parent to update activity B via this fragment's interface.
                                    ContentValues contentValues = new ContentValues();
                                    Result currMov = (Result) parent.getAdapter().getItem(position);
                                    contentValues.put("movTitle", currMov.getTitle());
                                    contentValues.put("movdate", currMov.getReleaseDate());
                                    contentValues.put("movavg", currMov.getVoteAverage());
                                    contentValues.put("movplot", currMov.getOverview()); //info as extras
                                    contentValues.put("posterpath", currMov.getPosterPath());
                                    contentValues.put("movieid", currMov.getId());

                                    //getting null pointer, so init to parent act
                                    gridFragmentInterface.sendDataToDetails(contentValues); // parent will do that.
                                    //now implement this in the parent
                                } else {
                                    //then we have a handset. So, launch anaother activity.
                                    Intent i = new Intent(getActivity(), MovieInfoAct.class);
                                    Result currMov = (Result) parent.getAdapter().getItem(position); // i know it is of type movie so cast is safe.
                                    i.putExtra("movtitle", currMov.getTitle());
                                    i.putExtra("movdate", currMov.getReleaseDate());
                                    i.putExtra("movavg", currMov.getVoteAverage());
                                    i.putExtra("movplot", currMov.getOverview()); //info as extras
                                    i.putExtra("posterpath", currMov.getPosterPath());
                                    i.putExtra("movieid", currMov.getId());
                                    // Log.i(currMov.getId().toString(),"debugFlag");
                                    startActivity(i);  //TODO: handle the activity launch for the handset case
                                }

                               /* Intent i = new Intent(MainActivity.this, MovieInfoAct.class);
                                Result currMov = (Result) parent.getAdapter().getItem(position); // i know it is of type movie so cast is safe.
                                i.putExtra("movtitle", currMov.getTitle());
                                i.putExtra("movdate", currMov.getReleaseDate());
                                i.putExtra("movavg", currMov.getVoteAverage());
                                i.putExtra("movplot", currMov.getOverview()); //info as extras
                                i.putExtra("posterpath", currMov.getPosterPath());
                                i.putExtra("movieid", currMov.getId());
                                // Log.i(currMov.getId().toString(),"debugFlag");
                                startActivity(i); */
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
                //if db is available load from there

              loading.dismiss();
                loadFromDb();
                Toast.makeText(
                        getActivity(),"Offline mode",Toast.LENGTH_SHORT
                ).show();

            }
        });


    }

    //TODO: later handle the tab case in which gridview is updated instantaneosly. need fragment communication.
    public void loadFromDb() {
        List<Result> moviesList = new ArrayList<Result>();

        //rss list is empty then retrieve information from the database.
        //we will try to get cursor if cursor is empty then db is empty

        db = DetailsFragment.mDbHelper.getWritableDatabase(); //will it know which db to get? as the db has been init in the Movieinfact
        //db= MovieInfoAct.db;   //stack people say each call refers to the same db, let us see.
        String query = "SELECT * FROM " + DatabaseContract.tableDefinition.TABLE_NAME + " WHERE 1"; //query syntax to retrive the entire database.
        // it must not be null terminated.

        Cursor c = db.query(DatabaseContract.tableDefinition.TABLE_NAME, null, null, null, null, null, null);
        // Cursor c =db.rawQuery(query,null);

        if (c.moveToFirst() == false) {
            Toast.makeText(getActivity(), "No favourites", Toast.LENGTH_SHORT).show();
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
                 voteAverage = c.getDouble(c.getColumnIndex("voteaverage"));
                //voteAverage=1.1;

                moviesList.add(new Result(posterPath, overview, releasedate, id, title, voteAverage));
                c.moveToNext();
            }

            db.close();

            //changed
           if (imageAdapter==null)
            {
               imageAdapter= new ImageAdapter(getActivity().getApplication().getApplicationContext(), moviesList);
                gridview.setAdapter(imageAdapter);

                //set listener again
                gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //when a particular poster is clicked, launch the info activity and bundle info for the other activity

                        //TODO: here include a check if fragment B is present or not
                        //if it is present then call its method telling
                        // mainact to update its contents.

                        //add condition here, if tablet then ask parent to update the other
                        //fragment's contentl, if handset then launch the other activity.

                        if (getResources().getBoolean(R.bool.isTablet) == true) {
                            //ask parent to update activity B via this fragment's interface.
                            ContentValues contentValues = new ContentValues();
                            Result currMov = (Result) parent.getAdapter().getItem(position);
                            contentValues.put("movTitle", currMov.getTitle());
                            contentValues.put("movdate", currMov.getReleaseDate());
                            contentValues.put("movavg", currMov.getVoteAverage());
                            contentValues.put("movplot", currMov.getOverview()); //info as extras
                            contentValues.put("posterpath", currMov.getPosterPath());
                            contentValues.put("movieid", currMov.getId());

                            //getting null pointer, so init to parent act
                            gridFragmentInterface.sendDataToDetails(contentValues); // parent will do that.
                            //now implement this in the parent
                        } else {
                            //then we have a handset. So, launch anaother activity.
                            Intent i = new Intent(getActivity(), MovieInfoAct.class);
                            Result currMov = (Result) parent.getAdapter().getItem(position); // i know it is of type movie so cast is safe.
                            i.putExtra("movtitle", currMov.getTitle());
                            i.putExtra("movdate", currMov.getReleaseDate());
                            i.putExtra("movavg", currMov.getVoteAverage());
                            i.putExtra("movplot", currMov.getOverview()); //info as extras
                            i.putExtra("posterpath", currMov.getPosterPath());
                            i.putExtra("movieid", currMov.getId());
                            // Log.i(currMov.getId().toString(),"debugFlag");
                            startActivity(i);  //TODO: handle the activity launch for the handset case
                        }

                               /* Intent i = new Intent(MainActivity.this, MovieInfoAct.class);
                                Result currMov = (Result) parent.getAdapter().getItem(position); // i know it is of type movie so cast is safe.
                                i.putExtra("movtitle", currMov.getTitle());
                                i.putExtra("movdate", currMov.getReleaseDate());
                                i.putExtra("movavg", currMov.getVoteAverage());
                                i.putExtra("movplot", currMov.getOverview()); //info as extras
                                i.putExtra("posterpath", currMov.getPosterPath());
                                i.putExtra("movieid", currMov.getId());
                                // Log.i(currMov.getId().toString(),"debugFlag");
                                startActivity(i); */
                    }
                });

            }
            imageAdapter.clear();
            imageAdapter.addAll(moviesList); //hopefully adds the list
            imageAdapter.notifyDataSetChanged();


            //the method assumes that imageAdapter is the adapter in service of gridView, this is ok
            //as by default when the app starts the gridview is populated after the call to
            // popular movies endpoint.


        }

    }

    public void setEndpointURL(String endpointURL) {
        this.endpointUrl = endpointURL;
    }
}
