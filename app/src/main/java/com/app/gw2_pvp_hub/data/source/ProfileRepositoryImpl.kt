package com.app.gw2_pvp_hub.data.source

import android.graphics.Bitmap
import android.util.Log
import com.app.gw2_pvp_hub.MyApplication
import io.realm.mongodb.User
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import org.bson.Document
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(

): ProfileRepository{

    private val TAG: String = "ProfileRepositoryImpl"

    override suspend fun getProfilePicture(user: User): Bitmap {
        return withContext(IO) {
            MyApplication.user!!.customData["profilePicture"] as Bitmap
        }
    }

    override suspend fun setProfilePicture(bitmap: Bitmap) {
        val collection = MyApplication.user!!
            .getMongoClient("mongodb-atlas")
            .getDatabase("GW2-PvP_HubDB")
            .getCollection("custom-user-data")

        val user = MyApplication.user

        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val image = stream.toByteArray()

        collection.insertOne(Document("user-id", user!!.id)
            .append("profilePicture", image)
            .append("_partition", "default")).getAsync {
                if (it.isSuccess) {
                    Log.d(
                        TAG,
                    "setProfilePicture: Profile picture inserted " +
                            it.get().insertedId
                    )
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