package app.mbl.hcmute.chatApp.ui.features.bookmark

import android.os.Bundle
import androidx.fragment.app.viewModels
import app.mbl.hcmute.base.common.BaseVmDbFragment
import app.mbl.hcmute.base.ext.observe
import app.mbl.hcmute.chatApp.R
import app.mbl.hcmute.chatApp.data.local.datastore.DataStoreManager
import app.mbl.hcmute.chatApp.databinding.FragmentBookmarkBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BookMarkFragment : BaseVmDbFragment<BookmarkViewModel, FragmentBookmarkBinding>() {
    override fun getLayoutId() = R.layout.fragment_bookmark

    @Inject
    lateinit var dataStore: DataStoreManager

    override val viewModel: BookmarkViewModel by viewModels()

    override fun setUpViews(savedInstanceState: Bundle?) {
        super.setUpViews(savedInstanceState)
        binding.vm = viewModel
    }

    override fun setUpObservers() {
        super.setUpObservers()
        observe(viewModel.uiSingleEvent) {
//            when (it) {
//            }
        }
    }
}