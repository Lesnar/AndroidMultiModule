package com.jemis.business_a

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.jemis.base.interfaces.IUserService
import com.jemis.base.router.RouterPath
import com.jemis.business_a.databinding.BizAActivityMainBinding
import com.therouter.TheRouter
import com.therouter.router.Autowired
import com.therouter.router.Route

/**
 * business-a 首页。
 *
 * [Route] 让本页面在编译期被登记进路由表，外部只需知道 [RouterPath.BUSINESS_A_HOME]
 * 这个字符串就能打开它，不需要拿到本类的 Class 引用，因而也不需要依赖 business-a 模块。
 * app 模块正是靠这一点，把对本模块的依赖从 implementation 降级成了 runtimeOnly。
 *
 * description 只是给人看的说明，会写进生成的路由表，便于排查路由冲突。
 */
@Route(
    path = RouterPath.BUSINESS_A_HOME,
    description = "business-a 首页"
)
class BusinessAActivity : AppCompatActivity() {

    private lateinit var binding: BizAActivityMainBinding

    /**
     * 路由传参的接收方，等价于手写 `intent.getStringExtra("from")`。
     *
     * 两个容易踩的点：
     * - 必须加 [JvmField]。KSP 生成的注入代码是直接对字段赋值的，
     *   而 Kotlin 属性默认会被编译成私有字段 + getter/setter，生成代码访问不到，
     *   加了 @JvmField 才会暴露成公开字段。
     * - name 必须与发起方 withString 的 key 完全一致，所以两边都引用 [RouterPath] 里的同一个常量。
     *
     * 声明了默认值，这样即使没传参也不会是 null，省去调用处的空判断。
     */
    @JvmField
    @Autowired(name = RouterPath.KEY_FROM)
    var from: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 触发 @Autowired 注入，把 intent 里的参数写进上面的字段。
        // 必须在 super.onCreate 之后（此时 intent 才可用）、且在读取 from 之前调用，
        // 否则拿到的只会是字段的默认值。
        TheRouter.inject(this)

        binding = BizAActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 把注入到的参数显示出来，作为「路由寻址 + 参数注入」链路走通的直观验证。
        binding.tv.text = "hello business-a\n来自：$from"

        // TheRouter.get 按接口类型取跨模块服务实现（本例的实现在 business-b），
        // 拿不到时说明提供方模块没有参与本次构建。
        binding.btnGetUser.setOnClickListener {
            binding.tvUserInfo.text = TheRouter.get(IUserService::class.java)?.getUserInfo()
                ?: "未找到 IUserService 实现"
        }
    }
}
