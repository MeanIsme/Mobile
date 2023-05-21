package app.mbl.hcmute.chatApp.data.product.repository

import app.mbl.hcmute.chatApp.data.local.room.ProductDAO
import app.mbl.hcmute.chatApp.data.network.retrofit.ProductsApi
import app.mbl.hcmute.chatApp.domain.entities.Product
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(private val productDAO: ProductDAO, private val productsApi: ProductsApi) : ProductRepository {
    override suspend fun getAll(): Flow<List<Product>> = productDAO.getAll()

    override suspend fun insertProduct(product: Product) {
        Timber.d("insertProduct: $product")
        productDAO.addProduct(product)
    }

    override suspend fun isExist(searchQuery: String): Boolean {
        return productDAO.isExist(searchQuery)
    }
}