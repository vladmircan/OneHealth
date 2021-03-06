plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("com.google.gms.google-services")
}

android {
    compileSdk = AppVersions.compileSdk

    defaultConfig {
        minSdk = AppVersions.minSdk
        targetSdk = AppVersions.targetSdk

        ndk.abiFilters += BuildTypes.defaultAbiFilters
        testInstrumentationRunner = Dependencies.Testing.testInstrumentationRunner
    }

    packagingOptions {
        resources.excludes.addAll(PackagingOptions.excludedFolders)
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
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
}

dependencies {
    implementation(project(":domain"))

    implementation(Dependencies.Android.core)

    implementation(Dependencies.DependencyInjection.hilt)
    kapt(Dependencies.DependencyInjection.hiltAndroidCompiler)
    kapt(Dependencies.DependencyInjection.hiltCompiler)

    implementation(platform(Dependencies.Firebase.bom))
    implementation(Dependencies.Firebase.auth)
    implementation(Dependencies.Firebase.firestore)

    implementation(Dependencies.Database.room)
    implementation(Dependencies.Database.roomRuntime)
    kapt(Dependencies.Database.roomCompiler)
    implementation(Dependencies.Database.sqlCipher)
    implementation(Dependencies.Database.sqlLite)

    implementation(Dependencies.Utility.timber)

    testImplementation(Dependencies.Testing.junit)
    androidTestImplementation(Dependencies.Testing.androidJunit)
    androidTestImplementation(Dependencies.Testing.hiltTest)
    androidTestImplementation(Dependencies.Testing.androidJunitRunner)
    kaptTest(Dependencies.DependencyInjection.hiltCompiler)
    kaptAndroidTest(Dependencies.DependencyInjection.hiltCompiler)
    testImplementation(Dependencies.Testing.hiltTest)
    testImplementation(Dependencies.Testing.assertionLibrary)
    androidTestImplementation(Dependencies.Testing.assertionLibrary)
}