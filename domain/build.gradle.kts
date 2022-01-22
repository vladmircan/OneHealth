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
    testImplementation(Dependencies.Testing.junit)
    testImplementation(Dependencies.Testing.assertionLibrary)
}