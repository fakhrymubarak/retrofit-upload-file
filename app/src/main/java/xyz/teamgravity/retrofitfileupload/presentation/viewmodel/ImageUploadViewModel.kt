package xyz.teamgravity.retrofitfileupload.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import xyz.teamgravity.retrofitfileupload.core.util.FileProvider
import xyz.teamgravity.retrofitfileupload.data.repository.FileRepository
import xyz.teamgravity.retrofitfileupload.data.repository.NetworkResult
import xyz.teamgravity.retrofitfileupload.data.repository.ProgressFlowRequestBody
import xyz.teamgravity.retrofitfileupload.presentation.model.UiType
import xyz.teamgravity.retrofitfileupload.presentation.model.UploadAttachment
import javax.inject.Inject

@HiltViewModel
class ImageUploadViewModel @Inject constructor(
    private val repository: FileRepository,
    private val fileProvider: FileProvider,
) : ViewModel() {

    private val _uploadState = MutableStateFlow(UploadAttachment())
    val uploadState = _uploadState.asStateFlow()

    fun onUpload() {
        viewModelScope.launch(Dispatchers.IO) {
            _uploadState.emit(
                _uploadState.value.copy(
                    uiType = UiType.LOADING,
                    progress = 0f,
                    onCancel = {
                        cancel("Upload attachment cancelled by user.")
                    },
                )
            )

            val file = fileProvider.provide()
            val fileRequestBody = ProgressFlowRequestBody("multipart/form-data", file)

            // Launch other scope to collect progressFlow
            launch(Dispatchers.Main) {
                fileRequestBody.progressFlow.collect { progress ->
                    _uploadState.emit(
                        _uploadState.value.copy(uiType = UiType.LOADING, progress = progress / 100f)
                    )
                }
            }

            when (repository.uploadImage(fileRequestBody, file.name)) {
                is NetworkResult.Success -> _uploadState.emit(_uploadState.value.copy(uiType = UiType.SUCCESS))
                is NetworkResult.Error -> _uploadState.emit(_uploadState.value.copy(uiType = UiType.ERROR))
            }
        }
    }
}
