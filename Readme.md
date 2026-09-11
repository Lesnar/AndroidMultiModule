这是资源合并冲突导致的。原因如下：

根本原因：app 模块和 business-a 模块的布局文件重名——两者都叫 activity_main.xml。Android 资源合并是按简单文件名合并的，不区分包名/module；而 app
模块（最终的宿主应用）优先级最高，会覆盖它依赖的 library 模块中同名的资源文件。

两个关键点：

1. 覆盖对所有引用方生效。即使是 business-a 自己的布局或代码引用 @string/lorem_ipsum（或 R.string.lorem_ipsum），运行时查到的也是 app 的值，因为库模块的代码在打包时使用的资源 ID
   就指向最终合并后的那张表。
2. 想反过来（库覆盖 app）需要显式声明：在 app 的 build.gradle 里用 android.resourceConfigurations 不行，得用 overrideLibrary 之外的资源覆盖机制——实际上没有官方的“库覆盖
   app”开关，常见做法是改名避免冲突，或把公共资源下沉到 base 模块。


项目模块化：
1.资源冲突解决：正确做法是「共享的下沉 + 私有的留本模块 + 前缀隔离」