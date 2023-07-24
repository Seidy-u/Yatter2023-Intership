package com.dmm.bootcamp.yatter2023

import androidx.lifecycle.ViewModel
import com.dmm.bootcamp.yatter2023.domain.service.CheckLoginService

class MainViewModel(
  private val checkLoginService: CheckLoginService,
) : ViewModel()