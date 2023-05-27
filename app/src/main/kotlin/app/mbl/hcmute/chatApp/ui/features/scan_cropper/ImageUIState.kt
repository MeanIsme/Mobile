package app.mbl.hcmute.chatApp.ui.features.scan_cropper

import app.mbl.hcmute.base.common.UIState

sealed class ImageUIState : UIState {
    object CaptureImage : ImageUIState()
    object CropImage : ImageUIState()
}