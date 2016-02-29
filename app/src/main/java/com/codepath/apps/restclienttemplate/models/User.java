package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * Created by hganeshmurthy on 2/18/16.
 */
@Parcel
public class User {

    private String name;
    private Long uid;
    private String screenName;
    private String profileImageUrl;
    private Long followers;
    private Long following;
    private String tagLine;


    public String getName() {
        return name;
    }

    public Long getFollowers() {
        return followers;
    }

    public Long getFollowing() {
        return following;
    }



    public Long getUid() {
        return uid;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getTagLine(){ return tagLine;}

    public static User fromJson(JSONObject jsonObject)
    {
        User u = new User();
        try {
            u.name = jsonObject.getString("name");
            u.uid = jsonObject.getLong("id_str");
            u.screenName = jsonObject.getString("screen_name");
            u.profileImageUrl = jsonObject.getString("profile_image_url");
            u.followers = jsonObject.getLong("followers_count");
            u.following = jsonObject.getLong("friends_count");
            u.tagLine = jsonObject.getString("description");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return u;
    }
}
