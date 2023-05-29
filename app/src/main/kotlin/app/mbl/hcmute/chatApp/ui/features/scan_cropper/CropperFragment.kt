package app.mbl.hcmute.chatApp.ui.features.scan_cropper

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.lifecycle.Observer
import app.mbl.hcmute.base.common.BaseVmDbFragment
import app.mbl.hcmute.chatApp.R
import app.mbl.hcmute.chatApp.databinding.FragmentCropperBinding
import app.mbl.hcmute.chatApp.di.module.navigationModule.AppNavigator
import com.canhub.cropper.CropImageView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CropperFragment : BaseVmDbFragment<SharedViewModel, FragmentCropperBinding>() {
    override fun getLayoutId() = R.layout.fragment_cropper
    override val viewModel: SharedViewModel
        get() = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

    @Inject
    lateinit var navigator: AppNavigator

    override fun setUpViews(savedInstanceState: Bundle?) {
        super.setUpViews(savedInstanceState)
        binding.vm = viewModel
    }

    override fun setUpObservers() {
        super.setUpObservers()
        viewModel.photoUri.observe(viewLifecycleOwner, Observer {
            binding.cropImageView.setImageUriAsync(it)
        })
        viewModel.clickEvent.observe(viewLifecycleOwner) {
            when (it) {
                is ImageUIState.CropImage -> {
                    binding.cropImageView.getCroppedImage()?.let { it1 -> viewModel.setResultCrop(it1) }
                    navigator.navigateTo(CropperFragmentDirections.actionCropperFragmentToResultCropFragment())
                }
            }
        }
    }
}