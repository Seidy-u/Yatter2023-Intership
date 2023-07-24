package com.dmm.bootcamp.yatter2023.infra.domain.converter

import com.dmm.bootcamp.yatter2023.domain.model.Account
import com.dmm.bootcamp.yatter2023.domain.model.Me
import com.dmm.bootcamp.yatter2023.infra.domain.model.MeImpl

object MeConverter {
  fun convertToMe(account: Account): Me {
    return MeImpl(
      id = account.id,
      username = account.username,
      displayName = account.displayName,
      note = account.note,
      avatar = account.avatar,
      header = account.header,
      followingCount = account.followingCount,
      followerCount = account.followerCount,
    )
  }
}
