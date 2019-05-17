package com.meeshotask.myapp.networkhelper

import com.meeshotask.myapp.model.PullRequest
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface GithubApiService {
    @Headers("Content-Type:application/json")
    @GET("/repos/{owner}/{repo}/pulls")
    fun getOpenPullRequests(@Path("owner")ownerName:String,
                            @Path("repo")repoName:String):Single<Response<List<PullRequest>>>
}