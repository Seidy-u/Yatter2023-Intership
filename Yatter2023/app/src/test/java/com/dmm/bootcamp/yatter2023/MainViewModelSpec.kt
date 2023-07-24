package com.dmm.bootcamp.yatter2023

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dmm.bootcamp.yatter2023.common.MainDispatcherRule
import com.dmm.bootcamp.yatter2023.domain.service.CheckLoginService
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class MainViewModelSpec {
  private val checkLoginService = mockk<CheckLoginService>()
  private val subject = MainViewModel(checkLoginService)

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  @get:Rule
  val rule: TestRule = InstantTaskExecutorRule()

  @Test
  fun navigateToPublicTimelineWhenLoggedIn() = runTest {
    coEvery {
      checkLoginService.execute()
    } returns true

    subject.onCreate()

    assertThat(subject.navigateToPublicTimeline.value).isNotNull()
    assertThat(subject.navigateToLogin.value).isNull()
  }

  @Test
  fun navigateToLoginWhenNotLoggedIn() = runTest {
    coEvery {
      checkLoginService.execute()
    } returns false

    subject.onCreate()

    assertThat(subject.navigateToLogin.value).isNotNull()
    assertThat(subject.navigateToPublicTimeline.value).isNull()
  }
}