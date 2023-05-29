package app.mbl.hcmute.chatApp.ui.features.scan

import android.graphics.Bitmap
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import app.mbl.hcmute.base.common.BaseVmDbFragment
import app.mbl.hcmute.chatApp.R
import app.mbl.hcmute.chatApp.databinding.FragmentResultCropBinding
import com.google.mlkit.codelab.translate.analyzer.TextAnalyzer
import com.google.mlkit.vision.common.InputImage


class ResultCropFragment : BaseVmDbFragment<SharedViewModel, FragmentResultCropBinding>() {
    override fun getLayoutId() = R.layout.fragment_result_crop

    override val viewModel: SharedViewModel by activityViewModels()

    override fun setUpObservers() {
        super.setUpObservers()
        viewModel.croppedImage.observe(viewLifecycleOwner, Observer {
            binding.croppedResult.setImageBitmap(it)
            textRecognition(it)
        })
    }

    private fun textRecognition(bitmap: Bitmap) {
        val textAnalyzer = TextAnalyzer(
            requireContext(),
            lifecycle,
            viewModel.sourceText,
        )
        textAnalyzer.recognizeText(InputImage.fromBitmap(bitmap, 0))
        viewModel.sourceText.observe(viewLifecycleOwner, Observer { binding.etSannedText.setText(it) })
    }
}