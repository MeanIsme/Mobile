package app.mbl.hcmute.chatApp.ui.features.bookmark

import android.view.View
import app.mbl.hcmute.base.common.BaseViewModel
import app.mbl.hcmute.chatApp.data.local.datastore.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(private val dataStore: DataStoreManager) : BaseViewModel() {

    override fun onClick(view: View) {
        when (view.id) {
            else -> {}
        }
    }
}