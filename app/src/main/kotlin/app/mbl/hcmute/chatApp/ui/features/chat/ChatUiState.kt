package app.mbl.hcmute.chatApp.ui.features.chat

import app.mbl.hcmute.base.common.UIState

sealed class ChatUiState : UIState {
    object SendMessage : ChatUiState()
    object Voice : ChatUiState()

}