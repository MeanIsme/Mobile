package app.mbl.hcmute.chatApp.domain.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import app.mbl.hcmute.chatApp.data.local.room.DbConstants.PRODUCT_TABLE

@Entity(tableName = PRODUCT_TABLE)
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
)
