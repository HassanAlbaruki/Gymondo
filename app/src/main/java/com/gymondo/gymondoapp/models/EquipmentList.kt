package com.gymondo.gymondoapp.models


data class EquipmentList(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Equipment>
)