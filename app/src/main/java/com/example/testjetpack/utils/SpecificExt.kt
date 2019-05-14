package com.example.testjetpack.utils

import android.location.Location
import android.os.Build
import com.example.testjetpack.dataflow.gps.acceleration_extra
import com.example.testjetpack.dataflow.gps.satellites_extra
import com.example.testjetpack.models.git.License
import com.example.testjetpack.models.git.User
import com.example.testjetpack.models.git.network.request.GitPage
import com.example.testjetpack.models.git.network.response.GitRepository
import kotlin.math.min

fun Int.getRestTimePV() = min(this.getPercentageValue(5), 1F)
fun Int.getRespiratoryRatePV() = min(this.getPercentageValue(40), 1F)
fun Int.getHeartRatePV() = min(this.getPercentageValue(120), 1F)
fun Int.getSleepTimePV() = min(this.getPercentageValue(10), 1F)
fun Int.getExercisesTimePV() = min(this.getPercentageValue(2), 1F)
fun Int.getEatCaloriesPV() = min(this.getPercentageValue(2500), 1F)

fun GitPage.reset(page: Int = 1) = this.copy(
    page = page,
    previous = null,
    next = null,
    last = null,
    first = null,
    isLast = null
)

fun GitRepository.toDBEntity(license: License?, owner: User, indexInResponse: Int) =
    com.example.testjetpack.models.git.db.GitRepository(
        stargazersCount = stargazersCount,
        pushedAt = pushedAt,
        subscriptionUrl = subscriptionUrl,
        language = language,
        branchesUrl = branchesUrl,
        issueCommentUrl = issueCommentUrl,
        labelsUrl = labelsUrl,
        score = score,
        subscribersUrl = subscribersUrl,
        releasesUrl = releasesUrl,
        svnUrl = svnUrl,
        id = id,
        forks = forks,
        archiveUrl = archiveUrl,
        gitRefsUrl = gitRefsUrl,
        forksUrl = forksUrl,
        statusesUrl = statusesUrl,
        sshUrl = sshUrl,
        licenseName = license?.name,
        fullName = fullName,
        size = size,
        languagesUrl = languagesUrl,
        htmlUrl = htmlUrl,
        collaboratorsUrl = collaboratorsUrl,
        cloneUrl = cloneUrl,
        name = name,
        pullsUrl = pullsUrl,
        defaultBranch = defaultBranch,
        hooksUrl = hooksUrl,
        treesUrl = treesUrl,
        tagsUrl = tagsUrl,
        privateRepo = privateRepo,
        contributorsUrl = contributorsUrl,
        hasDownloads = hasDownloads,
        notificationsUrl = notificationsUrl,
        openIssuesCount = openIssuesCount,
        description = description,
        createdAt = createdAt,
        watchers = watchers,
        keysUrl = keysUrl,
        deploymentsUrl = deploymentsUrl,
        hasProjects = hasProjects,
        archived = archived,
        hasWiki = hasWiki,
        updatedAt = updatedAt,
        commentsUrl = commentsUrl,
        stargazersUrl = stargazersUrl,
        gitUrl = gitUrl,
        hasPages = hasPages,
        ownerId = owner.id,
        commitsUrl = commitsUrl,
        compareUrl = compareUrl,
        gitCommitsUrl = gitCommitsUrl,
        blobsUrl = blobsUrl,
        gitTagsUrl = gitTagsUrl,
        mergesUrl = mergesUrl,
        downloadsUrl = downloadsUrl,
        hasIssues = hasIssues,
        url = url,
        contentsUrl = contentsUrl,
        mirrorUrl = mirrorUrl,
        milestonesUrl = milestonesUrl,
        teamsUrl = teamsUrl,
        fork = fork,
        issuesUrl = issuesUrl,
        eventsUrl = eventsUrl,
        issueEventsUrl = issueEventsUrl,
        assigneesUrl = assigneesUrl,
        openIssues = openIssues,
        watchersCount = watchersCount,
        nodeId = nodeId,
        homepage = homepage,
        forksCount = forksCount,
        indexInResponse = indexInResponse
    )

fun Location.toDBEntity(startTime: Long): com.example.testjetpack.models.own.Location {

    var acceleration = -1F
    var satellites = -1

    withNotNull(extras){
        acceleration = getFloat(acceleration_extra, acceleration)
        satellites = getInt(satellites_extra, satellites)
    }

    return com.example.testjetpack.models.own.Location(
        startTime = startTime,
        altitude = altitude,
        latitude = latitude,
        longitude = longitude,
        bearing = bearing,
        speed = speed,
        accuracy = accuracy,
        verticalAccuracyMeters = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) verticalAccuracyMeters else -1F,
        speedAccuracyMetersPerSecond = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) speedAccuracyMetersPerSecond else -1F,
        bearingAccuracyDegrees = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) bearingAccuracyDegrees else -1F,
        time = time,
        elapsedRealtimeNanos = elapsedRealtimeNanos,
        acceleration = acceleration,
        satellites = satellites
    )
}