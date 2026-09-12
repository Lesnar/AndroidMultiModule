package com.jemis.base.router

/**
 * 全局路由表：集中声明所有跨模块页面的路由路径与传参 key。
 *
 * 为什么放在 base 而不是各自的业务模块里：
 * 1. [com.therouter.router.Route] 的 path 是注解参数，只接受编译期常量，
 *    所以必须是 `const val`，且定义它的模块要能被使用方编译期访问到。
 * 2. app 与各业务模块都依赖 base，双方靠这里的同一份常量对齐路径。
 *    如果常量写在 business-a 里，app 为了引用它就得重新依赖 business-a，
 *    那就退回到接入路由之前的强耦合了，等于白做。
 * 3. 手写字符串容易拼错，而拼错在编译期没有任何提示，只会在运行时静默跳转失败。
 *
 * 路径统一用 `router://模块名/页面名` 的形式，是为了以后能平滑对接 H5 跳转和
 * 系统 deeplink —— 那两者天然就是 URL，届时换个 scheme 即可，不必重排路由表。
 */
object RouterPath {

    /** business-a 首页（传统 View 体系实现） */
    const val BUSINESS_A_HOME = "router://business-a/home"

    /** business-b 首页（Compose 实现） */
    const val BUSINESS_B_HOME = "router://business-b/home"

    /**
     * 跨模块传参的 key。
     *
     * 必须与目标页面 [com.therouter.router.Autowired] 的 name 一字不差，
     * 所以同样收敛成常量，由发起方和接收方共同引用，避免两边各写一遍字符串写岔。
     */
    const val KEY_FROM = "from"
}
