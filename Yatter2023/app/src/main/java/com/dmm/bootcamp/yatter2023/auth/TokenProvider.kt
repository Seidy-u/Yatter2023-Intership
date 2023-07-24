package com.dmm.bootcamp.yatter2023.auth

interface TokenProvider {
  suspend fun provide(): String
}