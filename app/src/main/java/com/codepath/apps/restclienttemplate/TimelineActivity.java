package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.restclienttemplate.fragments.HomeTimelineFragment;
import com.codepath.apps.restclienttemplate.fragments.MentionsTimelineFragment;
import com.codepath.apps.restclienttemplate.fragments.TweetsListFragment;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;

import java.util.ArrayList;


public class TimelineActivity extends AppCompatActivity {

    private static int count = 50;
    private long since_id = 1;
    private long max_id = 1;
    private final int REQUEST_CODE_DISPLAY = 30;
    User currentUser;
    private String status;
    Tweet insertedTweet;
    TweetsListFragment fragmentTweetsList;


    ArrayList<Tweet> allTweets;
    RestClient client;
    ViewPager vpPager;
    TweetsPagerAdapter twPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        allTweets= new ArrayList<Tweet>();
        client = RestApplication.getRestClient();
        vpPager = (ViewPager) findViewById(R.id.viewpager);
        twPagerAdapter = new TweetsPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(twPagerAdapter);
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabStrip.setViewPager(vpPager);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tbSearch);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;

    }

    //Return the order of the fragments in the view adapter


    public void onProfileView(View v){
        Intent n = new Intent(this,UserProfileActivity.class);
        startActivity(n);
    }

    public class TweetsPagerAdapter extends FragmentPagerAdapter{
        private String tabTitles[] = {"Home","Mentions"};

        public TweetsPagerAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 0){
                return new HomeTimelineFragment();
            }
            else if(position == 1){
                return new MentionsTimelineFragment();
            }
            else{
                return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }
    }


        public void insertNewTweet(View v) {
            Intent intent = new Intent(TimelineActivity.this, InsertTweetActivity.class);
            startActivity(intent);

/*
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

*/
        }


        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
           // if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_DISPLAY) {
                status = data.getExtras().getString("status");
                //twPagerAdapter.
                twPagerAdapter.notifyDataSetChanged();

              /*  client.insertTweet(new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        insertedTweet = Tweet.fromJson(response);
                        allTweets.add(0, insertedTweet);
                        //rcAdapter.notifyItemRangeInserted(0, 1);
                        //linearLayoutManager.scrollToPositionWithOffset(0, 0);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        System.out.print("Inside failure");
                    }
                }, status);
            }*/

          //  }

    /*
    public void playVideo(View v) {
        View parentRow = (View) v.getParent();
        ListView listView = (ListView) parentRow.getParent();
        final int position = listView.getPositionForView(parentRow);
        String url = allTweets.get(position).getEntity().getVideo_url();
        if(url != null) {
            Intent i = new Intent(TimelineActivity.this, VideoPlayerActivity.class);
            i.putExtra("url", url);
            startActivityForResult(i, 200);
        }

    }

    */


        }
}
