package app.mbl.hcmute.chatApp.ui.features.chat

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
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
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.stfalcon.chatkit.messages.MessageHolders
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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
//        binding.etInput.setOnEditorActionListener { _, actionId, _ ->
//            if (actionId == EditorInfo.IME_ACTION_DONE) {
//                viewModel.uiSingleEvent.postValue(ChatUiState.SendMessage)
//                true
//            } else false
//        }
    }

    private fun listen() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text")
        }
        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT)
        } catch (e: Exception) {
            showToast("Error: Your device do not support google voice command")
            Timber.d("Error: Your device do not support google voice command," + e.message)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && data != null) {
                data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.apply {
                    viewModel.setTypedText(get(0))
                    viewModel.sendClickCommand(ChatUiState.SendMessage)
                }
            }
        }
    }

    override fun setUpObservers() {
        super.setUpObservers()
        viewModel.clickEvent.observe(viewLifecycleOwner) {
            when (it) {
                is ChatUiState.SendMessage -> {
                    val message = viewModel.typedText.value
                    if (message.isNotEmpty()) {
                        val localMessage = createLocalMessage(message, userAuthor)
                        messagesAdapter.addToStart(localMessage, true)
                        viewModel.setTypedText("")
                        sendMessage()
                    }
                }

                is ChatUiState.Voice -> listen()
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
                        delay(150)
                        withContext(Dispatchers.Main) { messagesAdapter.update(responseMessage) }
                    }
                }
                initConversationTitle()
            } catch (ex: Exception) {
                Timber.e(ex)
                messagesAdapter.addToStart(createLocalMessage("Error: ${ex.message}", botAuthor), true)
            }
            viewModel.isBotTyping.tryEmit(false)
        }

    }

    private suspend fun initConversationTitle() {
        if (viewModel.conversation.value.title.isEmpty()) {
            val requestMessages = createSendMessages(2).toMutableList()
            requestMessages.add(ChatMessage(ChatRole.User, GET_CONVERSATION_TITLE_COMMAND))
            Timber.d("AAA$requestMessages")
            val response = openAi.chatCompletion(ChatCompletionRequest(ModelId(ChatBot.CHAT_GPT_MODEL), requestMessages))
            response.choices[0].message?.content?.let { responseTitle ->
                Timber.d("AAA response: $responseTitle")
                viewModel.conversation.tryEmit(viewModel.conversation.value.copy(title = responseTitle.substringAfterLast(":").trim()))
            }
        }
    }

    private fun createSendMessages(count: Int = MAX_SEND_MESSAGE) = messagesAdapter.lastNumberItems(count).filter { it.item is LocalChatMessage }
        .map { it.item as LocalChatMessage }
        .map {
            val author = if (it.messageAuthor.authorId == botAuthor.authorId) ChatRole.Assistant else ChatRole.User
            it.toChatMessage(author)
        }

    companion object {
        const val GET_CONVERSATION_TITLE_COMMAND = "Please name the above conversation in about 10 words," +
                " In case you don't know what to name the conversation, do your best, please." +
                " The answer is written in the following the format: Title : Name"

        const val MAX_SEND_MESSAGE = 5
        const val REQUEST_CODE_SPEECH_INPUT = 1
    }
}