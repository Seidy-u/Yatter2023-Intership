package com.dmm.bootcamp.yatter2023.infra.domain.service

import com.dmm.bootcamp.yatter2023.infra.pref.MePreferences
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class CheckLoginServiceImplSpec {
  private val mePreferences = mockk<MePreferences>()
  private val subject = CheckLoginServiceImpl(mePreferences)

  @Test
  fun getTrueWhenSavedUsername() = runTest {
    val username = "username"

    coEvery {
      mePreferences.getUsername()
    } returns username

    val result: Boolean = subject.execute()

    assertThat(result).isTrue()
  }

  @Test
  fun getFalseWhenUnsavedUsername() = runTest {
    val username = ""

    coEvery {
      mePreferences.getUsername()
    } returns username

    val result: Boolean = subject.execute()

    assertThat(result).isFalse()
  }
}