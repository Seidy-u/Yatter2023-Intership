package com.dmm.bootcamp.yatter2023.ui.timeline.bindingmodel.converter

import com.dmm.bootcamp.yatter2023.domain.model.Media
import com.dmm.bootcamp.yatter2023.ui.timeline.bindingmodel.MediaBindingModel

object MediaConverter {
    fun convertToDomainModel(mediaList: List<Media>): List<MediaBindingModel> =
        mediaList.map { convertToDomainModel(it) }

    fun convertToDomainModel(media: Media): MediaBindingModel =
        MediaBindingModel(
            id = media.id.value,
            type = media.type,
            url = media.url,
            description = media.description
        )
}