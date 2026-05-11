package com.example.library.management.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import jakarta.persistence.Version;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;

    @Version
    @Column(name = "version", nullable = false)
    public Long version;

    @CreatedDate
    @Column(name = "created", nullable = false, updatable = false)
    public LocalDateTime created;

    @LastModifiedDate
    @Column(name = "updated", nullable = false)
    public LocalDateTime updated;

    @Column(name = "deleted")
    public LocalDateTime deleted;

    public void softDelete() {
        this.deleted = LocalDateTime.now();
    }

    public boolean isDeleted() {
        return deleted != null;
    }

    @Transient
    public String getDisplayId() {
        if (id == null) {
            return getIdPrefix() + "NEW";
        }

        return "%s%07d".formatted(getIdPrefix(), id);
    }

    public abstract String getIdPrefix();
}
