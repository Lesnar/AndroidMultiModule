package com.jemis.multi

import android.app.Application
import android.content.Context
import cn.therouter.BuildConfig
import com.therouter.TheRouter

/**
 * 壳工程的 Application。
 *
 * 注意这里并没有调用 TheRouter 的初始化方法：TheRouter 通过一个自带的 ContentProvider
 * 完成自动初始化，无需手写任何初始化代码。本类存在的唯一目的是配置调试开关。
 */
class App : Application() {

    override fun attachBaseContext(base: Context) {
        // 必须写在 super.attachBaseContext 之前。
        // TheRouter 依赖 ContentProvider 自动初始化，而 ContentProvider 的 onCreate
        // 早于 Application.onCreate 执行，放到 onCreate 里设置就已经晚了，
        // 初始化阶段的路由日志会全部丢失。
        //
        // 开着它，logcat 过滤 TheRouter 就能看到路由表加载、路径匹配、参数注入的完整过程，
        // 这是排查「跳转没反应」类问题最直接的手段。Release 包自动关闭，避免泄露路由结构。
        // TheRouter 源码里这是个 Kotlin 属性，Java 侧才是 setDebug()，Kotlin 侧直接赋值。
        TheRouter.isDebug = BuildConfig.DEBUG
        super.attachBaseContext(base)
    }
}
