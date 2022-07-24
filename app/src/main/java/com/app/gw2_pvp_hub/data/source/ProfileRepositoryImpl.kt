package com.app.gw2_pvp_hub.data.source

import android.graphics.Bitmap
import android.util.Log
import com.app.gw2_pvp_hub.MyApplication
import com.app.gw2_pvp_hub.utils.ImageHandler
import io.realm.mongodb.User
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import org.bson.Document
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
): ProfileRepository {

    private val TAG: String = "ProfileRepositoryImpl"

    override suspend fun getProfilePicture(user: User): String {
        return withContext(IO) {
            val collection = MyApplication.user!!
                .getMongoClient("mongodb-atlas")
                .getDatabase("GW2-PvP_HubDB")
                .getCollection("custom-user-data")

            try {
                collection.findOne().get()["profilePicture"]
            } catch (e: Exception) {
                Log.e(TAG, "getProfilePicture: ${e.message}")
            }.toString()
        }
    }

    override suspend fun setProfilePicture(bitmap: Bitmap) {
        val collection = MyApplication.user!!
            .getMongoClient("mongodb-atlas")
            .getDatabase("GW2-PvP_HubDB")
            .getCollection("custom-user-data")

        val user = MyApplication.user
        val image = ImageHandler().convertToString(bitmap)

        collection.insertOne(Document("_id", user!!.id)
            .append("profilePicture", image)
            .append("_partition", "default")).getAsync {
                if (it.isSuccess) {
                    Log.d(
                        TAG,
                    "setProfilePicture: Profile picture inserted " +
                            it.get().insertedId
                    )
                    MyApplication.realm!!.executeTransactionAsync {
                        user.refreshCustomData()
                    }
                } else {
                    Log.e(
                        TAG,
                        "setProfilePicture: Couldn't insert picture  " +
                                "\n${it.error}" +
                                it.error.errorMessage +
                                "\n${it.error.errorCode}"
                    )
                }
        }
    }
}