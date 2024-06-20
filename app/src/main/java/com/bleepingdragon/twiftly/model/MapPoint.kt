package com.bleepingdragon.twiftly.model

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
public data class MapPoint(
    var name: String = "",
    var latitude: Float? = null,
    var longitude: Float? = null,
    var uuid: String = UUID.randomUUID().toString()
)