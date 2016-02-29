package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.fragments.InsertTweetFragment;
import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.apache.http.Header;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
public class InsertTweetActivity extends AppCompatActivity {
    @Bind(R.id.ivProfilePicture) ImageView ivProfilePicture;
    @Bind(R.id.btnTweet) Button btnTweet;
    @Bind(R.id.etStatus) EditText etStatus;
    @Bind(R.id.tvUserName) TextView tvUserName;
    @Bind(R.id.tvCharacters) TextView tvCharacters;
    RestClient client;
    User currentUser;

    private final TextWatcher mTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //This sets a textview to the current length
            tvCharacters.setText(String.valueOf(s.length()));
            if(s.length()>=140) {
                tvCharacters.setTextColor(Color.RED);
                btnTweet.setEnabled(false);
            }
            else
                btnTweet.setEnabled(true);
        }

        public void afterTextChanged(Editable s) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_tweet);
        ButterKnife.bind(this);
        currentUser = new User();
        client = RestApplication.getRestClient();

        client.getCurrentUserInfo(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                currentUser = User.fromJson(response);
                //getSupportActionBar().setTitle("@" + currentUser.getScreenName());
                loadContent();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });


    }

    public void loadContent(){
        Picasso.with(InsertTweetActivity.this).load(currentUser.getProfileImageUrl()).transform(new CircleTransform()).into(ivProfilePicture);
        tvUserName.setText(currentUser.getName());
        etStatus.addTextChangedListener(mTextEditorWatcher);
    }

    public void insertTweetDone(View v){

        InsertTweetFragment fragmentUserTimeline = InsertTweetFragment.newInstance(etStatus.getText().toString());
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //ft.add(fragmentUserTimeline,null);
        ft.replace(R.id.flTemp, fragmentUserTimeline);
        ft.commit();
        donewithFragment();
    }

    public void donewithFragment()
    {
        Intent i = new Intent();
        i.putExtra("status",etStatus.getText().toString());
        setResult(RESULT_OK, i);
        finish();

    }




/*
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_DISPLAY) {
            status = data.getExtras().getString("status");
            client.insertTweet(new JsonHttpResponseHandler() {

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
        }

    }

*/









    public class CircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap.Config config = source.getConfig() != null ? source.getConfig() : Bitmap.Config.ARGB_8888;


            Bitmap bitmap = Bitmap.createBitmap(size, size, config);

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                    BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }

    }


}
