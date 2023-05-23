package app.mbl.hcmute.chatApp.ui.features.chat

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import app.mbl.hcmute.base.common.BaseVmDbFragment
import app.mbl.hcmute.chatApp.R
import app.mbl.hcmute.chatApp.data.local.datastore.DataStoreManager
import app.mbl.hcmute.chatApp.databinding.FragmentChatBinding
import app.mbl.hcmute.chatApp.domain.entities.*
import app.mbl.hcmute.chatApp.ui.features.chat.chatKit.ChatAdapter
import app.mbl.hcmute.chatApp.ui.features.chat.chatKit.MarkDownProvider
import app.mbl.hcmute.chatApp.ui.features.chat.chatKit.MarkdownIncomingTextMessageViewHolder
import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.stfalcon.chatkit.messages.MessageHolders
import com.stfalcon.chatkit.messages.MessagesListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
@OptIn(BetaOpenAI::class)
class ChatFragment : BaseVmDbFragment<ChatViewModel, FragmentChatBinding>() {
    private val userAuthor: Author by lazy { Author(System.currentTimeMillis().toString(), "User", "") }
    private val botAuthor: Author by lazy { Author(ChatBot.gptAssistant.id, ChatBot.gptAssistant.name, "") }
    private val messagesAdapter: ChatAdapter by lazy {
        val messageHolders = MessageHolders()
        messageHolders.setIncomingTextConfig(MarkdownIncomingTextMessageViewHolder::class.java, R.layout.item_custom_incomming_text_message)
        ChatAdapter(userAuthor.id, messageHolders)
    }

    @Inject
    lateinit var dataStore: DataStoreManager

    @Inject
    lateinit var openAi: OpenAI

    override fun getLayoutId() = R.layout.fragment_chat

    override val viewModel: ChatViewModel by viewModels()

    override fun initOnCreate(savedInstanceState: Bundle?) {
        super.initOnCreate(savedInstanceState)
        MarkDownProvider.initMarkDown(requireContext()) // add markdown support for TextView
        messagesAdapter.addToStart(createLocalMessage(ChatBot.gptAssistant.welcomeMessage, botAuthor), true)
    }

    override fun setUpViews(savedInstanceState: Bundle?) {
        super.setUpViews(savedInstanceState)
        binding.vm = viewModel
        binding.messages.setAdapter(messagesAdapter)
        binding.etInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.uiSingleEvent.postValue(ChatUiState.SendMessage)
                true
            } else false
        }
    }

    override fun setUpObservers() {
        super.setUpObservers()
        viewModel.uiSingleEvent.observe(viewLifecycleOwner) {
            when (it) {
                is ChatUiState.SendMessage -> {
                    val message = binding.etInput.text.toString()
                    if (message.isNotEmpty()) {
                        val localMessage = createLocalMessage(message, userAuthor)
                        messagesAdapter.addToStart(localMessage, true)
                        binding.etInput.setText("")
                        sendMessage()
                    }
                }
            }
        }
    }

    private fun sendMessage() {
        viewModel.isBotTyping.tryEmit(true)
        val requestMessages = createSendMessages()
        val responseMessage = createLocalMessage("", botAuthor)
        messagesAdapter.addToStart(responseMessage, true) // add response message

        val chatCompletionRequest = ChatCompletionRequest(ModelId(ChatBot.CHAT_GPT_MODEL), requestMessages) //Init chat completion request
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            Timber.d("chatCompletionRequest: ${chatCompletionRequest.messages}")
            try {
                openAi.chatCompletions(chatCompletionRequest).collect {
                    it.choices[0].delta?.content?.let { newestMessage ->
                        responseMessage.messageContent += newestMessage
                        withContext(Dispatchers.Main) { messagesAdapter.update(responseMessage) }
                    }
                }
            } catch (ex: Exception) {
                Timber.e(ex)
                messagesAdapter.addToStart(createLocalMessage("Error: ${ex.message}", botAuthor), true)
            }
            viewModel.isBotTyping.tryEmit(false)
        }

    }

    private fun createSendMessages() = messagesAdapter.lastNumberItems(5).filter { it.item is LocalChatMessage }
        .map { it.item as LocalChatMessage }
        .map {
            val author = if (it.messageAuthor.authorId == botAuthor.authorId) ChatRole.Assistant else ChatRole.User
            it.toChatMessage(author)
        }

}