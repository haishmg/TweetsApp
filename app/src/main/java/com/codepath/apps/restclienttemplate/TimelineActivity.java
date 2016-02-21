package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TimelineActivity extends AppCompatActivity {

    private RestClient client;
    ArrayList<Tweet> allTweets;
    TweetsAdapter rcAdapter;
    LinearLayoutManager linearLayoutManager;
    private static int count = 50;
    private static long since_id = 1;
    private static long max_id = 1;
    @Bind((R.id.swipeContainer)) SwipeRefreshLayout swipeContainer;
    private final int REQUEST_CODE_DISPLAY = 30;
    User currentUser;
    private String status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        ButterKnife.bind(this);

        client = RestApplication.getRestClient();
        currentUser = new User();

         allTweets = new ArrayList<Tweet>();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvTweets);
        linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(linearLayoutManager);
        rcAdapter = new TweetsAdapter(TimelineActivity.this, allTweets);
        recyclerView.setAdapter(rcAdapter);
        allTweets.clear();
        rcAdapter.notifyDataSetChanged();
        populateTimeline();


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

    // This method probably sends out a network request and appends new data items to your adapter.
    public void customLoadMoreDataFromApi(int offset) {
        //allTweets.clear();
        //rcAdapter.notifyDataSetChanged();
        max_id = allTweets.get(allTweets.size() - 1).getUid();
        getmoreTimeline();
    }

    private void populateTimeline() {

        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                for (int i = 0; i < response.length(); i++) {
                    Tweet tweet = null;
                    try {
                        tweet = Tweet.fromJson(response.getJSONObject(i));
                        allTweets.add(tweet);
                        rcAdapter.notifyItemChanged(allTweets.size());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                System.out.print("Inside failure");
            }
        }, since_id, count);
    }

    public void insertNewTweet(View v) {

        client.getCurrentUserInfo(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                currentUser = User.fromJson(response);
                Intent intent = new Intent(TimelineActivity.this, InsertTweetActivity.class);
                intent.putExtra("name", currentUser.getName());
                intent.putExtra("profileURl", currentUser.getProfileImageUrl());
                startActivityForResult(intent, REQUEST_CODE_DISPLAY);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });


    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_DISPLAY) {
            status = data.getExtras().getString("status");
            client.insertTweet (new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    super.onSuccess(statusCode, headers, response);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    System.out.print("Inside failure");
                }
            }, status);
            allTweets.clear();
            rcAdapter.notifyDataSetChanged();
            populateTimeline();
        }

    }


    private void getmoreTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);

                for (int i = 0; i < response.length(); i++) {
                    Tweet tweet = null;
                    try {
                        tweet = Tweet.fromJson(response.getJSONObject(i));
                        allTweets.add(tweet);
                        rcAdapter.notifyItemChanged(allTweets.size());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                System.out.print("Inside failure");
            }
        }, max_id, count);
    }


}
