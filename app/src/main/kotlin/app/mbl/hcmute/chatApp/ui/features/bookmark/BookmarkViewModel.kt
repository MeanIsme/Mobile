package app.mbl.hcmute.chatApp.ui.features.bookmark

import android.view.View
import app.mbl.hcmute.base.common.BaseViewModel
import app.mbl.hcmute.chatApp.data.local.datastore.DataStoreManager
import app.mbl.hcmute.chatApp.data.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(private val chatRepository: ChatRepository) : BaseViewModel() {

    val bookmarks = chatRepository.getBookmarks()
}