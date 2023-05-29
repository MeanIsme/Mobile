package app.mbl.hcmute.chatApp.ui.features.conversation

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import app.mbl.hcmute.base.common.BaseVmDbFragment
import app.mbl.hcmute.chatApp.R
import app.mbl.hcmute.chatApp.databinding.FragmentConversationBinding
import app.mbl.hcmute.chatApp.domain.entities.Conversation
import app.mbl.hcmute.chatApp.ui.features.conversation.Const.Companion.CONVERSATION_LIST
import app.mbl.hcmute.chatApp.ui.features.conversation.multiAdapter.ConversationBinder
import mva3.adapter.ListSection
import mva3.adapter.MultiViewAdapter

class ConversationFragment : BaseVmDbFragment<ConversationViewModel, FragmentConversationBinding>() {
    private val conversationAdapter = MultiViewAdapter()

    override val viewModel: ConversationViewModel by viewModels()
    override fun getLayoutId() = R.layout.fragment_conversation

    override fun setUpViews(savedInstanceState: Bundle?) {
        super.setUpViews(savedInstanceState)
        binding.rvConversation.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = conversationAdapter
            conversationAdapter.registerItemBinders(ConversationBinder())
            val conversationSection = ListSection<Conversation>().apply { addAll(CONVERSATION_LIST) }
            conversationAdapter.addSection(conversationSection)
        }
    }
}