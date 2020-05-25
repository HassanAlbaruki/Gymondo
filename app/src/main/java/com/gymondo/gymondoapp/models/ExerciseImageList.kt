package com.gymondo.gymondoapp.models


data class ExerciseImageList(
    val count: Int,
    val next: String,
    val previous: Any,
    val results: List<Images>
)