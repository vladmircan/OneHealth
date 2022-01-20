package com.example.onehealth.app.utils

import com.example.onehealth.R
import com.example.onehealth.domain.core.Failure
import com.example.onehealth.domain.model.local.MeasurementType

val MeasurementType.labelStringId: Int
    get() {
        return when (this) {
            MeasurementType.BODY_WEIGHT -> R.string.body_weight
            MeasurementType.BODY_FAT_PERCENTAGE -> R.string.body_fat_percentage
        }
    }

val MeasurementType.unitCodeStringId: Int
    get() {
        return when (this) {
            MeasurementType.BODY_WEIGHT -> R.string.body_weight_unit_code
            MeasurementType.BODY_FAT_PERCENTAGE -> R.string.body_fat_percentage_unit_code
        }
    }

val Failure.userDisplayMessageId: Int?
    get() {
        return when (this) {
            is Failure.GenericFailure -> R.string.generic_error_toast_message
            Failure.NetworkConnectionFailure -> R.string.network_connection_error_toast_message
            Failure.TimeoutFailure -> R.string.timeout_error_toast_message
            Failure.MeasurementFailure.NumberFormat -> R.string.number_format_error_toast_message
            Failure.MeasurementFailure.MeasurementValueOutsideRange -> R.string.measurement_value_out_of_range
            Failure.AuthFailure.InvalidCredentials -> R.string.invalid_credentials
            Failure.AuthFailure.InvalidEmailFormat -> R.string.invalid_email_format
            Failure.AuthFailure.InvalidPasswordLength -> R.string.invalid_password_length
            Failure.AuthFailure.InvalidPasswordFormat -> R.string.invalid_password_format
            Failure.AuthFailure.PasswordsDoNotMatch -> R.string.passwords_do_not_match
            else -> null
        }
    }