plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
    alias(libs.plugins.jetbrains.kotlin.plugin.serialization)
}

kotlin {
    jvmToolchain(11)
}
java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.coroutines.core)
}
