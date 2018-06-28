package com.github.alexhanxs.lighttraffic.base.service;

import com.github.alexhanxs.lighttraffic.model.entity.Project;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Alexhanxs on 2018/6/27.
 */

public interface TestService {

        String BASE_URL = "https://api.github.com/";

        @GET("users/{user}/repos")
        Observable<List<Project>> getProjectList(@Path("user") String user);

        @GET("/repos/{user}/{reponame}")
        Observable<Project> getProjectDetails(@Path("user") String user, @Path("reponame") String projectName);
}
