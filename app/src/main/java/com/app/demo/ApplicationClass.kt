package com.app.demo

import android.app.Application
import com.app.demo.db.DemoDatabase
import com.app.demo.db.repo.FriendsRepository
import com.app.demo.db.repo.OwnerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class ApplicationClass : Application() {

    // No need to cancel this scope as it'll be torn down with the process
    private val applicationScope = CoroutineScope(SupervisorJob())

    /* Using by lazy so the database and the repository are only created when they're needed
     rather than when the application starts */

    private val database by lazy { DemoDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { FriendsRepository(database.friendsDao()) }
    val ownerRepository by lazy { OwnerRepository(database.ownerDao()) }

    override fun onCreate() {
        super.onCreate()
        instance = this

    }

    companion object {
        private var instance: ApplicationClass? = null

        fun getContext(): ApplicationClass {
            return instance!!
        }
    }
}