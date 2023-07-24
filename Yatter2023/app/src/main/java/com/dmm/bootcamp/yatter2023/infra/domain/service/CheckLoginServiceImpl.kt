package com.dmm.bootcamp.yatter2023.infra.domain.service

import com.dmm.bootcamp.yatter2023.domain.service.CheckLoginService
import com.dmm.bootcamp.yatter2023.infra.pref.MePreferences

class CheckLoginServiceImpl(
  private val mePreferences: MePreferences
) : CheckLoginService {
  override suspend fun execute(): Boolean {
    return mePreferences.getUsername() != ""
  }
}
