
import { defineComponent, PropType } from 'vue'
const __sfc__ = defineComponent(defineComponent({
  name: 'SimpleChart',
  props: {
    type: {
      type: String as PropType<'line' | 'bar' | 'pie' | 'area'>,
      default: 'line',
      validator: function(value: string): boolean {
        return ['line', 'bar', 'pie', 'area'].indexOf(value) !== -1
      }
    },
    data: {
      type: Array as PropType<any[]>,
      default: function(): any[] { return [] }
    },
    categories: {
      type: Array as PropType<string[]>,
      default: function(): string[] { return [] }
    },
    series: {
      type: Array as PropType<any[]>,
      default: function(): any[] { return [] }
    },
    width: {
      type: Number,
      default: 300
    },
    height: {
      type: Number,
      default: 200
    },
    colors: {
      type: Array as PropType<string[]>,
      default: function(): string[] { return ['#007AFF', '#34C759', '#FF9500', '#FF3B30', '#AF52DE'] }
    },
    showLegend: {
      type: Boolean,
      default: true
    },
    showGrid: {
      type: Boolean,
      default: true
    }
  },
  data(): { chartId: string; loading: boolean; error: string | null; ctx: any; chartData: any } {
    return {
      chartId: '',
      loading: false,
      error: null,
      ctx: null,
      chartData: null
    }
  },
  mounted(): void {
    this.chartId = 'chart_' + Math.random().toString(36).substring(2, 9)
    this.$nextTick(() => {
      this.initChart()
    })
  },
  methods: {
    async initChart(): Promise<void> {
      try {
        this.loading = true
        this.error = null
        this.ctx = uni.createCanvasContext(this.chartId, this)
        this.processData()
        await this.drawChart()
        this.loading = false
      } catch (err: any) {
        this.error = err && err.message ? err.message : '图表加载失败'
        this.loading = false
      }
    },
    processData(): void {
      if (this.data && this.data.length > 0) {
        this.chartData = this.data
      } else if (this.series && this.series.length > 0) {
        this.chartData = {
          categories: this.categories,
          series: this.series
        }
      } else {
        throw new Error('请提供有效的图表数据')
      }
    },
    async drawChart(): Promise<void> {
      if (!this.ctx || !this.chartData) return
      const ctx: any = this.ctx
      const width: number = this.width
      const height: number = this.height
      ctx.clearRect(0, 0, width, height)
      ctx.setFillStyle('#FFFFFF')
      ctx.fillRect(0, 0, width, height)
      switch (this.type) {
        case 'line':
          this.drawLineChart(ctx, width, height)
          break
        case 'bar':
          this.drawBarChart(ctx, width, height)
          break
        case 'pie':
          this.drawPieChart(ctx, width, height)
          break
        case 'area':
          this.drawAreaChart(ctx, width, height)
          break
      }
      if (this.showLegend) {
        this.drawLegend(ctx, width, height)
      }
      ctx.draw()
    },
    drawLineChart(ctx: any, width: number, height: number): void {
      const padding: number = 40
      const chartWidth: number = width - padding * 2
      const chartHeight: number = height - padding * 2
      
      if (!this.chartData.series || this.chartData.series.length === 0) return
      
      const series: any = this.chartData.series[0]
      const data: number[] = series.data || []
      const categories: string[] = this.chartData.categories || []
      
      if (data.length === 0) return
      
      // 计算数据范围
      const minValue: number = Math.min(...data)
      const maxValue: number = Math.max(...data)
      const valueRange: number = maxValue - minValue || 1
      
      // 绘制网格
      if (this.showGrid) {
        this.drawGrid(ctx, padding, chartWidth, chartHeight, categories.length, minValue, maxValue)
      }
      
      // 绘制折线
      ctx.setStrokeStyle(this.colors[0])
      ctx.setLineWidth(2)
      ctx.beginPath()
      
      for (let i = 0; i < data.length; i++) {
        const value: number = data[i]
        const x: number = padding + (i / (data.length - 1)) * chartWidth
        const y: number = padding + chartHeight - ((value - minValue) / valueRange) * chartHeight
        
        if (i === 0) {
          ctx.moveTo(x, y)
        } else {
          ctx.lineTo(x, y)
        }
      }
      
      ctx.stroke()
      
      // 绘制数据点
      ctx.setFillStyle(this.colors[0])
      for (let i = 0; i < data.length; i++) {
        const value: number = data[i]
        const x: number = padding + (i / (data.length - 1)) * chartWidth
        const y: number = padding + chartHeight - ((value - minValue) / valueRange) * chartHeight
        
        ctx.beginPath()
        ctx.arc(x, y, 4, 0, 2 * Math.PI)
        ctx.fill()
      }
    },
    drawBarChart(ctx: any, width: number, height: number): void {
      const padding: number = 40
      const chartWidth: number = width - padding * 2
      const chartHeight: number = height - padding * 2
      
      if (!this.chartData.series || this.chartData.series.length === 0) return
      
      const series: any = this.chartData.series[0]
      const data: number[] = series.data || []
      const categories: string[] = this.chartData.categories || []
      
      if (data.length === 0) return
      
      // 计算数据范围
      const minValue: number = Math.min(...data)
      const maxValue: number = Math.max(...data)
      const valueRange: number = maxValue - minValue || 1
      
      // 绘制网格
      if (this.showGrid) {
        this.drawGrid(ctx, padding, chartWidth, chartHeight, categories.length, minValue, maxValue)
      }
      
      // 绘制柱状图
      const barWidth: number = chartWidth / data.length * 0.8
      const barSpacing: number = chartWidth / data.length * 0.2
      
      for (let i = 0; i < data.length; i++) {
        const value: number = data[i]
        const barHeight: number = ((value - minValue) / valueRange) * chartHeight
        const x: number = padding + i * (barWidth + barSpacing) + barSpacing / 2
        const y: number = padding + chartHeight - barHeight
        
        ctx.setFillStyle(this.colors[i % this.colors.length])
        ctx.fillRect(x, y, barWidth, barHeight)
      }
    },
    drawPieChart(ctx: any, width: number, height: number): void {
      const centerX: number = width / 2
      const centerY: number = height / 2
      const radius: number = Math.min(width, height) / 2 - 40
      
      if (!this.chartData.series || this.chartData.series.length === 0) return
      
      const series: any = this.chartData.series[0]
      const data: number[] = series.data || []
      
      if (data.length === 0) return
      
      const total: number = data.reduce((sum: number, value: number) => sum + value, 0)
      let currentAngle: number = 0
      
      for (let i = 0; i < data.length; i++) {
        const value: number = data[i]
        const sliceAngle: number = (value / total) * 2 * Math.PI
        
        ctx.setFillStyle(this.colors[i % this.colors.length])
        ctx.beginPath()
        ctx.moveTo(centerX, centerY)
        ctx.arc(centerX, centerY, radius, currentAngle, currentAngle + sliceAngle)
        ctx.closePath()
        ctx.fill()
        
        currentAngle += sliceAngle
      }
    },
    drawAreaChart(ctx: any, width: number, height: number): void {
      const padding: number = 40
      const chartWidth: number = width - padding * 2
      const chartHeight: number = height - padding * 2
      
      if (!this.chartData.series || this.chartData.series.length === 0) return
      
      const series: any = this.chartData.series[0]
      const data: number[] = series.data || []
      const categories: string[] = this.chartData.categories || []
      
      if (data.length === 0) return
      
      // 计算数据范围
      const minValue: number = Math.min(...data)
      const maxValue: number = Math.max(...data)
      const valueRange: number = maxValue - minValue || 1
      
      // 绘制网格
      if (this.showGrid) {
        this.drawGrid(ctx, padding, chartWidth, chartHeight, categories.length, minValue, maxValue)
      }
      
      // 绘制面积图
      ctx.setFillStyle(this.colors[0] + '40') // 添加透明度
      ctx.beginPath()
      
      // 绘制到数据点的路径
      for (let i = 0; i < data.length; i++) {
        const value: number = data[i]
        const x: number = padding + (i / (data.length - 1)) * chartWidth
        const y: number = padding + chartHeight - ((value - minValue) / valueRange) * chartHeight
        
        if (i === 0) {
          ctx.moveTo(x, y)
        } else {
          ctx.lineTo(x, y)
        }
      }
      
      // 闭合路径形成面积
      ctx.lineTo(padding + chartWidth, padding + chartHeight)
      ctx.lineTo(padding, padding + chartHeight)
      ctx.closePath()
      ctx.fill()
      
      // 绘制边框线
      ctx.setStrokeStyle(this.colors[0])
      ctx.setLineWidth(2)
      ctx.beginPath()
      
      for (let i = 0; i < data.length; i++) {
        const value: number = data[i]
        const x: number = padding + (i / (data.length - 1)) * chartWidth
        const y: number = padding + chartHeight - ((value - minValue) / valueRange) * chartHeight
        
        if (i === 0) {
          ctx.moveTo(x, y)
        } else {
          ctx.lineTo(x, y)
        }
      }
      
      ctx.stroke()
    },
    drawLegend(ctx: any, width: number, height: number): void {
      if (!this.chartData.series || this.chartData.series.length === 0) return
      
      const legendY: number = height - 30
      const itemWidth: number = 80
      const itemHeight: number = 20
      
      for (let i = 0; i < this.chartData.series.length; i++) {
        const series: any = this.chartData.series[i]
        const x: number = 10 + i * itemWidth
        
        // 绘制颜色块
        ctx.setFillStyle(this.colors[i % this.colors.length])
        ctx.fillRect(x, legendY, 15, itemHeight)
        
        // 绘制文字
        ctx.setFillStyle('#333333')
        ctx.setFontSize(12)
        ctx.fillText(series.name || `系列${i + 1}`, x + 20, legendY + 15)
      }
    },
    drawGrid(ctx: any, padding: number, chartWidth: number, chartHeight: number, count: number, minValue: number, maxValue: number): void {
      ctx.setStrokeStyle('#E0E0E0')
      ctx.setLineWidth(1)
      
      // 绘制垂直网格线
      for (let i = 0; i <= count; i++) {
        const x: number = padding + (i / count) * chartWidth
        ctx.beginPath()
        ctx.moveTo(x, padding)
        ctx.lineTo(x, padding + chartHeight)
        ctx.stroke()
      }
      
      // 绘制水平网格线
      const gridLines: number = 5
      for (let i = 0; i <= gridLines; i++) {
        const y: number = padding + (i / gridLines) * chartHeight
        ctx.beginPath()
        ctx.moveTo(padding, y)
        ctx.lineTo(padding + chartWidth, y)
        ctx.stroke()
      }
    },
    onError(e: any): void {
      this.error = e && e.message ? e.message : 'Canvas错误'
    },
    retry(): void {
      this.initChart()
    },
    onTouchStart(e: any): void {},
    onTouchMove(e: any): void {},
    onTouchEnd(e: any): void {}
  }
}))

export default __sfc__
function GenComponentsSimpleChartRender(this: InstanceType<typeof __sfc__>): any | null {
const _ctx = this
const _cache = this.$.renderCache
  return createElementVNode("view", utsMapOf({
    class: "chart-container",
    id: 'ChartContainer'+_ctx.chartId
  }), [
    isTrue(_ctx.loading)
      ? createElementVNode("view", utsMapOf({
          key: 0,
          class: "loading-container"
        }), [
          createElementVNode("view", utsMapOf({ class: "loading-spinner" })),
          createElementVNode("text", utsMapOf({ class: "loading-text" }), "加载中...")
        ])
      : isTrue(_ctx.error)
        ? createElementVNode("view", utsMapOf({
            key: 1,
            class: "error-container",
            onClick: _ctx.retry
          }), [
            createElementVNode("view", utsMapOf({ class: "error-icon" }), "!"),
            createElementVNode("text", utsMapOf({ class: "error-text" }), toDisplayString(_ctx.error), 1 /* TEXT */),
            createElementVNode("text", utsMapOf({ class: "retry-text" }), "点击重试")
          ], 8 /* PROPS */, ["onClick"])
        : createElementVNode("canvas", utsMapOf({
            key: 2,
            id: _ctx.chartId,
            canvasId: _ctx.chartId,
            style: normalizeStyle(utsMapOf({ width: _ctx.width + 'px', height: _ctx.height + 'px' })),
            onError: _ctx.onError,
            onTouchstart: _ctx.onTouchStart,
            onTouchmove: _ctx.onTouchMove,
            onTouchend: _ctx.onTouchEnd
          }), null, 44 /* STYLE, PROPS, NEED_HYDRATION */, ["id", "canvasId", "onError", "onTouchstart", "onTouchmove", "onTouchend"])
  ], 8 /* PROPS */, ["id"])
}
const GenComponentsSimpleChartStyles = [utsMapOf([["chart-container", padStyleMapOf(utsMapOf([["position", "relative"], ["width", "100%"], ["height", "100%"], ["display", "flex"], ["alignItems", "center"], ["justifyContent", "center"]]))], ["loading-container", padStyleMapOf(utsMapOf([["display", "flex"], ["flexDirection", "column"], ["alignItems", "center"], ["justifyContent", "center"], ["height", "100%"]]))], ["loading-spinner", padStyleMapOf(utsMapOf([["width", "40rpx"], ["height", "40rpx"], ["borderTopWidth", "4rpx"], ["borderRightWidth", "4rpx"], ["borderBottomWidth", "4rpx"], ["borderLeftWidth", "4rpx"], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "#4e54c8"], ["borderRightColor", "#f3f3f3"], ["borderBottomColor", "#f3f3f3"], ["borderLeftColor", "#f3f3f3"], ["animation", "spin 1s linear infinite"], ["marginBottom", "20rpx"]]))], ["loading-text", padStyleMapOf(utsMapOf([["fontSize", "28rpx"], ["color", "#666666"]]))], ["error-container", padStyleMapOf(utsMapOf([["display", "flex"], ["flexDirection", "column"], ["alignItems", "center"], ["justifyContent", "center"], ["height", "100%"], ["paddingTop", "40rpx"], ["paddingRight", "40rpx"], ["paddingBottom", "40rpx"], ["paddingLeft", "40rpx"]]))], ["error-icon", padStyleMapOf(utsMapOf([["width", "80rpx"], ["height", "80rpx"], ["backgroundColor", "#ff3b30"], ["color", "#FFFFFF"], ["display", "flex"], ["alignItems", "center"], ["justifyContent", "center"], ["fontSize", "40rpx"], ["fontWeight", "bold"], ["marginBottom", "20rpx"]]))], ["error-text", padStyleMapOf(utsMapOf([["fontSize", "28rpx"], ["color", "#666666"], ["textAlign", "center"], ["marginBottom", "20rpx"]]))], ["retry-text", padStyleMapOf(utsMapOf([["fontSize", "24rpx"], ["color", "#4e54c8"], ["textDecoration", "underline"]]))], ["@FONT-FACE", utsMapOf([["0", utsMapOf([])]])]])]
