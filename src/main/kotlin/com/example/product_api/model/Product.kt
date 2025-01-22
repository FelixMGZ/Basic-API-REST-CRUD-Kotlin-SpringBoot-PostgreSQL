package com.example.product_api.model

import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
data class Product(
    @Id
    val id: Long? = null,
    val nameProduct: String,
    val descriptionProduct: String,
    val price: Double
)
