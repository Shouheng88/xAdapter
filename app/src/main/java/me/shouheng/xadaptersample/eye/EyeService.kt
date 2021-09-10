package me.shouheng.xadaptersample.eye

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface EyeService {

    @GET("v2/feed?&num=1")
    fun getFirstHomeDataAsync(@Query("date") date: Long?): Deferred<HomeBean>

    @GET
    fun getMoreHomeDataAsync(@Url url: String?): Deferred<HomeBean>
}