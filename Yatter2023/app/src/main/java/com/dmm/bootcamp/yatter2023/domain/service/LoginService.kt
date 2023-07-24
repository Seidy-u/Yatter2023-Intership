package com.dmm.bootcamp.yatter2023.domain.service

import com.dmm.bootcamp.yatter2023.domain.model.Password
import com.dmm.bootcamp.yatter2023.domain.model.Username

interface LoginService {
  suspend fun execute(username: Username, password: Password)
}