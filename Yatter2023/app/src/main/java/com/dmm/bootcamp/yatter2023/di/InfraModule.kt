package com.dmm.bootcamp.yatter2023.di

import com.dmm.bootcamp.yatter2023.auth.TokenProvider
import com.dmm.bootcamp.yatter2023.auth.TokenProviderImpl
import com.dmm.bootcamp.yatter2023.infra.api.YatterApiFactory
import com.dmm.bootcamp.yatter2023.infra.pref.MePreferences
import org.koin.dsl.module

internal val infraModule = module {
  single { MePreferences(get()) }
  single { YatterApiFactory().create() }

  factory<TokenProvider> { TokenProviderImpl(get()) }
}
