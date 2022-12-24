package com.app.demo.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.app.demo.db.repo.OwnerRepository
import com.app.demo.model.Owner
import kotlinx.coroutines.launch


class OwnerViewModel(private val repository: OwnerRepository) : ViewModel() {

    init {
        Log.d(TAG, "OwnerViewModel init ")
    }

    val allOwnerList: LiveData<List<Owner>> = repository.ownerList.asLiveData()

    fun insertOwner(owner: Owner) = viewModelScope.launch {
        repository.insertOwner(owner)
    }
    fun updateOwner(owner: Owner) = viewModelScope.launch {
        repository.updateOwner(owner)
    }



    class OwnerViewModelFactory(private val repository: OwnerRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(OwnerViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return OwnerViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    companion object{
        private const val TAG = "OwnerViewModel"
    }
}