package com.example.medcords.db

import com.yuyakaido.android.cardstackview.sample.Spot

class UserRepository(private val db: AppDatabase) {
    suspend fun saveUser(user: List<Spot>) = db.getUserDao().insert(user)
    suspend fun deleteUser(user: Spot) = db.getUserDao().deleteUser(user)
    suspend fun updateUser(user: Spot) = db.getUserDao().updateUser(user)
    fun getUser() = db.getUserDao().getList()
}
