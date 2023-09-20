package com.slicelife.tastyrecipes

import kotlinx.serialization.Serializable

@Serializable
data class Item(
    val name: String?,
    val cook_time_minutes: Double?,
    val total_time_minutes: Double?,
    val canonical_id: String?,
    val video_url: String?,
    val original_video_url: String?,
    val thumbnail_url: String?,
    val description: String?,
    val instructions: Array<Instruction>,
)