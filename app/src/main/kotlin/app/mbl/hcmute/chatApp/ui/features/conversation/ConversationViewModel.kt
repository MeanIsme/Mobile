package app.mbl.hcmute.chatApp.ui.features.conversation

import android.view.View
import app.mbl.hcmute.base.common.BaseViewModel
import app.mbl.hcmute.chatApp.R

class ConversationViewModel : BaseViewModel() {

    override fun onClick(view: View) {
        when (view.id) {
            R.id.createConversation -> uiSingleEvent.postValue(ConversationUiState.CreateConversationClick)
            R.id.scanDocument -> uiSingleEvent.postValue(ConversationUiState.ScanDocumentClick)
        }
    }
}