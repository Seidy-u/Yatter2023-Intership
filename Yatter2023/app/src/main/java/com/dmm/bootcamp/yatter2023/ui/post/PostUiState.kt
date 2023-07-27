package com.dmm.bootcamp.yatter2023.ui.post

data class PostUiState(
    val bindingModel: PostBindingModel,
    val isLoading: Boolean,
) {
    companion object {
        fun empty(): PostUiState = PostUiState(
            bindingModel = PostBindingModel(
                avatarUrl = null,
                statusText = ""
            ),
            isLoading = false,
        )
    }

    val canPost: Boolean
        get() = bindingModel.statusText.isNotBlank()
}