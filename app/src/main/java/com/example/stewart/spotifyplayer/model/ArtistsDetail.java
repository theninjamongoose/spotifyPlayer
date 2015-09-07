package com.example.stewart.spotifyplayer.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by sstew5 on 9/4/15.
 */
public class ArtistsDetail {

    @SerializedName("items")
    private List<Artist> artists = new ArrayList<Artist>();

    public List<Artist> getArtists() {
        return artists;
    }

}
