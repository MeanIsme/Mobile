package app.mbl.hcmute.chatApp.ui.features.conversation

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import app.mbl.hcmute.base.common.BaseVmDbFragment
import app.mbl.hcmute.base.ext.observe
import app.mbl.hcmute.chatApp.R
import app.mbl.hcmute.chatApp.databinding.FragmentConversationBinding
import app.mbl.hcmute.chatApp.di.module.navigationModule.AppNavigator
import app.mbl.hcmute.chatApp.domain.entities.Conversation
import app.mbl.hcmute.chatApp.ui.features.conversation.Const.Companion.CONVERSATION_LIST
import app.mbl.hcmute.chatApp.ui.features.conversation.conversationsProvider.BottomItemDecoration
import app.mbl.hcmute.chatApp.ui.features.conversation.conversationsProvider.ConversationBinder
import app.mbl.hcmute.chatApp.ui.firstScreen.FirstScreenFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import mva3.adapter.ListSection
import mva3.adapter.MultiViewAdapter
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class ConversationFragment : BaseVmDbFragment<ConversationViewModel, FragmentConversationBinding>() {
    private val conversationAdapter = MultiViewAdapter()

    override val viewModel: ConversationViewModel by viewModels()
    override fun getLayoutId() = R.layout.fragment_conversation

    @Inject
    lateinit var navigator: AppNavigator

    override fun setUpViews(savedInstanceState: Bundle?) {
        super.setUpViews(savedInstanceState)
        binding.vm = viewModel
        binding.rvConversation.apply {
            addItemDecoration(BottomItemDecoration())
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = conversationAdapter
            conversationAdapter.registerItemBinders(ConversationBinder())
            val conversationSection = ListSection<Conversation>().apply { addAll(CONVERSATION_LIST) }
            conversationAdapter.addSection(conversationSection)
        }
    }

    override fun setUpObservers() {
        super.setUpObservers()
        observe(viewModel.uiSingleEvent) {
            when (it) {
                ConversationUiState.CreateConversationClick -> {
                    showToast("Start ChatGpt screen")
                    val direction = FirstScreenFragmentDirections.actionFirstScreenFragmentToChatAssistantFragment()
                    navigator.navigateTo(direction)
                }
                ConversationUiState.ScanDocumentClick -> {
                    showToast("Scan document")
                }
                else -> {}
            }
        }
    }
}