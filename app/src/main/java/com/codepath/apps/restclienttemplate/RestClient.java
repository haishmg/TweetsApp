package com.codepath.apps.restclienttemplate;

import android.content.Context;
import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class RestClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "UD6MQONTNHuGVM1xjdG40xqHq";       // Change this
	public static final String REST_CONSUMER_SECRET = "Bmm2YZy3xKUiqyvjwjagEQtXvuY8NFKzWfxo5clY6oLCvKg2N0"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://cpsimpletweets"; // Change this (here and in manifest)

	public RestClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	public void getHomeTimeline(AsyncHttpResponseHandler handler, long since_id, int count) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		RequestParams params = new RequestParams();
		params.put("count", count);
		params.put("since_id",since_id);
		params.put("entities","true");
		getClient().get(apiUrl, params, handler);
	}


	public void getMentionsTimeline(AsyncHttpResponseHandler handler, int count) {
		String apiUrl = getApiUrl("statuses/mentions_timeline.json");
		RequestParams params = new RequestParams();
		params.put("count", 25);
		params.put("entities","true");
		getClient().get(apiUrl, params, handler);
	}

	public void getMoreMentionsTimeline(AsyncHttpResponseHandler handler, long max_id, int count) {
		String apiUrl = getApiUrl("statuses/mentions_timeline.json");
		RequestParams params = new RequestParams();
		params.put("count", 25);
		params.put("max_id", max_id);
		params.put("entities","true");
		getClient().get(apiUrl, params, handler);
	}

	public void getMoreTimeline(AsyncHttpResponseHandler handler, long max_id, int count) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		RequestParams params = new RequestParams();
		params.put("count", 25);
		params.put("max_id", max_id);
		params.put("entities","true");
		getClient().get(apiUrl, params, handler);
	}

	public void insertTweet(AsyncHttpResponseHandler handler, String status) {
		String apiUrl = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
		params.put("status", status);
		getClient().post(apiUrl, params, handler);
	}

	public void getCurrentUserInfo(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("account/verify_credentials.json");
		RequestParams params = new RequestParams();
		params.put("entities", "true");
		getClient().get(apiUrl, params, handler);
	}

	public void getUserTimeline(AsyncHttpResponseHandler handler,String screenName){
		String apiUrl = getApiUrl("statuses/user_timeline.json");
		RequestParams params = new RequestParams();
		params.put("count",25);
		params.put("entities", "true");
		params.put("screen_name",screenName);
		getClient().get(apiUrl, params, handler);
	}

	public void getMoreUserTimeline(AsyncHttpResponseHandler handler,String screenName, long max_id){
		String apiUrl = getApiUrl("statuses/user_timeline.json");
		RequestParams params = new RequestParams();
		params.put("count",25);
		params.put("entities", "true");
		params.put("screen_name",screenName);
		params.put("max_id", max_id);
		getClient().get(apiUrl, params, handler);
	}
}