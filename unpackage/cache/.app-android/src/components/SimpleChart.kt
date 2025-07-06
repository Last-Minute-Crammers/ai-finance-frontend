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
import io.dcloud.uniapp.extapi.createCanvasContext as uni_createCanvasContext
open class GenComponentsSimpleChart : VueComponent {
    constructor(__ins: ComponentInternalInstance) : super(__ins) {
        onMounted(fun() {
            this.chartId = "chart_" + Math.random().toString(36).substr(2, 9)
            this.`$nextTick`(fun(){
                this.initChart()
            }
            )
        }
        , __ins)
        this.`$watch`(fun(): Any? {
            return this.data
        }
        , fun() {
            this.initChart()
        }
        , WatchOptions(deep = true))
        this.`$watch`(fun(): Any? {
            return this.series
        }
        , fun() {
            this.initChart()
        }
        , WatchOptions(deep = true))
        this.`$watch`(fun(): Any? {
            return this.type
        }
        , fun() {
            this.initChart()
        }
        )
    }
    @Suppress("UNUSED_PARAMETER", "UNUSED_VARIABLE")
    override fun `$render`(): Any? {
        val _ctx = this
        val _cache = this.`$`.renderCache
        return createElementVNode("view", utsMapOf("class" to "chart-container", "id" to ("ChartContainer" + _ctx.chartId)), utsArrayOf(
            if (isTrue(_ctx.loading)) {
                createElementVNode("view", utsMapOf("key" to 0, "class" to "loading-container"), utsArrayOf(
                    createElementVNode("view", utsMapOf("class" to "loading-spinner")),
                    createElementVNode("text", utsMapOf("class" to "loading-text"), "加载中...")
                ))
            } else {
                if (isTrue(_ctx.error)) {
                    createElementVNode("view", utsMapOf("key" to 1, "class" to "error-container", "onClick" to _ctx.retry), utsArrayOf(
                        createElementVNode("view", utsMapOf("class" to "error-icon"), "!"),
                        createElementVNode("text", utsMapOf("class" to "error-text"), toDisplayString(_ctx.error), 1),
                        createElementVNode("text", utsMapOf("class" to "retry-text"), "点击重试")
                    ), 8, utsArrayOf(
                        "onClick"
                    ))
                } else {
                    createElementVNode("canvas", utsMapOf("key" to 2, "id" to _ctx.chartId, "canvasId" to _ctx.chartId, "style" to normalizeStyle(utsMapOf("width" to (_ctx.width + "px"), "height" to (_ctx.height + "px"))), "onError" to _ctx.onError, "onTouchstart" to _ctx.onTouchStart, "onTouchmove" to _ctx.onTouchMove, "onTouchend" to _ctx.onTouchEnd), null, 44, utsArrayOf(
                        "id",
                        "canvasId",
                        "onError",
                        "onTouchstart",
                        "onTouchmove",
                        "onTouchend"
                    ))
                }
            }
        ), 8, utsArrayOf(
            "id"
        ))
    }
    open var type: String by `$props`
    open var data: UTSArray<Any?> by `$props`
    open var categories: UTSArray<Any?> by `$props`
    open var series: UTSArray<Any?> by `$props`
    open var width: Number by `$props`
    open var height: Number by `$props`
    open var colors: UTSArray<Any?> by `$props`
    open var showLegend: Boolean by `$props`
    open var showGrid: Boolean by `$props`
    open var chartId: String by `$data`
    open var loading: Boolean by `$data`
    open var error: Any? by `$data`
    open var ctx: Any? by `$data`
    open var chartData: Any? by `$data`
    @Suppress("USELESS_CAST")
    override fun data(): Map<String, Any?> {
        return utsMapOf("chartId" to "", "loading" to false, "error" to null, "ctx" to null, "chartData" to null)
    }
    open var initChart = ::gen_initChart_fn
    open fun gen_initChart_fn(): UTSPromise<Unit> {
        return wrapUTSPromise(suspend {
                try {
                    this.loading = true
                    this.error = null
                    this.ctx = uni_createCanvasContext(this.chartId, this)
                    this.processData()
                    await(this.drawChart())
                    this.loading = false
                }
                 catch (err: Throwable) {
                    console.error("图表初始化失败:", err, " at components/SimpleChart.uvue:99")
                    this.error = err.message || "图表加载失败"
                    this.loading = false
                }
        })
    }
    open var processData = ::gen_processData_fn
    open fun gen_processData_fn() {
        if (this.data && this.data.length > 0) {
            this.chartData = this.data
        } else if (this.series && this.series.length > 0) {
            this.chartData = let {
                object : UTSJSONObject() {
                    var categories = it.categories
                    var series = it.series
                }
            }
        } else {
            throw UTSError("请提供有效的图表数据")
        }
    }
    open var drawChart = ::gen_drawChart_fn
    open fun gen_drawChart_fn(): UTSPromise<Unit> {
        return wrapUTSPromise(suspend w@{
                if (!this.ctx || !this.chartData) {
                    return@w
                }
                val ctx = this.ctx
                val width = this.width
                val height = this.height
                ctx.clearRect(0, 0, width, height)
                ctx.setFillStyle("#FFFFFF")
                ctx.fillRect(0, 0, width, height)
                when (this.type) {
                    "line" -> 
                        this.drawLineChart(ctx, width, height)
                    "bar" -> 
                        this.drawBarChart(ctx, width, height)
                    "pie" -> 
                        this.drawPieChart(ctx, width, height)
                    "area" -> 
                        this.drawAreaChart(ctx, width, height)
                }
                if (this.showLegend) {
                    this.drawLegend(ctx, width, height)
                }
                ctx.draw()
        })
    }
    open var drawLineChart = ::gen_drawLineChart_fn
    open fun gen_drawLineChart_fn(ctx, width, height) {
        val padding: Number = 40
        val chartWidth = width - padding * 2
        val chartHeight = height - padding * 2
        if (!this.chartData.series || this.chartData.series.length === 0) {
            return
        }
        val series = this.chartData.series[0]
        val data = series.data || utsArrayOf()
        val categories = this.chartData.categories || utsArrayOf()
        if (data.length === 0) {
            return
        }
        val minValue = Math.min(*data.toTypedArray())
        val maxValue = Math.max(*data.toTypedArray())
        val valueRange = maxValue - minValue || 1
        if (this.showGrid) {
            this.drawGrid(ctx, padding, chartWidth, chartHeight, categories.length, minValue, maxValue)
        }
        ctx.setStrokeStyle(this.colors[0])
        ctx.setLineWidth(2)
        ctx.beginPath()
        data.forEach(fun(value, index){
            val x = padding + (index / (data.length - 1)) * chartWidth
            val y = padding + chartHeight - ((value - minValue) / valueRange) * chartHeight
            if (index === 0) {
                ctx.moveTo(x, y)
            } else {
                ctx.lineTo(x, y)
            }
        }
        )
        ctx.stroke()
        ctx.setFillStyle(this.colors[0])
        data.forEach(fun(value, index){
            val x = padding + (index / (data.length - 1)) * chartWidth
            val y = padding + chartHeight - ((value - minValue) / valueRange) * chartHeight
            ctx.beginPath()
            ctx.arc(x, y, 3, 0, 2 * Math.PI)
            ctx.fill()
        }
        )
    }
    open var drawBarChart = ::gen_drawBarChart_fn
    open fun gen_drawBarChart_fn(ctx, width, height) {
        val padding: Number = 40
        val chartWidth = width - padding * 2
        val chartHeight = height - padding * 2
        if (!this.chartData.series || this.chartData.series.length === 0) {
            return
        }
        val series = this.chartData.series[0]
        val data = series.data || utsArrayOf()
        val categories = this.chartData.categories || utsArrayOf()
        if (data.length === 0) {
            return
        }
        val minValue = Math.min(*data.toTypedArray())
        val maxValue = Math.max(*data.toTypedArray())
        val valueRange = maxValue - minValue || 1
        if (this.showGrid) {
            this.drawGrid(ctx, padding, chartWidth, chartHeight, categories.length, minValue, maxValue)
        }
        val barWidth = chartWidth / data.length * 0.8
        val barSpacing = chartWidth / data.length * 0.2
        ctx.setFillStyle(this.colors[0])
        data.forEach(fun(value, index){
            val x = padding + index * (barWidth + barSpacing) + barSpacing / 2
            val barHeight = ((value - minValue) / valueRange) * chartHeight
            val y = padding + chartHeight - barHeight
            ctx.fillRect(x, y, barWidth, barHeight)
        }
        )
    }
    open var drawPieChart = ::gen_drawPieChart_fn
    open fun gen_drawPieChart_fn(ctx, width, height) {
        val centerX = width / 2
        val centerY = height / 2
        val radius = Math.min(width, height) / 2 - 40
        if (!this.chartData.series || this.chartData.series.length === 0) {
            return
        }
        val series = this.chartData.series[0]
        val data = series.data || utsArrayOf()
        if (data.length === 0) {
            return
        }
        val total = data.reduce(fun(sum, value){
            return sum + value
        }
        , 0)
        var currentAngle = -Math.PI / 2
        data.forEach(fun(value, index){
            val sliceAngle = (value / total) * 2 * Math.PI
            val color = this.colors[index % this.colors.length]
            ctx.setFillStyle(color)
            ctx.beginPath()
            ctx.moveTo(centerX, centerY)
            ctx.arc(centerX, centerY, radius, currentAngle, currentAngle + sliceAngle)
            ctx.closePath()
            ctx.fill()
            currentAngle += sliceAngle
        }
        )
    }
    open var drawAreaChart = ::gen_drawAreaChart_fn
    open fun gen_drawAreaChart_fn(ctx, width, height) {
        val padding: Number = 40
        val chartWidth = width - padding * 2
        val chartHeight = height - padding * 2
        if (!this.chartData.series || this.chartData.series.length === 0) {
            return
        }
        val series = this.chartData.series[0]
        val data = series.data || utsArrayOf()
        if (data.length === 0) {
            return
        }
        val minValue = Math.min(*data.toTypedArray())
        val maxValue = Math.max(*data.toTypedArray())
        val valueRange = maxValue - minValue || 1
        if (this.showGrid) {
            this.drawGrid(ctx, padding, chartWidth, chartHeight, data.length, minValue, maxValue)
        }
        ctx.setFillStyle(this.colors[0] + "40")
        ctx.beginPath()
        data.forEach(fun(value, index){
            val x = padding + (index / (data.length - 1)) * chartWidth
            val y = padding + chartHeight - ((value - minValue) / valueRange) * chartHeight
            if (index === 0) {
                ctx.moveTo(x, y)
            } else {
                ctx.lineTo(x, y)
            }
        }
        )
        ctx.lineTo(padding + chartWidth, padding + chartHeight)
        ctx.lineTo(padding, padding + chartHeight)
        ctx.closePath()
        ctx.fill()
        ctx.setStrokeStyle(this.colors[0])
        ctx.setLineWidth(2)
        ctx.beginPath()
        data.forEach(fun(value, index){
            val x = padding + (index / (data.length - 1)) * chartWidth
            val y = padding + chartHeight - ((value - minValue) / valueRange) * chartHeight
            if (index === 0) {
                ctx.moveTo(x, y)
            } else {
                ctx.lineTo(x, y)
            }
        }
        )
        ctx.stroke()
    }
    open var drawGrid = ::gen_drawGrid_fn
    open fun gen_drawGrid_fn(ctx, padding, chartWidth, chartHeight, dataCount, minValue, maxValue) {
        ctx.setStrokeStyle("#E5E5E5")
        ctx.setLineWidth(1)
        val gridLines: Number = 5
        run {
            var i: Number = 0
            while(i <= gridLines){
                val y = padding + (i / gridLines) * chartHeight
                ctx.beginPath()
                ctx.moveTo(padding, y)
                ctx.lineTo(padding + chartWidth, y)
                ctx.stroke()
                i++
            }
        }
        run {
            var i: Number = 0
            while(i <= dataCount){
                val x = padding + (i / dataCount) * chartWidth
                ctx.beginPath()
                ctx.moveTo(x, padding)
                ctx.lineTo(x, padding + chartHeight)
                ctx.stroke()
                i++
            }
        }
    }
    open var drawLegend = ::gen_drawLegend_fn
    open fun gen_drawLegend_fn(ctx, width, height) {
        if (!this.chartData.series || this.chartData.series.length === 0) {
            return
        }
        val series = this.chartData.series
        val legendY = height - 30
        val legendItemWidth: Number = 80
        val legendItemHeight: Number = 20
        series.forEach(fun(item, index){
            val x = 10 + index * legendItemWidth
            val color = this.colors[index % this.colors.length]
            ctx.setFillStyle(color)
            ctx.fillRect(x, legendY, 15, legendItemHeight)
            ctx.setFillStyle("#333333")
            ctx.setFontSize(12)
            val text = item.name || "\u7CFB\u5217" + (index + 1)
            ctx.fillText(text, x + 20, legendY + 15)
        }
        )
    }
    open var onError = ::gen_onError_fn
    open fun gen_onError_fn(e) {
        console.error("Canvas错误:", e, " at components/SimpleChart.uvue:391")
        this.error = "Canvas渲染错误"
    }
    open var onTouchStart = ::gen_onTouchStart_fn
    open fun gen_onTouchStart_fn(e) {}
    open var onTouchMove = ::gen_onTouchMove_fn
    open fun gen_onTouchMove_fn(e) {}
    open var onTouchEnd = ::gen_onTouchEnd_fn
    open fun gen_onTouchEnd_fn(e) {}
    open var retry = ::gen_retry_fn
    open fun gen_retry_fn() {
        this.initChart()
    }
    open var updateData = ::gen_updateData_fn
    open fun gen_updateData_fn(newData) {
        this.data = newData
        this.initChart()
    }
    open var updateConfig = ::gen_updateConfig_fn
    open fun gen_updateConfig_fn(newConfig) {
        Object.assign(this.`$props`, newConfig)
        this.initChart()
    }
    companion object {
        var name = "SimpleChart"
        val styles: Map<String, Map<String, Map<String, Any>>> by lazy {
            normalizeCssStyles(utsArrayOf(
                styles0
            ))
        }
        val styles0: Map<String, Map<String, Map<String, Any>>>
            get() {
                return utsMapOf("chart-container" to padStyleMapOf(utsMapOf("position" to "relative", "width" to "100%", "height" to "100%")), "loading-container" to padStyleMapOf(utsMapOf("display" to "flex", "flexDirection" to "column", "justifyContent" to "center", "alignItems" to "center", "height" to "100%")), "loading-spinner" to padStyleMapOf(utsMapOf("width" to 40, "height" to 40, "borderTopWidth" to 4, "borderRightWidth" to 4, "borderBottomWidth" to 4, "borderLeftWidth" to 4, "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#007AFF", "borderRightColor" to "#f3f3f3", "borderBottomColor" to "#f3f3f3", "borderLeftColor" to "#f3f3f3", "animation" to "spin 1s linear infinite")), "loading-text" to padStyleMapOf(utsMapOf("marginTop" to 10, "fontSize" to 14, "color" to "#666666")), "error-container" to padStyleMapOf(utsMapOf("display" to "flex", "flexDirection" to "column", "justifyContent" to "center", "alignItems" to "center", "height" to "100%")), "error-icon" to padStyleMapOf(utsMapOf("width" to 60, "height" to 60, "backgroundColor" to "#FF3B30", "color" to "#FFFFFF", "display" to "flex", "justifyContent" to "center", "alignItems" to "center", "fontSize" to 24, "fontWeight" to "bold")), "error-text" to padStyleMapOf(utsMapOf("marginTop" to 10, "fontSize" to 14, "color" to "#666666", "textAlign" to "center")), "retry-text" to padStyleMapOf(utsMapOf("marginTop" to 5, "fontSize" to 12, "color" to "#007AFF")), "@FONT-FACE" to utsMapOf("0" to utsMapOf()))
            }
        var inheritAttrs = true
        var inject: Map<String, Map<String, Any?>> = utsMapOf()
        var emits: Map<String, Any?> = utsMapOf()
        var props = normalizePropsOptions(utsMapOf("type" to utsMapOf("type" to "String", "default" to "line", "validator" to fun(value): Boolean {
            return utsArrayOf(
                "line",
                "bar",
                "pie",
                "area"
            ).includes(value)
        }
        ), "data" to utsMapOf("type" to "Array", "default" to fun(): UTSArray<Any?> {
            return utsArrayOf()
        }
        ), "categories" to utsMapOf("type" to "Array", "default" to fun(): UTSArray<Any?> {
            return utsArrayOf()
        }
        ), "series" to utsMapOf("type" to "Array", "default" to fun(): UTSArray<Any?> {
            return utsArrayOf()
        }
        ), "width" to utsMapOf("type" to "Number", "default" to 300), "height" to utsMapOf("type" to "Number", "default" to 200), "colors" to utsMapOf("type" to "Array", "default" to fun(): UTSArray<String> {
            return utsArrayOf(
                "#007AFF",
                "#34C759",
                "#FF9500",
                "#FF3B30",
                "#AF52DE"
            )
        }
        ), "showLegend" to utsMapOf("type" to "Boolean", "default" to true), "showGrid" to utsMapOf("type" to "Boolean", "default" to true)))
        var propsNeedCastKeys = utsArrayOf(
            "type",
            "data",
            "categories",
            "series",
            "width",
            "height",
            "colors",
            "showLegend",
            "showGrid"
        )
        var components: Map<String, CreateVueComponent> = utsMapOf()
    }
}
