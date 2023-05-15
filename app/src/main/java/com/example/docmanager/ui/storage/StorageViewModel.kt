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
        if (_sortDescending.value == true) {
            _filesInfo.value = when (sortType) {
                "Name"    ->
                    _filesInfo.value?.sortedByDescending { file -> file.name }?.sortedBy { file -> !file.isDirectory }
                "Changed" ->
                    _filesInfo.value?.sortedByDescending { file -> file.update_date }?.sortedBy { file -> !file.isDirectory }
                "Type"    ->
                    _filesInfo.value?.sortedByDescending { file -> file.path.substringAfterLast('.',"") }?.sortedBy { file -> !file.isDirectory }
                "Size"    ->
                    _filesInfo.value?.sortedByDescending { file -> file.size }?.sortedBy { file -> !file.isDirectory }

                else -> _filesInfo.value
            }
            return
        }

        _filesInfo.value = when (sortType) {
                "Name"    ->
                _filesInfo.value?.sortedBy { file -> file.name }?.sortedBy { file -> !file.isDirectory }
                "Changed" ->
                _filesInfo.value?.sortedBy { file -> file.update_date }?.sortedBy { file -> !file.isDirectory }
                "Type"    ->
                _filesInfo.value?.sortedBy { file -> file.path.substringAfterLast('.',"") }?.sortedBy { file -> !file.isDirectory }
                "Size"    ->
                _filesInfo.value?.sortedBy { file -> file.size }?.sortedBy { file -> !file.isDirectory }

            else -> _filesInfo.value
        }
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