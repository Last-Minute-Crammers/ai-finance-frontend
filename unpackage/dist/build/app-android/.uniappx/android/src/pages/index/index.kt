@file:Suppress("UNCHECKED_CAST", "USELESS_CAST", "INAPPLICABLE_JVM_NAME", "UNUSED_ANONYMOUS_PARAMETER", "NAME_SHADOWING", "UNNECESSARY_NOT_NULL_ASSERTION")
package uni.UNI5A7011F
import io.dcloud.uniapp.*
import io.dcloud.uniapp.extapi.*
import io.dcloud.uniapp.framework.*
import io.dcloud.uniapp.runtime.*
import io.dcloud.uniapp.vue.*
import io.dcloud.uniapp.vue.shared.*
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
            val balance = ref<Number>(0)
            val monthlyNet = ref<Number>(0)
            val loading = ref<Boolean>(true)
            val features = utsArrayOf(
                Feature(name = "记账", desc = "记录每一笔收支", icon = "/static/icons/book.png", path = "/pages/transaction/transaction"),
                Feature(name = "财务分析", desc = "可视化您的消费", icon = "/static/icons/chart.png", path = "/pages/analyze/analyze"),
                Feature(name = "AI理财宠物", desc = "陪伴式理财体验", icon = "/static/icons/pet.png", path = "/pages/pet/pet"),
                Feature(name = "财务报告", desc = "智能分析建议", icon = "/static/icons/report.png", path = "/pages/report/report"),
                Feature(name = "社交互动", desc = "与好友一起理财", icon = "/static/icons/social.png", path = "/pages/social/social"),
                Feature(name = "设置", desc = "个性化您的体验", icon = "/static/icons/settings.png", path = "/pages/settings/settings")
            ) as UTSArray<Feature>
            fun gen_goPage_fn(path: String): Unit {
                uni_navigateTo(NavigateToOptions(url = path))
            }
            val goPage = ::gen_goPage_fn
            fun gen_loadStatisticData_fn(): UTSPromise<Unit> {
                return wrapUTSPromise(suspend w@{
                        try {
                            loading.value = true
                            val token: String? = uni_getStorageSync("token")
                            if (!token) {
                                uni_navigateTo(NavigateToOptions(url = "/pages/login/login"))
                                return@w
                            }
                            val totalRes: Any = await(getTotalStatistic())
                            if (totalRes && totalRes.Data) {
                                balance.value = (totalRes.Data.total_assets || 0) / 100
                            }
                            val now: Date = Date()
                            val monthStart: Date = Date(now.getFullYear(), now.getMonth(), 1)
                            val monthEnd: Date = Date(now.getFullYear(), now.getMonth() + 1, 0)
                            val monthlyRes: Any = await(getMonthStatistic(object : UTSJSONObject() {
                                var startTime = monthStart.toISOString()
                                var endTime = monthEnd.toISOString()
                            }))
                            if (monthlyRes && monthlyRes.Data && monthlyRes.Data.List && monthlyRes.Data.List.length > 0) {
                                val monthData: Any = monthlyRes.Data.List[0]
                                val monthIncome: Number = (if (monthData.Income && monthData.Income.Amount) {
                                    monthData.Income.Amount
                                } else {
                                    0
                                }
                                ) / 100
                                val monthExpense: Number = (if (monthData.Expense && monthData.Expense.Amount) {
                                    monthData.Expense.Amount
                                } else {
                                    0
                                }
                                ) / 100
                                monthlyNet.value = monthIncome - monthExpense
                            }
                        }
                         catch (error: Throwable) {
                            balance.value = 0
                            monthlyNet.value = 0
                            uni_showToast(ShowToastOptions(title = "数据加载失败", icon = "none"))
                        }
                         finally{
                            loading.value = false
                        }
                })
            }
            val loadStatisticData = ::gen_loadStatisticData_fn
            onMounted(fun(): Unit {
                loadStatisticData()
            }
            )
            __expose(utsMapOf("onShow" to fun(): Unit {
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
                                goPage(item.path)
                            }
                            ), utsArrayOf(
                                createElementVNode("image", utsMapOf("src" to item.icon, "class" to "feature-icon"), null, 8, utsArrayOf(
                                    "src"
                                )),
                                createElementVNode("text", utsMapOf("class" to "feature-title"), toDisplayString(item.name), 1),
                                createElementVNode("text", utsMapOf("class" to "feature-sub"), toDisplayString(item.desc), 1)
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
                return utsMapOf("container" to padStyleMapOf(utsMapOf("display" to "flex", "flexDirection" to "column", "backgroundColor" to "#f2f3f5")), "header" to padStyleMapOf(utsMapOf("height" to "140rpx", "backgroundImage" to "linear-gradient(to right, #4e54c8, #8f94fb)", "backgroundColor" to "rgba(0,0,0,0)", "display" to "flex", "alignItems" to "center", "justifyContent" to "center")), "title" to padStyleMapOf(utsMapOf("color" to "#FFFFFF", "fontSize" to "36rpx", "fontWeight" to "bold")), "balance-card" to padStyleMapOf(utsMapOf("backgroundColor" to "#ffffff", "borderTopLeftRadius" to "20rpx", "borderTopRightRadius" to "20rpx", "borderBottomRightRadius" to "20rpx", "borderBottomLeftRadius" to "20rpx", "paddingTop" to "24rpx", "paddingRight" to 0, "paddingBottom" to "24rpx", "paddingLeft" to 0, "width" to "80%", "marginTop" to "-30rpx", "marginRight" to "auto", "marginBottom" to "20rpx", "marginLeft" to "auto", "textAlign" to "center", "boxShadow" to "0 4rpx 12rpx rgba(0, 0, 0, 0.08)", "display" to "flex", "flexDirection" to "column", "alignItems" to "center")), "summary-block" to padStyleMapOf(utsMapOf("display" to "flex", "flexDirection" to "column", "alignItems" to "center", "paddingTop" to "20rpx", "paddingRight" to 0, "paddingBottom" to "20rpx", "paddingLeft" to 0)), "balance-label" to padStyleMapOf(utsMapOf("fontSize" to "28rpx", "color" to "#666666", "marginBottom" to "10rpx")), "balance-value" to utsMapOf("" to utsMapOf("fontSize" to "48rpx", "fontWeight" to "bold", "color" to "#333333"), ".loading" to utsMapOf("fontSize" to "32rpx", "color" to "#999999"), ".income" to utsMapOf("color" to "#34C759"), ".expense" to utsMapOf("color" to "#FF3B30")), "divider" to padStyleMapOf(utsMapOf("width" to "80%", "height" to "1rpx", "backgroundColor" to "#eeeeee", "marginTop" to "10rpx", "marginRight" to 0, "marginBottom" to "10rpx", "marginLeft" to 0)), "expense-row" to padStyleMapOf(utsMapOf("display" to "flex", "alignItems" to "center", "gap" to "10rpx")), "expense-icon" to utsMapOf("" to utsMapOf("fontSize" to "32rpx"), ".positive" to utsMapOf("color" to "#34C759"), ".negative" to utsMapOf("color" to "#FF3B30")), "feature-grid" to padStyleMapOf(utsMapOf("flex" to 1, "gridTemplateColumns" to "repeat(2, 1fr)", "gap" to "20rpx", "paddingTop" to "20rpx", "paddingRight" to "20rpx", "paddingBottom" to "20rpx", "paddingLeft" to "20rpx", "overflowY" to "auto")), "feature-item" to padStyleMapOf(utsMapOf("backgroundColor" to "#ffffff", "borderTopLeftRadius" to "20rpx", "borderTopRightRadius" to "20rpx", "borderBottomRightRadius" to "20rpx", "borderBottomLeftRadius" to "20rpx", "paddingTop" to "40rpx", "paddingRight" to "30rpx", "paddingBottom" to "40rpx", "paddingLeft" to "30rpx", "textAlign" to "center", "boxShadow" to "0 4rpx 12rpx rgba(0, 0, 0, 0.08)", "transitionDuration" to "0.3s", "transitionTimingFunction" to "ease", "transform:active" to "scale(0.98)", "boxShadow:active" to "0 2rpx 8rpx rgba(0, 0, 0, 0.12)")), "feature-icon" to padStyleMapOf(utsMapOf("width" to "80rpx", "height" to "80rpx", "marginBottom" to "20rpx")), "feature-title" to padStyleMapOf(utsMapOf("fontSize" to "32rpx", "fontWeight" to "bold", "color" to "#333333", "marginBottom" to "10rpx")), "feature-sub" to padStyleMapOf(utsMapOf("fontSize" to "24rpx", "color" to "#666666", "lineHeight" to 1.4)), "@TRANSITION" to utsMapOf("feature-item" to utsMapOf("duration" to "0.3s", "timingFunction" to "ease")))
            }
        var inheritAttrs = true
        var inject: Map<String, Map<String, Any?>> = utsMapOf()
        var emits: Map<String, Any?> = utsMapOf()
        var props = normalizePropsOptions(utsMapOf())
        var propsNeedCastKeys: UTSArray<String> = utsArrayOf()
        var components: Map<String, CreateVueComponent> = utsMapOf()
    }
}
