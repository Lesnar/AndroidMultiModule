package com.jemis.multi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.jemis.base.router.RouterPath
import com.jemis.multi.databinding.ActivityMainBinding
import com.therouter.TheRouter

/**
 * 壳工程首页，负责分发到各业务模块。
 *
 * 注意这里已经没有任何 business-a / business-b 的 import 了。
 * 两个业务模块在 build.gradle.kts 中是 runtimeOnly 依赖，压根不在本模块的编译期类路径上，
 * 就算想写 `import com.jemis.business_a.BusinessAActivity` 也会编译失败。
 * 壳工程对业务模块的全部认知，只剩 [RouterPath] 里的几个字符串。
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 路由跳转三段式：build(路径) -> withXxx(参数) -> navigation(上下文)
        //
        // 对比改造前的 startActivity(Intent(this, BusinessAActivity::class.java))：
        // 那种写法要求编译期就持有目标类，而这里只传了一个字符串，
        // 目标类由 TheRouter 在编译期生成的路由表中按路径查出来。
        //
        // withString 的 key 与目标页面 @Autowired(name = ...) 共用同一个常量，
        // 到达目标页后由生成的注入代码自动赋值，无需在目标页手写 getStringExtra。
        binding.buttonBusinessA.setOnClickListener {
            TheRouter.build(RouterPath.BUSINESS_A_HOME)
                .withString(RouterPath.KEY_FROM, "app 首页")
                .navigation(this)
        }
        binding.buttonBusinessB.setOnClickListener {
            TheRouter.build(RouterPath.BUSINESS_B_HOME)
                .withString(RouterPath.KEY_FROM, "app 首页")
                .navigation(this)
        }
    }
}
