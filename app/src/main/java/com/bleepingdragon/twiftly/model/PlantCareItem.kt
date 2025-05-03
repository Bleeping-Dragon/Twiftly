package com.bleepingdragon.twiftly.model

import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.util.UUID

@Serializable
data class PlantCareItem (
    var name: String = "",
    var notes: String = "",
    var wateringFrequencyInDays: Int = 0,
    var lastWateringDate: String = LocalDateTime.now().toString(),
    var uuid: String = UUID.randomUUID().toString()
) {
    public fun getWateringSchedule(daysToGet: Int): List<LocalDateTime> {

        val startDate = LocalDateTime.parse(this.lastWateringDate)

        return (1..daysToGet).map { i ->
            startDate.plusDays((i * wateringFrequencyInDays).toLong())
        }
    }
}

