package com.dmm.bootcamp.yatter2023.infra.domain.converter

import com.dmm.bootcamp.yatter2023.domain.model.Status
import com.dmm.bootcamp.yatter2023.domain.model.StatusId
import com.dmm.bootcamp.yatter2023.infra.api.json.StatusJson

object StatusConverter {
  fun convertToDomainModel(jsonList: List<StatusJson>): List<Status> =
    jsonList.map { convertToDomainModel(it) }

  fun convertToDomainModel(json: StatusJson): Status = Status(
    id = StatusId(json.id),
    account = AccountConverter.convertToDomainModel(json.account),
    content = json.content ?: "",
    attachmentMediaList = MediaConverter.convertToDomainModel(json.attachmentMediaList)
  )
}
