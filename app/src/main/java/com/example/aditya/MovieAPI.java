package com.example.aditya.nanodegreeproj1;

/**
 * Created by Aditya on 04-04-2016.
 */
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Url;

public interface MovieAPI
{
    //tell retrofit from where should it get the json data and
    //give the method prototype for getting the json data as a list of movie objects.

    /* why is this void? ans: the method has a callback since the task will be asynchronous
    that callback will return a list of movies. The getMovies method however does
    not directly return anything and hence is declared void */

   // @GET("3/movie/popular?api_key=2cbf3960a93825f0becf35cc2d4a2429") //supposing that root part does not have /3...
    //public void getMovies(Callback<List<Movie>> response);

    @GET //new feature of retrofit allows us to pass in the FULL endpoint url at runtime
    Call<Movie> getMovieResults(@Url String url);

    //Call<Movie> getMovieResults();  earlier call which was not dynamic
}
