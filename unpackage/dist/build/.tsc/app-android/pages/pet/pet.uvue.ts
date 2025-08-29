import { ref, computed, onMounted } from 'vue'
import { getTotalStatistic } from '../../common/api/transaction'


const __sfc__ = defineComponent({
  __name: 'pet',
  setup(__props): any | null {
const __ins = getCurrentInstance()!;
const _ctx = __ins.proxy as InstanceType<typeof __sfc__>;
const _cache = __ins.renderCache;

const showAllBadges = ref(false)
const petMood = ref('很开心')
const statusClass = ref('status-green')

const allBadges = [
  { name: '目标达人', icon: '🎯' },
  { name: '省钱小能手', icon: '💰' },
  { name: '储蓄先锋', icon: '📈' },
  { name: '社交达人', icon: '🗣️' },
  { name: '理财王者', icon: '👑' },
  { name: '早起打卡', icon: '⏰' },
  { name: '账单清零', icon: '🧾' }
]

const displayedBadges = computed(() => {
  return showAllBadges.value ? allBadges : allBadges.slice(0, 3)
})

const toggleShowAll = () => {
  showAllBadges.value = !showAllBadges.value
}

const goToChat = () => {
  uni.navigateTo({ url: '/pages/AIchat/AIchat' })
}

// 根据收支情况设置宠物心情
const updatePetMood = async () => {
  try {
    const response = await getTotalStatistic()
    console.log('宠物页面 - 总统计响应:', response)
    
    if (response?.Data) {
      const { Income, Expense } = response.Data
      console.log('宠物页面 - 收入数据:', Income)
      console.log('宠物页面 - 支出数据:', Expense)
      
      const balance = (Income?.Amount || 0) - (Expense?.Amount || 0)
      console.log('宠物页面 - 计算余额:', balance)
      
      if (balance <= 0) {
        petMood.value = '很担心'
        statusClass.value = 'status-red'
      } else if (balance <= 1000) {
        petMood.value = '很欣慰'
        statusClass.value = 'status-yellow'
      } else {
        petMood.value = '很开心'
        statusClass.value = 'status-green'
      }
      
      console.log('宠物页面 - 最终心情:', petMood.value)
    }
  } catch (error) {
    console.error('获取收支数据失败:', error)
    // 默认设置为很开心
    petMood.value = '很开心'
    statusClass.value = 'status-green'
  }
}

onMounted(() => {
  updatePetMood()
})

return (): any | null => {

  return createElementVNode("view", utsMapOf({ class: "container" }), [
    createElementVNode("view", utsMapOf({ class: "header" }), [
      createElementVNode("text", utsMapOf({ class: "back-btn" }), "←"),
      createElementVNode("text", utsMapOf({ class: "title" }), "AI理财宠物"),
      createElementVNode("text", utsMapOf({ class: "subtitle" }), "您的智能理财小伙伴")
    ]),
    createElementVNode("view", utsMapOf({ class: "card" }), [
      createElementVNode("view", utsMapOf({ class: "avatar-wrapper" }), [
        createElementVNode("image", utsMapOf({
          src: "/static/icons/pet.png",
          class: "avatar",
          mode: "aspectFit"
        }))
      ]),
      createElementVNode("text", utsMapOf({ class: "pet-name" }), "理财小汪"),
      createElementVNode("text", utsMapOf({ class: "pet-status" }), [
        "状态：",
        createElementVNode("text", utsMapOf({
          class: normalizeClass(statusClass.value)
        }), toDisplayString(petMood.value), 3 /* TEXT, CLASS */)
      ])
    ]),
    createElementVNode("view", utsMapOf({ class: "action-column" }), [
      createElementVNode("view", utsMapOf({
        class: "action-item",
        onClick: goToChat
      }), [
        createElementVNode("text", utsMapOf({ class: "icon" }), "💬"),
        createElementVNode("text", utsMapOf({ class: "label" }), "聊天")
      ])
    ]),
    createElementVNode("view", utsMapOf({ class: "badge-section" }), [
      createElementVNode("view", utsMapOf({ class: "badge-header" }), [
        createElementVNode("text", utsMapOf({ class: "badge-title" }), "我的勋章"),
        createElementVNode("text", utsMapOf({
          class: "badge-link",
          onClick: toggleShowAll
        }), toDisplayString(showAllBadges.value ? '收起' : '查看全部'), 1 /* TEXT */)
      ]),
      createElementVNode("view", utsMapOf({ class: "badge-row" }), [
        createElementVNode(Fragment, null, RenderHelpers.renderList(displayedBadges.value, (badge, __key, __index, _cached): any => {
          return createElementVNode("view", utsMapOf({
            class: "badge-item",
            key: badge.name
          }), [
            createElementVNode("text", utsMapOf({ class: "badge-icon" }), toDisplayString(badge.icon), 1 /* TEXT */),
            createElementVNode("text", utsMapOf({ class: "badge-name" }), toDisplayString(badge.name), 1 /* TEXT */)
          ])
        }), 128 /* KEYED_FRAGMENT */)
      ])
    ])
  ])
}
}

})
export default __sfc__
const GenPagesPetPetStyles = [utsMapOf([["container", padStyleMapOf(utsMapOf([["backgroundColor", "#f5f7fa"], ["paddingBottom", "10%"]]))], ["header", padStyleMapOf(utsMapOf([["backgroundImage", "linear-gradient(to right, #4e54c8, #8f94fb)"], ["backgroundColor", "rgba(0,0,0,0)"], ["paddingTop", "80rpx"], ["paddingRight", "30rpx"], ["paddingBottom", "40rpx"], ["paddingLeft", "30rpx"], ["borderBottomLeftRadius", "30rpx"], ["borderBottomRightRadius", "30rpx"], ["position", "relative"], ["alignItems", "center"]]))], ["back-btn", padStyleMapOf(utsMapOf([["position", "absolute"], ["left", "30rpx"], ["top", "80rpx"], ["fontSize", "36rpx"], ["color", "#FFFFFF"]]))], ["title", padStyleMapOf(utsMapOf([["fontSize", "36rpx"], ["fontWeight", "bold"], ["color", "#FFFFFF"]]))], ["subtitle", padStyleMapOf(utsMapOf([["fontSize", "24rpx"], ["color", "#f0f0f0"], ["marginTop", "10rpx"]]))], ["card", padStyleMapOf(utsMapOf([["backgroundImage", "none"], ["backgroundColor", "#ffffff"], ["marginTop", "30rpx"], ["marginRight", "30rpx"], ["marginBottom", "30rpx"], ["marginLeft", "30rpx"], ["paddingTop", "30rpx"], ["paddingRight", "30rpx"], ["paddingBottom", "30rpx"], ["paddingLeft", "30rpx"], ["borderTopLeftRadius", "20rpx"], ["borderTopRightRadius", "20rpx"], ["borderBottomRightRadius", "20rpx"], ["borderBottomLeftRadius", "20rpx"], ["boxShadow", "0 6rpx 16rpx rgba(0, 0, 0, 0.06)"], ["textAlign", "center"]]))], ["avatar-wrapper", padStyleMapOf(utsMapOf([["display", "flex"], ["justifyContent", "center"], ["position", "relative"], ["alignItems", "center"], ["width", "100%"]]))], ["avatar", padStyleMapOf(utsMapOf([["width", "140rpx"], ["height", "140rpx"]]))], ["level", padStyleMapOf(utsMapOf([["position", "absolute"], ["right", "100rpx"], ["bottom", 0], ["width", "36rpx"], ["height", "36rpx"], ["backgroundImage", "none"], ["backgroundColor", "#FFA500"], ["color", "#ffffff"], ["fontSize", "22rpx"], ["fontWeight", "bold"], ["textAlign", "center"], ["lineHeight", "36rpx"]]))], ["pet-name", padStyleMapOf(utsMapOf([["fontSize", "30rpx"], ["fontWeight", "bold"], ["marginTop", "12rpx"]]))], ["pet-status", padStyleMapOf(utsMapOf([["fontSize", "24rpx"], ["marginTop", "6rpx"]]))], ["status-green", padStyleMapOf(utsMapOf([["color", "#35b765"]]))], ["status-yellow", padStyleMapOf(utsMapOf([["color", "#f39c12"]]))], ["status-red", padStyleMapOf(utsMapOf([["color", "#e74c3c"]]))], ["action-column", padStyleMapOf(utsMapOf([["display", "flex"], ["alignItems", "center"], ["marginTop", "40rpx"], ["marginRight", 0], ["marginBottom", "40rpx"], ["marginLeft", 0]]))], ["action-item", padStyleMapOf(utsMapOf([["width", "90%"], ["backgroundImage", "none"], ["backgroundColor", "#ffffff"], ["borderTopLeftRadius", "16rpx"], ["borderTopRightRadius", "16rpx"], ["borderBottomRightRadius", "16rpx"], ["borderBottomLeftRadius", "16rpx"], ["paddingTop", "24rpx"], ["paddingRight", 0], ["paddingBottom", "24rpx"], ["paddingLeft", 0], ["boxShadow", "0 2rpx 8rpx rgba(0, 0, 0, 0.06)"], ["display", "flex"], ["flexDirection", "column"], ["alignItems", "center"], ["justifyContent", "center"]]))], ["icon", padStyleMapOf(utsMapOf([["fontSize", "40rpx"]]))], ["label", padStyleMapOf(utsMapOf([["fontSize", "24rpx"], ["marginTop", "8rpx"]]))], ["badge-section", padStyleMapOf(utsMapOf([["marginTop", "40rpx"], ["marginRight", "30rpx"], ["marginBottom", "40rpx"], ["marginLeft", "30rpx"]]))], ["badge-header", padStyleMapOf(utsMapOf([["display", "flex"], ["justifyContent", "space-between"], ["alignItems", "center"], ["marginBottom", "20rpx"]]))], ["badge-title", padStyleMapOf(utsMapOf([["fontSize", "28rpx"], ["fontWeight", "bold"], ["color", "#333333"]]))], ["badge-link", padStyleMapOf(utsMapOf([["fontSize", "24rpx"], ["color", "#4e54c8"]]))], ["badge-row", padStyleMapOf(utsMapOf([["display", "flex"], ["flexDirection", "row"], ["flexWrap", "wrap"], ["justifyContent", "space-between"], ["gap", "20rpx 10rpx"]]))], ["badge-item", padStyleMapOf(utsMapOf([["width", "30%"], ["backgroundImage", "none"], ["backgroundColor", "#FFFFFF"], ["borderTopLeftRadius", "16rpx"], ["borderTopRightRadius", "16rpx"], ["borderBottomRightRadius", "16rpx"], ["borderBottomLeftRadius", "16rpx"], ["paddingTop", "20rpx"], ["paddingRight", 0], ["paddingBottom", "20rpx"], ["paddingLeft", 0], ["boxShadow", "0 2rpx 6rpx rgba(0, 0, 0, 0.05)"], ["display", "flex"], ["flexDirection", "column"], ["alignItems", "center"], ["justifyContent", "center"]]))], ["badge-icon", padStyleMapOf(utsMapOf([["fontSize", "36rpx"]]))], ["badge-name", padStyleMapOf(utsMapOf([["fontSize", "22rpx"], ["marginTop", "6rpx"]]))]])]
