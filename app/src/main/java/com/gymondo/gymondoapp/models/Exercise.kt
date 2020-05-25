package com.gymondo.gymondoapp.models


import com.google.gson.annotations.SerializedName
import java.io.Externalizable
import java.io.Serializable

 class Exercise(val category: Int,
    @SerializedName("creation_date")
    val creationDate: String?="",
    val description: String?,
    val equipment: List<Int>?,
    val id: Int?,
    val language: Int?,
    @SerializedName("license_author")
    val licenseAuthor: String?,
    val muscles: List<Int>?,
    @SerializedName("muscles_secondary")
    val musclesSecondary: List<Int>?,
    val name: String?,
    @SerializedName("name_original")
    val nameOriginal: String?,
    val imageId :Int?
):Serializable