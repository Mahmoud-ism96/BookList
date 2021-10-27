package com.example.android.booklist;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class Result extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {
    public static final String LOG_TAG = Result.class.getName();
    private BookAdapter mAdapter;
    private static final int BOOK_LOADER_ID = 1;
    private TextView mEmptyStateTextView;
    String USGS_REQUEST_URL;
//    // private static  String USGS_REQUEST_URL="https://www.googleapis.com/books/v1/volumes?q=a+intitle:+inauthor:&key=AIzaSyAyuxI0y8csjVRrQV5pM09ZmcjFISgezaw";
//    private String contains = new MainActivity().mContain;
//    private String title = new MainActivity().mTitle;
//    private String author = new MainActivity().mAuthor;
//    //    private String USGS_REQUEST_URL = "https://www.googleapis.com/books/v1/volumes?q=" + contains + "+intitle:" + title + "+inauthor:" + author + "&key=AIzaSyAyuxI0y8csjVRrQV5pM09ZmcjFISgezaw";
//    String USGS_REQUEST_URL = "https://www.googleapis.com/books/v1/volumes?q=+intitle:+inauthor:&key=AIzaSyAyuxI0y8csjVRrQV5pM09ZmcjFISgezaw";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);

        LoaderManager loadManager = getLoaderManager();
        loadManager.initLoader(BOOK_LOADER_ID, null, this);

        ListView bookListView = (ListView) findViewById(R.id.list);
        mAdapter = new BookAdapter(this, new ArrayList<Book>());

        bookListView.setAdapter(mAdapter);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        bookListView.setEmptyView(mEmptyStateTextView);

        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current earthquake that was clicked on
                Book currentEarthquake = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

    }


    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {
        if (MainActivity.mAuthor.equals("") && MainActivity.mTitle.equals(""))
            USGS_REQUEST_URL = "https://www.googleapis.com/books/v1/volumes?q=" + MainActivity.mContain + "&key=AIzaSyAyuxI0y8csjVRrQV5pM09ZmcjFISgezaw";
        else if (MainActivity.mTitle.equals(""))
            USGS_REQUEST_URL = "https://www.googleapis.com/books/v1/volumes?q=" + MainActivity.mContain + "+inauthor:" + MainActivity.mAuthor + "&key=AIzaSyAyuxI0y8csjVRrQV5pM09ZmcjFISgezaw";
        else if (MainActivity.mAuthor.equals(""))
            USGS_REQUEST_URL = "https://www.googleapis.com/books/v1/volumes?q=" + MainActivity.mContain + "+intitle:" + MainActivity.mTitle + "&key=AIzaSyAyuxI0y8csjVRrQV5pM09ZmcjFISgezaw";
        else
            USGS_REQUEST_URL = "https://www.googleapis.com/books/v1/volumes?q=" + MainActivity.mContain + "+intitle:" + MainActivity.mTitle + "+inauthor:" + MainActivity.mAuthor + "&key=AIzaSyAyuxI0y8csjVRrQV5pM09ZmcjFISgezaw";

        return new BookLoader(this, USGS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        View spinner = findViewById(R.id.loading_spinner);
        spinner.setVisibility(View.GONE);

        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (isConnected == false) {
            mEmptyStateTextView.setText(R.string.no_internet);
        } else
            mEmptyStateTextView.setText(R.string.no_results);
        mAdapter.clear();

        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        mAdapter.clear();
    }
}
