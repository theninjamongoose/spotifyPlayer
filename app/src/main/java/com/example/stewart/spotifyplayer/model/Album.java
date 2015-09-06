package com.example.stewart.spotifyplayer.model;

import com.example.stewart.spotifyplayer.util.Operation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sstew5 on 9/5/15.
 */
public class Album {

    private String albumType;
    private List<String> availableMarkets = new ArrayList<String>();
    private String href;
    private String id;
    private List<Image> images = new ArrayList<Image>();
    private String name;
    private String type;
    private String uri;

    public String getAlbumType() {
        return albumType;
    }

    public List<String> getAvailableMarkets() {
        return availableMarkets;
    }

    public String getHref() {
        return href;
    }

    public String getId() {
        return id;
    }

    public List<Image> getImages() {
        return images;
    }

    public String getName() {
        return name;
    }


    public String getType() {
        return type;
    }

    public String getUri() {
        return uri;
    }

    public Image getDefaultImage(){
        return Operation.INSTANCE.getOptimalImage(images);
    }
}
