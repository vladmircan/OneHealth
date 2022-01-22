plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("com.google.firebase.crashlytics")
}

android {
    compileSdk = AppVersions.compileSdk

    defaultConfig {
        minSdk = AppVersions.minSdk
        targetSdk = AppVersions.targetSdk
        versionCode = AppVersions.versionCode
        versionName = AppVersions.versionName

        ndk.abiFilters += BuildTypes.defaultAbiFilters
        testInstrumentationRunner = Dependencies.Testing.testInstrumentationRunner
        vectorDrawables {
            useSupportLibrary = true
        }

        ext.set("enableCrashlytics", true)
    }

    packagingOptions {
        resources.excludes.addAll(PackagingOptions.excludedFolders)
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            isDebuggable = true
            ndk.abiFilters += BuildTypes.emulatorAbiFilters
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = Versions.Language.javaVersion
        targetCompatibility = Versions.Language.javaVersion
    }
    kotlinOptions {
        jvmTarget = Versions.Language.javaLevel
        languageVersion = Versions.Language.kotlinLevel
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.CoreAndroid.compose
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":data"))

    implementation(Dependencies.Android.core)
    implementation(Dependencies.Android.appCompat)
    implementation(Dependencies.Android.material)
    implementation(Dependencies.Lifecycle.viewModel)
    implementation(Dependencies.Utility.timber)
    implementation(Dependencies.Compose.ui)
    implementation(Dependencies.Compose.material)
    implementation(Dependencies.Compose.toolingPreview)
    implementation(Dependencies.Compose.activity)
    debugImplementation(Dependencies.Compose.tooling)
    implementation(Dependencies.Lifecycle.runtime)
    implementation(Dependencies.Lifecycle.commonJava8)
    implementation(Dependencies.Lifecycle.process)
    implementation(Dependencies.Kotlin.reflection)

    implementation(Dependencies.Android.splashScreen)

    implementation(Dependencies.Charts.philJayChart)

    implementation(Dependencies.Firebase.firestore)
    implementation(Dependencies.Firebase.crashlytics)

    implementation(Dependencies.DependencyInjection.hilt)
    implementation(Dependencies.DependencyInjection.hiltNavigationCompose)
    kapt(Dependencies.DependencyInjection.hiltAndroidCompiler)
    kapt(Dependencies.DependencyInjection.hiltCompiler)

    implementation(Dependencies.Navigation.fragment)
    implementation(Dependencies.Navigation.ui)
    implementation(Dependencies.Navigation.compose)

    implementation(Dependencies.Utility.leakCanary)

    testImplementation(Dependencies.Testing.junit)
    androidTestImplementation(Dependencies.Testing.androidJunit)
    androidTestImplementation(Dependencies.Testing.androidEspressoCore)
    androidTestImplementation(Dependencies.Testing.junitCompose)
    testImplementation(Dependencies.Testing.hiltTest)
    androidTestImplementation(Dependencies.Testing.hiltTest)
    kaptTest(Dependencies.DependencyInjection.hiltCompiler)
    kaptAndroidTest(Dependencies.DependencyInjection.hiltCompiler)
    testImplementation(Dependencies.Testing.assertionLibrary)
    androidTestImplementation(Dependencies.Testing.assertionLibrary)
}