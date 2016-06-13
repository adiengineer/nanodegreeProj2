package com.example.aditya.nanodegreeproj1;

/**
 * Created by Aditya on 03-06-2016.
 */
import retrofit.Call;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Url;

public interface ReviewInterface
{


    @GET //new feature of retrofit allows us to pass in the FULL endpoint url at runtime
    Call<ReviewList> getReviewResults(@Url String url);

    //Call<Movie> getMovieResults();  earlier call which was not dynamic
}
