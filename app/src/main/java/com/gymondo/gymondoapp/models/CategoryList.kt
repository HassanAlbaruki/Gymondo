package com.gymondo.gymondoapp.models


data class CategoryList(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Category>
)