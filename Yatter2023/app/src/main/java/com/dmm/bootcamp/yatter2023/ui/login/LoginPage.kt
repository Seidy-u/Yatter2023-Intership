package com.dmm.bootcamp.yatter2023.ui.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun LoginPage(viewModel: LoginViewModel) {
    val uiState: LoginUiState by viewModel.uiState.collectAsStateWithLifecycle()

    LoginTemplate(
        userName = uiState.loginBindingModel.username,
        onChangedUserName = viewModel::onChangedUsername,
        password = uiState.loginBindingModel.password,
        onChangedPassword = viewModel::onChangedPassword,
        isEnableLogin = uiState.isEnableLogin,
        isLoading = uiState.isLoading,
        onClickLogin = viewModel::onClickLogin,
        onClickRegister = viewModel::onClickRegister,
    )
}