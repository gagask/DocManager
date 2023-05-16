package com.example.docmanager.ui.storage

import android.os.Environment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.docmanager.FileInfo
import java.io.File

class StorageViewModel : ViewModel() {

    private val rootPath = Environment.getExternalStorageDirectory().toString()
    private var currentDir = rootPath

    private val _backButtonEnabled = MutableLiveData<Boolean>()
    val backButtonEnabled: LiveData<Boolean> = _backButtonEnabled

    var sortType: String = "Name"

    private val _sortDescending = MutableLiveData<Boolean>()
    val sortDescending: LiveData<Boolean> = _sortDescending

    private val _filesInfo = MutableLiveData<List<FileInfo>>()
    val filesInfo: LiveData<List<FileInfo>> = _filesInfo

    init {
        _backButtonEnabled.value = false
        _sortDescending.value = false
        updateFilesInfo()
    }

    //TODO make not in IO Thread
    private fun updateFilesInfo(){

        val fileItems = File(currentDir).listFiles()?.map { file -> FileInfo(file)}
        if (fileItems != null) {
            _filesInfo.value = fileItems!!
            sortFiles()
        }
    }

    fun sortFiles(){
        _filesInfo.value = when (sortType) {
            "Name"    -> filesSortedWithFlag { file -> file.name }
            "Changed" -> filesSortedWithFlag { file -> file.update_date }
            "Type"    -> filesSortedWithFlag { file -> file.getType() }
            "Size"    -> filesSortedWithFlag { file -> file.size }
            else      -> _filesInfo.value
        }?.sortedBy { file -> !file.isDirectory }
    }

    private inline fun <R: Comparable<R>> filesSortedWithFlag(crossinline selector: (FileInfo) -> R?) = when(_sortDescending.value) {
        true -> _filesInfo.value?.sortedByDescending(selector)
        false -> _filesInfo.value?.sortedBy(selector)
        else -> _filesInfo.value
    }

    fun changeOrder() {
        _sortDescending.value = !_sortDescending.value!!
    }
    fun navTo(path: String) {
        currentDir = path
        updateFilesInfo()
        _backButtonEnabled.value = currentDir != rootPath
    }

    fun navBack() {
        currentDir  = currentDir.substring(0, currentDir.lastIndexOf('/'))
        updateFilesInfo()
        _backButtonEnabled.value = currentDir != rootPath
    }
}