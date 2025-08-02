package com.odensala.asr.speechrecognition.data.datasource

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import androidx.core.app.ActivityCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.max


@Singleton
class AudioRecorderDataSourceImpl @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val chunkSizeInBytes: Int = DEFAULT_CHUNK_SIZE,
    @ApplicationContext private val context: Context
) : AudioRecorderDataSource {
    companion object {
        // mimi ASR API requirements
        private const val SAMPLE_RATE = 16000
        private const val CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO
        private const val AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT
        private const val DEFAULT_CHUNK_SIZE: Int = 1024
    }

    private var audioRecord: AudioRecord? = null
    private var recordingJob: Job? = null

    private val _isRecording = MutableStateFlow(false)
    override val isRecording: StateFlow<Boolean> = _isRecording.asStateFlow()

    override fun startRecording(): Flow<ByteArray> = callbackFlow {

        val internalBuffer = max(
            AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT),
            chunkSizeInBytes
        )

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            close(SecurityException("Permission not granted"))
            return@callbackFlow
        }
        audioRecord = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            SAMPLE_RATE,
            CHANNEL_CONFIG,
            AUDIO_FORMAT,
            internalBuffer
        )

        if(audioRecord?.state != AudioRecord.STATE_INITIALIZED) {
            throw IllegalStateException("AudioRecord is not initialized")
        }

        audioRecord?.startRecording()
        _isRecording.value = true

        recordingJob = launch(ioDispatcher) {
            val buffer = ByteArray(chunkSizeInBytes)
            while (isActive && isRecording.value) {
                val bytesRead = audioRecord?.read(buffer, 0, buffer.size) ?: 0
                when {
                    bytesRead > 0 -> trySend(buffer.copyOf(bytesRead))
                    bytesRead == 0 -> Unit // nothing yet
                    else -> cancel("Audio read error ($bytesRead)")
                }
            }
        }

        awaitClose {
            stopRecording()
            close()
        }
    }

    /**
     * Stop recording audio
     */
    override fun stopRecording() {
        if (!isRecording.value) return

        _isRecording.value = false
        recordingJob?.cancel()
        recordingJob = null

        audioRecord?.run {
            if (recordingState == AudioRecord.RECORDSTATE_RECORDING) {
                stop()
            }
            release()
        }

        audioRecord = null

        Timber.d("Recording stopped")
    }
}