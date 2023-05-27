package app.mbl.hcmute.chatApp.ui.features.scan_cropper

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import app.mbl.hcmute.base.common.BaseVmDbFragment
import app.mbl.hcmute.chatApp.R
import app.mbl.hcmute.chatApp.databinding.FragmentResultCropBinding


class ResultCropFragment : BaseVmDbFragment<SharedViewModel, FragmentResultCropBinding>() {
    override fun getLayoutId() = R.layout.fragment_result_crop

    override val viewModel: SharedViewModel
        get() = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

    override fun setUpObservers() {
        super.setUpObservers()
        viewModel.resultCrop.observe(viewLifecycleOwner, Observer {
            binding.resultCrop.setImageBitmap(it)
        })
    }
}