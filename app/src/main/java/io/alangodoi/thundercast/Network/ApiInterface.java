package io.alangodoi.thundercast.Network;

import java.util.List;

import io.alangodoi.thundercast.Model.Episode;
import io.alangodoi.thundercast.Model.Podcast;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("podcasts")
    Call<List<Podcast>> podcasts (
    );

    @GET("featured")
    Call<Podcast> featured (
    );

    //change to trending
    @GET("trending")
    Call<List<Podcast>> trending (
    );

    @GET("episodes")
    Call<Episode> episodes (
            @Path("podcastId") int podcastId
    );

//    @GET("{movie_id}/videos")
//    Call<> getTrailers (
//            @Path("movie_id") int movieId,
//            @Query("api_key") String apiKey,
//            @Query("language") String language
//    );
}
