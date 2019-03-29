package com.example.testjetpack

object MainApplicationContract {
    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    const val DEFAULT_UI_DELAY: Long = 300L
    const val START_DELAY_MILLIS: Long = 5000L

    const val DATABASE_NAME = BuildConfig.DB_NAME

    const val API_BASE_URL = BuildConfig.API_BASE_URL
    const val API_GIT_URL = BuildConfig.API_GIT_URL

    const val DEFAULT_NETWORK_PAGE_SIZE = 10

}
