package com.dmm.bootcamp.yatter2023.usecase.impl.post

import android.accounts.AuthenticatorException
import com.dmm.bootcamp.yatter2023.domain.repository.StatusRepository
import com.dmm.bootcamp.yatter2023.usecase.post.PostStatusUseCase
import com.dmm.bootcamp.yatter2023.usecase.post.PostStatusUseCaseResult
import java.io.File

class PostStatusUseCaseImpl(
  private val statusRepository: StatusRepository
) : PostStatusUseCase {
  override suspend fun execute(
    content: String,
    attachmentList: List<File>
  ): PostStatusUseCaseResult {
    if (content == "" && attachmentList.isEmpty()) {
      return PostStatusUseCaseResult.Failure.EmptyContent
    }

    return try {
      statusRepository.create(
        content = content,
        attachmentList = emptyList()
      )

      PostStatusUseCaseResult.Success
    } catch (e: AuthenticatorException) {
      PostStatusUseCaseResult.Failure.NotLoggedIn
    } catch (e: Exception) {
      PostStatusUseCaseResult.Failure.OtherError(e)
    }
  }
}
