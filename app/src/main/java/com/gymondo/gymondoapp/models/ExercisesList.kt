package com.gymondo.gymondoapp.models


data class ExercisesList(
    val count: Int?,
    val next: String?,
    val previous: String?,
    val results: List<Exercise>
)