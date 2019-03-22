package com.example.testjetpack.dataflow.network

import com.example.testjetpack.models.git.network.SearchRepositoriesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface IGitApi {

    @GET("search/repositories")
    fun searchRepos(@Query("q") name: String, @Query("page") page: Int, @Query("per_page") perPage: Int ): Call<SearchRepositoriesResponse>

}