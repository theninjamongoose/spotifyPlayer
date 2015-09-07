package com.example.stewart.spotifyplayer.model;

/**
 * Created by sstew5 on 9/4/15.
 */

import java.util.ArrayList;
import java.util.List;

import com.example.stewart.spotifyplayer.util.Operation;

public class Artist {

    private String id;
    private List<Image> images = new ArrayList<Image>();
    private String name;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Image getDefaultImage() {
        return Operation.INSTANCE.getOptimalImage(images);
    }
}