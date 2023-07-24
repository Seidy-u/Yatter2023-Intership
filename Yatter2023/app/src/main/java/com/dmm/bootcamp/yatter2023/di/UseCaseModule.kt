package com.dmm.bootcamp.yatter2023.di

import com.dmm.bootcamp.yatter2023.usecase.impl.login.LoginUseCaseImpl
import com.dmm.bootcamp.yatter2023.usecase.impl.post.PostStatusUseCaseImpl
import com.dmm.bootcamp.yatter2023.usecase.impl.register.RegisterAccountUseCaseImpl
import com.dmm.bootcamp.yatter2023.usecase.login.LoginUseCase
import com.dmm.bootcamp.yatter2023.usecase.post.PostStatusUseCase
import com.dmm.bootcamp.yatter2023.usecase.register.RegisterAccountUseCase
import org.koin.dsl.module

internal val useCaseModule = module {
  factory<PostStatusUseCase> { PostStatusUseCaseImpl(get()) }
  factory<RegisterAccountUseCase> { RegisterAccountUseCaseImpl(get(), get()) }
  factory<LoginUseCase> { LoginUseCaseImpl(get()) }
}
