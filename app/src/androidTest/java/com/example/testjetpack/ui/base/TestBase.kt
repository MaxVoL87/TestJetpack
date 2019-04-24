package com.example.testjetpack.ui.base

import org.junit.Before


abstract class TestBase {

    private val initSleepTime = 1700L

    @Before
    open fun setUp() {
        Thread.sleep(initSleepTime)
    }

}