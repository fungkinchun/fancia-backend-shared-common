package com.fancia.backend.shared.common.storage.service

interface FileStorageService {
    fun deleteFile(fileName: String)
    fun listFiles(): MutableList<String?>
    fun moveFile(sourceFileName: String, destinationFileName: String)
}
