plugins {
    id("java-library")
    id("kotlin")
}

java {
    sourceCompatibility = Versions.Language.javaVersion
    targetCompatibility = Versions.Language.javaVersion
}

dependencies {
    implementation(Dependencies.Kotlin.coroutines)
    implementation(Dependencies.DependencyInjection.javaInject)
}