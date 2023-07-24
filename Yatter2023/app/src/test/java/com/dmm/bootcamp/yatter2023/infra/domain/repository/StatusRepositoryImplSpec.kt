package com.dmm.bootcamp.yatter2023.infra.domain.repository

import android.accounts.AuthenticatorException
import com.dmm.bootcamp.yatter2023.domain.model.AccountId
import com.dmm.bootcamp.yatter2023.domain.model.Status
import com.dmm.bootcamp.yatter2023.domain.model.StatusId
import com.dmm.bootcamp.yatter2023.domain.model.Username
import com.dmm.bootcamp.yatter2023.infra.api.YatterApi
import com.dmm.bootcamp.yatter2023.infra.api.json.AccountJson
import com.dmm.bootcamp.yatter2023.infra.api.json.PostStatusJson
import com.dmm.bootcamp.yatter2023.infra.api.json.StatusJson
import com.dmm.bootcamp.yatter2023.infra.domain.converter.StatusConverter
import com.dmm.bootcamp.yatter2023.infra.domain.model.AccountImpl
import com.dmm.bootcamp.yatter2023.infra.pref.MePreferences
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyAll
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.net.URL

class StatusRepositoryImplSpec {
  private val yatterApi = mockk<YatterApi>()
  private val mePreferences = mockk<MePreferences>()
  private val subject = StatusRepositoryImpl(yatterApi, mePreferences)

  @Test
  fun getPublicTimelineFromApi() = runTest {
    val jsonList = listOf(
      StatusJson(
        id = "id",
        account = AccountJson(
          id = "id",
          username = "username",
          displayName = "display name",
          note = "note",
          avatar = "https://www.google.com",
          header = "https://www.google.com",
          followingCount = 100,
          followersCount = 200,
          createAt = "2023-06-02T12:44:35.030Z"
        ),
        content = "content",
        createAt = "2023-06-02T12:44:35.030Z",
//        attachmentMediaList = listOf(),
      )
    )

    val expect = listOf(
      Status(
        id = StatusId(value = "id"),
        account = AccountImpl(
          id = AccountId("id"),
          username = Username("username"),
          displayName = "display name",
          note = "note",
          avatar = URL("https://www.google.com"),
          header = URL("https://www.google.com"),
          followingCount = 100,
          followerCount = 200
        ),
        content = "content",
        attachmentMediaList = listOf()
      )
    )

    coEvery {
      yatterApi.getPublicTimeline()
    } returns jsonList

    val result: List<Status> = subject.findAllPublic()

    coVerify {
      yatterApi.getPublicTimeline()
    }

    assertThat(result).isEqualTo(expect)
  }

  @Test
  fun postStatusWhenLoggedIn() = runTest {
    val username = "username"
    val content = "content"

    val statusJson = StatusJson(
      id = "id",
      account = AccountJson(
        id = "id",
        username = username,
        displayName = "",
        note = null,
        avatar = "",
        header = "",
        followingCount = 0,
        followersCount = 0,
        createAt = ""
      ),
      content = content,
      createAt = ""

    )

    coEvery {
      mePreferences.getUsername()
    } returns username

    coEvery {
      yatterApi.postStatus(any(), any())
    } returns statusJson

    val expect = StatusConverter.convertToDomainModel(statusJson)

    val result = subject.create(
      content,
      emptyList()
    )

    assertThat(result).isEqualTo(expect)

    coVerifyAll {
      mePreferences.getUsername()
      yatterApi.postStatus(
        username,
        PostStatusJson(
          status = content,
          mediaIds = emptyList()
        )
      )
    }
  }

  @Test
  fun postStatusWhenNotLoggedIn() = runTest {
    val username = null
    val content = "content"

    coEvery {
      mePreferences.getUsername()
    } returns username


    var error: Throwable? = null
    var result: Status? = null

    try {
      result = subject.create(
        content,
        emptyList()
      )
    } catch (e: Exception) {
      error = e
    }


    assertThat(result).isNull()
    assertThat(error).isInstanceOf(AuthenticatorException::class.java)

    coVerify {
      mePreferences.getUsername()
    }
  }
}