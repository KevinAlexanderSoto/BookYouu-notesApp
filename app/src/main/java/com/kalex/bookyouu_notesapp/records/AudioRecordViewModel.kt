package com.kalex.bookyouu_notesapp.records

import android.media.MediaRecorder
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class AudioRecordViewModel @Inject constructor() : ViewModel() {

    val canStopRecord = mutableStateOf(false)
    private val currentPath = mutableStateOf("")
    private var recorder: MediaRecorder? = null
    fun beginAudioRecord() {
// Set the audio source, output format, encoder and output file
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setAudioSamplingRate(44100)
            setOutputFile(currentPath.value)
            try {
                prepare()
            } catch (e: IOException) {
            }

            start()
        }

        canStopRecord.value = true
    }

    fun stopRecording() {
        // Stop and release the MediaRecorder when done
        recorder?.apply {
            stop()
            release()
        }
        recorder = null
        canStopRecord.value = false
    }

    fun getCurrentPath() = currentPath.value

    fun setCurrentPathToSave(pathToSave: String) {
        currentPath.value = pathToSave
    }
}
