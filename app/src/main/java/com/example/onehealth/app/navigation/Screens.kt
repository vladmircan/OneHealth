package com.example.onehealth.app.navigation

import com.example.onehealth.domain.model.local.MeasurementType

interface AuthScreen
sealed interface MainScreen {
    interface HomeScreen: MainScreen
}

sealed class Screen(internal val baseRoute: String) {

    object Home: Screen(baseRoute = "home_screen"), MainScreen.HomeScreen {
        fun getRoute() = baseRoute
    }

    object AddMeasurement:
        Screen(baseRoute = "add_measurement/{${Arguments.ARG_MEASUREMENT_TYPE}}"), MainScreen {
        fun getRoute(measurementType: MeasurementType): String {
            return this.baseRoute.replace("{$ARG_MEASUREMENT_TYPE_NAME}", measurementType.name)
        }

        const val ARG_MEASUREMENT_TYPE_NAME = Arguments.ARG_MEASUREMENT_TYPE
    }

    object ViewMeasurements:
        Screen(baseRoute = "view_measurements/{${Arguments.ARG_MEASUREMENT_TYPE}}"), MainScreen {
        fun getRoute(measurementType: MeasurementType): String {
            return this.baseRoute.replace("{$ARG_MEASUREMENT_TYPE_NAME}", measurementType.name)
        }

        const val ARG_MEASUREMENT_TYPE_NAME = Arguments.ARG_MEASUREMENT_TYPE
    }

    object Login: Screen(baseRoute = "login_screen"), AuthScreen {
        fun getRoute() = baseRoute
    }

    object Register: Screen(baseRoute = "register_screen"), AuthScreen {
        fun getRoute() = baseRoute
    }
}

private object Arguments {
    const val ARG_MEASUREMENT_TYPE = "measurementType"
}
