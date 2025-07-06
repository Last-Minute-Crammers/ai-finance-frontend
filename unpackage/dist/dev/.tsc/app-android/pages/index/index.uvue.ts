import { ref, onMounted } from 'vue'
import { getTotalStatistic, getMonthStatistic } from '../../common/api/transaction'

interface Feature {
  name: string
  desc: string
  icon: string
  path: string
}

const __sfc__ = defineComponent({
  __name: 'index',
  setup(__props, { expose: __expose }: SetupContext): any | null {
const __ins = getCurrentInstance()!;
const _ctx = __ins.proxy as InstanceType<typeof __sfc__>;
const _cache = __ins.renderCache;

const balance = ref<number>(0)
const monthlyNet = ref<number>(0)
const loading = ref<boolean>(true)

const features: Feature[] = [
  { name: '记账', desc: '记录每一笔收支', icon: '/static/icons/book.png', path: '/pages/transaction/transaction' },
  { name: '财务分析', desc: '可视化您的消费', icon: '/static/icons/chart.png', path: '/pages/analyze/analyze' },
  { name: 'AI理财宠物', desc: '陪伴式理财体验', icon: '/static/icons/pet.png', path: '/pages/pet/pet' },
  { name: '财务报告', desc: '智能分析建议', icon: '/static/icons/report.png', path: '/pages/report/report' },
  { name: '社交互动', desc: '与好友一起理财', icon: '/static/icons/social.png', path: '/pages/social/social' },
  { name: '设置', desc: '个性化您的体验', icon: '/static/icons/settings.png', path: '/pages/settings/settings' }
]

function goPage(path: string): void {
  uni.navigateTo({ url: path })
}

async function loadStatisticData(): Promise<void> {
  try {
    loading.value = true
    const token: string | null = uni.getStorageSync('token')
    if (!token) {
      uni.navigateTo({ url: '/pages/login/login' })
      return
    }
    const totalRes: any = await getTotalStatistic()
    if (totalRes && totalRes.Data) {
      balance.value = (totalRes.Data.total_assets || 0) / 100
    }
    const now: Date = new Date()
    const monthStart: Date = new Date(now.getFullYear(), now.getMonth(), 1)
    const monthEnd: Date = new Date(now.getFullYear(), now.getMonth() + 1, 0)
    const monthlyRes: any = await getMonthStatistic({
      startTime: monthStart.toISOString(),
      endTime: monthEnd.toISOString()
    })
    if (monthlyRes && monthlyRes.Data && monthlyRes.Data.List && monthlyRes.Data.List.length > 0) {
      const monthData: any = monthlyRes.Data.List[0]
      const monthIncome: number = (monthData.Income && monthData.Income.Amount ? monthData.Income.Amount : 0) / 100
      const monthExpense: number = (monthData.Expense && monthData.Expense.Amount ? monthData.Expense.Amount : 0) / 100
      monthlyNet.value = monthIncome - monthExpense
    }
  } catch (error: any) {
    balance.value = 0
    monthlyNet.value = 0
    uni.showToast({
      title: '数据加载失败',
      icon: 'none'
    })
  } finally {
    loading.value = false
  }
}

onMounted(function(): void {
  loadStatisticData()
})

__expose({
  onShow: function(): void {
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
const GenPagesIndexIndexStyles = [utsMapOf([["container", padStyleMapOf(utsMapOf([["display", "flex"], ["flexDirection", "column"], ["backgroundColor", "#f2f3f5"]]))], ["header", padStyleMapOf(utsMapOf([["height", "140rpx"], ["backgroundImage", "linear-gradient(to right, #4e54c8, #8f94fb)"], ["backgroundColor", "rgba(0,0,0,0)"], ["display", "flex"], ["alignItems", "center"], ["justifyContent", "center"]]))], ["title", padStyleMapOf(utsMapOf([["color", "#FFFFFF"], ["fontSize", "36rpx"], ["fontWeight", "bold"]]))], ["balance-card", padStyleMapOf(utsMapOf([["backgroundColor", "#ffffff"], ["borderTopLeftRadius", "20rpx"], ["borderTopRightRadius", "20rpx"], ["borderBottomRightRadius", "20rpx"], ["borderBottomLeftRadius", "20rpx"], ["paddingTop", "24rpx"], ["paddingRight", 0], ["paddingBottom", "24rpx"], ["paddingLeft", 0], ["width", "80%"], ["marginTop", "-30rpx"], ["marginRight", "auto"], ["marginBottom", "20rpx"], ["marginLeft", "auto"], ["textAlign", "center"], ["boxShadow", "0 4rpx 12rpx rgba(0, 0, 0, 0.08)"], ["display", "flex"], ["flexDirection", "column"], ["alignItems", "center"]]))], ["summary-block", padStyleMapOf(utsMapOf([["display", "flex"], ["flexDirection", "column"], ["alignItems", "center"], ["paddingTop", "20rpx"], ["paddingRight", 0], ["paddingBottom", "20rpx"], ["paddingLeft", 0]]))], ["balance-label", padStyleMapOf(utsMapOf([["fontSize", "28rpx"], ["color", "#666666"], ["marginBottom", "10rpx"]]))], ["balance-value", utsMapOf([["", utsMapOf([["fontSize", "48rpx"], ["fontWeight", "bold"], ["color", "#333333"]])], [".loading", utsMapOf([["fontSize", "32rpx"], ["color", "#999999"]])], [".income", utsMapOf([["color", "#34C759"]])], [".expense", utsMapOf([["color", "#FF3B30"]])]])], ["divider", padStyleMapOf(utsMapOf([["width", "80%"], ["height", "1rpx"], ["backgroundColor", "#eeeeee"], ["marginTop", "10rpx"], ["marginRight", 0], ["marginBottom", "10rpx"], ["marginLeft", 0]]))], ["expense-row", padStyleMapOf(utsMapOf([["display", "flex"], ["alignItems", "center"], ["gap", "10rpx"]]))], ["expense-icon", utsMapOf([["", utsMapOf([["fontSize", "32rpx"]])], [".positive", utsMapOf([["color", "#34C759"]])], [".negative", utsMapOf([["color", "#FF3B30"]])]])], ["feature-grid", padStyleMapOf(utsMapOf([["flex", 1], ["gridTemplateColumns", "repeat(2, 1fr)"], ["gap", "20rpx"], ["paddingTop", "20rpx"], ["paddingRight", "20rpx"], ["paddingBottom", "20rpx"], ["paddingLeft", "20rpx"], ["overflowY", "auto"]]))], ["feature-item", padStyleMapOf(utsMapOf([["backgroundColor", "#ffffff"], ["borderTopLeftRadius", "20rpx"], ["borderTopRightRadius", "20rpx"], ["borderBottomRightRadius", "20rpx"], ["borderBottomLeftRadius", "20rpx"], ["paddingTop", "40rpx"], ["paddingRight", "30rpx"], ["paddingBottom", "40rpx"], ["paddingLeft", "30rpx"], ["textAlign", "center"], ["boxShadow", "0 4rpx 12rpx rgba(0, 0, 0, 0.08)"], ["transitionDuration", "0.3s"], ["transitionTimingFunction", "ease"], ["transform:active", "scale(0.98)"], ["boxShadow:active", "0 2rpx 8rpx rgba(0, 0, 0, 0.12)"]]))], ["feature-icon", padStyleMapOf(utsMapOf([["width", "80rpx"], ["height", "80rpx"], ["marginBottom", "20rpx"]]))], ["feature-title", padStyleMapOf(utsMapOf([["fontSize", "32rpx"], ["fontWeight", "bold"], ["color", "#333333"], ["marginBottom", "10rpx"]]))], ["feature-sub", padStyleMapOf(utsMapOf([["fontSize", "24rpx"], ["color", "#666666"], ["lineHeight", 1.4]]))], ["@TRANSITION", utsMapOf([["feature-item", utsMapOf([["duration", "0.3s"], ["timingFunction", "ease"]])]])]])]
