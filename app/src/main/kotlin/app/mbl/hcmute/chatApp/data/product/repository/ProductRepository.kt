package app.mbl.hcmute.chatApp.data.product.repository

import app.mbl.hcmute.chatApp.domain.entities.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun getAll(): Flow<List<Product>>
    suspend fun insertProduct(product: Product)
    suspend fun isExist(searchQuery: String): Boolean
}