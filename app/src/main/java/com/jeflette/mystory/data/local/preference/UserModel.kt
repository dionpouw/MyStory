package com.jeflette.mystory.data.local.preference

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    val id: String?, val name: String?, val token: String?, val isLogin: Boolean
) : Parcelable
