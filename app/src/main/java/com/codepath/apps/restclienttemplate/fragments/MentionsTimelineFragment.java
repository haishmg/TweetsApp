package com.codepath.apps.restclienttemplate.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.EndlessRecyclerViewScrollListener;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.RestApplication;
import com.codepath.apps.restclienttemplate.RestClient;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by hganeshmurthy on 2/25/16.
 */
public class MentionsTimelineFragment extends TweetsListFragment{

    private static int count = 25;
    private long since_id = 1;
    private long max_id = 1;
    private RestClient client;


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                allTweets.clear();
                rcAdapter.notifyDataSetChanged();
                populateTimeline();
                swipeContainer.setRefreshing(false);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                customLoadMoreDataFromApi(page);
            }
        });


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        client = RestApplication.getRestClient();
        populateTimeline();
    }


    // This method probably sends out a network request and appends new data items to your adapter.
    public void customLoadMoreDataFromApi(int offset) {
        //allTweets.clear();
        //rcAdapter.notifyDataSetChanged();
        max_id = allTweets.get(allTweets.size() - 1).getUid();
        getmoreTimeline();
    }

    private void populateTimeline() {
        allTweets.clear();
        //rcAdapter.notifyDataSetChanged();
        since_id = 1;
        max_id = 1;
        int initSize = allTweets.size();
        if (isOnline() == true) {
            client.getMentionsTimeline(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    super.onSuccess(statusCode, headers, response);
                    for (int i = 0; i < response.length(); i++) {
                        Tweet tweet = null;
                        try {
                            tweet = Tweet.fromJson(response.getJSONObject(i));
                            allTweets.add(tweet);
                            rcAdapter.notifyItemRangeInserted(i, allTweets.size());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        addAll(allTweets);
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    System.out.print("Inside failure");
                }
            }, count);

        }else {
            Toast.makeText(getActivity(), "No data found, please check your internet and try again", Toast.LENGTH_LONG).show();
        }
    }

    private void getmoreTimeline() {
        client.getMoreMentionsTimeline(new JsonHttpResponseHandler() {
            int initSize = allTweets.size();

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);

                for (int i = 0; i < response.length(); i++) {
                    Tweet tweet = null;
                    try {
                        tweet = Tweet.fromJson(response.getJSONObject(i));
                        allTweets.add(tweet);
                        // rcAdapter.notifyItemRangeInserted
                        //rcAdapter.notifyItemChanged(allTweets.size());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                rcAdapter.notifyItemRangeInserted(initSize, allTweets.size());
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                System.out.print("Inside failure");
            }
        }, max_id, count);
    }


    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }
        return false;
    }


}
