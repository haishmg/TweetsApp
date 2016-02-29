package com.codepath.apps.restclienttemplate.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TweetsAdapter;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by hganeshmurthy on 2/23/16.
 */


public class TweetsListFragment extends Fragment{

    //private RestClient client;
    ArrayList<Tweet> allTweets;
    TweetsAdapter rcAdapter;
    LinearLayoutManager linearLayoutManager;
    SwipeRefreshLayout swipeContainer;
   //private static int count = 50;
   // private long since_id = 1;
    //private long max_id = 1;

    private final int REQUEST_CODE_DISPLAY = 30;
    User currentUser;
    private String status;
    Tweet insertedTweet;

    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.rvTweets);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(rcAdapter);

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(getActivity());

        //getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        //getActionBar().hide();

        //client = RestApplication.getRestClient();
        currentUser = new User();
        allTweets = new ArrayList<Tweet>();
        linearLayoutManager = new LinearLayoutManager(getActivity());
        rcAdapter = new TweetsAdapter(getActivity(), allTweets);
        //populateTimeline();


    }

    public void addAll(ArrayList<Tweet> aTweets )
    {
        allTweets = aTweets;
    }



}
