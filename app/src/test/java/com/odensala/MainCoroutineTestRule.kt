package com.odensala

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * MainCoroutineTestRule provides a test dispatcher to be used in unit tests.
 * This rule overrides the Main dispatcher with a test dispatcher that launches
 * coroutines used during the tests.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class MainCoroutineTestRule : TestWatcher() {

    val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()

    override fun starting(description: Description?) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description?) {
        Dispatchers.resetMain()
    }
}