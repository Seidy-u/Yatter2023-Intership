package com.dmm.bootcamp.yatter2023.ui.post

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun PostPage(viewModel: PostViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    PostTemplate(
        postBindingModel = uiState.bindingModel,
        isLoading = uiState.isLoading,
        canPost = uiState.canPost,
        onStatusTextChanged = viewModel::onChangedStatusText,
        onClickPost = viewModel::onClickPost,
        onClickNavIcon = viewModel::onClickNavIcon,
    )
}