package com.example.stewart.spotifyplayer.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sstew5 on 9/4/15.
 */

public class ArtistsResultParent {

    @SerializedName("artists")
    private ArtistsDetail artistsDetail;

    public ArtistsDetail getArtistsDetail() {
        return artistsDetail;
    }
}
