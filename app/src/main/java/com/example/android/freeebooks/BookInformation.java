package com.example.android.freeebooks;

import java.util.ArrayList;

/**
 * List item inputs created by Cian Nolan on 30/05/2017.
 */

class BookInformation {

    private String mThumbnailLink;
    private String mTitle;
    private ArrayList<String> mAuthor;
    private double mRating;
    private String mReaderUrl;

    BookInformation(String thumbnail, String title, ArrayList<String> author, double rating,
                    String reader) {
        mThumbnailLink = thumbnail;
        mTitle = title;
        mAuthor = author;
        mRating = rating;
        mReaderUrl = reader;
    }

    public String getThumbnailLink() {
        return mThumbnailLink;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getAuthor() {
        return checkAuthors();
    }

    public double getRating() {
        return mRating;
    }

    public String getReader() {
        return mReaderUrl;
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
