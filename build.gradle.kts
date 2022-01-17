buildscript {
    repositories {
        google()
        mavenCentral()
        maven { setUrl("https://jitpack.io") }
    }
    dependencies {
        classpath(GradlePlugins.android)
        classpath(GradlePlugins.kotlin)
        classpath(GradlePlugins.daggerHilt)
        classpath(GradlePlugins.googleServices)
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven { setUrl("https://jitpack.io") }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}