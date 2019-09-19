package br.com.eaglehorn.thundercast.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Subscription {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("podcastId")
    @Expose
    private Integer podcastId;

    public Subscription(Integer podcastId) {
        this.podcastId = podcastId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPodcastId() {
        return podcastId;
    }

    public void setPodcastId(Integer podcastId) {
        this.podcastId = podcastId;
    }
}
