package app.mbl.hcmute.chatApp.domain.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import app.mbl.hcmute.chatApp.data.local.room.DbConstants

@Entity(tableName = DbConstants.PRODUCT_TABLE)
data class Conversation(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val image: String? = "",
    val userId: String = "",
    var isPrivate: Boolean = false,
    var timeStamp: Long,
)
