package com.dmm.bootcamp.yatter2023.infra.domain.repository

import com.dmm.bootcamp.yatter2023.domain.model.Username
import com.dmm.bootcamp.yatter2023.infra.api.YatterApi
import com.dmm.bootcamp.yatter2023.infra.api.json.AccountJson
import com.dmm.bootcamp.yatter2023.infra.domain.converter.AccountConverter
import com.dmm.bootcamp.yatter2023.infra.pref.MePreferences
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class AccountRepositoryImplSpec {
  val yatterApi = mockk<YatterApi>()
  val mePreferences = mockk<MePreferences>()
  val subject = AccountRepositoryImpl(yatterApi, mePreferences)

  @Test
  fun getAccountByUsername() = runTest {
    val username = Username("username")
    val accountJson = AccountJson(
      id = "id",
      username = "username",
      displayName = "display name",
      note = null,
      avatar = "",
      header = "",
      followingCount = 0,
      followersCount = 0,
      createAt = ""
    )

    val expect = AccountConverter.convertToDomainModel(accountJson)

    coEvery {
      yatterApi.getAccountByUsername(any())
    } returns accountJson

    val result = subject.findByUsername(username)

    coVerify {
      yatterApi.getAccountByUsername(username.value)
    }

    assertThat(result).isEqualTo(expect)
  }

  @Test
  fun getMe() = runTest {
    val username = "username"
    val accountJson = AccountJson(
      id = "id",
      username = "username",
      displayName = "display name",
      note = null,
      avatar = "",
      header = "",
      followingCount = 0,
      followersCount = 0,
      createAt = ""
    )

    val expect = AccountConverter.convertToDomainModel(accountJson)

    coEvery {
      yatterApi.getAccountByUsername(any())
    } returns accountJson

    coEvery {
      mePreferences.getUsername()
    } returns username

    val result = subject.findMe()

    coVerify {
      yatterApi.getAccountByUsername(username)
    }

    coVerify {
      mePreferences.getUsername()
    }

    assertThat(result).isEqualTo(expect)
  }
}