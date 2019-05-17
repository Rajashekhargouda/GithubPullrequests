package com.meeshotask.myapp.model

import com.google.gson.annotations.SerializedName

data class PullRequest(@SerializedName("state")val state:String,
                       @SerializedName("title")val title:String,
                       @SerializedName("user")val user:User,
                       @SerializedName("created_at")val createdAt:String,
                       @SerializedName("body")val body:String,
                       @SerializedName("base")val base:Base,
                       @SerializedName("number")val number:String)


data class User(@SerializedName("login")val login:String,
                @SerializedName("avatar_url")val avatarUrl:String?=null)

data class Base(@SerializedName("repo")val repo:Repo)

data class Repo(@SerializedName("language")val language:String,
                @SerializedName("open_issues_count")val openIssuesCount:Long,
                @SerializedName("forks_count")val forksCount:Long,
                @SerializedName("watchers_count")val watchersCount:Long)