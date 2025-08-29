import { ref } from 'vue'

const __sfc__ = defineComponent({
  __name: 'social',
  setup(__props): any | null {
const __ins = getCurrentInstance()!;
const _ctx = __ins.proxy as InstanceType<typeof __sfc__>;
const _cache = __ins.renderCache;

const searchQuery = ref('')
const rankingData = [
  { name: '张小萌', progress: 98 },
  { name: '李小明', progress: 85 },
  { name: '王小红', progress: 78 },
  { name: '你', progress: 65 }
]
const friendsData = [
  { name: '赵小刚', spendReduction: 15, linkText: 'PK' },
  { name: '钱小丽', spendReduction: 45, linkText: '查看' }
]
const goToAddFriend = () => {
  uni.navigateTo({ url: '/pages/friend/friend' })
}

return (): any | null => {

  return createElementVNode("view", utsMapOf({ class: "container" }), [
    createElementVNode("view", utsMapOf({ class: "header" }), [
      createElementVNode("text", utsMapOf({ class: "back" }), "←"),
      createElementVNode("text", utsMapOf({ class: "title" }), "社交互动"),
      createElementVNode("text", utsMapOf({ class: "subtitle" }), "与好友一起理财更有趣"),
      createElementVNode("view", utsMapOf({ class: "search-bar" }), [
        createElementVNode("button", utsMapOf({
          class: "search-friend-btn",
          onClick: goToAddFriend
        }), "搜索好友")
      ])
    ]),
    createElementVNode("view", utsMapOf({ class: "ranking-card" }), [
      createElementVNode("text", utsMapOf({ class: "ranking-title" }), "本月理财排名"),
      createElementVNode(Fragment, null, RenderHelpers.renderList(rankingData, (item, index, __index, _cached): any => {
        return createElementVNode("view", utsMapOf({
          key: index,
          class: "ranking-item"
        }), [
          createElementVNode("text", utsMapOf({ class: "rank-number" }), toDisplayString(index + 1) + ".", 1 /* TEXT */),
          createElementVNode("text", utsMapOf({ class: "rank-name" }), toDisplayString(item.name), 1 /* TEXT */),
          createElementVNode("view", utsMapOf({ class: "progress-bar" }), [
            createElementVNode("view", utsMapOf({
              class: "progress",
              style: normalizeStyle(utsMapOf({ width: item.progress + '%' }))
            }), null, 4 /* STYLE */)
          ]),
          createElementVNode("text", utsMapOf({ class: "rank-progress" }), toDisplayString(item.progress) + "% 完成", 1 /* TEXT */)
        ])
      }), 64 /* STABLE_FRAGMENT */),
      createElementVNode("text", utsMapOf({ class: "view-all" }), "查看全部")
    ]),
    createElementVNode("view", utsMapOf({ class: "friends-card" }), [
      createElementVNode("text", utsMapOf({ class: "friends-title" }), "我的好友"),
      createElementVNode(Fragment, null, RenderHelpers.renderList(friendsData, (friend, index, __index, _cached): any => {
        return createElementVNode("view", utsMapOf({
          key: index,
          class: "friend-item"
        }), [
          createElementVNode("text", utsMapOf({ class: "friend-name" }), toDisplayString(friend.name), 1 /* TEXT */),
          createElementVNode("text", utsMapOf({ class: "friend-status" }), "本周消费减少 " + toDisplayString(friend.spendReduction) + "%", 1 /* TEXT */),
          createElementVNode("text", utsMapOf({ class: "friend-link" }), toDisplayString(friend.linkText), 1 /* TEXT */)
        ])
      }), 64 /* STABLE_FRAGMENT */)
    ])
  ])
}
}

})
export default __sfc__
const GenPagesSocialSocialStyles = [utsMapOf([["container", padStyleMapOf(utsMapOf([["backgroundColor", "#f5f7fa"], ["paddingBottom", "10%"]]))], ["header", padStyleMapOf(utsMapOf([["backgroundImage", "linear-gradient(to right, #4e54c8, #8f94fb)"], ["backgroundColor", "rgba(0,0,0,0)"], ["paddingTop", "80rpx"], ["paddingRight", "30rpx"], ["paddingBottom", "40rpx"], ["paddingLeft", "30rpx"], ["borderBottomLeftRadius", "30rpx"], ["borderBottomRightRadius", "30rpx"], ["position", "relative"], ["textAlign", "center"]]))], ["back", padStyleMapOf(utsMapOf([["position", "absolute"], ["left", "30rpx"], ["top", "80rpx"], ["fontSize", "36rpx"], ["color", "#FFFFFF"]]))], ["title", padStyleMapOf(utsMapOf([["width", "100%"], ["textAlign", "center"], ["fontSize", "36rpx"], ["fontWeight", "bold"], ["color", "#FFFFFF"]]))], ["subtitle", padStyleMapOf(utsMapOf([["width", "100%"], ["textAlign", "center"], ["fontSize", "24rpx"], ["color", "#f0f0f0"], ["marginTop", "10rpx"]]))], ["search-bar", padStyleMapOf(utsMapOf([["marginTop", "40rpx"], ["paddingTop", "6rpx"], ["paddingRight", "6rpx"], ["paddingBottom", "6rpx"], ["paddingLeft", "6rpx"], ["backgroundImage", "none"], ["backgroundColor", "#FFFFFF"], ["borderTopLeftRadius", "30rpx"], ["borderTopRightRadius", "30rpx"], ["borderBottomRightRadius", "30rpx"], ["borderBottomLeftRadius", "30rpx"], ["boxShadow", "0 4rpx 8rpx rgba(0, 0, 0, 0.1)"], ["display", "flex"], ["justifyContent", "center"]]))], ["search-friend-btn", padStyleMapOf(utsMapOf([["width", "85%"], ["paddingTop", "15rpx"], ["paddingRight", "20rpx"], ["paddingBottom", "15rpx"], ["paddingLeft", "20rpx"], ["fontSize", "28rpx"], ["borderTopWidth", "medium"], ["borderRightWidth", "medium"], ["borderBottomWidth", "medium"], ["borderLeftWidth", "medium"], ["borderTopStyle", "none"], ["borderRightStyle", "none"], ["borderBottomStyle", "none"], ["borderLeftStyle", "none"], ["borderTopColor", "#000000"], ["borderRightColor", "#000000"], ["borderBottomColor", "#000000"], ["borderLeftColor", "#000000"], ["backgroundImage", "none"], ["backgroundColor", "rgba(255,255,255,0.9)"], ["borderTopLeftRadius", "30rpx"], ["borderTopRightRadius", "30rpx"], ["borderBottomRightRadius", "30rpx"], ["borderBottomLeftRadius", "30rpx"], ["color", "#4e54c8"], ["boxShadow", "0 4rpx 8rpx rgba(0, 0, 0, 0.1)"], ["marginTop", 0], ["marginRight", "auto"], ["marginBottom", 0], ["marginLeft", "auto"]]))], ["ranking-card", padStyleMapOf(utsMapOf([["backgroundImage", "none"], ["backgroundColor", "#ffffff"], ["marginTop", "30rpx"], ["marginRight", "30rpx"], ["marginBottom", "30rpx"], ["marginLeft", "30rpx"], ["borderTopLeftRadius", "20rpx"], ["borderTopRightRadius", "20rpx"], ["borderBottomRightRadius", "20rpx"], ["borderBottomLeftRadius", "20rpx"], ["paddingTop", "30rpx"], ["paddingRight", "30rpx"], ["paddingBottom", "30rpx"], ["paddingLeft", "30rpx"], ["boxShadow", "0 6rpx 12rpx rgba(0, 0, 0, 0.06)"]]))], ["ranking-title", padStyleMapOf(utsMapOf([["fontSize", "28rpx"], ["fontWeight", "bold"], ["marginBottom", "20rpx"]]))], ["ranking-item", padStyleMapOf(utsMapOf([["display", "flex"], ["alignItems", "center"], ["marginBottom", "20rpx"]]))], ["rank-number", padStyleMapOf(utsMapOf([["fontSize", "26rpx"], ["fontWeight", "bold"], ["width", "30rpx"]]))], ["rank-name", padStyleMapOf(utsMapOf([["fontSize", "26rpx"], ["marginRight", "12rpx"], ["width", "100rpx"]]))], ["progress-bar", padStyleMapOf(utsMapOf([["width", "100%"], ["height", "10rpx"], ["backgroundImage", "none"], ["backgroundColor", "#e0e0e0"], ["borderTopLeftRadius", "5rpx"], ["borderTopRightRadius", "5rpx"], ["borderBottomRightRadius", "5rpx"], ["borderBottomLeftRadius", "5rpx"], ["marginTop", "10rpx"], ["marginRight", 0], ["marginBottom", "10rpx"], ["marginLeft", 0], ["position", "relative"]]))], ["progress", padStyleMapOf(utsMapOf([["height", "100%"], ["backgroundImage", "none"], ["backgroundColor", "#4caf50"], ["borderTopLeftRadius", "5rpx"], ["borderTopRightRadius", "5rpx"], ["borderBottomRightRadius", "5rpx"], ["borderBottomLeftRadius", "5rpx"]]))], ["rank-progress", padStyleMapOf(utsMapOf([["fontSize", "22rpx"], ["color", "#666666"]]))], ["friends-card", padStyleMapOf(utsMapOf([["backgroundImage", "none"], ["backgroundColor", "#ffffff"], ["marginTop", "30rpx"], ["marginRight", "30rpx"], ["marginBottom", "30rpx"], ["marginLeft", "30rpx"], ["borderTopLeftRadius", "20rpx"], ["borderTopRightRadius", "20rpx"], ["borderBottomRightRadius", "20rpx"], ["borderBottomLeftRadius", "20rpx"], ["paddingTop", "30rpx"], ["paddingRight", "30rpx"], ["paddingBottom", "30rpx"], ["paddingLeft", "30rpx"], ["boxShadow", "0 6rpx 12rpx rgba(0, 0, 0, 0.06)"]]))], ["friends-title", padStyleMapOf(utsMapOf([["fontSize", "28rpx"], ["fontWeight", "bold"], ["marginBottom", "20rpx"]]))], ["friend-item", padStyleMapOf(utsMapOf([["display", "flex"], ["justifyContent", "space-between"], ["marginBottom", "20rpx"]]))], ["friend-name", padStyleMapOf(utsMapOf([["fontSize", "26rpx"], ["fontWeight", "bold"]]))], ["friend-status", padStyleMapOf(utsMapOf([["fontSize", "22rpx"], ["color", "#666666"]]))], ["friend-link", padStyleMapOf(utsMapOf([["fontSize", "24rpx"], ["color", "#4e54c8"], ["cursor", "pointer"]]))]])]
