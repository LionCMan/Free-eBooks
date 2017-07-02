package com.example.android.freeebooks;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class SearchActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<BookInformation>> {

    private static final String BOOK_REQUEST_URL =
            "https://www.googleapis.com/books/v1/volumes?q=";
    private static final String BOOK_FILTER_URL =
            "&filter=free-ebooks&maxResults=40";

    private static final int LOADER_ID = 1;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.list_view);
        mRecyclerView.setVisibility(GONE);
        mRecyclerView.setLayoutManager(mLayoutManager);

        final TextView state = (TextView) findViewById(R.id.state_text);
        state.setText(R.string.empty_view);
        state.setVisibility(View.VISIBLE);

        final EditText searchText = (EditText) findViewById(R.id.search_text);

        ProgressBar loading = (ProgressBar) findViewById(R.id.loading);
        loading.setVisibility(GONE);

        final ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        ImageView searchButton = (ImageView) findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

                final boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();
                if (isConnected) {
                    state.setVisibility(GONE);
                    mRecyclerAdapter = new ItemRecyclerAdapter(SearchActivity.this,
                            new ArrayList<BookInformation>());
                    mRecyclerView.setAdapter(mRecyclerAdapter);

                    if (!TextUtils.isEmpty(searchText.getText().toString().trim())) {
                        updateInfo();
                    } else {
                        Toast.makeText(SearchActivity.this, getString(R.string.toast),
                                Toast.LENGTH_SHORT).show();
                        state.setVisibility(View.VISIBLE);
                        mRecyclerView.setVisibility(GONE);
                    }
                } else {
                    ProgressBar loadingSpinner = (ProgressBar) findViewById(R.id.loading);
                    loadingSpinner.setVisibility(GONE);
                    mRecyclerView.setVisibility(GONE);

                    state.setText(getString(R.string.no_internet));
                    state.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void updateInfo() {
        EditText search = (EditText) findViewById(R.id.search_text);
        String searchResult = search.getText().toString();
        searchResult = searchResult.replace(" ", "+");
        String uriString = BOOK_REQUEST_URL + searchResult + BOOK_FILTER_URL;
        Bundle args = new Bundle();
        args.putString("Uri", uriString);
        android.app.LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(LOADER_ID, args, SearchActivity.this);
        if (loaderManager.getLoader(LOADER_ID).isStarted()) {
            //restart it if there's one
            getLoaderManager().restartLoader(LOADER_ID, args, SearchActivity.this);
        }
    }

    @Override
    public android.content.Loader<List<BookInformation>> onCreateLoader(int id, Bundle args) {
        mRecyclerView.setVisibility(GONE);

        ProgressBar loadingSpinner = (ProgressBar) findViewById(R.id.loading);
        loadingSpinner.setVisibility(View.VISIBLE);
        return new BookLoader(this, args.getString("Uri"));
    }

    @Override
    public void onLoadFinished(android.content.Loader<List<BookInformation>> loader,
                               List<BookInformation> books) {
        ProgressBar loadingSpinner = (ProgressBar) findViewById(R.id.loading);
        loadingSpinner.setVisibility(GONE);

        mRecyclerView.setVisibility(View.VISIBLE);
        mRecyclerAdapter = new ItemRecyclerAdapter(SearchActivity.this,
                new ArrayList<BookInformation>());
        if (books != null && !books.isEmpty()) {
            mRecyclerAdapter = new ItemRecyclerAdapter(SearchActivity.this, books);
            mRecyclerView.setAdapter(mRecyclerAdapter);
        } else {
            TextView state = (TextView) findViewById(R.id.state_text);
            state.setVisibility(View.VISIBLE);
            state.setText(R.string.no_internet);
        }
    }

    @Override
    public void onLoaderReset(android.content.Loader<List<BookInformation>> loader) {
        mRecyclerAdapter = new ItemRecyclerAdapter(SearchActivity.this,
                new ArrayList<BookInformation>());
    }

    private static class BookLoader extends AsyncTaskLoader<List<BookInformation>> {

        private final String mUrl;

        BookLoader(Context context, String url) {
            super(context);
            mUrl = url;
        }

        @Override
        protected void onStartLoading() {
            forceLoad();
        }

        @Override
        public List<BookInformation> loadInBackground() {
            if (mUrl == null) {
                return null;
            }
            return QueryUtils.fetchBookData(mUrl);
        }
    }


}

