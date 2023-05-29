package app.mbl.hcmute.chatApp.ui.features.scan

import android.graphics.Bitmap
import android.os.Bundle
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import app.mbl.hcmute.base.common.BaseVmDbFragment
import app.mbl.hcmute.chatApp.R
import app.mbl.hcmute.chatApp.databinding.FragmentResultCropBinding
import app.mbl.hcmute.chatApp.di.module.navigationModule.AppNavigator
import app.mbl.hcmute.chatApp.ui.features.chat.ChatUiState
import com.google.mlkit.codelab.translate.analyzer.TextAnalyzer
import com.google.mlkit.vision.common.InputImage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ResultCropFragment : BaseVmDbFragment<SharedViewModel, FragmentResultCropBinding>() {
    override fun getLayoutId() = R.layout.fragment_result_crop

    override val viewModel: SharedViewModel by activityViewModels()

    @Inject
    lateinit var navigator: AppNavigator

    override fun setUpViews(savedInstanceState: Bundle?) {
        super.setUpViews(savedInstanceState)
        binding.vm = viewModel
    }

    override fun setUpObservers() {
        super.setUpObservers()
        viewModel.croppedImage.observe(viewLifecycleOwner, Observer {
            binding.croppedResult.setImageBitmap(it)
            textRecognition(it)
        })
        viewModel.sourceText.observe(viewLifecycleOwner, Observer {
            binding.etScannedText.setText(it)
            viewModel.isScanCompleted.tryEmit(true)
        })

        viewModel.clickEvent.observe(viewLifecycleOwner) {
            when (it) {
                is ImageUIState.SendScanText -> {
                    binding.etScannedText.text.let { it ->
                        val message = binding.etOptionScript.text.toString() + ":\n" + it.toString()
                        navigator.navigateTo(ResultCropFragmentDirections.actionResultCropFragmentToChatAssistantFragment(message))
                    }
                }
            }
        }
    }

    private fun textRecognition(bitmap: Bitmap) {
        val textAnalyzer = TextAnalyzer(
            requireContext(),
            lifecycle,
            viewModel.sourceText,
        )
        textAnalyzer.recognizeText(InputImage.fromBitmap(bitmap, 0))
    }
}