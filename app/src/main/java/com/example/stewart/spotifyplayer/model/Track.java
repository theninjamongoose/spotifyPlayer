package com.example.stewart.spotifyplayer.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sstew5 on 9/5/15.
 */
public class Track {


    private Album album;
    private List<Artist> artists = new ArrayList<>();
    private List<String> availableMarkets = new ArrayList<>();
    private int discNumber;
    private int durationMs;
    private boolean explicit;
    private String href;
    private String id;
    private String name;
    private int popularity;
    @SerializedName("preview_url")
    private String previewUrl;
    private int trackNumber;
    private String type;
    private String uri;


    public Album getAlbum() {
        return album;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public List<String> getAvailableMarkets() {
        return availableMarkets;
    }

    public int getDiscNumber() {
        return discNumber;
    }

    public int getDurationMs() {
        return durationMs;
    }

    public boolean isExplicit() {
        return explicit;
    }

    public String getHref() {
        return href;
    }


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPopularity() {
        return popularity;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public int getTrackNumber() {
        return trackNumber;
    }

    public String getType() {
        return type;
    }

    public String getUri() {
        return uri;
    }


}
