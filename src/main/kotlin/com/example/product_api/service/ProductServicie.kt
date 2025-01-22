package com.example.product_api.service

import com.example.product_api.model.Product
import com.example.product_api.repository.ProductRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ProductService(@Autowired val productRepository: ProductRepository) {

    fun getAllProducts(): List<Product> = productRepository.findAll()

    fun getProductById(id: Long): Product? = productRepository.findById(id).orElse(null)

    fun createProduct(product: Product): Product = productRepository.save(product)

    fun updateProduct(id: Long, product: Product): Product? {
        if (productRepository.existsById(id)) {
            return productRepository.save(product)
        }
        return null
    }

    fun deleteProduct(id: Long) {
        productRepository.deleteById(id)
    }
}
