package com.kalex.bookyouu_notesapp.journal.journalEntry.presentation

import android.media.MediaRecorder
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import java.io.IOException

class AudioJournalEntryViewModel(
    private val mediaRecorder: MediaRecorder
) : ViewModel() {

    val canStopRecord = mutableStateOf(false)
    private val currentPath = mutableStateOf("")

    fun beginAudioRecord() {
        mediaRecorder.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setAudioSamplingRate(44100)
            setOutputFile(currentPath.value)
            try {
                prepare()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            start()
        }
        canStopRecord.value = true
    }

    fun stopRecording() {
        mediaRecorder.apply {
            try {
                stop()
                reset()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        canStopRecord.value = false
    }

    fun getCurrentPath() = currentPath.value

    fun setCurrentPathToSave(pathToSave: String) {
        currentPath.value = pathToSave
    }
}
