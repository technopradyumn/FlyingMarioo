// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false

    // Ksp
    id ("com.google.devtools.ksp") version "1.9.10-1.0.13" apply false

    // Hilt - DI
    id ("com.google.dagger.hilt.android") version "2.44" apply false

}