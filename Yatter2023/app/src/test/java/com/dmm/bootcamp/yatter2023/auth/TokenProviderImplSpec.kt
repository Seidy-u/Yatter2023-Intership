package com.dmm.bootcamp.yatter2023.auth

import android.accounts.AuthenticatorException
import com.dmm.bootcamp.yatter2023.domain.model.AccountId
import com.dmm.bootcamp.yatter2023.domain.model.Username
import com.dmm.bootcamp.yatter2023.domain.service.GetMeService
import com.dmm.bootcamp.yatter2023.infra.domain.model.MeImpl
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.net.URL

class TokenProviderImplSpec {
  private val getMeService = mockk<GetMeService>()
  private val subject = TokenProviderImpl(getMeService)

  @Test
  fun getTokenSuccess() = runTest {
    val username = "username"
    val me = MeImpl(
      id = AccountId(value = "id"),
      username = Username(value = username),
      displayName = null,
      note = null,
      avatar = URL("https:www.google.com"),
      header = URL("https:www.google.com"),
      followingCount = 0,
      followerCount = 0
    )

    val expect = "username $username"

    coEvery {
      getMeService.execute()
    } returns me

    val result = subject.provide()

    coVerify {
      getMeService.execute()
    }

    assertThat(result).isEqualTo(expect)
  }

  @Test
  fun getTokenFailure() = runTest {
    coEvery {
      getMeService.execute()
    } returns null

    var error: Throwable? = null
    var result: String? = null

    try {
      result = subject.provide()
    } catch (e: Exception) {
      error = e
    }

    coVerify {
      getMeService.execute()
    }

    assertThat(result).isNull()
    assertThat(error).isInstanceOf(AuthenticatorException::class.java)
  }
}