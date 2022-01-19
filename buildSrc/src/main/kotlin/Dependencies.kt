import org.gradle.api.JavaVersion

object AppVersions {
    const val compileSdk = 31
    const val minSdk = 23
    const val targetSdk = 31
    const val versionCode = 1
    const val versionName = "1.0.0"
}

object Versions {
    object Language {
        val javaVersion = JavaVersion.VERSION_11
        const val kotlinVersion = "1.6.0"
        const val kotlinLevel = "1.6"
        const val javaLevel = "11"
    }

    object CoreAndroid {
        const val compose = "1.1.0-rc01"
    }

    internal object Other {
        const val coroutines = "1.5.2"
        const val androidGradlePlugin = "7.0.4"
        const val daggerHilt = "2.38.1"
        const val googleServices = "4.3.8"
        const val firebaseCrashlytics = "2.7.1"
        const val appDistribution = "2.2.0"
        const val firebasePerformance = "1.4.0"
        const val firebaseBom = "29.0.3"
        const val navigation = "2.4.0-alpha02"

        const val core = "1.7.0"
        const val appCompat = "1.4.0"
        const val annotation = "1.2.0"
        const val exifInterface = "1.3.2"
        const val fragment = "1.3.5"
        const val constraintLayout = "2.0.4"
        const val material = "1.4.0"
        const val preference = "1.1.1"
        const val swipeRefreshLayout = "1.1.0"
        const val workRuntime = "2.7.0"
        const val gifDrawable = "1.2.23"
        const val playCore = "1.8.1"
        const val lifecycle = "2.4.0"
        const val hiltLifecycle = "1.0.0-alpha03"
        const val hiltAndroid = "1.0.0"
        const val paging = "3.0.1"
        const val room = "2.3.0"
        const val sqlCipher = "4.4.3"
        const val sqlLite = "2.2.0"

        const val firestore = "23.0.1"
        const val storage = "20.0.0"
        const val pref = "20.0.1"
        const val storageUi = "4.3.2"
        const val analytics = "19.0.0"
        const val crashlytics = "18.1.0"
        const val firebaseAuthUI = "6.2.1"
        const val firebaseMessaging = "21.0.1"

        const val revenueCat = "4.2.1"

        const val lottie = "3.7.0"
        const val konfetti = "1.2.5"
        const val giphy = "2.1.0"
        const val androidBrowserHelper = "2.2.2"

        const val gson = "2.8.6"
        const val glide = "4.12.0"
        const val retrofit = "2.9.0"
        const val okHttp = "4.7.2"

        const val timber = "4.7.1"

        //MEMORY
        const val leakCanary = "2.7"

        //TESTS
        const val junit = "4.13.1"
        const val mockk = "1.10.0"
        const val coreTesting = "1.1.1"
        const val coroutineTest = "1.3.9"
        const val androidJunit = "1.1.2"
        const val androidTestCore = "1.3.0"
        const val espresso = "3.3.0"

        const val splashScreen = "1.0.0-alpha02"

        const val philJayChart = "3.1.0-alpha"
    }
}

object GradlePlugins {
    const val kotlin =
        "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.Language.kotlinVersion}"
    const val android = "com.android.tools.build:gradle:${Versions.Other.androidGradlePlugin}"
    const val daggerHilt =
        "com.google.dagger:hilt-android-gradle-plugin:${Versions.Other.daggerHilt}"
    const val googleServices = "com.google.gms:google-services:${Versions.Other.googleServices}"
    const val firebaseCrashlytics =
        "com.google.firebase:firebase-crashlytics-gradle:${Versions.Other.firebaseCrashlytics}"
    const val firebaseAppDistribution =
        "com.google.firebase:firebase-appdistribution-gradle:${Versions.Other.appDistribution}"
    const val firebasePerformance =
        "com.google.firebase:perf-plugin:${Versions.Other.firebasePerformance}"
}

object Dependencies {
    object Kotlin {
        const val coroutines =
            "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.Other.coroutines}"
        const val reflection =
            "org.jetbrains.kotlin:kotlin-reflect:${Versions.Language.kotlinVersion}"
    }

    object Android {
        const val core = "androidx.core:core-ktx:${Versions.Other.core}"
        const val annotation = "androidx.annotation:annotation:${Versions.Other.annotation}"
        const val appCompat = "androidx.appcompat:appcompat:${Versions.Other.appCompat}"
        const val fragment = "androidx.fragment:fragment-ktx:${Versions.Other.fragment}"
        const val constraintLayout =
            "androidx.constraintlayout:constraintlayout:${Versions.Other.constraintLayout}"
        const val material = "com.google.android.material:material:${Versions.Other.material}"
        const val swipeRefreshLayout =
            "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.Other.swipeRefreshLayout}"
        const val workRuntime = "androidx.work:work-runtime-ktx:${Versions.Other.workRuntime}"
        const val preference = "androidx.preference:preference-ktx:${Versions.Other.preference}"
        const val pagingRuntime = "androidx.paging:paging-runtime-ktx:${Versions.Other.paging}"
        const val androidBrowserHelper =
            "com.google.androidbrowserhelper:androidbrowserhelper:${Versions.Other.androidBrowserHelper}"
        const val splashScreen = "androidx.core:core-splashscreen:${Versions.Other.splashScreen}"
    }

    object Navigation {
        const val fragment =
            "androidx.navigation:navigation-fragment-ktx:${Versions.Other.navigation}"
        const val ui = "androidx.navigation:navigation-ui-ktx:${Versions.Other.navigation}"
        const val compose = "androidx.navigation:navigation-compose:${Versions.Other.navigation}"
    }

    object PlayServices {
        const val playCore = "com.google.android.play:core-ktx:${Versions.Other.playCore}"
    }

    object Compose {
        const val ui = "androidx.compose.ui:ui:${Versions.CoreAndroid.compose}"
        const val material = "androidx.compose.material:material:${Versions.CoreAndroid.compose}"
        const val toolingPreview =
            "androidx.compose.ui:ui-tooling-preview:${Versions.CoreAndroid.compose}"
        const val tooling = "androidx.compose.ui:ui-tooling:${Versions.CoreAndroid.compose}"
        const val activity = "androidx.activity:activity-compose:${Versions.CoreAndroid.compose}"
    }

    object Lifecycle {
        const val runtime = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.Other.lifecycle}"
        const val viewModel =
            "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.Other.lifecycle}"
        const val liveData = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.Other.lifecycle}"
        const val service = "androidx.lifecycle:lifecycle-service:${Versions.Other.lifecycle}"
        const val process = "androidx.lifecycle:lifecycle-process:${Versions.Other.lifecycle}"
        const val commonJava8 =
            "androidx.lifecycle:lifecycle-common-java8:${Versions.Other.lifecycle}"
    }

    object DependencyInjection {
        const val hilt = "com.google.dagger:hilt-android:${Versions.Other.daggerHilt}"
        const val hiltCompiler =
            "com.google.dagger:hilt-android-compiler:${Versions.Other.daggerHilt}"
        const val hiltLifecycle =
            "androidx.hilt:hilt-lifecycle-viewmodel:${Versions.Other.hiltLifecycle}"
        const val hiltWork = "androidx.hilt:hilt-work:${Versions.Other.hiltAndroid}"
        const val hiltAndroidCompiler = "androidx.hilt:hilt-compiler:${Versions.Other.hiltAndroid}"
        const val hiltNavigationCompose =
            "androidx.hilt:hilt-navigation-compose:${Versions.Other.hiltLifecycle}"

        const val javaInject = "javax.inject:javax.inject:1"
    }

    object Serialization {
        const val gson = "com.google.code.gson:gson:${Versions.Other.gson}"
    }

    object Firebase {
        const val bom = "com.google.firebase:firebase-bom:${Versions.Other.firebaseBom}"
        const val firestore =
            "com.google.firebase:firebase-firestore-ktx:${Versions.Other.firestore}"
        const val storage = "com.google.firebase:firebase-storage-ktx:${Versions.Other.storage}"
        const val performance = "com.google.firebase:firebase-perf:${Versions.Other.pref}"
        const val storageUi = "com.firebaseui:firebase-ui-storage:${Versions.Other.storageUi}"
        const val analytics =
            "com.google.firebase:firebase-analytics-ktx:${Versions.Other.analytics}"
        const val crashlytics =
            "com.google.firebase:firebase-crashlytics:${Versions.Other.crashlytics}"
        const val auth = "com.google.firebase:firebase-auth-ktx"
        const val messaging =
            "com.google.firebase:firebase-messaging:${Versions.Other.firebaseMessaging}"
    }

    object Testing {
        const val junit = "junit:junit:${Versions.Other.junit}"
        const val junitCompose =
            "androidx.compose.ui:ui-test-junit4:${Versions.CoreAndroid.compose}"
        const val mockk = "io.mockk:mockk:${Versions.Other.mockk}"
        const val androidCoreTest = "android.arch.core:core-testing:${Versions.Other.coreTesting}"
        const val coroutineTest =
            "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.Other.coroutineTest}"
        const val androidJunit = "androidx.test.ext:junit:${Versions.Other.androidJunit}"
        const val androidTestCore = "androidx.test:core:${Versions.Other.androidTestCore}"
        const val androidEspressoCore =
            "androidx.test.espresso:espresso-core:${Versions.Other.espresso}"

        const val testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    object ImageLoading {
        const val glide = "com.github.bumptech.glide:glide:${Versions.Other.glide}"
        const val glideCompiler = "com.github.bumptech.glide:compiler:${Versions.Other.glide}"
    }

    object Networking {
        const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.Other.retrofit}"
        const val retrofitGson = "com.squareup.retrofit2:converter-gson:${Versions.Other.retrofit}"
        const val loggingInterceptor =
            "com.squareup.okhttp3:logging-interceptor:${Versions.Other.okHttp}"
    }

    object Database {
        const val room = "androidx.room:room-ktx:${Versions.Other.room}"
        const val roomRuntime = "androidx.room:room-runtime:${Versions.Other.room}"
        const val roomCompiler = "androidx.room:room-compiler:${Versions.Other.room}"

        const val sqlCipher = "net.zetetic:android-database-sqlcipher:${Versions.Other.sqlCipher}"
        const val sqlLite = "androidx.sqlite:sqlite-ktx:${Versions.Other.sqlLite}"
    }

    object Animations {
        const val lottie = "com.airbnb.android:lottie:${Versions.Other.lottie}"
        const val konfetti = "nl.dionsegijn:konfetti:${Versions.Other.konfetti}"
    }

    object Charts {
        const val philJayChart = "com.github.PhilJay:MPAndroidChart:${Versions.Other.philJayChart}"
    }

    object Utility {
        const val timber = "com.jakewharton.timber:timber:${Versions.Other.timber}"
        const val leakCanary =
            "com.squareup.leakcanary:leakcanary-android:${Versions.Other.leakCanary}"
    }
}

object PackagingOptions {
    val excludedFolders = setOf(
        "META-INF/*",
        "META-INF/gradle/*"
    )
}

object BuildTypes {
    val defaultAbiFilters = listOf("armeabi-v7a", "arm64-v8a")
    val emulatorAbiFilters = listOf("x86", "x86_64")
}