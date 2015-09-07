package com.example.stewart.spotifyplayer.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.example.stewart.spotifyplayer.model.Image;

import java.util.Iterator;
import java.util.List;

/**
 * Created by sstew5 on 9/4/15.
 */
public enum Evaluation {
    INSTANCE;
    private final Image EMPTY_IMAGE = new Image();

    /**
     * Find smallest square image in a list
     * @param images
     * @return smallest square image or and empty image for picasso to default later
     */
    public Image getSmallestSquareImage(List<Image> images) {
        if (images.size() == 0) {
            return EMPTY_IMAGE;
        }
        Iterator<Image> imageIterator = images.iterator();
        Image smallestImage = null;
        while (imageIterator.hasNext()) {
            Image imageToCompare = imageIterator.next();
            int compareHeight = imageToCompare.getHeight();
            int compareWidth = imageToCompare.getWidth();
            if (isSquare(compareHeight, compareWidth)) {
                smallestImage = calculateSmallestImage(smallestImage, imageToCompare);
            }
        }
        //if no images are square, return an empty image, causing the default image to show
        if (smallestImage == null) {
            return EMPTY_IMAGE;
        }
        return smallestImage;
    }

    private Image calculateSmallestImage(Image smallestImage, Image imageToCompare) {
        if (smallestImage == null) {
            return imageToCompare;
        }
        //return the smallest of the two images
        return smallestImage.getHeight() + smallestImage.getWidth() <
                imageToCompare.getHeight() + imageToCompare.getWidth() ?
                smallestImage : imageToCompare;
    }

    /**
     * Considered square if values are greater then 0 and equal
     * @param compareHeight
     * @param compareWidth
     * @return
     */
    private boolean isSquare(int compareHeight, int compareWidth) {
        if (compareHeight > 0 && compareHeight == compareWidth) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp      A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }
}
