package com.dmm.bootcamp.yatter2023.usecase.register

interface RegisterAccountUseCase {
  suspend fun execute(
    username: String,
    password: String
  ): RegisterAccountUseCaseResult
}
