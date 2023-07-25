package com.dmm.bootcamp.yatter2023.ui.timeline.bindingmodel.converter


import com.dmm.bootcamp.yatter2023.domain.model.Status
import com.dmm.bootcamp.yatter2023.ui.timeline.bindingmodel.StatusBindingModel

object StatusConverter {
    fun convertToBindingModel(statusList: List<Status>): List<StatusBindingModel> =
        statusList.map { convertToBindingModel(it) }

    fun convertToBindingModel(status: Status): StatusBindingModel =
        StatusBindingModel(
            id = status.id.value,
            displayName = status.account.displayName ?: "",
            username = status.account.username.value,
            avatar = status.account.avatar.toString(),
            content = status.content,
            attachmentMediaList = MediaConverter.convertToDomainModel(status.attachmentMediaList)
        )
}