package com.dmm.bootcamp.yatter2023.infra.domain.service

import com.dmm.bootcamp.yatter2023.domain.model.Password
import com.dmm.bootcamp.yatter2023.domain.model.Username
import com.dmm.bootcamp.yatter2023.domain.service.LoginService
import com.dmm.bootcamp.yatter2023.infra.pref.MePreferences

class LoginServiceImpl(
  private val mePreferences: MePreferences
) : LoginService {

  override suspend fun execute(
    username: Username,
    password: Password
  ) {
    mePreferences.putUserName(username.value)
  }
}
