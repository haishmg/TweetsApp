package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.models.User;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import butterknife.Bind;
import butterknife.ButterKnife;

public class InsertTweetActivity extends AppCompatActivity {
    @Bind(R.id.ivProfilePicture) ImageView ivProfilePicture;
    @Bind(R.id.btnTweet) Button btnTweet;
    @Bind(R.id.etStatus) EditText etStatus;
    @Bind(R.id.tvUserName) TextView tvUserName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_tweet);
        ButterKnife.bind(this);
        User currentUser = new User();
        //String noUser = getIntent().getExtras().getString("noUser");
       // if (noUser.equals("false")) {
       // currentUser =  Parcels.unwrap(getIntent().getParcelableExtra("User"));
        Picasso.with(InsertTweetActivity.this).load(getIntent().getExtras().getString("profileURl")).transform(new CircleTransform()).into(ivProfilePicture);
        tvUserName.setText(getIntent().getExtras().getString("name"));
       // }
    }

    public void insertTweetDone(View v){
        Intent i = new Intent();
        i.putExtra("status",etStatus.getText().toString());
        setResult(RESULT_OK, i);
        finish();
    }


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
