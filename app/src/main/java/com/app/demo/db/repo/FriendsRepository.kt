package com.app.demo.db.repo

import androidx.annotation.WorkerThread
import com.app.demo.db.dao.FriendsDao
import com.app.demo.model.Friends
import kotlinx.coroutines.flow.Flow

class FriendsRepository(private val friendsDao: FriendsDao) {

     val friendsList: Flow<List<Friends>> = friendsDao.getAllFriendsList()

    @WorkerThread
    suspend fun insertFriends(friends: Friends) {
        friendsDao.insertFriends(friends)
    }

    @WorkerThread
    suspend fun deleteFriends(friends: Friends) {
        friendsDao.deleteFriends(friends)
    }

}