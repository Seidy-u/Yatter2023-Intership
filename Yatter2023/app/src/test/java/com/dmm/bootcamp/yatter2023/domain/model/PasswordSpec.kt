package com.dmm.bootcamp.yatter2023.domain.model

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Test

class PasswordSpec {
  @Test
  fun checkValidate() = runTest {
    val testCase = listOf(
      "abc" to false,
      "abcdefghi" to false,
      "Abcdefghi" to false,
      "Abcdefghi10" to false,
      "Abcdefghi10%" to true,
    )

    testCase.forEach {
      assertThat(Password(it.first).validate()).isEqualTo(it.second)
    }
  }
}
