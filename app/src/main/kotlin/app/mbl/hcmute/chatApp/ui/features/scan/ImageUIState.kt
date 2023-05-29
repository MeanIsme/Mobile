package app.mbl.hcmute.chatApp.ui.features.scan

import app.mbl.hcmute.base.common.UIState

sealed class ImageUIState : UIState {
    object CaptureImage : ImageUIState()
    object CropImage : ImageUIState()
    object SendScanText : ImageUIState()

}