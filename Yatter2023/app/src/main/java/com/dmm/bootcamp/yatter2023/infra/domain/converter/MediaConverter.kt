package com.dmm.bootcamp.yatter2023.infra.domain.converter

import com.dmm.bootcamp.yatter2023.domain.model.Media
import com.dmm.bootcamp.yatter2023.domain.model.MediaId
import com.dmm.bootcamp.yatter2023.infra.api.json.MediaJson

object MediaConverter {
  fun convertToDomainModel(jsonList: List<MediaJson>): List<Media> =
    jsonList.map { convertToDomainModel(it) }

  private fun convertToDomainModel(json: MediaJson): Media = Media(
    id = MediaId(value = json.id),
    type = json.type,
    url = json.url,
    description = json.description,
  )
}