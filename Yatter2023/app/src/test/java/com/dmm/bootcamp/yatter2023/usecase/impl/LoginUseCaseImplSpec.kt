package com.dmm.bootcamp.yatter2023.usecase.impl

import com.dmm.bootcamp.yatter2023.domain.model.Password
import com.dmm.bootcamp.yatter2023.domain.model.Username
import com.dmm.bootcamp.yatter2023.domain.service.LoginService
import com.dmm.bootcamp.yatter2023.usecase.impl.login.LoginUseCaseImpl
import com.dmm.bootcamp.yatter2023.usecase.login.LoginUseCaseResult
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class LoginUseCaseImplSpec {
  private val loginService = mockk<LoginService>()
  private val subject = LoginUseCaseImpl(loginService)

  @Test
  fun loginSuccess() = runTest {
    val username = Username("username")
    val password = Password("Password1%")

    coJustRun {
      loginService.execute(any(), any())
    }

    val result = subject.execute(username, password)

    coVerify {
      loginService.execute(username, password)
    }

    assertThat(result).isEqualTo(LoginUseCaseResult.Success)
  }

  @Test
  fun loginFailureUsernameEmpty() = runTest {
    val username = Username("")
    val password = Password("Password1%")

    coJustRun {
      loginService.execute(any(), any())
    }

    val result = subject.execute(username, password)

    coVerify(inverse = true) {
      loginService.execute(any(), any())
    }

    assertThat(result).isEqualTo(LoginUseCaseResult.Failure.EmptyUsername)
  }

  @Test
  fun loginFailurePasswordEmpty() = runTest {
    val username = Username("username")
    val password = Password("")

    coJustRun {
      loginService.execute(any(), any())
    }

    val result = subject.execute(username, password)

    coVerify(inverse = true) {
      loginService.execute(any(), any())
    }

    assertThat(result).isEqualTo(LoginUseCaseResult.Failure.EmptyPassword)
  }

  @Test
  fun loginFailurePasswordInvalid() = runTest {
    val username = Username("username")
    val password = Password("password")

    coJustRun {
      loginService.execute(any(), any())
    }

    val result = subject.execute(username, password)

    coVerify(inverse = true) {
      loginService.execute(any(), any())
    }

    assertThat(result).isEqualTo(LoginUseCaseResult.Failure.InvalidPassword)
  }

  @Test
  fun loginFailurePasswordOther() = runTest {
    val username = Username("username")
    val password = Password("Password1%")
    val error = Exception()

    coEvery {
      loginService.execute(any(), any())
    } throws error

    val result = subject.execute(username, password)

    coVerify {
      loginService.execute(any(), any())
    }

    assertThat(result).isEqualTo(LoginUseCaseResult.Failure.OtherError(error))
  }
}