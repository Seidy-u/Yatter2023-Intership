package com.dmm.bootcamp.yatter2023.infra.domain.service

import com.dmm.bootcamp.yatter2023.domain.model.AccountId
import com.dmm.bootcamp.yatter2023.domain.model.Username
import com.dmm.bootcamp.yatter2023.domain.repository.AccountRepository
import com.dmm.bootcamp.yatter2023.infra.domain.converter.MeConverter
import com.dmm.bootcamp.yatter2023.infra.domain.model.AccountImpl
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.net.URL

class GetMeServiceImplSpec {
  private val accountRepository = mockk<AccountRepository>()
  private val subject = GetMeServiceImpl(accountRepository)

  @Test
  fun getMe() {
    val account = AccountImpl(
      id = AccountId(value = ""),
      username = Username(value = ""),
      displayName = null,
      note = null,
      avatar = URL("https://www.google.com"),
      header = URL("https://www.google.com"),
      followingCount = 0,
      followerCount = 0
    )
    val expect = MeConverter.convertToMe(account)

    coEvery { accountRepository.findMe() } returns MeConverter.convertToMe(account)

    val result = runBlocking { subject.execute() }

    assertThat(result).isEqualTo(expect)
  }
}