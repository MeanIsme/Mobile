package app.mbl.hcmute.chatApp.ui.features.scan_cropper

import android.graphics.Bitmap
import android.net.Uri
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import app.mbl.hcmute.base.common.BaseViewModel
import app.mbl.hcmute.chatApp.R
import app.mbl.hcmute.chatApp.ui.features.chat.ChatUiState

class SharedViewModel : BaseViewModel() {
    private val _photoUri = MutableLiveData<Uri>()
    val photoUri: LiveData<Uri> = _photoUri
    fun setPhotoUri(uri: Uri) {
        _photoUri.value = uri
    }

    private val _resultCrop = MutableLiveData<Bitmap>()
    val resultCrop: LiveData<Bitmap> = _resultCrop
    fun setResultCrop(bitmap: Bitmap) {
        _resultCrop.value = bitmap
    }

    override fun onClick(view: View) {
        super.onClick(view)

        when (view.id) {
            R.id.btnCaptureImage -> _clickEvent.postValue(ImageUIState.CaptureImage)
            R.id.btnCrop -> _clickEvent.postValue(ImageUIState.CropImage)
        }
    }
}