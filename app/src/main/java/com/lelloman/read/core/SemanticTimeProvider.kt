package com.lelloman.read.core

import com.lelloman.read.R
import com.lelloman.read.persistence.db.model.Source

class SemanticTimeProvider(
    private val timeProvider: TimeProvider,
    private val resourceProvider: ResourceProvider
) {

    private val minutesThreshold = (60 * 60) - 1
    private val hoursThreshold = (60 * 60 * 24) - 1

    fun getTimeQuantity(ms: Long): String {
        val totSeconds = ms / 1000
        val quantityStrings = resourceProvider.getStringArray(R.array.time_quantities)

        return arrayOf(
            totSeconds % 60,
            (totSeconds / 60) % 60,
            (totSeconds / 3600) % 24,
            (totSeconds / (3600 * 24))
        )
            .mapIndexed { index, quantity ->
                quantity to quantityStrings[index].format(quantity)
            }
            .filter { (quantity, string) ->
                quantity > 0
            }
            .joinToString(" ") { (_, string) -> string }
    }

    private fun getTimeDiffString(timeUtcMs: Long): String {
        var delta = (timeProvider.nowUtcMs() - timeUtcMs) / 1000
        if (delta < 0) {
            delta = 0
        }

        val stringId: Int
        val value: Long

        when {
            delta <= 59 -> {
                stringId = R.string.time_diff_seconds
                value = delta
            }
            delta <= minutesThreshold -> {
                stringId = R.string.time_diff_minutes
                value = delta / 60
            }
            delta <= hoursThreshold -> {
                stringId = R.string.time_diff_hours
                value = delta / 3600
            }
            else -> {
                stringId = R.string.time_diff_days
                value = delta / (3600 * 24)
            }
        }

        return resourceProvider.getString(stringId, value)
    }

    fun getSourceLastFetchedString(source: Source): String {
        val lastFetchedValue = if (source.lastFetched <= 0L) {
            resourceProvider.getString(R.string.never)
        } else {
            getTimeDiffString(source.lastFetched)
        }
        return resourceProvider.getString(R.string.last_refresh, lastFetchedValue)
    }
}