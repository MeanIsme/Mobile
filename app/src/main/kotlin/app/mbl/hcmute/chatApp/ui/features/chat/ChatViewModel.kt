package app.mbl.hcmute.chatApp.ui.features.chat

import android.view.View
import androidx.databinding.Bindable
import app.mbl.hcmute.base.common.BaseViewModel
import app.mbl.hcmute.chatApp.R
import app.mbl.hcmute.chatApp.data.local.datastore.DataStoreManager
import app.mbl.hcmute.chatApp.domain.entities.Conversation
import app.mbl.hcmute.chatApp.domain.entities.LocalChatMessage
import com.aallam.openai.client.OpenAI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(private val dataStoreManager: DataStoreManager, val openAi: OpenAI) : BaseViewModel() {

    val typedText = MutableStateFlow("")
    val isBotTyping = MutableStateFlow(false)
    val conversation = MutableStateFlow(Conversation(timeStamp = System.currentTimeMillis()))

    fun setTypedText(text: String) {
        if (text == typedText.value) return
        typedText.tryEmit(text)
    }

    override fun onClick(view: View) {
        super.onClick(view)
        when (view.id) {
            R.id.btnSend -> _clickEvent.postValue(ChatUiState.SendMessage)
            R.id.btnVoice -> _clickEvent.postValue(ChatUiState.Voice)
            R.id.btnBackToHome -> _clickEvent.postValue(ChatUiState.BackToHome)
        }
    }

    fun sendClickCommand(command: ChatUiState) {
        _clickEvent.postValue(command)
    }

//    fun onTypingTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//        typedText.tryEmit(s.toString())
//    }

}