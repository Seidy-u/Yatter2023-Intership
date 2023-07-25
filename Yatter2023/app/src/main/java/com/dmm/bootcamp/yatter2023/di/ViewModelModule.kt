package com.dmm.bootcamp.yatter2023.di

import com.dmm.bootcamp.yatter2023.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val viewModelModule = module {
  viewModel { MainViewModel(get()) }
//  viewModel { PublicTimelineViewModel(get()) }
//  viewModel { PostViewModel(get(), get()) }
//  viewModel { RegisterAccountViewModel(get()) }
//  viewModel { LoginViewModel(get()) }
}
