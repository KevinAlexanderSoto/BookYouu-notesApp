package com.kalex.bookyouu_notesapp.records

import android.media.MediaRecorder
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AudioRecordViewModel @Inject constructor(
    private val recorder: MediaRecorder,
) : ViewModel() {

    val canStopRecord = mutableStateOf(false)
    private val currentPath = mutableStateOf("")

    fun beginAudioRecord() {
// Set the audio source, output format, encoder and output file
        recorder.apply {
            setAudioSource(MediaRecorder.AudioSource.DEFAULT)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(currentPath.value)
        }

        recorder.prepare()
        recorder.start()
        canStopRecord.value = true
    }

    fun stopRecording() {
        // Stop and release the MediaRecorder when done
        recorder.stop()
        recorder.release()
        canStopRecord.value = false
    }

    fun getCurrentPath() = currentPath.value

    fun setCurrentPathToSave(pathToSave: String) {
        currentPath.value = pathToSave
    }
}
