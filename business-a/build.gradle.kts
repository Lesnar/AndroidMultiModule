plugins {
    alias(libs.plugins.android.library)
    // 凡是使用了 @Route / @Autowired 注解的模块都要应用 KSP，
    // 由它在编译期生成本模块的路由表和参数注入代码。
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.jemis.business_a"
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
    buildFeatures {
        viewBinding = true
    }
    resourcePrefix = "biz_a_"
}

dependencies {
    // base 以 api 方式暴露了 therouter-router，这里依赖 base 即可传递拿到路由 API 和 RouterPath。
    implementation(project(":base"))
    // 注解处理器只在编译期参与，不会打进产物。用 ksp 而非 kapt：
    // 本项目基于 AGP 9 的内置 Kotlin 支持，kapt 需额外引入 kotlin-android 插件链，
    // 且 kapt 本身已进入维护状态。TheRouter 1.4.0 起的 apt 包已内置 KSP 处理器。
    ksp(libs.therouter.apt)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}