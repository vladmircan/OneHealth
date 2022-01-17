package com.example.onehealth.domain.core

interface DomainMappable<Model> {
    fun toDomainModel(): Model
}