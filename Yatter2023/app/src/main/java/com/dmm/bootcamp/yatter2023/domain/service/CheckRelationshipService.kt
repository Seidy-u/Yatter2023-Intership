package com.dmm.bootcamp.yatter2023.domain.service

import com.dmm.bootcamp.yatter2023.domain.model.Me
import com.dmm.bootcamp.yatter2023.domain.model.Relationship
import com.dmm.bootcamp.yatter2023.domain.model.Username

interface CheckRelationshipService {
  suspend fun execute(
    me: Me,
    usernameList: List<Username>
  ): List<Relationship>
}
