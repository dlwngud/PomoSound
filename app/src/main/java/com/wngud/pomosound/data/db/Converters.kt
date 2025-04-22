package com.wngud.pomosound.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromSoundVolumeList(soundVolumeList: List<Pair<Int, Float>>?): String? {
        return soundVolumeList?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toSoundVolumeList(soundVolumeListString: String?): List<Pair<Int, Float>>? {
        return soundVolumeListString?.let {
            val type = object : TypeToken<List<Pair<Int, Float>>>() {}.type
            gson.fromJson(it, type)
        }
    }
}
