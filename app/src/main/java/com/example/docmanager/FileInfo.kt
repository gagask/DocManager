package com.example.docmanager

import android.annotation.SuppressLint
import android.webkit.MimeTypeMap
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date


data class FileInfo(
    var name: String,
    var update_date: Long,
    var size: Long,
    var path: String,
    var isDirectory: Boolean
) {
    constructor(file: File): this(file.name, file.lastModified(), file.length(), file.path, file.isDirectory)
}