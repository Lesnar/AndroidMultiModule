// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.compose.compiler) apply false
    // 在根工程只声明版本、不应用（apply false），由各子模块按需 apply，
    // 保证整个工程用同一个版本，避免各模块各自写版本号导致不一致。
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.therouter) apply false
}