package com.example.testjetpack.utils

import android.location.Location
import android.os.Build
import com.example.testjetpack.models.git.License
import com.example.testjetpack.models.git.User
import com.example.testjetpack.models.git.network.request.GitPage
import com.example.testjetpack.models.git.network.response.GitRepository

fun GitPage.reset(page: Int = 1) =
    this.copy(page = page, previous = null, next = null, last = null, first = null, isLast = null)

fun GitRepository.toDBEntity(license: License?, owner: User, indexInResponse: Int) =
    com.example.testjetpack.models.git.db.GitRepository(
        stargazersCount,
        pushedAt,
        subscriptionUrl,
        language,
        branchesUrl,
        issueCommentUrl,
        labelsUrl,
        score,
        subscribersUrl,
        releasesUrl,
        svnUrl,
        id,
        forks,
        archiveUrl,
        gitRefsUrl,
        forksUrl,
        statusesUrl,
        sshUrl,
        license?.name,
        fullName,
        size,
        languagesUrl,
        htmlUrl,
        collaboratorsUrl,
        cloneUrl,
        name,
        pullsUrl,
        defaultBranch,
        hooksUrl,
        treesUrl,
        tagsUrl,
        privateRepo,
        contributorsUrl,
        hasDownloads,
        notificationsUrl,
        openIssuesCount,
        description,
        createdAt,
        watchers,
        keysUrl,
        deploymentsUrl,
        hasProjects,
        archived,
        hasWiki,
        updatedAt,
        commentsUrl,
        stargazersUrl,
        gitUrl,
        hasPages,
        owner.id,
        commitsUrl,
        compareUrl,
        gitCommitsUrl,
        blobsUrl,
        gitTagsUrl,
        mergesUrl,
        downloadsUrl,
        hasIssues,
        url,
        contentsUrl,
        mirrorUrl,
        milestonesUrl,
        teamsUrl,
        fork,
        issuesUrl,
        eventsUrl,
        issueEventsUrl,
        assigneesUrl,
        openIssues,
        watchersCount,
        nodeId,
        homepage,
        forksCount,
        indexInResponse
    )

fun Location.toDBEntity(startTime: Long) = com.example.testjetpack.models.gps.Location(
    startTime,
    altitude,
    latitude,
    longitude,
    bearing,
    speed,
    time,
    accuracy,
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) verticalAccuracyMeters else -1F,
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) speedAccuracyMetersPerSecond else -1F,
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) bearingAccuracyDegrees else -1F,
    elapsedRealtimeNanos
)