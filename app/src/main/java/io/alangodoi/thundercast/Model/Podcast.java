package io.alangodoi.thundercast.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Podcast {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("artistName")
    @Expose
    private String artistName;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("feed")
    @Expose
    private String feed;
    @SerializedName("artwork")
    @Expose
    private String artwork;
    @SerializedName("copyright")
    @Expose
    private String copyright;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public Podcast(Integer id, String artistName, String title, String description, String link, String feed, String artwork, String copyright, String createdAt, String updatedAt) {
        this.id = id;
        this.artistName = artistName;
        this.title = title;
        this.description = description;
        this.link = link;
        this.feed = feed;
        this.artwork = artwork;
        this.copyright = copyright;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getFeed() {
        return feed;
    }

    public void setFeed(String feed) {
        this.feed = feed;
    }

    public String getArtwork() {
        return artwork;
    }

    public void setArtwork(String artwork) {
        this.artwork = artwork;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

}
