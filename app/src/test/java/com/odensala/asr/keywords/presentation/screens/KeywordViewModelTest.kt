package com.odensala.asr.keywords.presentation.screens

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.odensala.MainCoroutineTestRule
import com.odensala.asr.keywords.domain.model.Keyword
import com.odensala.asr.keywords.domain.repository.KeywordRepository
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
class KeywordViewModelTest {

    @get:Rule
    val mainCoroutineTestRule = MainCoroutineTestRule()

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    private lateinit var keywordRepository: KeywordRepository

    private lateinit var viewModel: KeywordViewModel
    private lateinit var keywordsFlow: MutableSharedFlow<List<Keyword>>

    @Before
    fun setup() {
        keywordsFlow = MutableSharedFlow()
        every { keywordRepository.getAllKeywords() } returns keywordsFlow
        coEvery { keywordRepository.insertKeyword(any()) } returns Unit
        
        viewModel = KeywordViewModel(keywordRepository)
    }

    @Test
    fun `when keywords are loaded, then uiState shows keywords`() = runTest {
        val mockKeywords = listOf(
            Keyword(id = 1, keyword = "うなぎ", isActive = true),
            Keyword(id = 2, keyword = "中トロ", isActive = false)
        )

        viewModel.uiState.test {
            // Initial state
            val initialState = awaitItem()
            assertThat(initialState.isLoading).isTrue()

            // Emit keywords from repository
            keywordsFlow.emit(mockKeywords)

            // UI state should show the keywords
            val loadedState = awaitItem()
            assertThat(loadedState.isLoading).isFalse()
            assertThat(loadedState.keywords).hasSize(2)
            assertThat(loadedState.keywords[0].keyword).isEqualTo("うなぎ")
        }
    }

    @Test
    fun `when updating new keyword text, then uiState reflects the change`() = runTest {
        keywordsFlow.emit(emptyList())

        viewModel.updateNewKeywordText("寿司")

        assertThat(viewModel.uiState.value.newKeywordText).isEqualTo("寿司")
    }
}