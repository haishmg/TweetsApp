package com.codepath.apps.restclienttemplate.models;

import android.text.format.DateUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;

/**
 * Created by hganeshmurthy on 2/18/16.
 */
public class Tweet {

    private String body;
    private Long uid;
    private String createdAt;
    private User user;
    private Entity entity;

    public String getRelativeTime() {
        return relativeTime;
    }

    private String relativeTime;


    public String getBody() {
        return body;
    }

    public Long getUid() {
        return uid;
    }


    public String getCreatedAt() {
        return createdAt;
    }

    public User getUser() {
        return user;
    }

    public Entity getEntity() {
        return entity;
    }

    public static Tweet fromJson(JSONObject jsonObject) {
        Tweet tweet = new Tweet();
        try {
            tweet.body = jsonObject.getString("text");
            tweet.uid = jsonObject.getLong("id");
            tweet.createdAt = jsonObject.getString("created_at");
            tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
            tweet.entity = Entity.fromJson(jsonObject.getJSONObject("entities"));
            long currentTime = System.currentTimeMillis();

            if (jsonObject.getString("created_at") != null) {

                try {
                    DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
                    Date date = null;
                    date = dateFormat.parse(jsonObject.getString("created_at"));

                    long unixTime = (long) date.getTime();
                    System.out.println(unixTime);

                    CharSequence relativeTime = DateUtils.getRelativeTimeSpanString(unixTime, currentTime, 60000);
                    if (relativeTime.toString().contains("minutes"))
                        relativeTime = relativeTime.toString().replace("minutes ago", "m");
                    else if (relativeTime.toString().contains("minute"))
                        relativeTime = relativeTime.toString().replace("minute ago", "m");
                    else if (relativeTime.toString().contains("hours"))
                        relativeTime = relativeTime.toString().replace("hours ago", "h");
                    else if (relativeTime.toString().contains("hour"))
                        relativeTime = relativeTime.toString().replace("hour ago", "h");
                    else if (relativeTime.toString().contains("weeks"))
                        relativeTime = relativeTime.toString().replace("weeks ago", "w");
                    else if (relativeTime.toString().contains("week"))
                        relativeTime = relativeTime.toString().replace("week ago", "w");

                    tweet.relativeTime = relativeTime.toString();
                }catch(ParseException e){
                    e.printStackTrace();
                }
                }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tweet;

    }
}
