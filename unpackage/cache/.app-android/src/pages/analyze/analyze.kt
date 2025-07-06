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
import io.dcloud.uniapp.extapi.navigateBack as uni_navigateBack
import io.dcloud.uniapp.extapi.showToast as uni_showToast
open class GenPagesAnalyzeAnalyze : BasePage {
    constructor(__ins: ComponentInternalInstance, __renderer: String?) : super(__ins, __renderer) {}
    companion object {
        @Suppress("UNUSED_PARAMETER", "UNUSED_VARIABLE")
        var setup: (__props: GenPagesAnalyzeAnalyze) -> Any? = fun(__props): Any? {
            val __ins = getCurrentInstance()!!
            val _ctx = __ins.proxy as GenPagesAnalyzeAnalyze
            val _cache = __ins.renderCache
            val timeTabs = utsArrayOf<UTSJSONObject>(object : UTSJSONObject() {
                var label = "本周"
                var value = "week"
            }, object : UTSJSONObject() {
                var label = "本月"
                var value = "month"
            }, object : UTSJSONObject() {
                var label = "本年"
                var value = "year"
            })
            val activeTimeTab = ref("month")
            val isLoading = ref(false)
            val trendChartData = ref(UTSJSONObject())
            val categoryChartData = ref(UTSJSONObject())
            val chartColors: UTSJSONObject = object : UTSJSONObject(UTSSourceMapPosition("chartColors", "pages/analyze/analyze.uvue", 108, 7)) {
                var bar = utsArrayOf(
                    "#91CB74",
                    "#EE6666"
                )
                var pie = utsArrayOf(
                    "#1890FF",
                    "#91CB74",
                    "#FAC858",
                    "#EE6666",
                    "#73C0DE",
                    "#3CA272",
                    "#FC8452",
                    "#9A60B4"
                )
            }
            fun gen_goBack_fn() {
                uni_navigateBack(null)
            }
            val goBack = ::gen_goBack_fn
            fun gen_switchTimeTab_fn(tab: String) {
                activeTimeTab.value = tab
                loadData()
            }
            val switchTimeTab = ::gen_switchTimeTab_fn
            fun gen_getPeriodText_fn(): String {
                val now = Date()
                val year = now.getFullYear()
                val month = now.getMonth() + 1
                when (activeTimeTab.value) {
                    "week" -> 
                        return "" + year + "\u5E74\u7B2C" + Math.ceil((now.getTime() - Date(year, 0, 1).getTime()) / 604800000 + 1) + "\u5468"
                    "month" -> 
                        return "" + year + "\u5E74" + month + "\u6708"
                    "year" -> 
                        return "" + year + "\u5E74"
                    else -> 
                        return ""
                }
            }
            val getPeriodText = ::gen_getPeriodText_fn
            fun gen_formatAmount_fn(amount: Number): String {
                return (amount / 100).toFixed(2)
            }
            val formatAmount = ::gen_formatAmount_fn
            fun gen_loadData_fn(): UTSPromise<Unit> {
                return wrapUTSPromise(suspend w@{
                        if (isLoading.value) {
                            return@w
                        }
                        isLoading.value = true
                        try {
                            await(loadTrendDataFromTransactions())
                            await(loadCategoryData())
                        }
                         catch (error: Throwable) {
                            console.error("加载数据失败:", error, " at pages/analyze/analyze.uvue:157")
                            uni_showToast(ShowToastOptions(title = "加载失败，请重试", icon = "none"))
                            trendChartData.value = object : UTSJSONObject() {
                                var categories = utsArrayOf()
                                var series = utsArrayOf(
                                    object : UTSJSONObject() {
                                        var name = "收入"
                                        var data = utsArrayOf()
                                    },
                                    object : UTSJSONObject() {
                                        var name = "支出"
                                        var data = utsArrayOf()
                                    }
                                )
                            }
                            categoryChartData.value = object : UTSJSONObject() {
                                var series = utsArrayOf()
                            }
                        }
                         finally{
                            isLoading.value = false
                        }
                })
            }
            val loadData = ::gen_loadData_fn
            fun gen_loadTrendDataFromTransactions_fn(): UTSPromise<Unit> {
                return wrapUTSPromise(suspend {
                        try {
                            console.log("开始从交易数据生成趋势图，时间范围:", activeTimeTab.value, " at pages/analyze/analyze.uvue:179")
                            val incomeResponse = await(getTransactionListForCategory(object : UTSJSONObject() {
                                var type = activeTimeTab.value
                                var income_expense = "income"
                            }))
                            val expenseResponse = await(getTransactionListForCategory(object : UTSJSONObject() {
                                var type = activeTimeTab.value
                                var income_expense = "expense"
                            }))
                            console.log("收入交易响应:", incomeResponse, " at pages/analyze/analyze.uvue:193")
                            console.log("支出交易响应:", expenseResponse, " at pages/analyze/analyze.uvue:194")
                            var incomeTransactions = utsArrayOf()
                            var expenseTransactions = utsArrayOf()
                            if (incomeResponse.Data?.List) {
                                incomeTransactions = incomeResponse.Data.List
                            }
                            if (expenseResponse.Data?.List) {
                                expenseTransactions = expenseResponse.Data.List
                            }
                            console.log("收入交易数量:", incomeTransactions.length, " at pages/analyze/analyze.uvue:208")
                            console.log("支出交易数量:", expenseTransactions.length, " at pages/analyze/analyze.uvue:209")
                            processTrendDataFromTransactions(incomeTransactions, expenseTransactions)
                        }
                         catch (error: Throwable) {
                            console.error("加载趋势数据失败:", error, " at pages/analyze/analyze.uvue:215")
                            trendChartData.value = object : UTSJSONObject() {
                                var categories = utsArrayOf()
                                var series = utsArrayOf(
                                    object : UTSJSONObject() {
                                        var name = "收入"
                                        var data = utsArrayOf()
                                    },
                                    object : UTSJSONObject() {
                                        var name = "支出"
                                        var data = utsArrayOf()
                                    }
                                )
                            }
                        }
                })
            }
            val loadTrendDataFromTransactions = ::gen_loadTrendDataFromTransactions_fn
            fun gen_processTrendDataFromTransactions_fn(incomeTransactions: UTSArray<Any>, expenseTransactions: UTSArray<Any>) {
                console.log("从交易数据生成趋势图", " at pages/analyze/analyze.uvue:228")
                val categories = utsArrayOf()
                val incomeData = utsArrayOf()
                val expenseData = utsArrayOf()
                val timeRanges = generateTimeRanges()
                console.log("生成的时间段:", timeRanges, " at pages/analyze/analyze.uvue:236")
                timeRanges.forEach(fun(range, index){
                    categories.push(range.label)
                    val incomeInRange = incomeTransactions.filter(fun(transaction): Boolean {
                        val tradeTime = Date(transaction.TradeTime)
                        return tradeTime >= range.start && tradeTime <= range.end
                    }
                    )
                    val totalIncome = incomeInRange.reduce(fun(sum, transaction): Any {
                        return sum + (transaction.Amount || 0)
                    }
                    , 0)
                    val expenseInRange = expenseTransactions.filter(fun(transaction): Boolean {
                        val tradeTime = Date(transaction.TradeTime)
                        return tradeTime >= range.start && tradeTime <= range.end
                    }
                    )
                    val totalExpense = expenseInRange.reduce(fun(sum, transaction): Any {
                        return sum + (transaction.Amount || 0)
                    }
                    , 0)
                    console.log("\u65F6\u95F4\u6BB5" + (index + 1) + " (" + range.label + "): \u6536\u5165 " + totalIncome + ", \u652F\u51FA " + totalExpense, " at pages/analyze/analyze.uvue:261")
                    incomeData.push(totalIncome / 100)
                    expenseData.push(totalExpense / 100)
                }
                )
                trendChartData.value = UTSJSONObject(Map<String, Any?>(utsArrayOf(
                    utsArrayOf(
                        "categories",
                        categories
                    ),
                    utsArrayOf(
                        "series",
                        utsArrayOf(
                            object : UTSJSONObject() {
                                var name = "收入"
                                var data = incomeData
                            },
                            object : UTSJSONObject() {
                                var name = "支出"
                                var data = expenseData
                            }
                        )
                    )
                )))
                console.log("最终趋势图数据:", trendChartData.value, " at pages/analyze/analyze.uvue:281")
            }
            val processTrendDataFromTransactions = ::gen_processTrendDataFromTransactions_fn
            fun gen_generateTimeRanges_fn(): UTSArray<Any?> {
                val now = Date()
                val ranges = utsArrayOf()
                when (activeTimeTab.value) {
                    "week" -> 
                        run {
                            var i: Number = 6
                            while(i >= 0){
                                val date = Date(now)
                                date.setDate(date.getDate() - i)
                                val start = Date(date)
                                start.setHours(0, 0, 0, 0)
                                val end = Date(date)
                                end.setHours(23, 59, 59, 999)
                                ranges.push(UTSJSONObject(Map<String, Any?>(utsArrayOf(
                                    utsArrayOf(
                                        "label",
                                        ("" + (date.getMonth() + 1) + "/" + date.getDate())
                                    ),
                                    utsArrayOf(
                                        "start",
                                        start
                                    ),
                                    utsArrayOf(
                                        "end",
                                        end
                                    )
                                ))))
                                i--
                            }
                        }
                    "month" -> 
                        run {
                            var i: Number = 3
                            while(i >= 0){
                                val weekStart = Date(now)
                                weekStart.setDate(weekStart.getDate() - (weekStart.getDay() + 7 * i))
                                weekStart.setHours(0, 0, 0, 0)
                                val weekEnd = Date(weekStart)
                                weekEnd.setDate(weekEnd.getDate() + 6)
                                weekEnd.setHours(23, 59, 59, 999)
                                ranges.push(object : UTSJSONObject() {
                                    var label = "\u7B2C" + Math.ceil((now.getTime() - Date(now.getFullYear(), 0, 1).getTime()) / 604800000 - i) + "\u5468"
                                    var start = weekStart
                                    var end = weekEnd
                                })
                                i--
                            }
                        }
                    "year" -> 
                        run {
                            var i: Number = 11
                            while(i >= 0){
                                val monthStart = Date(now.getFullYear(), now.getMonth() - i, 1)
                                val monthEnd = Date(now.getFullYear(), now.getMonth() - i + 1, 0, 23, 59, 59, 999)
                                ranges.push(object : UTSJSONObject() {
                                    var label = "" + (monthStart.getMonth() + 1) + "\u6708"
                                    var start = monthStart
                                    var end = monthEnd
                                })
                                i--
                            }
                        }
                }
                return ranges
            }
            val generateTimeRanges = ::gen_generateTimeRanges_fn
            fun gen_loadCategoryData_fn(): UTSPromise<Unit> {
                return wrapUTSPromise(suspend {
                        try {
                            console.log("开始加载分类数据，参数:", object : UTSJSONObject() {
                                var type = activeTimeTab.value
                                var income_expense = "expense"
                            }, " at pages/analyze/analyze.uvue:346")
                            console.log("获取分类列表...", " at pages/analyze/analyze.uvue:352")
                            val categoryResponse = await(getCategoryList(object : UTSJSONObject() {
                                var income_expense = "expense"
                            }))
                            console.log("分类列表响应:", categoryResponse, " at pages/analyze/analyze.uvue:357")
                            val categoryMap = Map()
                            if (categoryResponse.data && categoryResponse.data.length > 0) {
                                categoryResponse.data.forEach(fun(category){
                                    categoryMap.set(category.id, category.name)
                                }
                                )
                                console.log("分类映射:", Object.fromEntries(categoryMap), " at pages/analyze/analyze.uvue:365")
                            }
                            val response = await(getTransactionListForCategory(object : UTSJSONObject() {
                                var type = activeTimeTab.value
                                var income_expense = "expense"
                            }))
                            console.log("交易列表响应:", response, " at pages/analyze/analyze.uvue:374")
                            console.log("交易列表响应类型:", UTSAndroid.`typeof`(response), " at pages/analyze/analyze.uvue:375")
                            console.log("交易列表响应所有属性:", Object.keys(response), " at pages/analyze/analyze.uvue:376")
                            console.log("response.Data:", response.Data, " at pages/analyze/analyze.uvue:379")
                            console.log("response.data:", response.data, " at pages/analyze/analyze.uvue:380")
                            console.log("response.Msg:", response.Msg, " at pages/analyze/analyze.uvue:381")
                            if (response.Data) {
                                console.log("Data 字段类型:", UTSAndroid.`typeof`(response.Data), " at pages/analyze/analyze.uvue:385")
                                console.log("Data 字段所有属性:", Object.keys(response.Data), " at pages/analyze/analyze.uvue:386")
                                console.log("Data.List:", response.Data.List, " at pages/analyze/analyze.uvue:387")
                                console.log("Data.list:", response.Data.list, " at pages/analyze/analyze.uvue:388")
                            }
                            var transactionList = null
                            if (response.Data?.List && response.Data.List.length > 0) {
                                transactionList = response.Data.List
                                console.log("使用 response.Data.List", " at pages/analyze/analyze.uvue:395")
                            } else if (response.Data?.list && response.Data.list.length > 0) {
                                transactionList = response.Data.list
                                console.log("使用 response.Data.list", " at pages/analyze/analyze.uvue:398")
                            } else if (response.data?.List && response.data.List.length > 0) {
                                transactionList = response.data.List
                                console.log("使用 response.data.List", " at pages/analyze/analyze.uvue:401")
                            } else if (response.data?.list && response.data.list.length > 0) {
                                transactionList = response.data.list
                                console.log("使用 response.data.list", " at pages/analyze/analyze.uvue:404")
                            }
                            if (transactionList && transactionList.length > 0) {
                                console.log("开始处理交易数据，数据条数:", transactionList.length, " at pages/analyze/analyze.uvue:408")
                                val categoryStats = generateCategoryStatsFromTransactions(transactionList, categoryMap)
                                updateCategoryChart(categoryStats)
                            } else {
                                console.log("没有交易数据，显示空饼图", " at pages/analyze/analyze.uvue:413")
                                categoryChartData.value = object : UTSJSONObject() {
                                    var series = utsArrayOf()
                                }
                            }
                        }
                         catch (error: Throwable) {
                            console.error("加载分类数据失败:", error, " at pages/analyze/analyze.uvue:417")
                            categoryChartData.value = object : UTSJSONObject() {
                                var series = utsArrayOf()
                            }
                        }
                })
            }
            val loadCategoryData = ::gen_loadCategoryData_fn
            fun gen_generateCategoryStatsFromTransactions_fn(transactions: UTSArray<Any>, categoryMap: Map<Number, String>): UTSArray<*> {
                console.log("从交易数据生成分类统计，原始交易数据:", transactions, " at pages/analyze/analyze.uvue:425")
                val categoryMapForStats = Map()
                transactions.forEach(fun(transaction, index){
                    console.log("\u5904\u7406\u7B2C" + (index + 1) + "\u6761\u4EA4\u6613:", transaction, " at pages/analyze/analyze.uvue:431")
                    console.log("\u7B2C" + (index + 1) + "\u6761\u4EA4\u6613\u7684\u5C5E\u6027:", Object.keys(transaction), " at pages/analyze/analyze.uvue:432")
                    val categoryId = transaction.CategoryId || transaction.categoryId || transaction.Category?.Id || 0
                    var categoryName = "未知分类"
                    val amount = transaction.Amount || transaction.amount || 0
                    if (transaction.CategoryName) {
                        categoryName = transaction.CategoryName
                    } else if (transaction.categoryName) {
                        categoryName = transaction.categoryName
                    } else if (transaction.Category?.Name) {
                        categoryName = transaction.Category.Name
                    } else if (transaction.Category?.name) {
                        categoryName = transaction.Category.name
                    }
                    if (categoryMap.has(categoryId)) {
                        categoryName = categoryMap.get(categoryId) || categoryName
                    }
                    console.log("\u7B2C" + (index + 1) + "\u6761\u4EA4\u6613 - \u5206\u7C7BID: " + categoryId + ", \u5206\u7C7B\u540D\u79F0: " + categoryName + ", \u91D1\u989D: " + amount, " at pages/analyze/analyze.uvue:455")
                    if (categoryMapForStats.has(categoryId)) {
                        val existing = categoryMapForStats.get(categoryId)
                        existing.Amount += amount
                        existing.Count += 1
                    } else {
                        categoryMapForStats.set(categoryId, object : UTSJSONObject() {
                            var Category = object : UTSJSONObject() {
                                var Name = categoryName
                            }
                            var Amount = amount
                            var Count: Number = 1
                        })
                    }
                }
                )
                val categoryStats = UTSArray.from(categoryMapForStats.values()).sort(fun(a, b): Number {
                    return b.Amount - a.Amount
                }
                ).slice(0, 8)
                console.log("生成的分类统计:", categoryStats, " at pages/analyze/analyze.uvue:475")
                return categoryStats
            }
            val generateCategoryStatsFromTransactions = ::gen_generateCategoryStatsFromTransactions_fn
            fun gen_updateCategoryChart_fn(categoryList: UTSArray<Any>) {
                console.log("更新分类图表，原始数据:", categoryList, " at pages/analyze/analyze.uvue:480")
                if (categoryList.length > 0) {
                    val series = categoryList.map(fun(item, index): UTSJSONObject {
                        val name = item.Category?.Name || "未知分类"
                        val data = (item.Amount || 0) / 100
                        console.log("\u5206\u7C7B" + (index + 1) + ": " + name + " - " + data, " at pages/analyze/analyze.uvue:486")
                        return UTSJSONObject(Map<String, Any?>(utsArrayOf(
                            utsArrayOf(
                                "name",
                                name
                            ),
                            utsArrayOf(
                                "data",
                                data
                            )
                        )))
                    })
                    categoryChartData.value = UTSJSONObject(Map<String, Any?>(utsArrayOf(
                        utsArrayOf(
                            "series",
                            series
                        )
                    )))
                    console.log("最终分类饼图数据:", categoryChartData.value, " at pages/analyze/analyze.uvue:491")
                } else {
                    console.log("分类列表为空，显示空饼图", " at pages/analyze/analyze.uvue:493")
                    categoryChartData.value = object : UTSJSONObject() {
                        var series = utsArrayOf()
                    }
                }
            }
            val updateCategoryChart = ::gen_updateCategoryChart_fn
            onMounted(fun(){
                loadData()
            }
            )
            return fun(): Any? {
                return createElementVNode("view", utsMapOf("class" to "container"), utsArrayOf(
                    createElementVNode("view", utsMapOf("class" to "header"), utsArrayOf(
                        createElementVNode("text", utsMapOf("class" to "back", "onClick" to goBack), "←"),
                        createElementVNode("text", utsMapOf("class" to "title"), "财务分析"),
                        createElementVNode("text", utsMapOf("class" to "subtitle"), "深度分析您的财务状况")
                    )),
                    createElementVNode("view", utsMapOf("class" to "time-selector"), utsArrayOf(
                        createElementVNode("view", utsMapOf("class" to "tabs"), utsArrayOf(
                            createElementVNode(Fragment, null, RenderHelpers.renderList(timeTabs, fun(tab, __key, __index, _cached): Any {
                                return createElementVNode("view", utsMapOf("key" to tab["value"], "class" to normalizeClass(utsArrayOf(
                                    "tab",
                                    if (activeTimeTab.value === tab["value"]) {
                                        "active"
                                    } else {
                                        ""
                                    }
                                )), "onClick" to fun(){
                                    switchTimeTab(tab["value"])
                                }
                                ), toDisplayString(tab["label"]), 11, utsArrayOf(
                                    "onClick"
                                ))
                            }
                            ), 64)
                        ))
                    )),
                    createElementVNode("view", utsMapOf("class" to "chart-card"), utsArrayOf(
                        createElementVNode("view", utsMapOf("class" to "chart-header"), utsArrayOf(
                            createElementVNode("text", utsMapOf("class" to "chart-title"), "收支趋势"),
                            createElementVNode("text", utsMapOf("class" to "chart-subtitle"), toDisplayString(getPeriodText()) + "收支变化", 1)
                        )),
                        createElementVNode("view", utsMapOf("class" to "chart-container"), utsArrayOf(
                            if (isTrue(trendChartData.value.categories && trendChartData.value.categories.length > 0)) {
                                createVNode(unref(`default`), utsMapOf("key" to 0, "type" to "bar", "series" to trendChartData.value.series, "categories" to trendChartData.value.categories, "width" to 350, "height" to 200, "colors" to utsArrayOf(
                                    "#91CB74",
                                    "#EE6666"
                                ), "showLegend" to true, "showGrid" to true), null, 8, utsArrayOf(
                                    "series",
                                    "categories"
                                ))
                            } else {
                                createElementVNode("view", utsMapOf("key" to 1, "class" to "empty-chart"), utsArrayOf(
                                    createElementVNode("text", utsMapOf("class" to "empty-text"), "暂无数据")
                                ))
                            }
                        ))
                    )),
                    createElementVNode("view", utsMapOf("class" to "chart-card"), utsArrayOf(
                        createElementVNode("view", utsMapOf("class" to "chart-header"), utsArrayOf(
                            createElementVNode("text", utsMapOf("class" to "chart-title"), "支出分类"),
                            createElementVNode("text", utsMapOf("class" to "chart-subtitle"), "按分类统计支出")
                        )),
                        createElementVNode("view", utsMapOf("class" to "chart-container pie-chart-container"), utsArrayOf(
                            if (isTrue(categoryChartData.value.series && categoryChartData.value.series.length > 0)) {
                                createVNode(unref(`default`), utsMapOf("key" to 0, "type" to "pie", "series" to categoryChartData.value.series, "categories" to utsArrayOf(), "width" to 350, "height" to 300, "colors" to utsArrayOf(
                                    "#1890FF",
                                    "#91CB74",
                                    "#FAC858",
                                    "#EE6666",
                                    "#73C0DE",
                                    "#3CA272",
                                    "#FC8452",
                                    "#9A60B4"
                                ), "showLegend" to false, "showGrid" to false), null, 8, utsArrayOf(
                                    "series"
                                ))
                            } else {
                                createElementVNode("view", utsMapOf("key" to 1, "class" to "empty-chart"), utsArrayOf(
                                    createElementVNode("text", utsMapOf("class" to "empty-text"), "暂无数据")
                                ))
                            }
                        )),
                        if (isTrue(categoryChartData.value.series && categoryChartData.value.series.length > 0)) {
                            createElementVNode("view", utsMapOf("key" to 0, "class" to "custom-legend"), utsArrayOf(
                                createElementVNode(Fragment, null, RenderHelpers.renderList(categoryChartData.value.series, fun(item, idx, __index, _cached): Any {
                                    return createElementVNode("view", utsMapOf("class" to "legend-item", "key" to item.name), utsArrayOf(
                                        createElementVNode("view", utsMapOf("class" to "legend-dot", "style" to normalizeStyle(utsMapOf("backgroundColor" to chartColors["pie"][idx % chartColors["pie"].length]))), null, 4),
                                        createElementVNode("text", utsMapOf("class" to "legend-text"), toDisplayString(item.name), 1)
                                    ))
                                }), 128)
                            ))
                        } else {
                            createCommentVNode("v-if", true)
                        }
                    )),
                    if (isTrue(isLoading.value)) {
                        createElementVNode("view", utsMapOf("key" to 0, "class" to "loading-overlay"), utsArrayOf(
                            createElementVNode("text", utsMapOf("class" to "loading-text"), "加载中...")
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
                return utsMapOf("container" to padStyleMapOf(utsMapOf("backgroundImage" to "none", "backgroundColor" to "#f5f7fa", "paddingBottom" to "10%")), "header" to padStyleMapOf(utsMapOf("position" to "relative", "backgroundImage" to "linear-gradient(135deg, #667eea 0%, #764ba2 100%)", "backgroundColor" to "rgba(0,0,0,0)", "paddingTop" to "80rpx", "paddingRight" to "30rpx", "paddingBottom" to "40rpx", "paddingLeft" to "30rpx", "borderBottomLeftRadius" to "30rpx", "borderBottomRightRadius" to "30rpx", "textAlign" to "center", "boxShadow" to "0 4rpx 20rpx rgba(102, 126, 234, 0.2)")), "back" to padStyleMapOf(utsMapOf("position" to "absolute", "left" to "30rpx", "top" to "80rpx", "fontSize" to "36rpx", "color" to "#FFFFFF", "fontWeight" to "bold")), "title" to padStyleMapOf(utsMapOf("textAlign" to "center", "width" to "100%", "fontSize" to "36rpx", "fontWeight" to "bold", "color" to "#FFFFFF", "marginBottom" to "8rpx")), "subtitle" to padStyleMapOf(utsMapOf("textAlign" to "center", "width" to "100%", "fontSize" to "24rpx", "color" to "rgba(255,255,255,0.8)")), "time-selector" to padStyleMapOf(utsMapOf("marginTop" to "30rpx", "marginRight" to "30rpx", "marginBottom" to "30rpx", "marginLeft" to "30rpx")), "tabs" to padStyleMapOf(utsMapOf("display" to "flex", "justifyContent" to "center", "backgroundImage" to "none", "backgroundColor" to "rgba(255,255,255,0.95)", "borderTopLeftRadius" to "20rpx", "borderTopRightRadius" to "20rpx", "borderBottomRightRadius" to "20rpx", "borderBottomLeftRadius" to "20rpx", "paddingTop" to "6rpx", "paddingRight" to "6rpx", "paddingBottom" to "6rpx", "paddingLeft" to "6rpx", "boxShadow" to "0 4rpx 16rpx rgba(0, 0, 0, 0.08)")), "tab" to utsMapOf("" to utsMapOf("fontSize" to "26rpx", "color" to "#666666", "paddingTop" to "16rpx", "paddingRight" to "32rpx", "paddingBottom" to "16rpx", "paddingLeft" to "32rpx", "borderTopLeftRadius" to "16rpx", "borderTopRightRadius" to "16rpx", "borderBottomRightRadius" to "16rpx", "borderBottomLeftRadius" to "16rpx", "textAlign" to "center", "transitionDuration" to "0.3s"), ".active" to utsMapOf("backgroundImage" to "linear-gradient(135deg, #667eea 0%, #764ba2 100%)", "backgroundColor" to "rgba(0,0,0,0)", "color" to "#FFFFFF", "fontWeight" to "bold")), "chart-card" to padStyleMapOf(utsMapOf("backgroundImage" to "none", "backgroundColor" to "rgba(255,255,255,0.95)", "marginTop" to "30rpx", "marginRight" to "30rpx", "marginBottom" to "30rpx", "marginLeft" to "30rpx", "borderTopLeftRadius" to "20rpx", "borderTopRightRadius" to "20rpx", "borderBottomRightRadius" to "20rpx", "borderBottomLeftRadius" to "20rpx", "paddingTop" to "30rpx", "paddingRight" to "30rpx", "paddingBottom" to "30rpx", "paddingLeft" to "30rpx", "boxShadow" to "0 8rpx 24rpx rgba(0, 0, 0, 0.1)")), "chart-header" to padStyleMapOf(utsMapOf("marginBottom" to "20rpx")), "chart-title" to padStyleMapOf(utsMapOf("fontSize" to "28rpx", "fontWeight" to "bold", "color" to "#333333", "marginBottom" to "8rpx")), "chart-subtitle" to padStyleMapOf(utsMapOf("fontSize" to "22rpx", "color" to "#666666")), "chart-container" to padStyleMapOf(utsMapOf("height" to "400rpx")), "pie-chart-container" to padStyleMapOf(utsMapOf("height" to "600rpx")), "loading-overlay" to padStyleMapOf(utsMapOf("position" to "fixed", "top" to 0, "left" to 0, "right" to 0, "bottom" to 0, "backgroundImage" to "none", "backgroundColor" to "rgba(0,0,0,0.5)", "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "zIndex" to 9999)), "loading-text" to padStyleMapOf(utsMapOf("color" to "#FFFFFF", "fontSize" to "28rpx")), "empty-chart" to padStyleMapOf(utsMapOf("height" to "100%", "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "backgroundColor" to "#f5f7fa", "borderTopLeftRadius" to "10rpx", "borderTopRightRadius" to "10rpx", "borderBottomRightRadius" to "10rpx", "borderBottomLeftRadius" to "10rpx", "marginTop" to "20rpx")), "empty-text" to padStyleMapOf(utsMapOf("fontSize" to "28rpx", "color" to "#999999", "fontWeight" to "bold")), "custom-legend" to padStyleMapOf(utsMapOf("display" to "flex", "flexWrap" to "wrap", "justifyContent" to "center", "marginTop" to "20rpx")), "legend-item" to padStyleMapOf(utsMapOf("display" to "flex", "alignItems" to "center", "marginTop" to 0, "marginRight" to "16rpx", "marginBottom" to "12rpx", "marginLeft" to 0)), "legend-dot" to padStyleMapOf(utsMapOf("width" to "20rpx", "height" to "20rpx", "marginRight" to "8rpx")), "legend-text" to padStyleMapOf(utsMapOf("fontSize" to "24rpx", "color" to "#666666")), "@TRANSITION" to utsMapOf("tab" to utsMapOf("duration" to "0.3s")))
            }
        var inheritAttrs = true
        var inject: Map<String, Map<String, Any?>> = utsMapOf()
        var emits: Map<String, Any?> = utsMapOf()
        var props = normalizePropsOptions(utsMapOf())
        var propsNeedCastKeys: UTSArray<String> = utsArrayOf()
        var components: Map<String, CreateVueComponent> = utsMapOf()
    }
}
