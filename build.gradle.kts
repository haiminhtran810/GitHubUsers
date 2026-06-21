buildscript {
    dependencies {
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.59.2")
    }
    configurations.classpath {
        resolutionStrategy.force("org.jetbrains:annotations:23.0.0")
    }
}
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.google.devtools.ksp) apply false
    alias(libs.plugins.jetbrains.kotlin.plugin.serialization) apply false
}
