package com.dmm.bootcamp.yatter2023.infra.api.json

import com.squareup.moshi.Json

data class PostStatusJson(
  val status: String,
  @Json(name = "media_ids") val mediaIds: List<Int>
)
