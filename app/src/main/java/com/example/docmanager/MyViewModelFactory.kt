package com.example.docmanager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.docmanager.ui.changes.ChangesViewModel
import com.example.docmanager.ui.storage.StorageViewModel

class MyViewModelFactory(
    //private val dataSource: StrategyDatabaseDao,
    //private val application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StorageViewModel::class.java)) {
            return StorageViewModel() as T
        }
        if (modelClass.isAssignableFrom(ChangesViewModel::class.java)) {
            return ChangesViewModel() as T
        }
//        if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
//            return FavoriteViewModel(dataSource, application) as T
//        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}