package app.mbl.hcmute.chatApp.ui.features.chat

import android.view.View
import app.mbl.hcmute.base.common.BaseViewModel
import app.mbl.hcmute.chatApp.R
import app.mbl.hcmute.chatApp.data.local.datastore.DataStoreManager
import com.aallam.openai.client.OpenAI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(private val dataStoreManager: DataStoreManager, val openAi: OpenAI) : BaseViewModel() {

    val typedText = MutableStateFlow("")
    val isBotTyping = MutableStateFlow(false)

    override fun onClick(view: View) {
        super.onClick(view)
        when (view.id) {
            R.id.btnSend -> uiSingleEvent.postValue(ChatUiState.SendMessage)
        }
    }

    fun onTypingTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        typedText.tryEmit(s.toString())
    }

}