package com.jeflette.mystory.data.local

import com.jeflette.mystory.data.local.db.StoryDatabase
import com.jeflette.mystory.data.local.preference.LoginPreference
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val storyDatabase: StoryDatabase, private val loginPreference: LoginPreference
) {}