package com.gymondo.gymondoapp.models


import com.google.gson.annotations.SerializedName

data class ExerciseInfo(
    val category: Category,
    val description: String,
    val equipment: List<Equipment>,
    val muscles: List<Muscle>,
    @SerializedName("muscles_secondary")
    val musclesSecondary: List<MusclesSecondary>,
    val name: String
)