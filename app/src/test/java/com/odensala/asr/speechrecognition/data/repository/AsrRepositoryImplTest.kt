package com.odensala.asr.speechrecognition.data.repository

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.odensala.MainCoroutineTestRule
import com.odensala.asr.speechrecognition.data.datasource.AudioRecorderDataSource
import com.odensala.asr.speechrecognition.data.datasource.auth.AuthDataSource
import com.odensala.asr.speechrecognition.data.datasource.websocket.AsrWebSocketDataSource
import com.odensala.asr.speechrecognition.data.datasource.websocket.WebSocketEvent
import com.odensala.asr.speechrecognition.domain.model.AsrState
import com.odensala.asr.speechrecognition.domain.model.Token
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AsrRepositoryImplTest {

    @get:Rule
    val mainCoroutineTestRule = MainCoroutineTestRule()

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    private lateinit var webSocket: AsrWebSocketDataSource

    @MockK
    private lateinit var audioRecorder: AudioRecorderDataSource

    @MockK
    private lateinit var authDataSource: AuthDataSource
    
    private lateinit var asrRepository: AsrRepositoryImpl

    @Before
    fun setup() {
        asrRepository = AsrRepositoryImpl(
            webSocket = webSocket,
            audioRecorder = audioRecorder,
            authDataSource = authDataSource,
            ioDispatcher = mainCoroutineTestRule.testDispatcher
        )
    }

    @Test
    fun `observeAsr returns connected state when authentication succeeds`() = runTest {
        val mockToken = Token("test-token")
        coEvery { authDataSource.requestAccessToken(any(), any(), any()) } returns Result.success(mockToken)
        every { webSocket.startAsr("test-token") } returns flowOf(WebSocketEvent.Connected)
        every { audioRecorder.startRecording() } returns flowOf(byteArrayOf(1, 2, 3))

        asrRepository.observeAsr().test {
            val state = awaitItem()
            assertThat(state).isEqualTo(AsrState.Connected)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `observeAsr returns error when authentication fails`() = runTest {
        val authError = IllegalStateException("Auth failed")
        coEvery { authDataSource.requestAccessToken(any(), any(), any()) } returns Result.failure(authError)

        asrRepository.observeAsr().test {
            val state = awaitItem()
            assertThat(state).isInstanceOf(AsrState.Error::class.java)
            cancelAndIgnoreRemainingEvents()
        }
    }
}