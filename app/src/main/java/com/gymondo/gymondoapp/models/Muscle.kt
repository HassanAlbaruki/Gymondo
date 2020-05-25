package com.gymondo.gymondoapp.models


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Muscle(
    val id: Int,
    @SerializedName("is_front")
    val isFront: Boolean,
    val name: String
): Serializable