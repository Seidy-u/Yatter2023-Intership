package com.dmm.bootcamp.yatter2023.ui.timeline.bindingmodel

data class StatusBindingModel(
    val id: String,
    val displayName: String,
    val username: String,
    val avatar: String?,
    val content: String,
    val attachmentMediaList: List<MediaBindingModel>
)