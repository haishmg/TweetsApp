package com.codepath.apps.restclienttemplate.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hganeshmurthy on 2/20/16.
 */
public class Entity {
    String media_url;
    String video_url;

    public String getMedia_url() {
        return media_url;
    }

    public String getVideo_url() {
        return video_url;
    }

    public static Entity fromJson(JSONObject jsonObject) {
        Entity u = new Entity();
        u.media_url="";
        u.video_url="";
        try {
            if(jsonObject.has("media")) {
                JSONArray jarray = jsonObject.getJSONArray("media");
                for (int i = 0; i < jarray.length(); i++) {
                    JSONObject jObject = (JSONObject) jarray.get(i);
                    if (jObject.getString("media_url") != null) {
                        u.media_url = jObject.getString("media_url");
                        break;
                    }
                }
            }
            if(jsonObject.has("extended_entities")) {
                JSONArray jarray = jsonObject.getJSONArray("media");
                for (int i = 0; i < jarray.length(); i++) {
                    JSONObject jObject = (JSONObject) jarray.get(i);
                    if(jObject.has("video_info")) {
                        JSONObject jvideoObj = jObject.getJSONObject("video_info");
                        if (jvideoObj.has("variants")) {
                            JSONArray vArray = jsonObject.getJSONArray("variants");
                            for (int j = 0; j < vArray.length(); j++) {
                                JSONObject videoObject = (JSONObject) vArray.get(j);
                                if(videoObject.getString("url") != null) {
                                    u.video_url = videoObject.getString("url");
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return u;
    }
}
