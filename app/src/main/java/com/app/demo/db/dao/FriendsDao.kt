package com.app.demo.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.demo.model.Friends
import kotlinx.coroutines.flow.Flow

@Dao
interface FriendsDao {

    @Query("SELECT * FROM friends_table Order  by id desc")
    fun getAllFriendsList(): Flow<List<Friends>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFriends(friends: Friends)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFriendsList(friends: List<Friends>)

    @Delete
    suspend fun deleteFriends(friends: Friends)

}