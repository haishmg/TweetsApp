package com.codepath.apps.restclienttemplate;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

/**
 * Created by hganeshmurthy on 2/18/16.
 */
public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ArticleViewHolders> {

    private ArrayList<Tweet> allTweets;
    private Context context;
    int pos;

    public  TweetsAdapter(Context context, ArrayList<Tweet> allTweets) {
        this.allTweets = allTweets;
        this.context = context;
    }

    @Override
    public ArticleViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_tweet, null);
        ArticleViewHolders rcv = new ArticleViewHolders(layoutView);
        return rcv;
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onBindViewHolder(ArticleViewHolders holder, int position) {

        holder.tvTweet.setText(allTweets.get(position).getBody().toString());
        holder.tvUserName.setText(allTweets.get(position).getUser().getName().toString());
        //Glide.with(context).load(allTweets.get(position).getUser().getProfileImageUrl()).fitCenter().into(holder.ivProfilePicture);
        Picasso.with(context).load(allTweets.get(position).getUser().getProfileImageUrl()).transform(new CircleTransform()).into(holder.ivProfilePicture);
        holder.tvTimeSincePosting.setText(allTweets.get(position).getRelativeTime());
        if (allTweets.get(position).getEntity().getMedia_url() != null &&  !allTweets.get(position).getEntity().getMedia_url().equals(""))
            Picasso.with(context).load(allTweets.get(position).getEntity().getMedia_url()).into(holder.ivPhoto);

    }

    private int[] getViewLocations(View view) {
        int[] locations = new int[2];
        view.getLocationOnScreen(locations);
        return locations;
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

    @Override
    public int getItemCount() {
        return this.allTweets.size();
    }

    public class ArticleViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener  {


        TextView tvUserName;
        TextView tvTimeSincePosting;
        TextView tvTweet;
        ImageView ivPhoto;
        ImageView ivProfilePicture;


        public ArticleViewHolders(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
             tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
             tvTimeSincePosting = (TextView) itemView.findViewById(R.id.tVTimeSincePosting);
             tvTweet = (TextView) itemView.findViewById(R.id.tvTweet);
             ivPhoto = (ImageView) itemView.findViewById(R.id.ivPhoto);
             ivProfilePicture = (ImageView) itemView.findViewById(R.id.ivProfilePicture);
        }

        public void onClick(View v) {
         /*   //Toast.makeText(v.getContext(), "Clicked Position view holder = " +getPosition(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(v.getContext(), DisplayArticle.class);
            NYTArticle article =  articleList.get(getPosition());
            intent.putExtra("article", Parcels.wrap(article));
            v.getContext().startActivity(intent);
*/
        }

    }
}
