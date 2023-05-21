package app.mbl.hcmute.chatApp.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.mbl.hcmute.chatApp.data.local.room.DbConstants.PRODUCT_TABLE
import app.mbl.hcmute.chatApp.domain.entities.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDAO {
    @Query("SELECT * FROM $PRODUCT_TABLE ORDER BY id ASC")
    fun getAll(): Flow<List<Product>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addProduct(product: Product)

    @Query("SELECT EXISTS(SELECT * FROM $PRODUCT_TABLE WHERE name = :searchKeyword)")
    suspend fun isExist(searchKeyword: String): Boolean
}