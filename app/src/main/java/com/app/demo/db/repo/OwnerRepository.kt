package com.app.demo.db.repo

import androidx.annotation.WorkerThread
import com.app.demo.db.dao.OwnerDao
import com.app.demo.model.Owner
import kotlinx.coroutines.flow.Flow

class OwnerRepository(val ownerDao: OwnerDao) {

    val ownerList: Flow<List<Owner>> = ownerDao.getAllOwnerList()

    @WorkerThread
    suspend fun insertOwner(owner: Owner) {
        ownerDao.insertOwner(owner)
    }

    @WorkerThread
    suspend fun updateOwner(owner: Owner) {
        ownerDao.updateOwner(owner)
    }

    @WorkerThread
    suspend fun deleteOwner(owner: Owner) {
        ownerDao.deleteOwner(owner)
    }
}