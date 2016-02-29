package com.codepath.apps.restclienttemplate.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.codepath.apps.restclienttemplate.RestApplication;
import com.codepath.apps.restclienttemplate.RestClient;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by hganeshmurthy on 2/27/16.
 */
public class InsertTweetFragment  extends TweetsListFragment {

    private RestClient client;
    User currentTweet;
    String status;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = RestApplication.getRestClient();
        populateTimeline();
    }

    public static InsertTweetFragment newInstance(String insertStatus) {
        InsertTweetFragment insertFragment = new InsertTweetFragment();
        Bundle args = new Bundle();
        args.putString("status", insertStatus);
        insertFragment.setArguments(args);
        return insertFragment;
    }


    private void populateTimeline() {
        //allTweets.clear();
        status = getArguments().getString("status");
        client.insertTweet(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                insertedTweet = Tweet.fromJson(response);
                allTweets.add(0, insertedTweet);
                rcAdapter.notifyItemRangeInserted(0, 1);
                linearLayoutManager.scrollToPositionWithOffset(0, 0);


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                System.out.print("Inside failure");
            }
        }, status);
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
