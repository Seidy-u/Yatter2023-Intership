package com.dmm.bootcamp.yatter2023.domain.service

interface CheckLoginService {
  suspend fun execute(): Boolean
}
