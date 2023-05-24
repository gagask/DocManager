package com.example.docmanager.ui.changes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.docmanager.FileInfo

class ChangesViewModel : ViewModel() {
    var sortType: String = "Name"

    private val _sortDescending = MutableLiveData<Boolean>()
    val sortDescending: LiveData<Boolean> = _sortDescending

    private val _pickedFilter = MutableLiveData<String>()
    val pickedFilter: LiveData<String> = _pickedFilter

    private val _changedFilesInfo = MutableLiveData<List<FileInfo>>()
    val changedFilesInfo: LiveData<List<FileInfo>> = _changedFilesInfo


    init {
        _sortDescending.value = false
        //updateFilesInfo()
    }

    fun changeOrder() {
        _sortDescending.value = !_sortDescending.value!!
    }
    fun filterFiles(filter: String) {
        _pickedFilter.value = filter
        //TODO("Not yet implemented")
    }
}