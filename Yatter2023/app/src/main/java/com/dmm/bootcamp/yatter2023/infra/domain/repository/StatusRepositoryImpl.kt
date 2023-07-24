package com.dmm.bootcamp.yatter2023.infra.domain.repository

import com.dmm.bootcamp.yatter2023.auth.TokenProvider
import com.dmm.bootcamp.yatter2023.domain.model.Me
import com.dmm.bootcamp.yatter2023.domain.model.Status
import com.dmm.bootcamp.yatter2023.domain.model.StatusId
import com.dmm.bootcamp.yatter2023.domain.repository.StatusRepository
import com.dmm.bootcamp.yatter2023.infra.api.YatterApi
import com.dmm.bootcamp.yatter2023.infra.api.json.PostStatusJson
import com.dmm.bootcamp.yatter2023.infra.domain.converter.StatusConverter
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import java.io.File

class StatusRepositoryImpl(
  private val yatterApi: YatterApi,
  private val tokenProvider: TokenProvider,
) : StatusRepository {
  override suspend fun findById(id: StatusId): Status? = withContext(IO) {
    val statusList = yatterApi.getPublicTimeline()
    StatusConverter.convertToDomainModel(statusList).firstOrNull { it.id == id }
  }

  override suspend fun findAllPublic(): List<Status> = withContext(IO) {
    val statusList = yatterApi.getPublicTimeline()
    StatusConverter.convertToDomainModel(statusList)
  }

  override suspend fun findAllHome(): List<Status> = withContext(IO) {
    val statusList = yatterApi.getHomeTimeline(tokenProvider.provide())
    StatusConverter.convertToDomainModel(statusList)
  }

  override suspend fun findAllFollowings(me: Me): List<Status> {
    TODO("Not yet implemented")
  }

  override suspend fun create(
    content: String,
    attachmentList: List<File>
  ): Status = withContext(IO) {
    val statusJson = yatterApi.postStatus(
      tokenProvider.provide(),
      PostStatusJson(
        content,
        listOf()
      )
    )
    StatusConverter.convertToDomainModel(statusJson)
  }

  override suspend fun delete(status: Status) {
    TODO("Not yet implemented")
  }
}
