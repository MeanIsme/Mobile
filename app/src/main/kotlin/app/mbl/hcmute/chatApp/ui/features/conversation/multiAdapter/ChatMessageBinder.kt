package app.mbl.hcmute.chatApp.ui.features.conversation.multiAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import app.mbl.hcmute.chatApp.databinding.ConversationItemBinding
import app.mbl.hcmute.chatApp.domain.entities.Conversation
import mva3.extension.DBItemBinder
import java.text.SimpleDateFormat
import java.util.*

class ConversationBinder() : DBItemBinder<Conversation, ConversationItemBinding>() {

    override fun canBindData(item: Any): Boolean {
        return item is Conversation
    }

    override fun createBinding(parent: ViewGroup): ConversationItemBinding {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ConversationItemBinding.inflate(layoutInflater, parent, false)
    }

    override fun bindModel(item: Conversation, binding: ConversationItemBinding) {
        binding.apply {
            tvTitle.text = item.title
//            lastMessage.text = item.lastMessage
            tvTime.text = item.timeStamp.mapToDateTime()
        }
    }

}

//Return dateTimeFormat with format HH:mm:ss dd/MM/yyyy
private fun Number.mapToDateTime(): String {
    val date = Date(this.toLong())
    val format = SimpleDateFormat("HH:mm:ss dd/MM/yyyy", Locale.getDefault())
    return format.format(date)
}
