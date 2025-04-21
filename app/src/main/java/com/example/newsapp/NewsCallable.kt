package com.example.newsapp

import retrofit2.Call
import retrofit2.http.GET

interface NewsCallable {
    @GET ("/v2/top-headlines?country=us&category=general&apiKey=0620622563044ede8de75a51c4b90b97&pageSize=30")
    fun getNews(): Call<News>
}


//