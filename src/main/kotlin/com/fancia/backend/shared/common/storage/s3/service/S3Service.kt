package com.fancia.backend.shared.common.storage.s3.service

import com.fancia.backend.shared.common.storage.s3.config.S3Configuration
import com.fancia.backend.shared.common.storage.service.FileStorageService
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.CopyObjectRequest
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request

@Service
class S3Service(
    private val s3Client: S3Client,
    private val s3Configuration: S3Configuration,
) : FileStorageService {
    override fun deleteFile(fileName: String) {
        try {
            val request = DeleteObjectRequest.builder()
                .bucket(s3Configuration.bucketName)
                .key(fileName)
                .build()
            s3Client.deleteObject(request)
        } catch (e: Exception) {
            throw RuntimeException("Failed to delete file: $fileName", e)
        }
    }

    override fun listFiles(): MutableList<String?> {
        return try {
            val listRequest = ListObjectsV2Request.builder()
                .bucket(s3Configuration.bucketName)
                .build()
            val response = s3Client.listObjectsV2(listRequest)
            response.contents()
                .map { it.key() }
                .toMutableList()
        } catch (e: Exception) {
            throw RuntimeException("Failed to list files in bucket", e)
        }
    }

    override fun moveFile(sourceFileName: String, destinationFileName: String) {
        if (sourceFileName == destinationFileName) return
        val bucket = s3Configuration.bucketName
        try {
            s3Client.copyObject(
                CopyObjectRequest.builder()
                    .sourceBucket(bucket)
                    .sourceKey(sourceFileName)
                    .destinationBucket(bucket)
                    .destinationKey(destinationFileName)
                    .build()
            )
            s3Client.deleteObject(
                DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(sourceFileName)
                    .build()
            )
        } catch (e: Exception) {
            throw RuntimeException(
                "Failed to move file from $sourceFileName to $destinationFileName",
                e
            )
        }
    }
}
