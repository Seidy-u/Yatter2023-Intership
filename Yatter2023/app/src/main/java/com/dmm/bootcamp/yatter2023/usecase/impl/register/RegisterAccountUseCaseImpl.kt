package com.dmm.bootcamp.yatter2023.usecase.impl.register

import com.dmm.bootcamp.yatter2023.domain.model.Password
import com.dmm.bootcamp.yatter2023.domain.model.Username
import com.dmm.bootcamp.yatter2023.domain.repository.AccountRepository
import com.dmm.bootcamp.yatter2023.infra.pref.MePreferences
import com.dmm.bootcamp.yatter2023.usecase.register.RegisterAccountUseCase
import com.dmm.bootcamp.yatter2023.usecase.register.RegisterAccountUseCaseResult

class RegisterAccountUseCaseImpl(
  private val accountRepository: AccountRepository,
  private val mePreferences: MePreferences
) : RegisterAccountUseCase {
  override suspend fun execute(
    username: String,
    password: String
  ): RegisterAccountUseCaseResult {
    if (username == "") {
      return RegisterAccountUseCaseResult.Failure.EmptyUsername
    }
    if (password == "") {
      return RegisterAccountUseCaseResult.Failure.EmptyPassword
    }
    val newPassword = Password(password)
    if (!newPassword.validate()) {
      return RegisterAccountUseCaseResult.Failure.InvalidPassword
    }

    val me = accountRepository.create(
      Username(username),
      newPassword
    )
    mePreferences.putUserName(me.username.value)
    return RegisterAccountUseCaseResult.Success
  }
}
