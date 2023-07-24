package com.dmm.bootcamp.yatter2023

import android.app.Application
import com.dmm.bootcamp.yatter2023.di.domainImplModule
import com.dmm.bootcamp.yatter2023.di.infraModule
import com.dmm.bootcamp.yatter2023.di.useCaseModule
import com.dmm.bootcamp.yatter2023.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class YatterApplication : Application() {
  override fun onCreate() {
    super.onCreate()

    startKoin {
      androidLogger()
      // Reference Android context
      androidContext(this@YatterApplication)
      modules(
        modules = listOf(
          domainImplModule,
          infraModule,
          useCaseModule,
          viewModelModule,
        )
      )
    }
  }
}