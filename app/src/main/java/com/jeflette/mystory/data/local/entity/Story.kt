package com.jeflette.mystory.data.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
@Entity(tableName = "story")
data class Story(

    @field:SerializedName("photoUrl") val photoUrl: String? = null,

    @field:SerializedName("createdAt") val createdAt: String? = null,

    @field:SerializedName("name") val name: String? = null,

    @field:SerializedName("description") val description: String? = null,


    @field:SerializedName("lon") val lon: @RawValue Any? = null,

    @PrimaryKey @field:SerializedName("id") val id: String,

    @field:SerializedName("lat") val lat: @RawValue Any? = null
) : Parcelable
