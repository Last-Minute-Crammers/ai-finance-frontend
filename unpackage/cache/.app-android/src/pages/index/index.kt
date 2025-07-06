@file:Suppress("UNCHECKED_CAST", "USELESS_CAST", "INAPPLICABLE_JVM_NAME", "UNUSED_ANONYMOUS_PARAMETER", "NAME_SHADOWING", "UNNECESSARY_NOT_NULL_ASSERTION")
package uni.UNI5A7011F
import io.dcloud.uniapp.*
import io.dcloud.uniapp.extapi.*
import io.dcloud.uniapp.framework.*
import io.dcloud.uniapp.runtime.*
import io.dcloud.uniapp.vue.*
import io.dcloud.uniapp.vue.shared.*
import io.dcloud.unicloud.*
import io.dcloud.uts.*
import io.dcloud.uts.Map
import io.dcloud.uts.Set
import io.dcloud.uts.UTSAndroid
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import io.dcloud.uniapp.extapi.getStorageSync as uni_getStorageSync
import io.dcloud.uniapp.extapi.navigateTo as uni_navigateTo
import io.dcloud.uniapp.extapi.removeStorageSync as uni_removeStorageSync
import io.dcloud.uniapp.extapi.showToast as uni_showToast
open class GenPagesIndexIndex : BasePage {
    constructor(__ins: ComponentInternalInstance, __renderer: String?) : super(__ins, __renderer) {}
    open var onShow: () -> Unit
        get() {
            return unref(this.`$exposed`["onShow"]) as () -> Unit
        }
        set(value) {
            setRefValue(this.`$exposed`, "onShow", value)
        }
    companion object {
        @Suppress("UNUSED_PARAMETER", "UNUSED_VARIABLE")
        var setup: (__props: GenPagesIndexIndex, _arg1: SetupContext) -> Any? = fun(__props, ref1): Any? {
            var __expose = ref1.expose
            val __ins = getCurrentInstance()!!
            val _ctx = __ins.proxy as GenPagesIndexIndex
            val _cache = __ins.renderCache
            val balance = ref(0)
            val monthlyNet = ref(0)
            val loading = ref(true)
            val features = utsArrayOf<UTSJSONObject>(object : UTSJSONObject() {
                var name = "记账"
                var desc = "记录每一笔收支"
                var icon = "/static/icons/book.png"
                var path = "/pages/transaction/transaction"
            }, object : UTSJSONObject() {
                var name = "财务分析"
                var desc = "可视化您的消费"
                var icon = "/static/icons/chart.png"
                var path = "/pages/analyze/analyze"
            }, object : UTSJSONObject() {
                var name = "AI理财宠物"
                var desc = "陪伴式理财体验"
                var icon = "/static/icons/pet.png"
                var path = "/pages/pet/pet"
            }, object : UTSJSONObject() {
                var name = "财务报告"
                var desc = "智能分析建议"
                var icon = "/static/icons/report.png"
                var path = "/pages/report/report"
            }, object : UTSJSONObject() {
                var name = "社交互动"
                var desc = "与好友一起理财"
                var icon = "/static/icons/social.png"
                var path = "/pages/social/social"
            }, object : UTSJSONObject() {
                var name = "设置"
                var desc = "个性化您的体验"
                var icon = "/static/icons/settings.png"
                var path = "/pages/settings/settings"
            })
            val goPage = fun(path: String){
                uni_navigateTo(NavigateToOptions(url = path))
            }
            val loadStatisticData = fun(): UTSPromise<Unit> {
                return wrapUTSPromise(suspend w@{
                        try {
                            loading.value = true
                            val token = uni_getStorageSync("token")
                            if (!token) {
                                console.error("没有找到token，跳转到登录页", " at pages/index/index.uvue:75")
                                uni_navigateTo(NavigateToOptions(url = "/pages/login/login"))
                                return@w
                            }
                            val totalRes = await(getTotalStatistic())
                            console.log("=== 总统计响应 ===", " at pages/index/index.uvue:82")
                            console.log("完整响应:", JSON.stringify(totalRes, null, 2), " at pages/index/index.uvue:83")
                            if (totalRes?.Data) {
                                console.log("总资产字段:", totalRes.Data.total_assets, " at pages/index/index.uvue:86")
                                console.log("收入数据:", totalRes.Data.Income, " at pages/index/index.uvue:87")
                                console.log("支出数据:", totalRes.Data.Expense, " at pages/index/index.uvue:88")
                                balance.value = (totalRes.Data.total_assets || 0) / 100
                                console.log("最终设置的余额:", balance.value, " at pages/index/index.uvue:92")
                            }
                            val now = Date()
                            val monthStart = Date(now.getFullYear(), now.getMonth(), 1)
                            val monthEnd = Date(now.getFullYear(), now.getMonth() + 1, 0)
                            val monthlyRes = await(getMonthStatistic(object : UTSJSONObject() {
                                var startTime = monthStart.toISOString()
                                var endTime = monthEnd.toISOString()
                            }))
                            console.log("=== 月度统计响应 ===", " at pages/index/index.uvue:105")
                            console.log("完整响应:", JSON.stringify(monthlyRes, null, 2), " at pages/index/index.uvue:106")
                            if (monthlyRes?.Data?.List?.length > 0) {
                                val monthData = monthlyRes.Data.List[0]
                                console.log("月度数据:", JSON.stringify(monthData, null, 2), " at pages/index/index.uvue:110")
                                val monthIncome = (monthData.Income?.Amount || 0) / 100
                                val monthExpense = (monthData.Expense?.Amount || 0) / 100
                                monthlyNet.value = monthIncome - monthExpense
                                console.log("月度收支计算:", object : UTSJSONObject() {
                                    var income = monthIncome
                                    var expense = monthExpense
                                    var net = monthlyNet.value
                                }, " at pages/index/index.uvue:117")
                            }
                        }
                         catch (error: Throwable) {
                            console.error("加载统计数据失败:", error, " at pages/index/index.uvue:125")
                            val errorMessage = String(error)
                            if (errorMessage.includes("Unauthorized")) {
                                uni_removeStorageSync("token")
                                uni_removeStorageSync("current_user")
                                uni_showToast(ShowToastOptions(title = "登录已过期，请重新登录", icon = "none"))
                                setTimeout(fun(){
                                    uni_navigateTo(NavigateToOptions(url = "/pages/login/login"))
                                }, 1500)
                            } else {
                                balance.value = 0
                                monthlyNet.value = 0
                                uni_showToast(ShowToastOptions(title = "数据加载失败", icon = "none"))
                            }
                        }
                         finally{
                            loading.value = false
                        }
                })
            }
            onMounted(fun(){
                loadStatisticData()
            }
            )
            __expose(utsMapOf("onShow" to fun() {
                loadStatisticData()
            }
            ))
            return fun(): Any? {
                return createElementVNode("view", utsMapOf("class" to "container"), utsArrayOf(
                    createElementVNode("view", utsMapOf("class" to "header"), utsArrayOf(
                        createElementVNode("text", utsMapOf("class" to "title"), "智能记账")
                    )),
                    createElementVNode("view", utsMapOf("class" to "balance-card"), utsArrayOf(
                        createElementVNode("view", utsMapOf("class" to "summary-block"), utsArrayOf(
                            createElementVNode("text", utsMapOf("class" to "balance-label"), "当前余额"),
                            if (isTrue(!loading.value)) {
                                createElementVNode("text", utsMapOf("key" to 0, "class" to "balance-value"), "¥" + toDisplayString(balance.value.toFixed(2)), 1)
                            } else {
                                createElementVNode("text", utsMapOf("key" to 1, "class" to "balance-value loading"), "加载中...")
                            }
                        )),
                        createElementVNode("view", utsMapOf("class" to "divider")),
                        createElementVNode("view", utsMapOf("class" to "summary-block"), utsArrayOf(
                            createElementVNode("text", utsMapOf("class" to "balance-label"), "本月收支"),
                            if (isTrue(!loading.value)) {
                                createElementVNode("view", utsMapOf("key" to 0, "class" to "expense-row"), utsArrayOf(
                                    createElementVNode("text", utsMapOf("class" to normalizeClass(utsArrayOf(
                                        "expense-icon",
                                        if (monthlyNet.value >= 0) {
                                            "positive"
                                        } else {
                                            "negative"
                                        }
                                    ))), toDisplayString(if (monthlyNet.value >= 0) {
                                        "🔺"
                                    } else {
                                        "🔻"
                                    }), 3),
                                    createElementVNode("text", utsMapOf("class" to normalizeClass(utsArrayOf(
                                        "balance-value",
                                        if (monthlyNet.value >= 0) {
                                            "income"
                                        } else {
                                            "expense"
                                        }
                                    ))), " ¥" + toDisplayString(Math.abs(monthlyNet.value).toFixed(2)), 3)
                                ))
                            } else {
                                createElementVNode("text", utsMapOf("key" to 1, "class" to "balance-value loading"), "加载中...")
                            }
                        ))
                    )),
                    createElementVNode("view", utsMapOf("class" to "feature-grid"), utsArrayOf(
                        createElementVNode(Fragment, null, RenderHelpers.renderList(features, fun(item, index, __index, _cached): Any {
                            return createElementVNode("view", utsMapOf("class" to "feature-item", "key" to index, "onClick" to fun(){
                                goPage(item["path"])
                            }
                            ), utsArrayOf(
                                createElementVNode("image", utsMapOf("src" to item["icon"], "class" to "feature-icon"), null, 8, utsArrayOf(
                                    "src"
                                )),
                                createElementVNode("text", utsMapOf("class" to "feature-title"), toDisplayString(item["name"]), 1),
                                createElementVNode("text", utsMapOf("class" to "feature-sub"), toDisplayString(item["desc"]), 1)
                            ), 8, utsArrayOf(
                                "onClick"
                            ))
                        }
                        ), 64)
                    ))
                ))
            }
        }
        val styles: Map<String, Map<String, Map<String, Any>>> by lazy {
            normalizeCssStyles(utsArrayOf(
                styles0
            ), utsArrayOf(
                GenApp.styles
            ))
        }
        val styles0: Map<String, Map<String, Map<String, Any>>>
            get() {
                return utsMapOf("container" to padStyleMapOf(utsMapOf("display" to "flex", "flexDirection" to "column", "backgroundColor" to "#f2f3f5")), "header" to padStyleMapOf(utsMapOf("height" to "140rpx", "backgroundImage" to "linear-gradient(to right, #4e54c8, #8f94fb)", "backgroundColor" to "rgba(0,0,0,0)", "display" to "flex", "alignItems" to "center", "justifyContent" to "center")), "title" to padStyleMapOf(utsMapOf("color" to "#FFFFFF", "fontSize" to "36rpx", "fontWeight" to "bold")), "balance-card" to padStyleMapOf(utsMapOf("backgroundColor" to "#ffffff", "borderTopLeftRadius" to "20rpx", "borderTopRightRadius" to "20rpx", "borderBottomRightRadius" to "20rpx", "borderBottomLeftRadius" to "20rpx", "paddingTop" to "24rpx", "paddingRight" to 0, "paddingBottom" to "24rpx", "paddingLeft" to 0, "width" to "80%", "marginTop" to "-30rpx", "marginRight" to "auto", "marginBottom" to "20rpx", "marginLeft" to "auto", "textAlign" to "center", "boxShadow" to "0 4rpx 12rpx rgba(0, 0, 0, 0.08)", "display" to "flex", "flexDirection" to "column", "alignItems" to "center")), "summary-block" to padStyleMapOf(utsMapOf("display" to "flex", "flexDirection" to "column", "alignItems" to "center", "marginBottom" to "6rpx")), "balance-label" to padStyleMapOf(utsMapOf("fontSize" to "24rpx", "color" to "#666666")), "balance-value" to utsMapOf("" to utsMapOf("fontSize" to "36rpx", "color" to "#333333", "fontWeight" to "bold", "marginTop" to "4rpx"), ".expense" to utsMapOf("color" to "#ff3b30"), ".income" to utsMapOf("color" to "#34c759"), ".loading" to utsMapOf("color" to "#999999", "fontSize" to "28rpx")), "expense-row" to padStyleMapOf(utsMapOf("display" to "flex", "alignItems" to "center", "justifyContent" to "center", "marginTop" to "4rpx")), "expense-icon" to utsMapOf("" to utsMapOf("fontSize" to "30rpx", "marginRight" to "6rpx"), ".positive" to utsMapOf("color" to "#34c759"), ".negative" to utsMapOf("color" to "#ff3b30")), "divider" to padStyleMapOf(utsMapOf("width" to "60%", "height" to "1rpx", "backgroundColor" to "#eeeeee", "marginTop" to "14rpx", "marginRight" to 0, "marginBottom" to "14rpx", "marginLeft" to 0)), "feature-grid" to padStyleMapOf(utsMapOf("flex" to 1, "display" to "flex", "flexWrap" to "wrap", "justifyContent" to "space-between", "alignContent" to "space-around", "paddingTop" to 0, "paddingRight" to "24rpx", "paddingBottom" to 0, "paddingLeft" to "24rpx")), "feature-item" to padStyleMapOf(utsMapOf("width" to "48%", "height" to "30%", "backgroundColor" to "#ffffff", "borderTopLeftRadius" to "16rpx", "borderTopRightRadius" to "16rpx", "borderBottomRightRadius" to "16rpx", "borderBottomLeftRadius" to "16rpx", "paddingTop" to "20rpx", "paddingRight" to "10rpx", "paddingBottom" to "20rpx", "paddingLeft" to "10rpx", "display" to "flex", "flexDirection" to "column", "alignItems" to "center", "justifyContent" to "center", "boxShadow" to "0 4rpx 10rpx rgba(0, 0, 0, 0.04)", "marginBottom" to "20rpx")), "feature-icon" to padStyleMapOf(utsMapOf("width" to "60rpx", "height" to "60rpx", "marginBottom" to "10rpx")), "feature-title" to padStyleMapOf(utsMapOf("fontSize" to "26rpx", "color" to "#333333", "fontWeight" to "bold", "textAlign" to "center")), "feature-sub" to padStyleMapOf(utsMapOf("fontSize" to "20rpx", "color" to "#999999", "marginTop" to "4rpx", "textAlign" to "center")))
            }
        var inheritAttrs = true
        var inject: Map<String, Map<String, Any?>> = utsMapOf()
        var emits: Map<String, Any?> = utsMapOf()
        var props = normalizePropsOptions(utsMapOf())
        var propsNeedCastKeys: UTSArray<String> = utsArrayOf()
        var components: Map<String, CreateVueComponent> = utsMapOf()
    }
}
