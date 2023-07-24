package com.dmm.bootcamp.yatter2023.usecase.post

sealed interface PostStatusUseCaseResult {
  object Success : PostStatusUseCaseResult
  sealed interface Failure : PostStatusUseCaseResult {
    object EmptyContent : Failure
    object NotLoggedIn : Failure
    data class OtherError(val throwable: Throwable) : Failure
  }
}
