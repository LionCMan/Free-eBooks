package com.example.android.freeebooks;

import java.util.ArrayList;


/**
 * Created by Cian Nolan on 30/05/2017.
 */

public class BookInformation {

    private int mThumbnailId;
    private String mTitle;
    private String mSubtitle;
    private ArrayList<String> mAuthor;
    private double mRating;

    public BookInformation(int thumbnailId, String title, String subtitle, ArrayList<String> author,
                           double rating) {
        mThumbnailId = thumbnailId;
        mTitle = title;
        mSubtitle = subtitle;
        mAuthor = author;
        mRating = rating;
    }

    public int getThumbnailId() {
        return mThumbnailId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getSubtitle() {
        return mSubtitle;
    }

    public String getAuthor() {
        return checkAuthors();
    }

    public double getRating() {
        return mRating;
    }


    private String checkAuthors() {
        String authors = mAuthor.get(0);
        if (mAuthor.size() > 1) {
            for (int i = 1; i < mAuthor.size(); i++) {
                authors += ", " + mAuthor.get(i);
            }
        }
        return authors;
    }
}
