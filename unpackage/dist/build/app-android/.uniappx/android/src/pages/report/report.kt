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
import io.dcloud.uniapp.extapi.navigateBack as uni_navigateBack
import io.dcloud.uniapp.extapi.showToast as uni_showToast
open class GenPagesReportReport : BasePage {
    constructor(__ins: ComponentInternalInstance, __renderer: String?) : super(__ins, __renderer) {}
    companion object {
        @Suppress("UNUSED_PARAMETER", "UNUSED_VARIABLE")
        var setup: (__props: GenPagesReportReport) -> Any? = fun(__props): Any? {
            val __ins = getCurrentInstance()!!
            val _ctx = __ins.proxy as GenPagesReportReport
            val _cache = __ins.renderCache
            fun gen_getTimeRange_fn(type: String): {
                var startTime: String
                var endTime: String
            } {
                val now = Date()
                var startTime: Date
                var endTime: Date = now
                when (type) {
                    "week" -> 
                        {
                            val day = now.getDay()
                            val diff = now.getDate() - day + (if (day === 0) {
                                -6
                            } else {
                                1
                            }
                            )
                            startTime = Date(now.getFullYear(), now.getMonth(), diff)
                        }
                    "month" -> 
                        startTime = Date(now.getFullYear(), now.getMonth(), 1)
                    "year" -> 
                        startTime = Date(now.getFullYear(), 0, 1)
                    else -> 
                        startTime = Date(now.getFullYear(), now.getMonth(), 1)
                }
                return object : UTSJSONObject() {
                    var startTime = startTime.toISOString().split("T")[0]
                    var endTime = endTime.toISOString().split("T")[0]
                }
            }
            val getTimeRange = ::gen_getTimeRange_fn
            val tabs = utsArrayOf(
                "周报",
                "月报",
                "年报"
            )
            val activeTab = ref("月报")
            val summary = ref("")
            val suggestion = ref("")
            val tags = ref(utsArrayOf<String>())
            val isGenerating = ref(false)
            val reportData = ref(utsArrayOf(
                object : UTSJSONObject() {
                    var label = "总收入"
                    var value = "¥0"
                    var trend = "暂无数据"
                    var trendType = "gray"
                },
                object : UTSJSONObject() {
                    var label = "总支出"
                    var value = "¥0"
                    var trend = "暂无数据"
                    var trendType = "gray"
                },
                object : UTSJSONObject() {
                    var label = "储蓄率"
                    var value = "0%"
                    var trend = "暂无数据"
                    var trendType = "gray"
                },
                object : UTSJSONObject() {
                    var label = "净收入"
                    var value = "¥0"
                    var trend = "暂无数据"
                    var trendType = "gray"
                }
            ))
            fun gen_goBack_fn() {
                uni_navigateBack(null)
            }
            val goBack = ::gen_goBack_fn
            fun gen_switchTab_fn(tab: String) {
                activeTab.value = tab
                summary.value = ""
                suggestion.value = ""
                tags.value = utsArrayOf()
                resetReportData()
            }
            val switchTab = ::gen_switchTab_fn
            fun gen_resetReportData_fn() {
                reportData.value = utsArrayOf(
                    object : UTSJSONObject() {
                        var label = "总收入"
                        var value = "¥0"
                        var trend = "暂无数据"
                        var trendType = "gray"
                    },
                    object : UTSJSONObject() {
                        var label = "总支出"
                        var value = "¥0"
                        var trend = "暂无数据"
                        var trendType = "gray"
                    },
                    object : UTSJSONObject() {
                        var label = "储蓄率"
                        var value = "0%"
                        var trend = "暂无数据"
                        var trendType = "gray"
                    },
                    object : UTSJSONObject() {
                        var label = "净收入"
                        var value = "¥0"
                        var trend = "暂无数据"
                        var trendType = "gray"
                    }
                )
            }
            val resetReportData = ::gen_resetReportData_fn
            fun gen_getReportTitle_fn(): String {
                val now = Date()
                val year = now.getFullYear()
                val month = now.getMonth() + 1
                if (activeTab.value === "周报") {
                    val startOfYear = Date(year, 0, 1)
                    val days = Math.floor((now.getTime() - startOfYear.getTime()) / 86400000)
                    val weekNumber = Math.ceil((days + startOfYear.getDay() + 1) / 7)
                    return "" + year + "\u5E74\u7B2C" + weekNumber + "\u5468\u8D22\u52A1\u62A5\u544A"
                } else if (activeTab.value === "月报") {
                    return "" + year + "\u5E74" + month + "\u6708\u8D22\u52A1\u62A5\u544A"
                } else {
                    return "" + year + "\u5E74\u8D22\u52A1\u62A5\u544A"
                }
            }
            val getReportTitle = ::gen_getReportTitle_fn
            fun gen_getCurrentDate_fn(): String {
                val now = Date()
                return "" + now.getFullYear() + "-" + String(now.getMonth() + 1).padStart(2, "0") + "-" + String(now.getDate()).padStart(2, "0")
            }
            val getCurrentDate = ::gen_getCurrentDate_fn
            fun gen_getTimeRangeText_fn(): String {
                val now = Date()
                val year = now.getFullYear()
                val month = now.getMonth() + 1
                val date = now.getDate()
                if (activeTab.value === "周报") {
                    val day = now.getDay()
                    val diff = now.getDate() - day + (if (day === 0) {
                        -6
                    } else {
                        1
                    })
                    val weekStart = Date(year, now.getMonth(), diff)
                    val weekStartMonth = weekStart.getMonth() + 1
                    val weekStartDate = weekStart.getDate()
                    return "" + year + "\u5E74" + weekStartMonth + "\u6708" + weekStartDate + "\u65E5 - " + year + "\u5E74" + month + "\u6708" + date + "\u65E5"
                } else if (activeTab.value === "月报") {
                    return "" + year + "\u5E74" + month + "\u67081\u65E5 - " + year + "\u5E74" + month + "\u6708" + date + "\u65E5"
                } else {
                    return "" + year + "\u5E741\u67081\u65E5 - " + year + "\u5E74" + month + "\u6708" + date + "\u65E5"
                }
            }
            val getTimeRangeText = ::gen_getTimeRangeText_fn
            fun gen_generateReport_fn(): UTSPromise<Unit> {
                return wrapUTSPromise(suspend w@{
                        if (isGenerating.value) {
                            return@w
                        }
                        isGenerating.value = true
                        try {
                            val type = if (activeTab.value === "周报") {
                                "week"
                            } else {
                                if (activeTab.value === "月报") {
                                    "month"
                                } else {
                                    "year"
                                }
                            }
                            console.log("开始生成AI报告，类型:", type)
                            val aiResponse = await(getAIReport(UTSJSONObject(Map<String, Any?>(utsArrayOf(
                                utsArrayOf(
                                    "type",
                                    type
                                )
                            )))))
                            console.log("AI报告生成响应:", aiResponse)
                            if (aiResponse && (aiResponse.summary || aiResponse.suggestion || aiResponse.tags)) {
                                summary.value = aiResponse.summary || ""
                                suggestion.value = aiResponse.suggestion || ""
                                tags.value = aiResponse.tags || utsArrayOf()
                                console.log("AI报告数据处理成功:", object : UTSJSONObject() {
                                    var summary = summary.value
                                    var suggestion = suggestion.value
                                    var tags = tags.value
                                })
                            } else {
                                console.log("AI报告数据格式错误:", aiResponse)
                                throw UTSError("AI返回数据格式错误")
                            }
                            try {
                                val statsResponse = await(getTotalStatistics())
                                console.log("总统计数据响应:", statsResponse)
                                if (statsResponse) {
                                    var totalStats = null
                                    if (statsResponse.Data) {
                                        totalStats = statsResponse.Data
                                    } else if (statsResponse.data) {
                                        totalStats = statsResponse.data
                                    } else {
                                        totalStats = statsResponse
                                    }
                                    console.log("总统计数据:", totalStats)
                                    if (totalStats && (totalStats.Income || totalStats.Expense)) {
                                        updateReportDataWithTotalStats(totalStats)
                                    } else {
                                        console.log("没有找到有效的统计数据")
                                        updateReportDataWithNoData()
                                    }
                                } else {
                                    console.log("没有找到统计数据，显示默认信息")
                                    updateReportDataWithNoData()
                                }
                            }
                             catch (statsError: Throwable) {
                                console.error("获取统计数据失败:", statsError)
                                updateReportDataWithNoData()
                            }
                            uni_showToast(ShowToastOptions(title = "报告生成成功", icon = "success"))
                        }
                         catch (error: Throwable) {
                            console.error("生成报告失败:", error)
                            uni_showToast(ShowToastOptions(title = "生成失败，请重试", icon = "none"))
                            summary.value = "生成报告时出现错误，请检查网络连接或稍后重试。"
                            suggestion.value = "建议检查您的网络连接，确保能够正常访问服务器。"
                            tags.value = utsArrayOf(
                                "网络错误",
                                "请重试",
                                "稍后再来"
                            )
                            updateReportDataWithNoData()
                        }
                         finally{
                            isGenerating.value = false
                        }
                })
            }
            val generateReport = ::gen_generateReport_fn
            fun gen_updateReportDataWithTotalStats_fn(totalStats: Any) {
                console.log("开始处理总统计数据:", totalStats)
                console.log("totalStats类型:", UTSAndroid.`typeof`(totalStats))
                console.log("totalStats.Income:", totalStats.Income)
                console.log("totalStats.Expense:", totalStats.Expense)
                val income = totalStats.Income || object : UTSJSONObject() {
                    var Amount: Number = 0
                    var Count: Number = 0
                }
                val expense = totalStats.Expense || object : UTSJSONObject() {
                    var Amount: Number = 0
                    var Count: Number = 0
                }
                console.log("收入数据:", income)
                console.log("支出数据:", expense)
                console.log("收入Amount:", income.Amount)
                console.log("支出Amount:", expense.Amount)
                val incomeAmount = income.Amount / 100
                val expenseAmount = expense.Amount / 100
                val savings = incomeAmount - expenseAmount
                val savingsRate = if (incomeAmount > 0) {
                    (savings / incomeAmount * 100)
                } else {
                    0
                }
                console.log("计算后的金额:", UTSJSONObject(Map<String, Any?>(utsArrayOf(
                    utsArrayOf(
                        "incomeAmount",
                        incomeAmount
                    ),
                    utsArrayOf(
                        "expenseAmount",
                        expenseAmount
                    ),
                    utsArrayOf(
                        "savings",
                        savings
                    ),
                    utsArrayOf(
                        "savingsRate",
                        savingsRate
                    )
                ))))
                reportData.value = utsArrayOf(
                    object : UTSJSONObject() {
                        var label = "总收入"
                        var value = "\xa5" + incomeAmount.toFixed(2)
                        var trend = "" + income.Count + "\u7B14\u4EA4\u6613"
                        var trendType = "green"
                    },
                    object : UTSJSONObject() {
                        var label = "总支出"
                        var value = "\xa5" + expenseAmount.toFixed(2)
                        var trend = "" + expense.Count + "\u7B14\u4EA4\u6613"
                        var trendType = "red"
                    },
                    object : UTSJSONObject() {
                        var label = "储蓄率"
                        var value = "" + savingsRate.toFixed(1) + "%"
                        var trend = if (savings >= 0) {
                            "正储蓄"
                        } else {
                            "负储蓄"
                        }
                        var trendType = if (savings >= 0) {
                            "green"
                        } else {
                            "red"
                        }
                    },
                    object : UTSJSONObject() {
                        var label = "净收入"
                        var value = "\xa5" + savings.toFixed(2)
                        var trend = if (savings >= 0) {
                            "收入大于支出"
                        } else {
                            "支出大于收入"
                        }
                        var trendType = if (savings >= 0) {
                            "green"
                        } else {
                            "red"
                        }
                    }
                )
                console.log("更新后的报告数据:", reportData.value)
            }
            val updateReportDataWithTotalStats = ::gen_updateReportDataWithTotalStats_fn
            fun gen_updateReportDataWithNoData_fn() {
                reportData.value = utsArrayOf(
                    object : UTSJSONObject() {
                        var label = "总收入"
                        var value = "¥0.00"
                        var trend = "暂无交易记录"
                        var trendType = "gray"
                    },
                    object : UTSJSONObject() {
                        var label = "总支出"
                        var value = "¥0.00"
                        var trend = "暂无交易记录"
                        var trendType = "gray"
                    },
                    object : UTSJSONObject() {
                        var label = "储蓄率"
                        var value = "0.0%"
                        var trend = "暂无数据"
                        var trendType = "gray"
                    },
                    object : UTSJSONObject() {
                        var label = "净收入"
                        var value = "¥0.00"
                        var trend = "暂无数据"
                        var trendType = "gray"
                    }
                )
            }
            val updateReportDataWithNoData = ::gen_updateReportDataWithNoData_fn
            onMounted(fun(){
                console.log("周报时间范围:", getTimeRange("week"))
                console.log("月报时间范围:", getTimeRange("month"))
                console.log("年报时间范围:", getTimeRange("year"))
            }
            )
            return fun(): Any? {
                return createElementVNode("view", utsMapOf("class" to "container"), utsArrayOf(
                    createElementVNode("view", utsMapOf("class" to "header"), utsArrayOf(
                        createElementVNode("text", utsMapOf("class" to "back", "onClick" to goBack), "←"),
                        createElementVNode("text", utsMapOf("class" to "title"), "财务报告"),
                        createElementVNode("text", utsMapOf("class" to "subtitle"), "智能分析您的财务状况"),
                        createElementVNode("view", utsMapOf("class" to "tabs"), utsArrayOf(
                            createElementVNode(Fragment, null, RenderHelpers.renderList(tabs, fun(tab, __key, __index, _cached): Any {
                                return createElementVNode("view", utsMapOf("key" to tab, "class" to normalizeClass(utsArrayOf(
                                    "tab",
                                    if (activeTab.value === tab) {
                                        "active"
                                    } else {
                                        ""
                                    }
                                )), "onClick" to fun(){
                                    switchTab(tab)
                                }
                                ), toDisplayString(tab), 11, utsArrayOf(
                                    "onClick"
                                ))
                            }
                            ), 64)
                        ))
                    )),
                    createElementVNode("view", utsMapOf("class" to "generate-section"), utsArrayOf(
                        createElementVNode("button", utsMapOf("class" to "generate-btn", "onClick" to generateReport, "disabled" to isGenerating.value), utsArrayOf(
                            if (isTrue(isGenerating.value)) {
                                createElementVNode("text", utsMapOf("key" to 0), "生成中...")
                            } else {
                                createElementVNode("text", utsMapOf("key" to 1), "生成" + toDisplayString(activeTab.value), 1)
                            }
                        ), 8, utsArrayOf(
                            "disabled"
                        ))
                    )),
                    if (reportData.value.length > 0) {
                        createElementVNode("view", utsMapOf("key" to 0, "class" to "card report-card"), utsArrayOf(
                            createElementVNode("view", utsMapOf("class" to "report-header"), utsArrayOf(
                                createElementVNode("text", utsMapOf("class" to "report-title"), toDisplayString(getReportTitle()), 1),
                                createElementVNode("text", utsMapOf("class" to "report-date"), "生成于 " + toDisplayString(getCurrentDate()), 1)
                            )),
                            createElementVNode("view", utsMapOf("class" to "time-range"), utsArrayOf(
                                createElementVNode("text", utsMapOf("class" to "time-range-text"), toDisplayString(getTimeRangeText()), 1)
                            )),
                            createElementVNode("view", utsMapOf("class" to "report-grid"), utsArrayOf(
                                createElementVNode(Fragment, null, RenderHelpers.renderList(reportData.value, fun(item, __key, __index, _cached): Any {
                                    return createElementVNode("view", utsMapOf("class" to "report-item", "key" to item.label), utsArrayOf(
                                        createElementVNode("text", utsMapOf("class" to "label"), toDisplayString(item.label), 1),
                                        createElementVNode("text", utsMapOf("class" to "value"), toDisplayString(item.value), 1),
                                        createElementVNode("text", utsMapOf("class" to normalizeClass(utsArrayOf(
                                            "trend",
                                            item.trendType
                                        ))), toDisplayString(item.trend), 3)
                                    ))
                                }), 128)
                            ))
                        ))
                    } else {
                        createCommentVNode("v-if", true)
                    }
                    ,
                    if (isTrue(summary.value || suggestion.value)) {
                        createElementVNode("view", utsMapOf("key" to 1, "class" to "card ai-card"), utsArrayOf(
                            createElementVNode("text", utsMapOf("class" to "section-title"), "AI收支总结"),
                            createElementVNode("view", utsMapOf("class" to "summary"), toDisplayString(summary.value ?: "暂无数据"), 1),
                            createElementVNode("text", utsMapOf("class" to "section-title", "style" to normalizeStyle(utsMapOf("margin-top" to "24rpx"))), "AI理财建议", 4),
                            createElementVNode("view", utsMapOf("class" to "suggestion"), toDisplayString(suggestion.value ?: "暂无建议"), 1),
                            if (tags.value.length > 0) {
                                createElementVNode("view", utsMapOf("key" to 0, "class" to "tag-row"), utsArrayOf(
                                    createElementVNode(Fragment, null, RenderHelpers.renderList(tags.value, fun(tag, __key, __index, _cached): Any {
                                        return createElementVNode("view", utsMapOf("class" to "tag", "key" to tag), toDisplayString(tag), 1)
                                    }), 128)
                                ))
                            } else {
                                createCommentVNode("v-if", true)
                            }
                        ))
                    } else {
                        createCommentVNode("v-if", true)
                    }
                    ,
                    if (isTrue(!summary.value && !suggestion.value && !isGenerating.value)) {
                        createElementVNode("view", utsMapOf("key" to 2, "class" to "empty-state"), utsArrayOf(
                            createElementVNode("text", utsMapOf("class" to "empty-text"), "点击上方按钮生成" + toDisplayString(activeTab.value), 1)
                        ))
                    } else {
                        createCommentVNode("v-if", true)
                    }
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
                return utsMapOf("container" to padStyleMapOf(utsMapOf("backgroundImage" to "none", "backgroundColor" to "#f5f7fa", "paddingBottom" to "10%")), "header" to padStyleMapOf(utsMapOf("position" to "relative", "backgroundImage" to "linear-gradient(to right, #4e54c8, #8f94fb)", "backgroundColor" to "rgba(0,0,0,0)", "paddingTop" to "80rpx", "paddingRight" to "30rpx", "paddingBottom" to "40rpx", "paddingLeft" to "30rpx", "borderBottomLeftRadius" to "30rpx", "borderBottomRightRadius" to "30rpx")), "back" to padStyleMapOf(utsMapOf("position" to "absolute", "left" to "30rpx", "top" to "80rpx", "fontSize" to "36rpx", "color" to "#FFFFFF", "zIndex" to 10)), "title" to padStyleMapOf(utsMapOf("textAlign" to "center", "width" to "100%", "fontSize" to "36rpx", "fontWeight" to "bold", "color" to "#FFFFFF")), "subtitle" to padStyleMapOf(utsMapOf("textAlign" to "center", "width" to "100%", "fontSize" to "24rpx", "color" to "#f0f0f0", "marginTop" to "10rpx")), "tabs" to padStyleMapOf(utsMapOf("marginTop" to "40rpx", "display" to "flex", "justifyContent" to "center", "backgroundImage" to "none", "backgroundColor" to "rgba(255,255,255,0.1)", "borderTopLeftRadius" to "40rpx", "borderTopRightRadius" to "40rpx", "borderBottomRightRadius" to "40rpx", "borderBottomLeftRadius" to "40rpx", "paddingTop" to "6rpx", "paddingRight" to "6rpx", "paddingBottom" to "6rpx", "paddingLeft" to "6rpx")), "tab" to utsMapOf("" to utsMapOf("fontSize" to "26rpx", "color" to "#FFFFFF", "paddingTop" to "12rpx", "paddingRight" to "36rpx", "paddingBottom" to "12rpx", "paddingLeft" to "36rpx", "borderTopLeftRadius" to "30rpx", "borderTopRightRadius" to "30rpx", "borderBottomRightRadius" to "30rpx", "borderBottomLeftRadius" to "30rpx", "textAlign" to "center"), ".active" to utsMapOf("backgroundImage" to "none", "backgroundColor" to "#ffffff", "color" to "#4e54c8", "fontWeight" to "bold")), "generate-section" to padStyleMapOf(utsMapOf("marginTop" to "30rpx", "marginRight" to "30rpx", "marginBottom" to "30rpx", "marginLeft" to "30rpx")), "generate-btn" to padStyleMapOf(utsMapOf("width" to "100%", "backgroundImage" to "linear-gradient(to right, #4e54c8, #8f94fb)", "backgroundColor" to "rgba(0,0,0,0)", "color" to "#FFFFFF", "fontSize" to "28rpx", "borderTopWidth" to "medium", "borderRightWidth" to "medium", "borderBottomWidth" to "medium", "borderLeftWidth" to "medium", "borderTopStyle" to "none", "borderRightStyle" to "none", "borderBottomStyle" to "none", "borderLeftStyle" to "none", "borderTopColor" to "#000000", "borderRightColor" to "#000000", "borderBottomColor" to "#000000", "borderLeftColor" to "#000000", "borderTopLeftRadius" to "16rpx", "borderTopRightRadius" to "16rpx", "borderBottomRightRadius" to "16rpx", "borderBottomLeftRadius" to "16rpx", "paddingTop" to "24rpx", "paddingRight" to 0, "paddingBottom" to "24rpx", "paddingLeft" to 0, "fontWeight" to "bold", "backgroundImage:disabled" to "none", "backgroundColor:disabled" to "#cccccc", "color:disabled" to "#999999")), "card" to padStyleMapOf(utsMapOf("backgroundImage" to "none", "backgroundColor" to "#ffffff", "marginTop" to "30rpx", "marginRight" to "30rpx", "marginBottom" to "30rpx", "marginLeft" to "30rpx", "borderTopLeftRadius" to "20rpx", "borderTopRightRadius" to "20rpx", "borderBottomRightRadius" to "20rpx", "borderBottomLeftRadius" to "20rpx", "paddingTop" to "30rpx", "paddingRight" to "30rpx", "paddingBottom" to "30rpx", "paddingLeft" to "30rpx", "boxShadow" to "0 6rpx 12rpx rgba(0, 0, 0, 0.06)")), "report-header" to padStyleMapOf(utsMapOf("display" to "flex", "justifyContent" to "space-between", "marginBottom" to "20rpx")), "report-title" to padStyleMapOf(utsMapOf("fontSize" to "28rpx", "fontWeight" to "bold")), "report-date" to padStyleMapOf(utsMapOf("fontSize" to "24rpx", "color" to "#999999")), "time-range" to padStyleMapOf(utsMapOf("marginTop" to "20rpx", "paddingTop" to "16rpx", "paddingRight" to 0, "paddingBottom" to "16rpx", "paddingLeft" to 0, "borderTopWidth" to "1rpx", "borderTopStyle" to "solid", "borderTopColor" to "#eeeeee", "borderBottomWidth" to "1rpx", "borderBottomStyle" to "solid", "borderBottomColor" to "#eeeeee", "textAlign" to "center")), "time-range-text" to padStyleMapOf(utsMapOf("fontSize" to "24rpx", "color" to "#666666")), "report-grid" to padStyleMapOf(utsMapOf("display" to "flex", "flexWrap" to "wrap", "flexDirection" to "row", "justifyContent" to "space-between", "gap" to "20rpx 0")), "report-item" to padStyleMapOf(utsMapOf("width" to "48%", "backgroundImage" to "none", "backgroundColor" to "#f9f9f9", "borderTopLeftRadius" to "16rpx", "borderTopRightRadius" to "16rpx", "borderBottomRightRadius" to "16rpx", "borderBottomLeftRadius" to "16rpx", "paddingTop" to "20rpx", "paddingRight" to "20rpx", "paddingBottom" to "20rpx", "paddingLeft" to "20rpx", "display" to "flex", "flexDirection" to "column", "alignItems" to "center", "textAlign" to "center")), "label" to padStyleMapOf(utsMapOf("fontSize" to "22rpx", "color" to "#999999")), "value" to padStyleMapOf(utsMapOf("fontSize" to "30rpx", "fontWeight" to "bold", "marginTop" to "10rpx", "marginRight" to 0, "marginBottom" to "10rpx", "marginLeft" to 0)), "trend" to padStyleMapOf(utsMapOf("fontSize" to "22rpx")), "green" to padStyleMapOf(utsMapOf("color" to "#35b765")), "red" to padStyleMapOf(utsMapOf("color" to "#e74c3c")), "gray" to padStyleMapOf(utsMapOf("color" to "#999999")), "section-title" to padStyleMapOf(utsMapOf("fontSize" to "28rpx", "fontWeight" to "bold", "marginBottom" to "20rpx")), "summary" to padStyleMapOf(utsMapOf("fontSize" to "26rpx", "lineHeight" to 1.6, "color" to "#333333", "marginBottom" to "24rpx")), "suggestion" to padStyleMapOf(utsMapOf("fontSize" to "26rpx", "lineHeight" to 1.6, "color" to "#333333", "marginBottom" to "24rpx")), "tag-row" to padStyleMapOf(utsMapOf("marginTop" to "20rpx", "display" to "flex", "flexWrap" to "wrap", "gap" to "20rpx")), "tag" to padStyleMapOf(utsMapOf("paddingTop" to "12rpx", "paddingRight" to "24rpx", "paddingBottom" to "12rpx", "paddingLeft" to "24rpx", "backgroundImage" to "none", "backgroundColor" to "#eef1f7", "borderTopLeftRadius" to "30rpx", "borderTopRightRadius" to "30rpx", "borderBottomRightRadius" to "30rpx", "borderBottomLeftRadius" to "30rpx", "fontSize" to "22rpx", "color" to "#333333")), "empty-state" to padStyleMapOf(utsMapOf("marginTop" to "100rpx", "marginRight" to "30rpx", "marginBottom" to "100rpx", "marginLeft" to "30rpx", "textAlign" to "center")), "empty-text" to padStyleMapOf(utsMapOf("fontSize" to "28rpx", "color" to "#999999")))
            }
        var inheritAttrs = true
        var inject: Map<String, Map<String, Any?>> = utsMapOf()
        var emits: Map<String, Any?> = utsMapOf()
        var props = normalizePropsOptions(utsMapOf())
        var propsNeedCastKeys: UTSArray<String> = utsArrayOf()
        var components: Map<String, CreateVueComponent> = utsMapOf()
    }
}
