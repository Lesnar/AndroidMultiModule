// 独立调试开关：见根目录 gradle.properties 的说明。打开后对应业务模块自己就是
// com.android.application，AGP 不允许 app 再以 project 依赖去引用一个 application 模块，
// 所以下面的 runtimeOnly 依赖要按同样的开关跳过。
val businessAIsolationRun = providers.gradleProperty("businessA.isolationRun").getOrElse("false").toBoolean()
val businessBIsolationRun = providers.gradleProperty("businessB.isolationRun").getOrElse("false").toBoolean()

plugins {
    alias(libs.plugins.android.application)
    // TheRouter 字节码插件只需应用在壳工程。
    // 它在打包阶段扫描所有模块 KSP 生成的 RouterMap__TheRouter__xxx 类，
    // 把它们聚合进一张总路由表并织入初始化代码。
    // 这正是 TheRouter 能做到「无运行时扫描、无反射」的原因——
    // 路由表在编译期就已确定，App 启动时不需要遍历 dex 去找路由。
    alias(libs.plugins.therouter)
}

android {
    namespace = "com.jemis.multi"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.jemis.multi"
        minSdk = 28
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
        // App 类中要用 BuildConfig.DEBUG 决定是否开启路由日志
        buildConfig = true
    }
}

dependencies {
    // base 提供路由 API 和 RouterPath 常量，是壳工程与业务模块之间唯一的编译期公共依赖。
    implementation(project(":base"))

    // 关键：业务模块用 runtimeOnly 而非 implementation。
    // runtimeOnly 只把模块打进 APK，不会放进 app 的编译期类路径，
    // 因此 app 里再也 import 不到 BusinessAActivity —— 解耦不再靠自觉，而是编译器强制的。
    // 跳转只剩路由一条路，新增业务模块时 app 的代码一行都不用改。
    if (!businessAIsolationRun) runtimeOnly(project(":business-a"))
    if (!businessBIsolationRun) runtimeOnly(project(":business-b"))
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