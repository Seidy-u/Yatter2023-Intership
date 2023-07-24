package com.dmm.bootcamp.yatter2023.infra.domain.service

import com.dmm.bootcamp.yatter2023.domain.model.Password
import com.dmm.bootcamp.yatter2023.domain.model.Username
import com.dmm.bootcamp.yatter2023.infra.pref.MePreferences
import io.mockk.coVerify
import io.mockk.justRun
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class LoginServiceImplSpec {
  private val mePreferences = mockk<MePreferences>()
  private val subject = LoginServiceImpl(mePreferences)

  @Test
  fun saveUsername() = runTest {
    val username = Username("username")
    val password = Password("Password1%")
    justRun {
      mePreferences.putUserName(any())
    }

    subject.execute(username, password)

    coVerify {
      mePreferences.putUserName(username.value)
    }
  }
}