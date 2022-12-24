package com.app.demo.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.app.demo.model.Owner
import kotlinx.coroutines.flow.Flow

@Dao
interface OwnerDao {

    @Query("SELECT * FROM owner_table")
    fun getAllOwnerList(): Flow<List<Owner>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOwner(owner: Owner)

  @Update
    suspend fun updateOwner(owner: Owner)


    @Delete
    suspend fun deleteOwner(owner: Owner)
}