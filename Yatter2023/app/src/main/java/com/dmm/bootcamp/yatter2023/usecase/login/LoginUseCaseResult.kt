package com.dmm.bootcamp.yatter2023.usecase.login

sealed interface LoginUseCaseResult {
  object Success : LoginUseCaseResult
  sealed interface Failure : LoginUseCaseResult {
    object EmptyUsername : Failure
    object EmptyPassword : Failure
    object InvalidPassword : Failure
    data class OtherError(val throwable: Throwable) : Failure
  }
}
