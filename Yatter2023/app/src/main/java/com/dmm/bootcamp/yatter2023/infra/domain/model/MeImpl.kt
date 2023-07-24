package com.dmm.bootcamp.yatter2023.infra.domain.model

import com.dmm.bootcamp.yatter2023.domain.model.Account
import com.dmm.bootcamp.yatter2023.domain.model.AccountId
import com.dmm.bootcamp.yatter2023.domain.model.Me
import com.dmm.bootcamp.yatter2023.domain.model.Username
import java.net.URL

class MeImpl(
  id: AccountId,
  username: Username,
  displayName: String?,
  note: String?,
  avatar: URL,
  header: URL,
  followingCount: Int,
  followerCount: Int,
) : Me(
  id = id,
  username = username,
  displayName = displayName,
  note = note,
  avatar = avatar,
  header = header,
  followingCount = followingCount,
  followerCount = followerCount,
) {
  override suspend fun follow(username: Username) {
    TODO("Not yet implemented")
  }

  override suspend fun unfollow(username: Username) {
    TODO("Not yet implemented")
  }

  override suspend fun followings(): List<Account> {
    TODO("Not yet implemented")
  }

  override suspend fun followers(): List<Account> {
    TODO("Not yet implemented")
  }
}
