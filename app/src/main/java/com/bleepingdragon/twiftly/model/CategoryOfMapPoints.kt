package com.bleepingdragon.twiftly.model

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class CategoryOfMapPoints(
    var name: String = "",
    var listOfMapPoints: MutableList<MapPoint> = mutableListOf(),
    var uuid: String = UUID.randomUUID().toString()
)
