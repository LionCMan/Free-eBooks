package com.example.android.freeebooks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.InputStream;

public class SearchActivity extends AppCompatActivity {

    private static final String BOOK_REQUEST_URL =
            "https://www.googleapis.com/books/v1/volumes?q=";

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.list_view);
        mRecyclerView.setVisibility(View.GONE);
        mRecyclerView.setLayoutManager(mLayoutManager);

        TextView state = (TextView) findViewById(R.id.state_text);
        state.setText(R.string.empty_view);
        state.setVisibility(View.VISIBLE);

        final EditText searchText = (EditText) findViewById(R.id.search_text);

        ProgressBar loading = (ProgressBar) findViewById(R.id.loading);
        loading.setVisibility(View.GONE);

        ImageView searchButton = (ImageView) findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Check Network Connection
                // TODO: If Connected {...} else {...}
            }
        });


    }

}
