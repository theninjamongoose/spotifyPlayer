package com.example.stewart.spotifyplayer.model;

/**
 * Created by sstew5 on 9/4/15.
 */
import java.util.ArrayList;
import java.util.List;

import com.example.stewart.spotifyplayer.util.Operation;

public class Artist {
    private String href;
    private String id;
    private List<Image> images = new ArrayList<Image>();
    private String name;
    private String uri;
    private int defaultImage;

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

    public String getUri() {
        return uri;
    }

    public Image getDefaultImage() {
        return Operation.INSTANCE.getOptimalImage(images);
    }
}