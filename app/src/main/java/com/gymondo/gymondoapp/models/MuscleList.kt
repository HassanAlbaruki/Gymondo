package com.gymondo.gymondoapp.models


data class MuscleList(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Muscle>
)