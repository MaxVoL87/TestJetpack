package com.example.testjetpack

object MainApplicationContract {
    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    const val DEFAULT_UI_DELAY: Long = 300L
    const val START_DELAY_MILLIS: Long = 5000L
}