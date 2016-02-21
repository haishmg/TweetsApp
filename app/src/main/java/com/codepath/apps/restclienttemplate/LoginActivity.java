package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.oauth.OAuthLoginActionBarActivity;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;
import org.parceler.Parcels;

public class LoginActivity extends OAuthLoginActionBarActivity<RestClient> {

	private RestClient client;
	User currentUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		client = RestApplication.getRestClient();
		currentUser = new User();
	}


	// Inflate the menu; this adds items to the action bar if it is present.
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	// OAuth authenticated successfully, launch primary authenticated activity
	// i.e Display application "homepage"
	@Override
	public void onLoginSuccess() {
		System.out.print("Inside Login");
		client.getCurrentUserInfo(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				currentUser = User.fromJson(response);

				Intent i = new Intent(LoginActivity.this, TimelineActivity.class);
				i.putExtra("User", Parcels.wrap(currentUser));
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(i);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});


	}

	// OAuth authentication flow failed, handle the error
	// i.e Display an error dialog or toast
	@Override
	public void onLoginFailure(Exception e) {
		e.printStackTrace();
	}

	// Click handler method for the button used to start OAuth flow
	// Uses the client to initiate OAuth authorization
	// This should be tied to a button used to login
	public void loginToRest(View view) {
		getClient().connect();
	}

}
