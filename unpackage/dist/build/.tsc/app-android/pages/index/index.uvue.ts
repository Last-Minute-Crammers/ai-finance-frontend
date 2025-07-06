import { ref, onMounted } from 'vue'
import { getTotalStatistic, getMonthStatistic } from '../../common/api/transaction'


const __sfc__ = defineComponent({
  __name: 'index',
  setup(__props, { expose: __expose }: SetupContext): any | null {
const __ins = getCurrentInstance()!;
const _ctx = __ins.proxy as InstanceType<typeof __sfc__>;
const _cache = __ins.renderCache;

const balance = ref(0)
const monthlyNet = ref(0)
const loading = ref(true)

const features = [
  { name: '记账', desc: '记录每一笔收支', icon: '/static/icons/book.png', path: '/pages/transaction/transaction' },
  { name: '财务分析', desc: '可视化您的消费', icon: '/static/icons/chart.png', path: '/pages/analyze/analyze' },
  { name: 'AI理财宠物', desc: '陪伴式理财体验', icon: '/static/icons/pet.png', path: '/pages/pet/pet' },
  { name: '财务报告', desc: '智能分析建议', icon: '/static/icons/report.png', path: '/pages/report/report' },
  { name: '社交互动', desc: '与好友一起理财', icon: '/static/icons/social.png', path: '/pages/social/social' },
  { name: '设置', desc: '个性化您的体验', icon: '/static/icons/settings.png', path: '/pages/settings/settings' }
]

const goPage = (path: string) => {
  uni.navigateTo({ url: path })
}

// 加载统计数据
const loadStatisticData = async () => {
  try {
    loading.value = true
    
    // 检查token是否存在
    const token = uni.getStorageSync('token')
    if (!token) {
      console.error('没有找到token，跳转到登录页')
      uni.navigateTo({ url: '/pages/login/login' })
      return
    }
    
    // 获取总资产统计
    const totalRes = await getTotalStatistic()
    console.log('=== 总统计响应 ===')
    console.log('完整响应:', JSON.stringify(totalRes, null, 2))
    
    if (totalRes?.Data) {
      console.log('总资产字段:', totalRes.Data.total_assets)
      console.log('收入数据:', totalRes.Data.Income)
      console.log('支出数据:', totalRes.Data.Expense)
      
      // 使用正确的字段名
      balance.value = (totalRes.Data.total_assets || 0) / 100 // 转换为元
      console.log('最终设置的余额:', balance.value)
    }
    
    // 获取本月统计
    const now = new Date()
    const monthStart = new Date(now.getFullYear(), now.getMonth(), 1)
    const monthEnd = new Date(now.getFullYear(), now.getMonth() + 1, 0)
    
    const monthlyRes = await getMonthStatistic({
      startTime: monthStart.toISOString(),
      endTime: monthEnd.toISOString()
    })
    
    console.log('=== 月度统计响应 ===')
    console.log('完整响应:', JSON.stringify(monthlyRes, null, 2))
    
    if (monthlyRes?.Data?.List?.length > 0) {
      const monthData = monthlyRes.Data.List[0]
      console.log('月度数据:', JSON.stringify(monthData, null, 2))
      
      // 使用正确的字段名
      const monthIncome = (monthData.Income?.Amount || 0) / 100
      const monthExpense = (monthData.Expense?.Amount || 0) / 100
      monthlyNet.value = monthIncome - monthExpense
      
      console.log('月度收支计算:', {
        income: monthIncome,
        expense: monthExpense,
        net: monthlyNet.value
      })
    }
    
  } catch (error) {
    console.error('加载统计数据失败:', error)
    
    const errorMessage = String(error)
    if (errorMessage.includes('Unauthorized')) {
      uni.removeStorageSync('token')
      uni.removeStorageSync('current_user')
      uni.showToast({
        title: '登录已过期，请重新登录',
        icon: 'none'
      })
      setTimeout(() => {
        uni.navigateTo({ url: '/pages/login/login' })
      }, 1500)
    } else {
      balance.value = 0
      monthlyNet.value = 0
      uni.showToast({
        title: '数据加载失败',
        icon: 'none'
      })
    }
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadStatisticData()
})

// 使用uni-app的生命周期钩子
__expose({
  onShow() {
    loadStatisticData()
  }
})

return (): any | null => {

  return createElementVNode("view", utsMapOf({ class: "container" }), [
    createElementVNode("view", utsMapOf({ class: "header" }), [
      createElementVNode("text", utsMapOf({ class: "title" }), "智能记账")
    ]),
    createElementVNode("view", utsMapOf({ class: "balance-card" }), [
      createElementVNode("view", utsMapOf({ class: "summary-block" }), [
        createElementVNode("text", utsMapOf({ class: "balance-label" }), "当前余额"),
        isTrue(!loading.value)
          ? createElementVNode("text", utsMapOf({
              key: 0,
              class: "balance-value"
            }), "¥" + toDisplayString(balance.value.toFixed(2)), 1 /* TEXT */)
          : createElementVNode("text", utsMapOf({
              key: 1,
              class: "balance-value loading"
            }), "加载中...")
      ]),
      createElementVNode("view", utsMapOf({ class: "divider" })),
      createElementVNode("view", utsMapOf({ class: "summary-block" }), [
        createElementVNode("text", utsMapOf({ class: "balance-label" }), "本月收支"),
        isTrue(!loading.value)
          ? createElementVNode("view", utsMapOf({
              key: 0,
              class: "expense-row"
            }), [
              createElementVNode("text", utsMapOf({
                class: normalizeClass(["expense-icon", monthlyNet.value >= 0 ? 'positive' : 'negative'])
              }), toDisplayString(monthlyNet.value >= 0 ? '🔺' : '🔻'), 3 /* TEXT, CLASS */),
              createElementVNode("text", utsMapOf({
                class: normalizeClass(["balance-value", monthlyNet.value >= 0 ? 'income' : 'expense'])
              }), " ¥" + toDisplayString(Math.abs(monthlyNet.value).toFixed(2)), 3 /* TEXT, CLASS */)
            ])
          : createElementVNode("text", utsMapOf({
              key: 1,
              class: "balance-value loading"
            }), "加载中...")
      ])
    ]),
    createElementVNode("view", utsMapOf({ class: "feature-grid" }), [
      createElementVNode(Fragment, null, RenderHelpers.renderList(features, (item, index, __index, _cached): any => {
        return createElementVNode("view", utsMapOf({
          class: "feature-item",
          key: index,
          onClick: () => {goPage(item.path)}
        }), [
          createElementVNode("image", utsMapOf({
            src: item.icon,
            class: "feature-icon"
          }), null, 8 /* PROPS */, ["src"]),
          createElementVNode("text", utsMapOf({ class: "feature-title" }), toDisplayString(item.name), 1 /* TEXT */),
          createElementVNode("text", utsMapOf({ class: "feature-sub" }), toDisplayString(item.desc), 1 /* TEXT */)
        ], 8 /* PROPS */, ["onClick"])
      }), 64 /* STABLE_FRAGMENT */)
    ])
  ])
}
}

})
export default __sfc__
const GenPagesIndexIndexStyles = [utsMapOf([["container", padStyleMapOf(utsMapOf([["display", "flex"], ["flexDirection", "column"], ["backgroundColor", "#f2f3f5"]]))], ["header", padStyleMapOf(utsMapOf([["height", "140rpx"], ["backgroundImage", "linear-gradient(to right, #4e54c8, #8f94fb)"], ["backgroundColor", "rgba(0,0,0,0)"], ["display", "flex"], ["alignItems", "center"], ["justifyContent", "center"]]))], ["title", padStyleMapOf(utsMapOf([["color", "#FFFFFF"], ["fontSize", "36rpx"], ["fontWeight", "bold"]]))], ["balance-card", padStyleMapOf(utsMapOf([["backgroundColor", "#ffffff"], ["borderTopLeftRadius", "20rpx"], ["borderTopRightRadius", "20rpx"], ["borderBottomRightRadius", "20rpx"], ["borderBottomLeftRadius", "20rpx"], ["paddingTop", "24rpx"], ["paddingRight", 0], ["paddingBottom", "24rpx"], ["paddingLeft", 0], ["width", "80%"], ["marginTop", "-30rpx"], ["marginRight", "auto"], ["marginBottom", "20rpx"], ["marginLeft", "auto"], ["textAlign", "center"], ["boxShadow", "0 4rpx 12rpx rgba(0, 0, 0, 0.08)"], ["display", "flex"], ["flexDirection", "column"], ["alignItems", "center"]]))], ["summary-block", padStyleMapOf(utsMapOf([["display", "flex"], ["flexDirection", "column"], ["alignItems", "center"], ["marginBottom", "6rpx"]]))], ["balance-label", padStyleMapOf(utsMapOf([["fontSize", "24rpx"], ["color", "#666666"]]))], ["balance-value", utsMapOf([["", utsMapOf([["fontSize", "36rpx"], ["color", "#333333"], ["fontWeight", "bold"], ["marginTop", "4rpx"]])], [".expense", utsMapOf([["color", "#ff3b30"]])], [".income", utsMapOf([["color", "#34c759"]])], [".loading", utsMapOf([["color", "#999999"], ["fontSize", "28rpx"]])]])], ["expense-row", padStyleMapOf(utsMapOf([["display", "flex"], ["alignItems", "center"], ["justifyContent", "center"], ["marginTop", "4rpx"]]))], ["expense-icon", utsMapOf([["", utsMapOf([["fontSize", "30rpx"], ["marginRight", "6rpx"]])], [".positive", utsMapOf([["color", "#34c759"]])], [".negative", utsMapOf([["color", "#ff3b30"]])]])], ["divider", padStyleMapOf(utsMapOf([["width", "60%"], ["height", "1rpx"], ["backgroundColor", "#eeeeee"], ["marginTop", "14rpx"], ["marginRight", 0], ["marginBottom", "14rpx"], ["marginLeft", 0]]))], ["feature-grid", padStyleMapOf(utsMapOf([["flex", 1], ["display", "flex"], ["flexWrap", "wrap"], ["justifyContent", "space-between"], ["alignContent", "space-around"], ["paddingTop", 0], ["paddingRight", "24rpx"], ["paddingBottom", 0], ["paddingLeft", "24rpx"]]))], ["feature-item", padStyleMapOf(utsMapOf([["width", "48%"], ["height", "30%"], ["backgroundColor", "#ffffff"], ["borderTopLeftRadius", "16rpx"], ["borderTopRightRadius", "16rpx"], ["borderBottomRightRadius", "16rpx"], ["borderBottomLeftRadius", "16rpx"], ["paddingTop", "20rpx"], ["paddingRight", "10rpx"], ["paddingBottom", "20rpx"], ["paddingLeft", "10rpx"], ["display", "flex"], ["flexDirection", "column"], ["alignItems", "center"], ["justifyContent", "center"], ["boxShadow", "0 4rpx 10rpx rgba(0, 0, 0, 0.04)"], ["marginBottom", "20rpx"]]))], ["feature-icon", padStyleMapOf(utsMapOf([["width", "60rpx"], ["height", "60rpx"], ["marginBottom", "10rpx"]]))], ["feature-title", padStyleMapOf(utsMapOf([["fontSize", "26rpx"], ["color", "#333333"], ["fontWeight", "bold"], ["textAlign", "center"]]))], ["feature-sub", padStyleMapOf(utsMapOf([["fontSize", "20rpx"], ["color", "#999999"], ["marginTop", "4rpx"], ["textAlign", "center"]]))]])]
