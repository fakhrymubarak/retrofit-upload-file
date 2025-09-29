package xyz.teamgravity.retrofitfileupload.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import xyz.teamgravity.retrofitfileupload.presentation.model.UiType
import xyz.teamgravity.retrofitfileupload.presentation.viewmodel.ImageUploadViewModel

@Composable
fun ImageUploadScreen(
    viewModel: ImageUploadViewModel = hiltViewModel(),
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        val uploadState = viewModel.uploadState.collectAsState()
        uploadState.value.let { state ->
            when (state.uiType) {
                UiType.INITIAL -> {
                    Button(onClick = viewModel::onUpload) {
                        Text(text = "UPLOAD IMAGE")
                    }
                }
                UiType.LOADING -> {
                    Button(onClick = { state.onCancel?.invoke() }) {
                        Text(text = "CANCEL UPLOAD")
                    }

                    LinearProgressIndicator(progress = state.progress)
                    Text("${(state.progress * 100).toInt()} %")
                }
                UiType.SUCCESS -> {
                    Button(onClick = viewModel::onUpload) {
                        Text(text = "UPLOAD IMAGE")
                    }
                    Text("Success Upload Image")
                }
                UiType.ERROR -> {
                    Button(onClick = viewModel::onUpload) {
                        Text(text = "ERROR, REUPLOAD")
                    }
                }
            }
        }
    }
}