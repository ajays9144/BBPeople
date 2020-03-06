package com.bigBlue.people.connectivity;

import com.bigBlue.people.model.BaseModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiServices {

    @GET("/posts/1/comments")
    Call<List<BaseModel>> getComments();

    @GET("/posts/1/comments")
    Call<List<BaseModel>> getCommentAndPost(@Query("postId") String id);

    @GET("/posts")
    Call<List<BaseModel>> getPosts(@Query("userId") String id);
}
