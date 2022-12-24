package com.app.demo.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.app.demo.db.repo.FriendsRepository
import com.app.demo.model.Friends
import kotlinx.coroutines.launch

private const val TAG = "FriendsViewModel"

class FriendsViewModel(private val repository: FriendsRepository) : ViewModel() {

    init {
        Log.d(TAG, "FriendsViewModel init ")
    }

    val allFriendsList: LiveData<List<Friends>> = repository.friendsList.asLiveData()

    fun insertFriends(friends: Friends) = viewModelScope.launch {
        repository.insertFriends(friends)
    }

    fun deleteFriends(friends: Friends) = viewModelScope.launch {
        repository.deleteFriends(friends)
    }


    class FriendsViewModelFactory(private val repository: FriendsRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FriendsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return FriendsViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}