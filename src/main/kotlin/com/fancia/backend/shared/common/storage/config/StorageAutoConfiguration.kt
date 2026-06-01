package com.fancia.backend.shared.common.storage.config

import com.fancia.backend.shared.common.storage.s3.config.S3Configuration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(S3Configuration::class)
class StorageAutoConfiguration
