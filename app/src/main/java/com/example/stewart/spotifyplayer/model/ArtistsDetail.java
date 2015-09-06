package com.example.stewart.spotifyplayer.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by sstew5 on 9/4/15.
 */
public class ArtistsDetail {

    private String href;
    @SerializedName("items")
    private List<Artist> artists = new ArrayList<Artist>();

    /**
     *
     * @return
     * The href
     */
    public String getHref() {
        return href;
    }

    /**
     *
     * @param href
     * The href
     */
    public void setHref(String href) {
        this.href = href;
    }

    /**
     *
     * @return
     * The artist
     */
    public List<Artist> getArtists() {
        return artists;
    }

    /**
     *
     * @param artists
     * The artist
     */
    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }


}
