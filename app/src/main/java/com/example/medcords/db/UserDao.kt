package com.example.medcords.db

import androidx.room.*
import com.yuyakaido.android.cardstackview.sample.Spot

@Dao
interface UserDao {

    @Update
    suspend fun updateUser(user: Spot)

    @Delete
    suspend fun deleteUser(user: Spot)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: List<Spot>)

    @Query("SELECT * FROM user ORDER BY id DESC")
    fun getList(): List<Spot>

}