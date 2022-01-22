package com.example.onehealth.data

import com.example.onehealth.data.database.MeasurementDao
import com.example.onehealth.data.database.entities.MeasurementEntity
import com.example.onehealth.domain.model.local.MeasurementType
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.hasSize
import strikt.assertions.isEqualTo
import javax.inject.Inject
import javax.inject.Named

@HiltAndroidTest
class DatabaseMeasurementTests {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    @Named("testMeasurementsDao")
    lateinit var sut: MeasurementDao

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun savingMeasurementInDatabaseSucceeds() {
        val userId = "userId"
        val measurementType = MeasurementType.BODY_WEIGHT
        val insertedMeasurements = listOf(
            MeasurementEntity(
                id = "measurementId",
                value = 32.3,
                measurementType = measurementType,
                timeStamp = System.currentTimeMillis(),
                userId = userId
            )
        )

        sut.addMeasurements(insertedMeasurements)

        val retrievedMeasurements = sut.getMeasurements(
            userId = userId,
            measurementType = measurementType,
            periodStartTime = 0,
            periodEndTime = System.currentTimeMillis(),
            maxNumberOfMeasurementsToBeRetrieved = 100
        )

        expectThat(retrievedMeasurements).isEqualTo(insertedMeasurements)
    }

    @Test
    fun measurementsAreRetrievedByUserId() {
        val userId = "userId"
        val measurementType = MeasurementType.BODY_WEIGHT
        val measurementEntity = MeasurementEntity(
            id = "measurementId",
            value = 32.3,
            measurementType = measurementType,
            timeStamp = System.currentTimeMillis(),
            userId = userId
        )
        val insertedMeasurements = listOf(
            measurementEntity,
            measurementEntity.copy(id = "differentMeasurementId", userId = "anotherUserId")
        )

        sut.addMeasurements(insertedMeasurements)

        val retrievedMeasurements = sut.getMeasurements(
            userId = userId,
            measurementType = measurementType,
            periodStartTime = 0,
            periodEndTime = System.currentTimeMillis(),
            maxNumberOfMeasurementsToBeRetrieved = 100
        )

        expectThat(retrievedMeasurements).isEqualTo(listOf(measurementEntity))
    }

    @Test
    fun measurementRetrievalRespectsTimeConstraints() {
        val userId = "userId"
        val measurementType = MeasurementType.BODY_WEIGHT
        val measurementEntity = MeasurementEntity(
            id = "measurementId",
            value = 32.3,
            measurementType = measurementType,
            timeStamp = System.currentTimeMillis(),
            userId = userId
        )
        val insertedMeasurements = listOf(
            measurementEntity,
            measurementEntity.copy(
                id = "differentMeasurementId",
                timeStamp = 0
            )
        )

        sut.addMeasurements(insertedMeasurements)

        val retrievedMeasurements = sut.getMeasurements(
            userId = userId,
            measurementType = measurementType,
            periodStartTime = 1,
            periodEndTime = System.currentTimeMillis(),
            maxNumberOfMeasurementsToBeRetrieved = 100
        )

        expectThat(retrievedMeasurements).isEqualTo(listOf(measurementEntity))
    }

    @Test
    fun successfullyRetrievesMostRecentMeasurement() {
        val userId = "userId"
        val measurementType = MeasurementType.BODY_FAT_PERCENTAGE
        val measurementEntity = MeasurementEntity(
            id = "measurementId",
            value = 32.3,
            measurementType = measurementType,
            timeStamp = 1,
            userId = userId
        )
        val insertedMeasurements = listOf(
            measurementEntity,
            measurementEntity.copy(
                id = "anotherMeasurementId",
                timeStamp = measurementEntity.timeStamp - 1
            ),
            measurementEntity.copy(
                id = "differentMeasurementId",
                userId = "anotherUserId",
                timeStamp = measurementEntity.timeStamp + 1
            )
        )

        sut.addMeasurements(insertedMeasurements)

        val mostRecentMeasurement = sut.getMostRecentMeasurement(userId)

        expectThat(mostRecentMeasurement).isEqualTo(measurementEntity)
    }

    @Test
    fun limitingMeasurementsRetrievedWorks() {
        val userId = "userId"
        val measurementType = MeasurementType.BODY_FAT_PERCENTAGE
        val measurementEntity = MeasurementEntity(
            id = "measurementId",
            value = 32.3,
            measurementType = measurementType,
            timeStamp = 1,
            userId = userId
        )
        val insertedMeasurements = listOf(
            measurementEntity,
            measurementEntity.copy(id = "anotherMeasurementId"),
            measurementEntity.copy(id = "differentMeasurementId")
        )

        sut.addMeasurements(insertedMeasurements)

        val retrievedMeasurements = sut.getMeasurements(
            userId = userId,
            measurementType = measurementType,
            periodStartTime = 0,
            periodEndTime = System.currentTimeMillis(),
            maxNumberOfMeasurementsToBeRetrieved = 2
        )

        expectThat(retrievedMeasurements).hasSize(expected = 2)
    }

    @Test
    fun flowingLastMeasurementsWorks() {
        runBlocking {
            val userId = "userId"
            val measurementType = MeasurementType.BODY_WEIGHT
            val measurementEntity = MeasurementEntity(
                id = "measurementId",
                value = 32.3,
                measurementType = measurementType,
                timeStamp = System.currentTimeMillis(),
                userId = userId
            )
            val expected = listOf(
                measurementEntity,
                measurementEntity.copy(id = "anotherMeasurementId"),
            )

            sut.addMeasurements(expected)
            sut.addMeasurements(
                listOf(
                    measurementEntity.copy(
                        id = "differentMeasurementId",
                        userId = "anotherUserId"
                    )
                )
            )

            val retrievedMeasurements = sut.flowLastMeasurements(
                userId = userId,
                measurementType = measurementType,
                numberOfMeasurementsToBeRetrieved = 10
            ).first()

            expectThat(expected).isEqualTo(
                expected = retrievedMeasurements
            )
        }
    }
}