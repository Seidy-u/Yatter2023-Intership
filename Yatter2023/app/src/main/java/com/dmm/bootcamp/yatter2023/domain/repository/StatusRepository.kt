package com.dmm.bootcamp.yatter2023.domain.repository

import com.dmm.bootcamp.yatter2023.domain.model.Me
import com.dmm.bootcamp.yatter2023.domain.model.Status
import com.dmm.bootcamp.yatter2023.domain.model.StatusId
import java.io.File

interface StatusRepository {
  suspend fun findById(id: StatusId): Status?

  suspend fun findAllPublic(): List<Status>

  suspend fun findAllHome(): List<Status>

  suspend fun findAllFollowings(
    me: Me
  ): List<Status>

  suspend fun create(
    content: String,
    attachmentList: List<File>
  ): Status

  suspend fun delete(
    status: Status
  )
}
