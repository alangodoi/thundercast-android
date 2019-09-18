package br.com.eaglehorn.thundercast.Network;

import java.util.List;

import br.com.eaglehorn.thundercast.Model.Episode;
import br.com.eaglehorn.thundercast.Model.Podcast;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiInterface {

    @GET("podcasts")
    Call<List<Podcast>> podcasts ();

    @GET("featured")
    Call<Podcast> featured ();

    //change to trending
    @GET("trending")
    Call<List<Podcast>> trending ();

    @GET("episodes")
    Call<List<Episode>> episodes (
            @Query("podcastId") int podcastId
    );

    @GET
    Call<ResponseBody> download (
            @Url String fileUrl
    );

}
