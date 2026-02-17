package com.example.sporthub.data.health

import android.content.Context
import android.util.Log
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.ActiveCaloriesBurnedRecord
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.OxygenSaturationRecord
import androidx.health.connect.client.records.SleepSessionRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.request.AggregateRequest
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import androidx.health.connect.client.units.Energy
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId

class HealthState(private val context: Context) {

    private val healthConnectClient by lazy {
        HealthConnectClient.getOrCreate(context)
    }

    val permissions = setOf(
        HealthPermission.getReadPermission(StepsRecord::class),
        HealthPermission.getReadPermission(SleepSessionRecord::class),
        HealthPermission.getReadPermission(HeartRateRecord::class),
        HealthPermission.getReadPermission(OxygenSaturationRecord::class),
        HealthPermission.getReadPermission(ActiveCaloriesBurnedRecord::class),
    )

    suspend fun checkPermissions(): Boolean {
        val granted = healthConnectClient.permissionController.getGrantedPermissions()
        return granted.containsAll(permissions)
    }

    suspend fun readStepsToday(): Long {
        val startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0)
        val endOfDay = LocalDateTime.now()

        val response = healthConnectClient.aggregate(
            AggregateRequest(
                metrics = setOf(StepsRecord.COUNT_TOTAL),
                timeRangeFilter = TimeRangeFilter.between(
                    startOfDay.atZone(ZoneId.systemDefault()).toInstant(),
                    endOfDay.atZone(ZoneId.systemDefault()).toInstant()
                )
            )
        )
        return response[StepsRecord.COUNT_TOTAL] ?: 0L
    }

    suspend fun readSleepToday(): Long {
        val startOfDay = LocalDateTime.now().minusDays(1)
        val endOfDay = LocalDateTime.now()

        val response = healthConnectClient.aggregate(
            AggregateRequest(
                metrics = setOf(SleepSessionRecord.SLEEP_DURATION_TOTAL),
                timeRangeFilter = TimeRangeFilter.between(
                    startOfDay.atZone(ZoneId.systemDefault()).toInstant(),
                    endOfDay.atZone(ZoneId.systemDefault()).toInstant()
                )
            )
        )

        val duration = response[SleepSessionRecord.SLEEP_DURATION_TOTAL] ?: Duration.ZERO
        return duration.toMinutes()
    }

    suspend fun readHeartToday(): Long {
        val startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0)
        val endOfDay = LocalDateTime.now()

        val response = healthConnectClient.readRecords(
            ReadRecordsRequest(
                recordType = HeartRateRecord::class,
                timeRangeFilter = TimeRangeFilter.between(
                    startOfDay.atZone(ZoneId.systemDefault()).toInstant(),
                    endOfDay.atZone(ZoneId.systemDefault()).toInstant()
                ),
                ascendingOrder = false,
                pageSize = 1
            )
        )

        val lastRecord = response.records.firstOrNull()
        return lastRecord?.samples?.lastOrNull()?.beatsPerMinute ?: 0L
    }

    suspend fun readOxygenToday(): Int {
        val startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0)
        val endOfDay = LocalDateTime.now()

        val response = healthConnectClient.readRecords(
            ReadRecordsRequest(
                recordType = OxygenSaturationRecord::class,
                timeRangeFilter = TimeRangeFilter.between(
                    startOfDay.atZone(ZoneId.systemDefault()).toInstant(),
                    endOfDay.atZone(ZoneId.systemDefault()).toInstant()
                ),
                ascendingOrder = false,
                pageSize = 1
            )
        )

        return response.records.firstOrNull()?.percentage?.value?.toInt() ?: 0
    }

    suspend fun readCalories(steps: Long, weight: Float?): Energy {
        val startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0)
        val endOfDay = LocalDateTime.now()

        return try {
            val response = healthConnectClient.aggregate(
                AggregateRequest(
                    metrics = setOf(ActiveCaloriesBurnedRecord.ACTIVE_CALORIES_TOTAL),
                    timeRangeFilter = TimeRangeFilter.between(
                        startOfDay.atZone(ZoneId.systemDefault()).toInstant(),
                        endOfDay.atZone(ZoneId.systemDefault()).toInstant()
                    )
                )
            )

            val calories = response[ActiveCaloriesBurnedRecord.ACTIVE_CALORIES_TOTAL]

            if (calories != null && calories.inKilocalories > 0) {
                calories
            } else {
                calcCalories(steps, weight)
            }
        } catch (e: Exception) {
            Log.d("MyLog", "Нет доступа к активности с устройства", e)
            calcCalories(steps, weight)
        }
    }
    
    private fun calcCalories(steps: Long, weight: Float?) : Energy {
        val burnedKcal = steps * (weight ?: 70f) * 0.0005f
        return Energy.kilocalories(burnedKcal.toDouble())
    }
}