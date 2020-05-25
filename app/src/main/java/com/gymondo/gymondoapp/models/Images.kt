package com.gymondo.gymondoapp.models


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Images(
    val exercise: Int,
    val id: Int,
    val image: String
):Serializable
