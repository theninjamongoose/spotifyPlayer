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
public enum Operation {
    INSTANCE;
    private final Image EMPTY_IMAGE = new Image();

    public Image getOptimalImage(List<Image> images) {
        //if no images are in the list, return an empty image, causing Picasso to choose default image
        if (images.size() == 0) {
            return EMPTY_IMAGE;
        } else {
            return getSmallestSquareImage(images);
        }
    }

    private Image getSmallestSquareImage(List<Image> images) {
        if (images.size() == 0) {
            throw new IllegalArgumentException("Attempting to get Smallest Image of an Empty Image list");
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
        return smallestImage.getHeight() + smallestImage.getWidth() <
                imageToCompare.getHeight() + imageToCompare.getWidth() ?
                smallestImage : imageToCompare;
    }

    private boolean isSquare(int compareHeight, int compareWidth) {
        if (compareHeight > 0 && compareWidth > 0 && compareHeight == compareWidth) {
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
