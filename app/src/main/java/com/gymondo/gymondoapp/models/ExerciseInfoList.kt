package com.gymondo.gymondoapp.models


data class ExerciseInfoList(
    val count: Int,
    val next: String,
    val previous: Any,
    val results: List<ExerciseInfo>
)