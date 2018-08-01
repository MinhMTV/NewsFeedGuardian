package com.minhtv.newsfeedrecycler;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.minhtv.newsfeedrecycler.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    /**
     * Adapter for the list of earthquakes
     */
    private NewsAdapter mAdapter;
    private RecyclerView recList;

    /**
     * Constant value for the NewsFeed loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int NewsFeed_LOADER_ID = 1;

    public static final String LOG_TAG = MainActivity.class.getName();

    private static final String GUARDIAN_JSON = "http://content.guardianapis.com/search?order-by=newest&show-tags=contributor&page-size=20&api-key=5d306e31-6b06-4795-970a-d26408ed4497";

    /**
     * TextView that is displayed when the list is empty
     */
    private TextView mEmptyStateTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newsfeed_list_item);

        // Find a reference to the {@link ListView} in the layout
        RecyclerView NewsFeedListView = (RecyclerView) findViewById(R.id.list);
        NewsFeedListView.setHasFixedSize(true);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        NewsFeedListView.setLayoutManager(mLinearLayoutManager);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);

        NewsFeedListView.setEmptyView(mEmptyStateTextView);

        // Create a new adapter that takes an empty list of earthquakes as input
        mAdapter = new NewsAdapter(new ArrayList<News>(), this);
        recList.setAdapter(mAdapter);


        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        NewsFeedListView.setAdapter(mAdapter);

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(NewsFeed_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loading_spinner);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }


    //Loader methods
    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        // Create a new loader for the given URL
        return new NewsLoader(this, GUARDIAN_JSON);
    }

    @Override
    public void onLoadFinished
            (Loader<List<News>> loader, List<News> news) {
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_spinner);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No News Found."
        mEmptyStateTextView.setText(R.string.no_news);

        Log.i(LOG_TAG, "Test: onLoadFinished called...");

        // Clear the adapter of previous earthquake data
        mAdapter.clear();

        // If there is a valid list of {@link News}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (news != null && !news.isEmpty()) {
            mAdapter.addAll();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        Log.i(LOG_TAG, "Test: onLoadReset called...");
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }


}


