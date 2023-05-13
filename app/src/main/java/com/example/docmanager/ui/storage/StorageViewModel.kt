package com.example.docmanager.ui.storage

import android.os.Environment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.docmanager.FileInfo
import java.io.File

class StorageViewModel : ViewModel() {

    val root_path = Environment.getExternalStorageDirectory().toString()
    var current_dir = root_path

    private val _backButtonEnabled = MutableLiveData<Boolean>()
    val backButtonEnabled: LiveData<Boolean> = _backButtonEnabled

    private val _filesInfo = MutableLiveData<List<FileInfo>>()
    val filesInfo: LiveData<List<FileInfo>> = _filesInfo

    init {
        _backButtonEnabled.value = false
        updateFilesInfo()
    }

    //TODO make not in IO Thread
    private fun updateFilesInfo(){


        val fileItems = File(current_dir).listFiles()?.map { file -> FileInfo(file)}
        if (fileItems != null) {
            _filesInfo.value = fileItems.sortedBy { file -> file.name }.sortedBy { file -> !file.isDirectory }
        }



    }

    fun navTo(path: String) {
        current_dir = path
        updateFilesInfo()
        _backButtonEnabled.value = current_dir != root_path
    }

    fun navBack() {
        current_dir  = current_dir.substring(0, current_dir.lastIndexOf('/'))
        updateFilesInfo()
        _backButtonEnabled.value = current_dir != root_path
    }
}