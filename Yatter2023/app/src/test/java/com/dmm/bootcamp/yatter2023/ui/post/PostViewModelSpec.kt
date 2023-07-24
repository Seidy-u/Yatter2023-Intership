package com.dmm.bootcamp.yatter2023.ui.post

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dmm.bootcamp.yatter2023.common.MainDispatcherRule
import com.dmm.bootcamp.yatter2023.domain.model.AccountId
import com.dmm.bootcamp.yatter2023.domain.model.Username
import com.dmm.bootcamp.yatter2023.domain.service.GetMeService
import com.dmm.bootcamp.yatter2023.infra.domain.model.MeImpl
import com.dmm.bootcamp.yatter2023.usecase.post.PostStatusUseCase
import com.dmm.bootcamp.yatter2023.usecase.post.PostStatusUseCaseResult
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import java.net.URL

class PostViewModelSpec {
  private val getMeService = mockk<GetMeService>()
  private val postStatusUseCase = mockk<PostStatusUseCase>()
  private val subject = PostViewModel(
    postStatusUseCase,
    getMeService,
  )

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  @get:Rule
  val rule: TestRule = InstantTaskExecutorRule()

  @Test
  fun getMeWhenOnCreate() = runTest {
    val avatarUrl = URL("https://www.dmm.com")
    val me = MeImpl(
      id = AccountId(value = "me account"),
      username = Username(value = ""),
      displayName = null,
      note = null,
      avatar = avatarUrl,
      header = URL("https://www.google.com"),
      followingCount = 0,
      followerCount = 0
    )
    coEvery {
      getMeService.execute()
    } returns me

    subject.onCreate()

    assertThat(subject.uiState.value.bindingModel.avatarUrl).isEqualTo(avatarUrl.toString())
  }


  @Test
  fun changeStatusAndCanPost() = runTest {
    val newStatusText = "new"

    subject.onChangedStatusText(newStatusText)

    assertThat(subject.uiState.value.bindingModel.statusText).isEqualTo(newStatusText)
    assertThat(subject.uiState.value.canPost).isTrue()
  }

  @Test
  fun changeStatusAndCannotPost() = runTest {
    val oldStatusText = "old"
    val newStatusText = ""

    subject.onChangedStatusText(oldStatusText)
    assertThat(subject.uiState.value.bindingModel.statusText).isEqualTo(oldStatusText)
    assertThat(subject.uiState.value.canPost).isTrue()

    subject.onChangedStatusText(newStatusText)

    assertThat(subject.uiState.value.bindingModel.statusText).isEqualTo(newStatusText)
    assertThat(subject.uiState.value.canPost).isFalse()
  }

  @Test
  fun postSuccess() = runTest {
    val status = "status"
    subject.onChangedStatusText(status)

    coEvery {
      postStatusUseCase.execute(any(), any())
    } returns PostStatusUseCaseResult.Success

    subject.onClickPost()

    coVerify {
      postStatusUseCase.execute(status, emptyList())
    }

    assertThat(subject.goBack.value).isNotNull()
  }

  @Test
  fun postFailure() = runTest {
    val status = "status"
    subject.onChangedStatusText(status)

    coEvery {
      postStatusUseCase.execute(any(), any())
    } returns PostStatusUseCaseResult.Failure.OtherError(Exception())

    subject.onClickPost()

    coVerify {
      postStatusUseCase.execute(status, emptyList())
    }

    assertThat(subject.goBack.value).isNull()
  }

  @Test
  fun clickBack() = runTest {
    subject.onClickNavIcon()

    assertThat(subject.goBack.value).isNotNull()
  }
}