package com.dmm.bootcamp.yatter2023.infra.domain.model

import com.dmm.bootcamp.yatter2023.domain.model.Account
import com.dmm.bootcamp.yatter2023.domain.model.AccountId
import com.dmm.bootcamp.yatter2023.domain.model.Username
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

class AccountImpl(
  id: AccountId,
  username: Username,
  displayName: String?,
  note: String?,
  avatar: URL,
  header: URL,
  followingCount: Int,
  followerCount: Int,
) : Account(
  id,
  username,
  displayName,
  note,
  avatar,
  header,
  followingCount,
  followerCount,
) {
  override suspend fun followings(): List<Account> = withContext(Dispatchers.IO) {
    TODO("Not yet implemented")
  }

  override suspend fun followers(): List<Account> {
    TODO("Not yet implemented")
  }
}
