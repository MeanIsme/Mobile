package app.mbl.hcmute.chatApp.domain.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "chat_message", foreignKeys =
    [ForeignKey(
        entity = Conversation::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("conversationId"),
        onDelete = ForeignKey.CASCADE //auto delete all chat message after delete conversation.
    )]
)
data class ChatMessage(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val message: String,
    //foreign key on Conversation class
    val conversationId: Int,
    val userId: String,
    val timeStamp: Long,
    val type: String,
)
