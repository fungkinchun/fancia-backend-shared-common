package com.fancia.backend.shared.common.location.core.dto

import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Size

data class LocationDto(
    @field:Size(max = 500, message = "Location label must be at most 500 characters")
    val label: String? = null,
    @field:Size(max = 255, message = "Place id must be at most 255 characters")
    val placeId: String? = null,
    @field:DecimalMin("-90.0")
    @field:DecimalMax("90.0")
    val latitude: Double? = null,
    @field:DecimalMin("-180.0")
    @field:DecimalMax("180.0")
    val longitude: Double? = null,
    @field:Size(max = 500, message = "Address line must be at most 500 characters")
    val addressLine: String? = null,
    @field:Size(max = 255, message = "City must be at most 255 characters")
    val city: String? = null,
    @field:Size(max = 50, message = "Postcode must be at most 50 characters")
    val postcode: String? = null,
    @field:Size(max = 100, message = "Country must be at most 100 characters")
    val country: String? = null,
)
