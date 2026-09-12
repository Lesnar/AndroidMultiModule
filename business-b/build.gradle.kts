plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose.compiler)
    // 同 business-a：本模块使用了 @Route / @Autowired，需要 KSP 生成路由表。
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.jemis.business_b"
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
        compose = true
    }
    resourcePrefix = "biz_b_"
}

dependencies {
    // base 以 api 方式暴露了 therouter-router，依赖 base 即可传递拿到路由 API 和 RouterPath。
    implementation(project(":base"))
    // 编译期注解处理器，生成本模块的路由表与参数注入代码。
    ksp(libs.therouter.apt)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    val composeBom = platform(libs.compose.bom)
    implementation(composeBom)
    implementation(libs.androidx.activity.compose)
    implementation(libs.compose.material3)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.graphics)
    implementation(libs.compose.ui.tooling.preview)
    debugImplementation(libs.compose.ui.tooling)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}