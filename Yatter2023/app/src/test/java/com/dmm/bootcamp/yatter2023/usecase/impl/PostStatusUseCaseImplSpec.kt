package com.dmm.bootcamp.yatter2023.usecase.impl

import android.accounts.AuthenticatorException
import com.dmm.bootcamp.yatter2023.domain.model.AccountId
import com.dmm.bootcamp.yatter2023.domain.model.Status
import com.dmm.bootcamp.yatter2023.domain.model.StatusId
import com.dmm.bootcamp.yatter2023.domain.model.Username
import com.dmm.bootcamp.yatter2023.domain.repository.StatusRepository
import com.dmm.bootcamp.yatter2023.infra.domain.model.MeImpl
import com.dmm.bootcamp.yatter2023.usecase.impl.post.PostStatusUseCaseImpl
import com.dmm.bootcamp.yatter2023.usecase.post.PostStatusUseCaseResult
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.net.URL

class PostStatusUseCaseImplSpec {
  private val statusRepository = mockk<StatusRepository>()
  private val subject = PostStatusUseCaseImpl(statusRepository)

  @Test
  fun postStatusWithSuccess() = runTest {
    val content = "content"

    val status = Status(
      id = StatusId(value = ""),
      account = MeImpl(
        id = AccountId(value = ""),
        username = Username(value = ""),
        displayName = null,
        note = null,
        avatar = URL("https://www.google.com"),
        header = URL("https://www.google.com"),
        followingCount = 0,
        followerCount = 0
      ),
      content = content,
      attachmentMediaList = listOf(),
    )

    coEvery {
      statusRepository.create(
        any(),
        any(),
      )
    } returns status

    val result = subject.execute(
      content,
      emptyList(),
    )

    coVerify {
      statusRepository.create(
        content,
        emptyList(),
      )
    }

    assertThat(result).isEqualTo(PostStatusUseCaseResult.Success)
  }

  @Test
  fun postStatusWithEmptyContent() = runTest {
    val content = ""

    val result = subject.execute(
      content,
      emptyList(),
    )

    coVerify(inverse = true) {
      statusRepository.create(
        any(),
        any(),
      )
    }

    assertThat(result).isEqualTo(PostStatusUseCaseResult.Failure.EmptyContent)
  }

  @Test
  fun postStatusWithNotLoggedIn() = runTest {
    val content = "content"

    coEvery {
      statusRepository.create(
        any(),
        any(),
      )
    } throws AuthenticatorException()

    val result = subject.execute(
      content,
      emptyList(),
    )


    coVerify {
      statusRepository.create(
        any(),
        any(),
      )
    }

    assertThat(result).isEqualTo(PostStatusUseCaseResult.Failure.NotLoggedIn)
  }

  @Test
  fun postStatusWithOtherError() = runTest {
    val content = "content"
    val exception = Exception()

    coEvery {
      statusRepository.create(
        any(),
        any(),
      )
    } throws exception

    val result = subject.execute(
      content,
      emptyList(),
    )


    coVerify {
      statusRepository.create(
        any(),
        any(),
      )
    }

    assertThat(result).isEqualTo(PostStatusUseCaseResult.Failure.OtherError(exception))
  }
}