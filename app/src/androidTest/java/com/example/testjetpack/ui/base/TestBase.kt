package com.example.testjetpack.ui.base

import com.example.testjetpack.TestingContract.START_DELAY_MILLIS
import org.junit.Before


abstract class TestBase {

    @Before
    open fun before() {
        Thread.sleep(START_DELAY_MILLIS)
    }

}