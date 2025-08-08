package com.odensala.asr.keywords.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.odensala.MainCoroutineTestRule
import com.odensala.asr.keywords.domain.model.Keyword
import com.odensala.asr.keywords.domain.repository.KeywordRepository
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
class DetectKeywordsUseCaseTest {

    @get:Rule
    val mainCoroutineTestRule = MainCoroutineTestRule()

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    private lateinit var keywordRepository: KeywordRepository

    private lateinit var useCase: DetectKeywordsUseCase

    @Before
    fun setup() {
        useCase = DetectKeywordsUseCase(keywordRepository)
    }

    @Test
    fun `when text contains keywords, then returns matching keywords`() = runTest {
        val keywords = listOf(
            Keyword(id = 1, keyword = "うなぎ", isActive = true),
            Keyword(id = 2, keyword = "丼", isActive = true),
            Keyword(id = 3, keyword = "中トロ", isActive = true)
        )
        every { keywordRepository.getActiveKeywords() } returns flowOf(keywords)

        val inputText = "今日はうなぎ丼を食べました"

        val result = useCase(inputText)

        assertThat(result).hasSize(2)
        assertThat(result[0].keyword).isEqualTo("うなぎ")
        assertThat(result[1].keyword).isEqualTo("丼")
    }

    @Test
    fun `when text contains no keywords, then returns empty list`() = runTest {
        val keywords = listOf(
            Keyword(id = 1, keyword = "うなぎ", isActive = true),
            Keyword(id = 2, keyword = "中トロ", isActive = true)
        )
        every { keywordRepository.getActiveKeywords() } returns flowOf(keywords)

        val inputText = "今日はラーメンを食べました"

        val result = useCase(inputText)

        assertThat(result).isEmpty()
    }
}