package com.dmm.bootcamp.yatter2023.infra.api

import com.dmm.bootcamp.yatter2023.infra.api.json.AccountJson
import com.dmm.bootcamp.yatter2023.infra.api.json.CreateAccountJson
import com.dmm.bootcamp.yatter2023.infra.api.json.FollowAccountJson
import com.dmm.bootcamp.yatter2023.infra.api.json.PostStatusJson
import com.dmm.bootcamp.yatter2023.infra.api.json.StatusJson
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface YatterApi {

  @GET("timelines/home")
  suspend fun getHomeTimeline(
    @Header("Authentication") token: String,
    @Query("only_media") onlyMedia: Boolean = false,
    @Query("max_id") maxId: String? = null,
    @Query("since_id") sinceId: String? = null,
    @Query("limit") limit: Int = 80
  ): List<StatusJson>

  @GET("timelines/public")
  suspend fun getPublicTimeline(
    @Query("only_media") onlyMedia: Boolean = false,
    @Query("max_id") maxId: String? = null,
    @Query("since_id") sinceId: String? = null,
    @Query("limit") limit: Int = 80
  ): List<StatusJson>

  @POST("accounts")
  suspend fun createNewAccount(
    @Body accountJson: CreateAccountJson
  ): AccountJson

  @GET("accounts/{username}")
  suspend fun getAccountByUsername(
    @Path("username") username: String
  ): AccountJson

  @POST("statuses")
  suspend fun postStatus(
    @Header("Authentication") token: String,
    @Body statusJson: PostStatusJson
  ): StatusJson

  @GET("accounts/{username}/follow")
  suspend fun followAccount(
    @Path("username") username: String
  ): FollowAccountJson
}
