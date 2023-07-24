package com.dmm.bootcamp.yatter2023.domain.model

import com.dmm.bootcamp.yatter2023.common.ddd.Entity

class Status(
  id: StatusId,
  val account: Account,
  val content: String,
  val attachmentMediaList: List<Media>
) : Entity<StatusId>(id)
