package com.patriciabajureanu.metgallery.ui.routes

import kotlinx.serialization.Serializable

@Serializable
data class DetailsRoute(
    val objectId: Int,
    val title: String
)
