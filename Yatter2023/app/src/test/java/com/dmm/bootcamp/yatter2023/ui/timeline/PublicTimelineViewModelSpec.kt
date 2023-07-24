package com.dmm.bootcamp.yatter2023.ui.timeline

import com.dmm.bootcamp.yatter2023.common.MainDispatcherRule
import com.dmm.bootcamp.yatter2023.domain.model.AccountId
import com.dmm.bootcamp.yatter2023.domain.model.Status
import com.dmm.bootcamp.yatter2023.domain.model.StatusId
import com.dmm.bootcamp.yatter2023.domain.model.Username
import com.dmm.bootcamp.yatter2023.domain.repository.StatusRepository
import com.dmm.bootcamp.yatter2023.infra.domain.model.AccountImpl
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import java.net.URL

class PublicTimelineViewModelSpec {
  private val statusRepository = mockk<StatusRepository>()
  private val subject = PublicTimelineViewModel(statusRepository)

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  @Test
  fun getPublicTimelineFromRepository() = runTest {
    val statusList = listOf(
      Status(
        id = StatusId(value = "id"),
        account = AccountImpl(
          id= AccountId("id"),
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

    val expect = StatusConverter.convertToBindingModel(statusList)

    coEvery {
      statusRepository.findAllPublic()
    } returns statusList

    subject.onResume()

    coVerify {
      statusRepository.findAllPublic()
    }

    assertThat(subject.uiState.value.statusList).isEqualTo(expect)
  }

  @Test
  fun onRefreshPublicTimeline() = runTest {
    val statusList = listOf(
      Status(
        id = StatusId(value = "id"),
        account = AccountImpl(
          id= AccountId("id"),
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

    val expect = StatusConverter.convertToBindingModel(statusList)

    coEvery {
      statusRepository.findAllPublic()
    } returns statusList

    subject.onRefresh()

    coVerify {
      statusRepository.findAllPublic()
    }

    assertThat(subject.uiState.value.statusList).isEqualTo(expect)
  }
}