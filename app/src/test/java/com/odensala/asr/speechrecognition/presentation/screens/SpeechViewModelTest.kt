package com.odensala.asr.speechrecognition.presentation.screens

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.odensala.MainCoroutineTestRule
import com.odensala.asr.keywords.domain.usecase.DetectKeywordsUseCase
import com.odensala.asr.speechrecognition.domain.model.AsrState
import com.odensala.asr.speechrecognition.domain.repository.AsrRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SpeechViewModelTest {

    @get:Rule
    val mainCoroutineTestRule = MainCoroutineTestRule()

    @MockK
    private lateinit var asrRepository: AsrRepository

    @MockK
    private lateinit var detectKeywordsUseCase: DetectKeywordsUseCase

    private lateinit var viewModel: SpeechViewModel
    private lateinit var asrFlow: MutableSharedFlow<AsrState>

    @get:Rule
    val mockkRule = MockKRule(this)

    @Before
    fun setup() {
        asrFlow = MutableSharedFlow()
        every { asrRepository.observeAsr() } returns asrFlow
        coEvery { detectKeywordsUseCase(any()) } returns emptyList()
        
        viewModel = SpeechViewModel(asrRepository, detectKeywordsUseCase)
    }

    @Test
    fun `when recording starts and connects successfully, then ui state shows recording`() = runTest {
        viewModel.uiState.test {

            val initialState = awaitItem()
            assertThat(initialState.isLoading).isFalse()
            assertThat(initialState.isRecording).isFalse()

            viewModel.toggleRecording()

            // Should show loading state
            val loadingState = awaitItem()
            assertThat(loadingState.isLoading).isTrue()
            assertThat(loadingState.isRecording).isFalse()

            asrFlow.emit(AsrState.Connected)

            val connectedState = awaitItem()
            assertThat(connectedState.isLoading).isFalse()
            assertThat(connectedState.isRecording).isTrue()
        }
    }

    @Test
    fun `when recording stops, then ui state shows not recording`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            // Start recording
            viewModel.toggleRecording()
            awaitItem()
            
            asrFlow.emit(AsrState.Connected)
            awaitItem()

            // Stop recording
            viewModel.toggleRecording()

            // Should emit disconnected state automatically
            val stoppedState = awaitItem()
            assertThat(stoppedState.isRecording).isFalse()
            assertThat(stoppedState.isLoading).isFalse()
        }
    }
}