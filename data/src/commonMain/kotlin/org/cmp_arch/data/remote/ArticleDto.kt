package org.cmp_arch.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ArticleDto(
    @SerialName("id") val id: String,
    @SerialName("title") val title: String,
    @SerialName("subtitle") val subtitle: String,
    @SerialName("image_url") val imageUrl: String,
    @SerialName("published_at") val publishedAtEpochMillis: Long,
)
