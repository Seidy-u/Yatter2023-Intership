package com.dmm.bootcamp.yatter2023.infra.api.json

import com.squareup.moshi.Json

data class MediaJson(
  @Json(name = "id") val id: String,
  @Json(name = "type") val type: String,
  @Json(name = "url") val url: String,
  @Json(name = "description") val description: String
)
