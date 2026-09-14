package com.jemis.business_b

import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.jemis.base.router.RouterPath
import com.therouter.TheRouter
import com.therouter.router.Autowired
import com.therouter.router.Route

/**
 * business-b 首页（Compose 实现）。
 *
 * 与 business-a 的对照点：路由注册与参数注入跟 UI 用什么体系无关。
 * 本类继承的是 [ComponentActivity] 而非 AppCompatActivity，
 * [TheRouter.inject] 同样适用——它只要求是个能拿到 intent 的对象。
 */
@Route(
    path = RouterPath.BUSINESS_B_HOME,
    description = "business-b 首页"
)
class MainActivity : ComponentActivity() {

    /** 接收路由传参，@JvmField 的必要性见 BusinessAActivity 中的说明 */
    @JvmField
    @Autowired(name = RouterPath.KEY_FROM)
    var from: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 必须在读取 from 之前调用，否则拿到的是字段默认值
        TheRouter.inject(this)

        setContent {
            BusinessBScreen(from = from)
        }
    }
}

@Composable
fun BusinessBScreen(modifier: Modifier = Modifier, from: String = "") {
    Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Text(
                text = "Hello business-b\n来自：$from",
            )
            HorizontalDivider()
            AndroidView(
                factory = {
                    TextView(it)
                },
                update = {
                    it.text = "haha"
                }
            )
            HorizontalDivider()

        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BusinessBScreenPreview() {
    BusinessBScreen(from = "预览")
}
