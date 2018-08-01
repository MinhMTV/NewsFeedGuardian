package com.minhtv.newsfeedrecycler;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.minhtv.newsfeedrecycler.News;
import com.minhtv.newsfeedrecycler.R;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private List<News> news_item;
    public Context mcontext;

    public NewsAdapter(List<News> news, Context context) {
        news_item = news;
        mcontext = context;
    }


    private static final String TIME_SEPERATOR = "T";

    private String date;
    private String time;


    // Create new views (invoked by the layout manager)
    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_main, null);

        // create ViewHolder

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // - get data from your itemsData at this position
        // - replace the contents of the view with that itemsData

        News currentNews = news_item.get(position);
        viewHolder.webtitle.setText(currentNews.getNewsTitle());
        viewHolder.categorie.setText(currentNews.getNewsCategory());
        viewHolder.author.setText(currentNews.getNewsAuthor());

        String fulldate = currentNews.getNewsDate();

        fulldate = fulldate.substring(0, fulldate.length() - 1);

        if (fulldate.contains(TIME_SEPERATOR)) {
            String[] parts = fulldate.split(TIME_SEPERATOR);
            date = parts[0];
            time = parts[1];
        }

        viewHolder.date.setText(date);
        viewHolder.time.setText(time);

        viewHolder.linearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                News currentNews = news_item.get(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri newsUri = Uri.parse(currentNews.getUrl());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);

                // Send the intent to launch a new activity
                mcontext.startActivity(websiteIntent);

            }
        });

    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView webtitle;
        public TextView categorie;
        public TextView author;
        public TextView date;
        public TextView time;
        public View linearlayout;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            webtitle = (TextView) itemLayoutView.findViewById(R.id.webTitle);
            categorie = (TextView) itemLayoutView.findViewById(R.id.categorie);
            author = (TextView) itemLayoutView.findViewById(R.id.author);
            date = (TextView) itemLayoutView.findViewById(R.id.date);
            time = (TextView) itemLayoutView.findViewById(R.id.time);
            linearlayout = (TextView) itemLayoutView.findViewById(R.id.linearlayout);


        }
    }


    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return news_item.size();
    }

}


