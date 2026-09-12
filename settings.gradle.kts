pluginManagement {
    // TheRouter 没有向仓库发布插件标记坐标（cn.therouter:therouter.gradle.plugin 在
    // Maven Central 与 plugins.gradle.org 均为 404），Gradle 的 plugins {} DSL
    // 默认按「插件 id + .gradle.plugin」去找标记坐标，因此会解析失败。
    // 这里手动把插件 id 映射到它真实的坐标 cn.therouter:plugin，
    // 这样各模块仍可以用统一的 plugins { alias(libs.plugins.therouter) } 写法，
    // 而不必退回到老式的 buildscript { classpath ... } + apply(plugin = ...)。
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "therouter") {
                useModule("cn.therouter:plugin:${requested.version}")
            }
        }
    }
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "AndroidMultiModule"
include(":app")
include(":base")
include(":business-a")
include(":business-b")
