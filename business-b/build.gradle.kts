import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension

// 独立调试开关：见根目录 gradle.properties 中的 businessB.isolationRun 说明。
val isolationRun = providers.gradleProperty("businessB.isolationRun").getOrElse("false").toBoolean()

plugins {
    alias(libs.plugins.compose.compiler)
    // 同 business-a：本模块使用了 @Route / @Autowired，需要 KSP 生成路由表。
    alias(libs.plugins.ksp)
}

// android.application / android.library / therouter 的版本已经在根 build.gradle.kts 里
// 通过 `apply false` 声明过，这里只需按开关用 apply(plugin = ...) 二选一即可。
// 不能写进上面的 plugins {} 块：plugins {} 的内容会被单独抽出编译，看不到本文件里的 isolationRun。
if (isolationRun) {
    apply(plugin = "com.android.application")
    // 独立运行时本模块自己就是壳工程，需要自己聚合路由表（见 app/build.gradle.kts 里的说明）。
    apply(plugin = "therouter")
} else {
    apply(plugin = "com.android.library")
}

if (isolationRun) {
    configure<ApplicationExtension> {
        namespace = "com.jemis.business_b"
        compileSdk {
            version = release(37)
        }

        defaultConfig {
            applicationId = "com.jemis.business_b"
            minSdk = 28
            targetSdk = 37
            versionCode = 1
            versionName = "1.0"

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
        // 独立运行需要一个 LAUNCHER 入口，main 的 manifest 里 Activity 是 exported=false 的，
        // 这里整体换成一份专门加了 intent-filter 的 manifest（会替换而非合并 main 的 manifest）。
        sourceSets["main"].manifest.srcFile("src/isolationRun/AndroidManifest.xml")
    }
} else {
    configure<LibraryExtension> {
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
}

// dependencies {} 块里 implementation/testImplementation 等也是基于 plugins {} 里
// 声明过的插件才生成的类型安全访问器，android 插件是按开关用 apply(plugin = ...) 动态应用的，
// 访问器不可用，所以这里统一改用 DependencyHandler.add(configurationName, ...) 按名字添加。
dependencies.apply {
    // base 以 api 方式暴露了 therouter-router，依赖 base 即可传递拿到路由 API 和 RouterPath。
    add("implementation", project(":base"))
    // 编译期注解处理器，生成本模块的路由表与参数注入代码。
    add("ksp", libs.therouter.apt)
    add("implementation", libs.androidx.activity.ktx)
    add("implementation", libs.androidx.appcompat)
    add("implementation", libs.androidx.constraintlayout)
    add("implementation", libs.androidx.core.ktx)
    add("implementation", libs.material)
    val composeBom = platform(libs.compose.bom)
    add("implementation", composeBom)
    add("implementation", libs.androidx.activity.compose)
    add("implementation", libs.compose.material3)
    add("implementation", libs.compose.ui)
    add("implementation", libs.compose.ui.graphics)
    add("implementation", libs.compose.ui.tooling.preview)
    add("debugImplementation", libs.compose.ui.tooling)
    add("testImplementation", libs.junit)
    add("androidTestImplementation", libs.androidx.espresso.core)
    add("androidTestImplementation", libs.androidx.junit)
}
