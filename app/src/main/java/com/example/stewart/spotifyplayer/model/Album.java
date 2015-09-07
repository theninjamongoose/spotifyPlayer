package com.example.stewart.spotifyplayer.model;

import com.example.stewart.spotifyplayer.util.Evaluation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sstew5 on 9/5/15.
 */
public class Album {

    private String id;
    private List<Image> images = new ArrayList<Image>();
    private String name;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Image getDefaultImage(){
        return Evaluation.INSTANCE.getSmallestSquareImage(images);
    }
}
