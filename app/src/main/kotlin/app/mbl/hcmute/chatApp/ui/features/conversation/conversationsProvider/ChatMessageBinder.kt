package app.mbl.hcmute.chatApp.ui.features.conversation.conversationsProvider

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import app.mbl.hcmute.chatApp.databinding.ConversationItemBinding
import app.mbl.hcmute.chatApp.domain.entities.Conversation
import mva3.extension.DBItemBinder
import java.text.SimpleDateFormat
import java.util.*

class ConversationBinder(private val onItemClick: (id: Long) -> Unit) : DBItemBinder<Conversation, ConversationItemBinding>() {

    override fun canBindData(item: Any): Boolean {
        return item is Conversation
    }

    override fun createBinding(parent: ViewGroup): ConversationItemBinding {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ConversationItemBinding.inflate(layoutInflater, parent, false)
    }

    override fun bindModel(item: Conversation, binding: ConversationItemBinding) {
        binding.root.setOnClickListener {
//            Toast.makeText(binding.root.context, "Start ChatGpt screen", Toast.LENGTH_SHORT).show()
            onItemClick.invoke(item.id)
        }
        binding.apply {
            tvTitle.text = item.title.ifEmpty { "New Conversation" }
//            lastMessage.text = item.lastMessage
            tvTime.text = item.lastUpdated.mapToDateTime()

            ivImage.setImageBitmap(tvTitle.text.toString().getFirstLetterBitmap())
        }
    }

    fun String.getFirstLetterBitmap(): Bitmap {
        val letter = this.first().toString().toUpperCase(Locale.getDefault())

        val width = 200 // Độ rộng của ảnh
        val height = 200 // Chiều cao của ảnh
        val radius = 100f // Bán kính của ảnh

        val paint = Paint().apply {
            color = getRandomColor() // Lấy màu ngẫu nhiên
            isAntiAlias = true
            style = Paint.Style.FILL
            textSize = 100f
            textAlign = Paint.Align.CENTER
        }

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawCircle(radius, radius, radius, paint)
        paint.color = Color.WHITE
        canvas.drawText(letter, radius, radius + paint.textSize / 3, paint)

        return bitmap
    }

    private fun getRandomColor(): Int {
        val random = Random()
        return Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))
    }


}

//Return dateTimeFormat with format HH:mm:ss dd/MM/yyyy
private fun Number.mapToDateTime(): String {
    val date = Date(this.toLong())
    val format = SimpleDateFormat("HH:mm:ss dd/MM/yyyy", Locale.getDefault())
    return format.format(date)
}
