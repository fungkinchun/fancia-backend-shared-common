package com.fancia.backend.shared.common.core.entity

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import org.hibernate.Hibernate
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.SoftDelete
import org.hibernate.annotations.UuidGenerator
import java.time.LocalDateTime
import java.util.*

@MappedSuperclass
@SoftDelete(columnName = "deleted")
class AbstractEntity {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    @GeneratedValue
    @JsonProperty("id")
    var id: UUID? = null
    var createdBy: UUID? = null

    @CreationTimestamp
    var createdAt: LocalDateTime? = null
    override fun hashCode(): Int {
        return this::class.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as AbstractEntity
        return id == other.id
    }
}