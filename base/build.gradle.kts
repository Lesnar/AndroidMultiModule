plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.jemis.base"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        minSdk = 28

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

}

dependencies {
    // 用 api 而不是 implementation：让所有依赖 base 的模块（app / business-a / business-b）
    // 都能传递拿到 TheRouter 的 API，不必各自重复声明这条依赖。
    // RouterPath 里的路由常量也定义在本模块，二者一起构成「路由能力」的统一出口。
    api(libs.therouter.router)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}