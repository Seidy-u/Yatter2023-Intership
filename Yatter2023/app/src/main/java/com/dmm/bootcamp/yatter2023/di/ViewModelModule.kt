package com.dmm.bootcamp.yatter2023.di

import com.dmm.bootcamp.yatter2023.MainViewModel
import com.dmm.bootcamp.yatter2023.ui.login.LoginViewModel
import com.dmm.bootcamp.yatter2023.ui.post.PostViewModel
import com.dmm.bootcamp.yatter2023.ui.timeline.PublicTimelineViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val viewModelModule = module {
  viewModel { MainViewModel(get()) }
  viewModel { PublicTimelineViewModel(get()) }
  viewModel { PostViewModel(get(), get()) }
//  viewModel { RegisterAccountViewModel(get()) }
  viewModel { LoginViewModel(get()) }
}
