package xyz.teamgravity.retrofitfileupload.presentation.model

data class UploadAttachment(
    val uiType: UiType = UiType.INITIAL,
    val progress: Float = 0f,
    val onCancel: (() -> Unit)? = null,
)
enum class UiType {
    INITIAL, LOADING, SUCCESS, ERROR
}