package com.minhtv.newsfeedrecycler;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    /**
     * Adapter for the list of earthquakes
     */
    private NewsAdapter mAdapter;

    /**
     * Constant value for the NewsFeed loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int NewsFeed_LOADER_ID = 1;

    public static final String LOG_TAG = MainActivity.class.getName();

    private static final String GUARDIAN_JSON = "http://content.guardianapis.com/search?";
    /**
     * TextView that is displayed when the list is empty
     */
    private TextView mEmptyStateTextView;


    //order-by=newest&show-tags=contributor&page-size=20&api-key=

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newsfeed_list_item);

        // Find a reference to the {@link ListView} in the layout
        RecyclerView NewsFeedListView = (RecyclerView) findViewById(R.id.list);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(this, 1);
        NewsFeedListView.setLayoutManager(mGridLayoutManager);
        NewsFeedListView.setHasFixedSize(true);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        NewsFeedListView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);

        // Create a new adapter that takes an empty list of earthquakes as input
        mAdapter = new NewsAdapter(new ArrayList<News>(), this);
        NewsFeedListView.setAdapter(mAdapter);

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

    @Override
    public void onLoadFinished
            (Loader<List<News>> loader, List<News> news) {
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_spinner);
        loadingIndicator.setVisibility(View.GONE);
        Log.i(LOG_TAG, "Test: onLoadFinished called...");

        // Clear the adapter of previous news data
        mAdapter.clear(news);

        // If there is a valid list of {@link News}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (news != null && !news.isEmpty()) {
            mAdapter.addAll(news);
        } else {
            // Set empty state text to display "No News Found."
            mEmptyStateTextView.setText(R.string.no_news);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        Log.i(LOG_TAG, "Test: onLoadReset called...");
        // Loader reset, so we can clear out our existing data.
    }

    @Override
    // This method initialize the contents of the Activity's options menu.
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the Options Menu we specified in XML
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    // onCreateLoader instantiates and returns a new Loader for the given ID
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        //  getString retrieves a String value from the preferences. The second parameter is the default value for this preference.
        String orderBy = sharedPrefs.getString("order-by",
                getString(R.string.settings_order_by_default));

        // So i am setting the first String to "order-by" because when i use getString(R.string.settings_order_by_key, the getString returns a wrong String and
        // instead of passing "relevance" the String "orderBy" will get the String business   ...and i dont know how to fix it.

        Log.i(LOG_TAG, "this URL is " + orderBy);

        String section = sharedPrefs.getString(
                getString(R.string.settings_section_key),
                getString(R.string.settings_section_default));


        String itemNumber = sharedPrefs.getString(
                getString(R.string.settings_item_number_key),
                getString(R.string.settings_item_number_default));

        String contributor = sharedPrefs.getString(
                getString(R.string.contributor_key),
                getString(R.string.contributor_key));

        // parse breaks apart the URI string that's passed into its parameter
        Uri baseUri = Uri.parse(GUARDIAN_JSON);

        // buildUpon prepares the baseUri that we just parsed so we can add query parameters to it
        Uri.Builder uriBuilder = baseUri.buildUpon();

        Log.v(LOG_TAG, "url " + uriBuilder.toString());

        // Append query parameter and its value.
        uriBuilder.appendQueryParameter("order-by", orderBy);
        uriBuilder.appendQueryParameter("section", section);
        uriBuilder.appendQueryParameter("page-size", itemNumber);
        uriBuilder.appendQueryParameter("show-tags", contributor);

        String url = uriBuilder.toString() + "&api-key=";
        String apiKey = BuildConfig.THE_GUARDIAN_API_KEY;
        String finalurl = url + apiKey;

        Log.v(LOG_TAG, "final url is " + finalurl);

        // Return the completed uri `http://content.guardianapis.com/search?order-by=relevance&section=football&page-size=20&api-key=5d306e31-6b06-4795-970a-d26408ed4497
        return new NewsLoader(this, finalurl);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}


