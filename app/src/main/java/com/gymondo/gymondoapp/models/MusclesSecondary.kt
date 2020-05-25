package com.gymondo.gymondoapp.models


import com.google.gson.annotations.SerializedName

data class MusclesSecondary(
    val id: Int?,
    @SerializedName("is_front")
    val isFront: Boolean?,
    val name: String?
)