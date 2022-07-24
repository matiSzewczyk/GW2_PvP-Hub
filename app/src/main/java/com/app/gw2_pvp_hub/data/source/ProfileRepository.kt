package com.app.gw2_pvp_hub.data.source

import android.graphics.Bitmap
import io.realm.mongodb.User

interface ProfileRepository {
    suspend fun getProfilePicture(user: User): Bitmap
    suspend fun setProfilePicture(bitmap: Bitmap)
}