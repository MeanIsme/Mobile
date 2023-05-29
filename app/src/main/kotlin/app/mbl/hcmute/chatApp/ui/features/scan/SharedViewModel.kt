package app.mbl.hcmute.chatApp.ui.features.scan

import android.Manifest
import android.graphics.Bitmap
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import app.mbl.hcmute.base.common.BaseViewModel
import app.mbl.hcmute.chatApp.R
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor() : BaseViewModel() {
    private val _croppedImage = MutableLiveData<Bitmap>()
    val croppedImage: LiveData<Bitmap> = _croppedImage
    fun setCroppedImage(bitmap: Bitmap) {
        _croppedImage.postValue(bitmap)
    }

    val sourceText = MutableLiveData<String>()
    val imageCropPercentages = MutableLiveData<Pair<Int, Int>>()
        .apply { value = Pair(DESIRED_HEIGHT_CROP_PERCENT, DESIRED_WIDTH_CROP_PERCENT) }

    override fun onClick(view: View) {
        super.onClick(view)
        when (view.id) {
            R.id.btnCaptureImage -> _clickEvent.postValue(ImageUIState.CaptureImage)
            R.id.btnCrop -> _clickEvent.postValue(ImageUIState.CropImage)
        }
    }

    companion object {
        const val DESIRED_WIDTH_CROP_PERCENT = 8
        const val DESIRED_HEIGHT_CROP_PERCENT = 74
    }
}