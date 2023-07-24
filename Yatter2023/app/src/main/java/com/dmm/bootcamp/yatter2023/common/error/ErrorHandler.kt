package com.dmm.bootcamp.yatter2023.common.error

import kotlinx.coroutines.flow.StateFlow

interface ErrorHandler {
  val errorMessage: StateFlow<String?>

  fun handle(errorMessage: String)
}