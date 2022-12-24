package com.app.demo.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.app.demo.db.dao.FriendsDao
import com.app.demo.db.dao.OwnerDao
import com.app.demo.model.Friends
import com.app.demo.model.Owner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Owner::class, Friends::class], version = 1, exportSchema = false)
abstract class DemoDatabase : RoomDatabase() {
    abstract fun friendsDao(): FriendsDao
    abstract fun ownerDao(): OwnerDao

    private class DemoDatabaseCallback(
        private val scope: CoroutineScope
    ) : Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.friendsDao())
                }
            }
        }

        suspend fun populateDatabase(friendDao: FriendsDao) {


            val friendsList = arrayListOf(
                Friends( name="Muzammil Hussain", age =  23),
                Friends( name="Hafiz Adnan", age =  25),
                Friends( name="Ali Raza", age =  27),
                Friends( name="Naveed Khaliq", age =  29),
                Friends( name="Mujtaba Maqbool", age =  23),
                Friends( name="Ahmad Shah", age =  25),
                Friends( name="Rana Zaid", age =  24),
                Friends( name="Arslan Jatala", age =  25),
                Friends( name="Ahmad Jatala", age =  23),
                Friends( name="Hammad Sultan", age =  22),
                Friends( name="Hamza Waseem", age =  24),
                Friends( name="Faraz ", age =  25),
                Friends( name="Khaleel", age =  25),
                Friends( name="Hanan Shahid", age =  28),
                Friends( name="Sohaib Ahmad", age =  25),

            )

            friendDao.insertFriendsList(friendsList.toList())


        }
    }

    companion object {
        @Volatile
        private var INSTANCE: DemoDatabase? = null

        fun getDatabase(
            context: Context, scope: CoroutineScope
        ): DemoDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, DemoDatabase::class.java, "demo_database"
                ).fallbackToDestructiveMigration().addCallback(DemoDatabaseCallback(scope)).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}