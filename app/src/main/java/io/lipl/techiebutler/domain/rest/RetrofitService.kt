package io.lipl.techiebutler.domain.rest


import io.lipl.techiebutler.domain.model.PostItem
import io.lipl.techiebutler.domain.model.PostResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface RetrofitService {

    @GET("posts")
    suspend fun getPosts(@QueryMap reqParam: Map<String, Int>): Response<ArrayList<PostItem>>

    @GET("posts/{postId}")
    suspend fun getPostDetail(@Path("postId") postId: Int): Response<PostItem>


}