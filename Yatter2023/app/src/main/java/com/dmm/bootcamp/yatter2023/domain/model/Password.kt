package com.dmm.bootcamp.yatter2023.domain.model

data class Password(
  val value: String
) {
  companion object {
    private const val SYMBOLS = "/*!@#$%^&*()\"{}_[]|\\?/<>,."
    private const val MIN_LENGTH = 8
  }

  fun validate(): Boolean = value.isNotEmpty() &&
      hasUpperCase() &&
      hasLowerCase() &&
      hasSymbol() &&
      hasMinLength()

  private fun hasUpperCase(): Boolean = value.toCharArray().any { it.isUpperCase() }

  private fun hasLowerCase(): Boolean = value.toCharArray().any { it.isLowerCase() }

  private fun hasSymbol(): Boolean = value.toCharArray().any { SYMBOLS.contains(it) }

  private fun hasMinLength(): Boolean = value.length >= MIN_LENGTH
}
