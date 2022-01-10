package com.healthy.umfit.entity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Social implements Serializable {
    private String followeeCount;
    private String followerCount;

    public Social(String social) {
        try{
            JSONObject joSocial = new JSONObject(social);

            followeeCount = joSocial.getString("followeeCount");
            followerCount = joSocial.getString("followerCount");

        }catch (JSONException e){

        }
    }

    public String getFolloweeCount() {
        return followeeCount;
    }

    public void setFolloweeCount(String followeeCount) {
        this.followeeCount = followeeCount;
    }

    public String getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(String followerCount) {
        this.followerCount = followerCount;
    }
}
