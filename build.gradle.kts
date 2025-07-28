// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("org.jetbrains.kotlin.jvm") version "2.0.0" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0" apply false
    // otros plugins como:
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
}