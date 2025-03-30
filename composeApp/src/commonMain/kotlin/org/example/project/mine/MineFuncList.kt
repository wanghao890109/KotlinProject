package org.example.project.mine

data class MineFuncBean(val iconPath:String, val title:String,val router:String)

val mineFuncList = listOf(
    MineFuncBean("drawable/home_ic_my_share_app.png", "分享", "share"),
    MineFuncBean("drawable/home_ic_my_order.png", "订单", "order"),
    MineFuncBean("drawable/home_ic_my_setting.png", "设置", "setting")
)