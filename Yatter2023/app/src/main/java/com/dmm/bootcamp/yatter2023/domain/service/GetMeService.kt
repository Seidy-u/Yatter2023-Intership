package com.dmm.bootcamp.yatter2023.domain.service

import com.dmm.bootcamp.yatter2023.domain.model.Me

interface GetMeService {
  suspend fun execute(): Me?
}
