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
        onMounted(fun(): Unit {
            this.chartId = "chart_" + Math.random().toString(36).substring(2, 9)
            this.`$nextTick`(fun(){
                this.initChart()
            }
            )
        }
        , __ins)
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
                    console.error("图表初始化失败:", err, " at components/SimpleChart.uvue:109")
                    this.error = err.message || "图表加载失败"
                    this.loading = false
                }
        })
    }
    open var processData = ::gen_processData_fn
    open fun gen_processData_fn(): Unit {
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
                val ctx: Any = this.ctx
                val width: Number = this.width
                val height: Number = this.height
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
    open fun gen_drawLineChart_fn(ctx: Any, width: Number, height: Number): Unit {
        val padding: Number = 40
        val chartWidth: Number = width - padding * 2
        val chartHeight: Number = height - padding * 2
        if (!this.chartData.series || this.chartData.series.length === 0) {
            return
        }
        val series: Any = this.chartData.series[0]
        val data: UTSArray<Number> = series.data || utsArrayOf()
        val categories: UTSArray<String> = this.chartData.categories || utsArrayOf()
        if (data.length === 0) {
            return
        }
        val minValue: Number = Math.min(*data.toTypedArray())
        val maxValue: Number = Math.max(*data.toTypedArray())
        val valueRange: Number = maxValue - minValue || 1
        if (this.showGrid) {
            this.drawGrid(ctx, padding, chartWidth, chartHeight, categories.length, minValue, maxValue)
        }
        ctx.setStrokeStyle(this.colors[0])
        ctx.setLineWidth(2)
        ctx.beginPath()
        run {
            var i: Number = 0
            while(i < data.length){
                val value: Number = data[i]
                val x: Number = padding + (i / (data.length - 1)) * chartWidth
                val y: Number = padding + chartHeight - ((value - minValue) / valueRange) * chartHeight
                if (i === 0) {
                    ctx.moveTo(x, y)
                } else {
                    ctx.lineTo(x, y)
                }
                i++
            }
        }
        ctx.stroke()
        ctx.setFillStyle(this.colors[0])
        run {
            var i: Number = 0
            while(i < data.length){
                val value: Number = data[i]
                val x: Number = padding + (i / (data.length - 1)) * chartWidth
                val y: Number = padding + chartHeight - ((value - minValue) / valueRange) * chartHeight
                ctx.beginPath()
                ctx.arc(x, y, 4, 0, 2 * Math.PI)
                ctx.fill()
                i++
            }
        }
    }
    open var drawBarChart = ::gen_drawBarChart_fn
    open fun gen_drawBarChart_fn(ctx: Any, width: Number, height: Number): Unit {
        val padding: Number = 40
        val chartWidth: Number = width - padding * 2
        val chartHeight: Number = height - padding * 2
        if (!this.chartData.series || this.chartData.series.length === 0) {
            return
        }
        val series: Any = this.chartData.series[0]
        val data: UTSArray<Number> = series.data || utsArrayOf()
        val categories: UTSArray<String> = this.chartData.categories || utsArrayOf()
        if (data.length === 0) {
            return
        }
        val minValue: Number = Math.min(*data.toTypedArray())
        val maxValue: Number = Math.max(*data.toTypedArray())
        val valueRange: Number = maxValue - minValue || 1
        if (this.showGrid) {
            this.drawGrid(ctx, padding, chartWidth, chartHeight, categories.length, minValue, maxValue)
        }
        val barWidth: Number = chartWidth / data.length * 0.8
        val barSpacing: Number = chartWidth / data.length * 0.2
        run {
            var i: Number = 0
            while(i < data.length){
                val value: Number = data[i]
                val barHeight: Number = ((value - minValue) / valueRange) * chartHeight
                val x: Number = padding + i * (barWidth + barSpacing) + barSpacing / 2
                val y: Number = padding + chartHeight - barHeight
                ctx.setFillStyle(this.colors[i % this.colors.length])
                ctx.fillRect(x, y, barWidth, barHeight)
                i++
            }
        }
    }
    open var drawPieChart = ::gen_drawPieChart_fn
    open fun gen_drawPieChart_fn(ctx: Any, width: Number, height: Number): Unit {
        val centerX: Number = width / 2
        val centerY: Number = height / 2
        val radius: Number = Math.min(width, height) / 2 - 40
        if (!this.chartData.series || this.chartData.series.length === 0) {
            return
        }
        val series: Any = this.chartData.series[0]
        val data: UTSArray<Number> = series.data || utsArrayOf()
        if (data.length === 0) {
            return
        }
        val total: Number = data.reduce(fun(sum: Number, value: Number): Number {
            return sum + value
        }
        , 0)
        var currentAngle: Number = 0
        run {
            var i: Number = 0
            while(i < data.length){
                val value: Number = data[i]
                val sliceAngle: Number = (value / total) * 2 * Math.PI
                ctx.setFillStyle(this.colors[i % this.colors.length])
                ctx.beginPath()
                ctx.moveTo(centerX, centerY)
                ctx.arc(centerX, centerY, radius, currentAngle, currentAngle + sliceAngle)
                ctx.closePath()
                ctx.fill()
                currentAngle += sliceAngle
                i++
            }
        }
    }
    open var drawAreaChart = ::gen_drawAreaChart_fn
    open fun gen_drawAreaChart_fn(ctx: Any, width: Number, height: Number): Unit {
        val padding: Number = 40
        val chartWidth: Number = width - padding * 2
        val chartHeight: Number = height - padding * 2
        if (!this.chartData.series || this.chartData.series.length === 0) {
            return
        }
        val series: Any = this.chartData.series[0]
        val data: UTSArray<Number> = series.data || utsArrayOf()
        val categories: UTSArray<String> = this.chartData.categories || utsArrayOf()
        if (data.length === 0) {
            return
        }
        val minValue: Number = Math.min(*data.toTypedArray())
        val maxValue: Number = Math.max(*data.toTypedArray())
        val valueRange: Number = maxValue - minValue || 1
        if (this.showGrid) {
            this.drawGrid(ctx, padding, chartWidth, chartHeight, categories.length, minValue, maxValue)
        }
        ctx.setFillStyle(this.colors[0] + "40")
        ctx.beginPath()
        run {
            var i: Number = 0
            while(i < data.length){
                val value: Number = data[i]
                val x: Number = padding + (i / (data.length - 1)) * chartWidth
                val y: Number = padding + chartHeight - ((value - minValue) / valueRange) * chartHeight
                if (i === 0) {
                    ctx.moveTo(x, y)
                } else {
                    ctx.lineTo(x, y)
                }
                i++
            }
        }
        ctx.lineTo(padding + chartWidth, padding + chartHeight)
        ctx.lineTo(padding, padding + chartHeight)
        ctx.closePath()
        ctx.fill()
        ctx.setStrokeStyle(this.colors[0])
        ctx.setLineWidth(2)
        ctx.beginPath()
        run {
            var i: Number = 0
            while(i < data.length){
                val value: Number = data[i]
                val x: Number = padding + (i / (data.length - 1)) * chartWidth
                val y: Number = padding + chartHeight - ((value - minValue) / valueRange) * chartHeight
                if (i === 0) {
                    ctx.moveTo(x, y)
                } else {
                    ctx.lineTo(x, y)
                }
                i++
            }
        }
        ctx.stroke()
    }
    open var drawGrid = ::gen_drawGrid_fn
    open fun gen_drawGrid_fn(ctx: Any, padding: Number, chartWidth: Number, chartHeight: Number, dataCount: Number, minValue: Number, maxValue: Number): Unit {
        ctx.setStrokeStyle("#E0E0E0")
        ctx.setLineWidth(1)
        run {
            var i: Number = 0
            while(i <= dataCount){
                val x: Number = padding + (i / dataCount) * chartWidth
                ctx.beginPath()
                ctx.moveTo(x, padding)
                ctx.lineTo(x, padding + chartHeight)
                ctx.stroke()
                i++
            }
        }
        val gridLines: Number = 5
        run {
            var i: Number = 0
            while(i <= gridLines){
                val y: Number = padding + (i / gridLines) * chartHeight
                ctx.beginPath()
                ctx.moveTo(padding, y)
                ctx.lineTo(padding + chartWidth, y)
                ctx.stroke()
                i++
            }
        }
    }
    open var drawLegend = ::gen_drawLegend_fn
    open fun gen_drawLegend_fn(ctx: Any, width: Number, height: Number): Unit {
        if (!this.chartData.series || this.chartData.series.length === 0) {
            return
        }
        val legendY: Number = height - 30
        val itemWidth: Number = 80
        val itemHeight: Number = 20
        run {
            var i: Number = 0
            while(i < this.chartData.series.length){
                val series: Any = this.chartData.series[i]
                val x: Number = 10 + i * itemWidth
                ctx.setFillStyle(this.colors[i % this.colors.length])
                ctx.fillRect(x, legendY, 15, itemHeight)
                ctx.setFillStyle("#333333")
                ctx.setFontSize(12)
                ctx.fillText(series.name || "\u7CFB\u5217" + (i + 1), x + 20, legendY + 15)
                i++
            }
        }
    }
    open var onError = ::gen_onError_fn
    open fun gen_onError_fn(event: Any): Unit {
        console.error("Canvas错误:", event, " at components/SimpleChart.uvue:403")
        this.error = "图表渲染失败"
    }
    open var onTouchStart = ::gen_onTouchStart_fn
    open fun gen_onTouchStart_fn(event: Any): Unit {
        console.log("触摸开始:", event, " at components/SimpleChart.uvue:409")
    }
    open var onTouchMove = ::gen_onTouchMove_fn
    open fun gen_onTouchMove_fn(event: Any): Unit {
        console.log("触摸移动:", event, " at components/SimpleChart.uvue:414")
    }
    open var onTouchEnd = ::gen_onTouchEnd_fn
    open fun gen_onTouchEnd_fn(event: Any): Unit {
        console.log("触摸结束:", event, " at components/SimpleChart.uvue:419")
    }
    open var retry = ::gen_retry_fn
    open fun gen_retry_fn(): Unit {
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
                return utsMapOf("chart-container" to padStyleMapOf(utsMapOf("position" to "relative", "width" to "100%", "height" to "100%", "display" to "flex", "alignItems" to "center", "justifyContent" to "center")), "loading-container" to padStyleMapOf(utsMapOf("display" to "flex", "flexDirection" to "column", "alignItems" to "center", "justifyContent" to "center", "height" to "100%")), "loading-spinner" to padStyleMapOf(utsMapOf("width" to "40rpx", "height" to "40rpx", "borderTopWidth" to "4rpx", "borderRightWidth" to "4rpx", "borderBottomWidth" to "4rpx", "borderLeftWidth" to "4rpx", "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#4e54c8", "borderRightColor" to "#f3f3f3", "borderBottomColor" to "#f3f3f3", "borderLeftColor" to "#f3f3f3", "animation" to "spin 1s linear infinite", "marginBottom" to "20rpx")), "loading-text" to padStyleMapOf(utsMapOf("fontSize" to "28rpx", "color" to "#666666")), "error-container" to padStyleMapOf(utsMapOf("display" to "flex", "flexDirection" to "column", "alignItems" to "center", "justifyContent" to "center", "height" to "100%", "paddingTop" to "40rpx", "paddingRight" to "40rpx", "paddingBottom" to "40rpx", "paddingLeft" to "40rpx")), "error-icon" to padStyleMapOf(utsMapOf("width" to "80rpx", "height" to "80rpx", "backgroundColor" to "#ff3b30", "color" to "#FFFFFF", "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "fontSize" to "40rpx", "fontWeight" to "bold", "marginBottom" to "20rpx")), "error-text" to padStyleMapOf(utsMapOf("fontSize" to "28rpx", "color" to "#666666", "textAlign" to "center", "marginBottom" to "20rpx")), "retry-text" to padStyleMapOf(utsMapOf("fontSize" to "24rpx", "color" to "#4e54c8", "textDecoration" to "underline")), "@FONT-FACE" to utsMapOf("0" to utsMapOf()))
            }
        var inheritAttrs = true
        var inject: Map<String, Map<String, Any?>> = utsMapOf()
        var emits: Map<String, Any?> = utsMapOf()
        var props = normalizePropsOptions(utsMapOf("type" to utsMapOf("type" to "String", "default" to "line", "validator" to fun(value: String): Boolean {
            return utsArrayOf(
                "line",
                "bar",
                "pie",
                "area"
            ).includes(value)
        }
        ), "data" to utsMapOf("type" to "Array", "default" to fun(): UTSArray<Any> {
            return utsArrayOf()
        }
        ), "categories" to utsMapOf("type" to "Array", "default" to fun(): UTSArray<Any> {
            return utsArrayOf()
        }
        ), "series" to utsMapOf("type" to "Array", "default" to fun(): UTSArray<Any> {
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
