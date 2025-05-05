package com.bleepingdragon.twiftly.model

import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.util.UUID

@Serializable
data class PlantCareItem (
    var name: String = "",
    var notes: String = "",
    var wateringFrequencyInDays: Int = 0,
    var startingWateringDate: String = LocalDateTime.now().toString(),
    var uuid: String = UUID.randomUUID().toString()
)