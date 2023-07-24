package com.dmm.bootcamp.yatter2023.domain.model

import com.dmm.bootcamp.yatter2023.common.ddd.Entity

class Media(
  id: MediaId,
  val type: String,
  val url: String,
  val description: String,
) : Entity<MediaId>(id)
