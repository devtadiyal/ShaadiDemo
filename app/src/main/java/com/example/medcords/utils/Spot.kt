package com.yuyakaido.android.cardstackview.sample

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class Spot(
        val id: Long = counter++,
        val name: String,
        val city: String,
        val url: String,
        val accept: String,
        val reject: String
) {
    companion object {
        private var counter = 0L
    }
    @PrimaryKey(autoGenerate = true)
    var no: Int = 0

}

