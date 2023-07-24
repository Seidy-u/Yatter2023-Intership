package com.dmm.bootcamp.yatter2023.common.error

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ErrorHandlerImpl : ErrorHandler {
  private val _errorMassage: MutableStateFlow<String?> = MutableStateFlow(null)
  override val errorMessage: StateFlow<String?> = _errorMassage

  override fun handle(errorMessage: String) {
    _errorMassage.value = errorMessage
  }
}