package app.mbl.hcmute.chatApp.ui.features.bookmark.bookmarksProvider

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import app.mbl.hcmute.chatApp.databinding.BookmarkItemBinding
import app.mbl.hcmute.chatApp.domain.entities.ChatBookmark
import mva3.extension.DBItemBinder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Random

class BookmarkBinder(private val onItemClick: (convId: Long, messId: String) -> Unit) : DBItemBinder<ChatBookmark, BookmarkItemBinding>() {

    override fun canBindData(item: Any): Boolean {
        return item is ChatBookmark
    }

    override fun createBinding(parent: ViewGroup): BookmarkItemBinding {
        val layoutInflater = LayoutInflater.from(parent.context)
        return BookmarkItemBinding.inflate(layoutInflater, parent, false)
    }

    override fun bindModel(item: ChatBookmark, binding: BookmarkItemBinding) {
        binding.root.setOnClickListener {
//            Toast.makeText(binding.root.context, "Start ChatGpt screen", Toast.LENGTH_SHORT).show()
            onItemClick.invoke(item.conversationId, item.messageId)
        }
        binding.apply {
            tvTitle.text = item.content.ifEmpty { "New Bookmark" }
//            lastMessage.text = item.lastMessage
            tvTime.text = item.createdTime.mapToDateTime()

            ivImage.setImageBitmap(tvTitle.text.toString().getFirstLetterBitmap())
        }
    }

    fun String.getFirstLetterBitmap(): Bitmap {
        val letter = this.first().toString().toUpperCase(Locale.getDefault())

        val width = 240 // Độ rộng của ảnh
        val height = 240 // Chiều cao của ảnh
        val radius = 120f // Bán kính của ảnh

        val paint = Paint().apply {
            color = getRandomColor() // Lấy màu ngẫu nhiên
            isAntiAlias = true
            style = Paint.Style.FILL
            textSize = 120f
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
