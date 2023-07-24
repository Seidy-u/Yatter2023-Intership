package com.dmm.bootcamp.yatter2023.util

import android.util.Log
import androidx.annotation.MainThread
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Observerに一度のみ通知するLiveData
 */
class SingleLiveEvent<T> : MutableLiveData<T>() {

  private val pending = AtomicBoolean(false)

  @MainThread
  override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
    if (hasObservers()) {
      Log.w(
        "SingleLiveEvent",
        "Multiple observers registered but only one will be notified of changes.",
      )
    }

    super.observe(
      owner,
    ) {
      // 新たに値がsetValueされているとき
      if (pending.compareAndSet(true, false)) {
        observer.onChanged(it)
      }
    }
  }

  @VisibleForTesting
  override fun observeForever(observer: Observer<in T>) {
    super.observeForever(observer)
  }

  @MainThread
  override fun setValue(value: T?) {
    pending.set(true)
    super.setValue(value)
  }

  override fun postValue(value: T) {
    pending.set(true)
    super.postValue(value)
  }
}
