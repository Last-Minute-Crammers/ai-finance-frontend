import { ref, onMounted } from 'vue'


const __sfc__ = defineComponent({
  __name: 'settings',
  setup(__props): any | null {
const __ins = getCurrentInstance()!;
const _ctx = __ins.proxy as InstanceType<typeof __sfc__>;
const _cache = __ins.renderCache;

const nickname = ref('')
const avatarUrl = ref('/static/icons/default-avatar.png')
const userId = ref('')

onMounted(() => {
  // 从本地存储获取当前用户信息
  const userStr = uni.getStorageSync('current_user')
  if (userStr) {
    try {
      const user = UTSAndroid.consoleDebugError(JSON.parse(userStr), " at pages/settings/settings.uvue:54")
      nickname.value = user.username || ''
      userId.value = user.id ? String(user.id) : ''
      // 头像可根据后端实际字段调整
      // avatarUrl.value = user.avatarUrl || avatarUrl.value
    } catch (e) {
      // ignore
    }
  }
})

function goBack() {
  uni.navigateBack()
}

function chooseAvatar() {
  uni.chooseImage({
    count: 1,
    success: (res) => {
      avatarUrl.value = res.tempFilePaths[0]
      uni.showToast({
        title: '头像更新成功',
        icon: 'success'
      })
    }
  })
}

function showLogoutConfirm() {
  uni.showModal({
    title: '确认退出',
    content: '确定要退出登录吗？',
    confirmText: '退出',
    cancelText: '取消',
    confirmColor: '#dc143c',
    success: (res) => {
      if (res.confirm) {
        logout()
      }
    }
  })
}

function logout() {
  uni.showLoading({
    title: '退出中...'
  })
  
  setTimeout(() => {
    uni.hideLoading()
    uni.showToast({
      title: '已退出登录',
      icon: 'success',
      duration: 2000
    })
    uni.removeStorageSync('current_user');
    uni.removeStorageSync('token');
    uni.reLaunch({ url: '/pages/login/login' })
  }, 1000)
}

return (): any | null => {

  return createElementVNode("view", utsMapOf({ class: "container" }), [
    createElementVNode("view", utsMapOf({ class: "header" }), [
      createElementVNode("text", utsMapOf({
        class: "back",
        onClick: goBack
      }), "←"),
      createElementVNode("view", utsMapOf({ class: "title-section" }), [
        createElementVNode("text", utsMapOf({ class: "title" }), "设置"),
        createElementVNode("text", utsMapOf({ class: "subtitle" }), "个人信息管理")
      ])
    ]),
    createElementVNode("view", utsMapOf({ class: "user-card" }), [
      createElementVNode("view", utsMapOf({ class: "info-item" }), [
        createElementVNode("text", utsMapOf({ class: "info-label" }), "头像"),
        createElementVNode("view", utsMapOf({
          class: "info-content",
          onClick: chooseAvatar
        }), [
          createElementVNode("image", utsMapOf({
            src: avatarUrl.value,
            class: "avatar",
            mode: "aspectFill"
          }), null, 8 /* PROPS */, ["src"])
        ])
      ]),
      createElementVNode("view", utsMapOf({ class: "info-item" }), [
        createElementVNode("text", utsMapOf({ class: "info-label" }), "昵称"),
        createElementVNode("text", utsMapOf({ class: "info-content" }), toDisplayString(nickname.value || '未设置'), 1 /* TEXT */)
      ]),
      createElementVNode("view", utsMapOf({ class: "info-item" }), [
        createElementVNode("text", utsMapOf({ class: "info-label" }), "用户ID"),
        createElementVNode("text", utsMapOf({ class: "info-content" }), toDisplayString(userId.value || '未知'), 1 /* TEXT */)
      ])
    ]),
    createElementVNode("view", utsMapOf({ class: "logout-section" }), [
      createElementVNode("button", utsMapOf({
        class: "logout-btn",
        onClick: showLogoutConfirm
      }), [
        createElementVNode("text", utsMapOf({ class: "logout-icon" }), "🚪"),
        createElementVNode("text", utsMapOf({ class: "logout-text" }), "退出登录")
      ])
    ])
  ])
}
}

})
export default __sfc__
const GenPagesSettingsSettingsStyles = [utsMapOf([["container", padStyleMapOf(utsMapOf([["backgroundImage", "linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%)"], ["backgroundColor", "rgba(0,0,0,0)"], ["paddingBottom", "10%"]]))], ["header", padStyleMapOf(utsMapOf([["backgroundImage", "linear-gradient(135deg, #667eea 0%, #764ba2 100%)"], ["backgroundColor", "rgba(0,0,0,0)"], ["paddingTop", "80rpx"], ["paddingRight", "30rpx"], ["paddingBottom", "40rpx"], ["paddingLeft", "30rpx"], ["borderBottomLeftRadius", "30rpx"], ["borderBottomRightRadius", "30rpx"], ["position", "relative"], ["textAlign", "center"], ["boxShadow", "0 4rpx 20rpx rgba(102, 126, 234, 0.2)"]]))], ["back", padStyleMapOf(utsMapOf([["position", "absolute"], ["left", "30rpx"], ["top", "80rpx"], ["fontSize", "36rpx"], ["color", "#FFFFFF"], ["fontWeight", "bold"]]))], ["title-section", padStyleMapOf(utsMapOf([["textAlign", "center"]]))], ["title", padStyleMapOf(utsMapOf([["fontSize", "36rpx"], ["fontWeight", "bold"], ["color", "#FFFFFF"], ["marginBottom", "8rpx"]]))], ["subtitle", padStyleMapOf(utsMapOf([["fontSize", "24rpx"], ["color", "rgba(255,255,255,0.8)"]]))], ["user-card", padStyleMapOf(utsMapOf([["backgroundImage", "none"], ["backgroundColor", "rgba(255,255,255,0.95)"], ["marginTop", "40rpx"], ["marginRight", "30rpx"], ["marginBottom", "20rpx"], ["marginLeft", "30rpx"], ["borderTopLeftRadius", "20rpx"], ["borderTopRightRadius", "20rpx"], ["borderBottomRightRadius", "20rpx"], ["borderBottomLeftRadius", "20rpx"], ["boxShadow", "0 8rpx 24rpx rgba(0, 0, 0, 0.1)"], ["paddingTop", "30rpx"], ["paddingRight", "30rpx"], ["paddingBottom", "30rpx"], ["paddingLeft", "30rpx"], ["backdropFilter", "blur(10rpx)"]]))], ["info-item", padStyleMapOf(utsMapOf([["marginBottom", "40rpx"], ["marginBottom:last-child", 0]]))], ["info-label", padStyleMapOf(utsMapOf([["fontSize", "26rpx"], ["color", "#666666"], ["marginBottom", "16rpx"]]))], ["info-content", padStyleMapOf(utsMapOf([["fontSize", "30rpx"], ["color", "#333333"], ["paddingTop", "20rpx"], ["paddingRight", 0], ["paddingBottom", "20rpx"], ["paddingLeft", 0], ["borderBottomWidth", "1rpx"], ["borderBottomStyle", "solid"], ["borderBottomColor", "#f0f0f0"], ["display", "flex"], ["alignItems", "center"], ["justifyContent", "center"]]))], ["avatar", padStyleMapOf(utsMapOf([["width", "120rpx"], ["height", "120rpx"], ["backgroundImage", "none"], ["backgroundColor", "#f0f0f0"], ["borderTopWidth", "3rpx"], ["borderRightWidth", "3rpx"], ["borderBottomWidth", "3rpx"], ["borderLeftWidth", "3rpx"], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "#667eea"], ["borderRightColor", "#667eea"], ["borderBottomColor", "#667eea"], ["borderLeftColor", "#667eea"], ["boxShadow", "0 4rpx 16rpx rgba(102, 126, 234, 0.2)"]]))], ["logout-section", padStyleMapOf(utsMapOf([["marginTop", "40rpx"], ["marginRight", "30rpx"], ["marginBottom", 0], ["marginLeft", "30rpx"]]))], ["logout-btn", padStyleMapOf(utsMapOf([["width", "100%"], ["backgroundImage", "linear-gradient(135deg, #dc143c, #b22222)"], ["backgroundColor", "rgba(0,0,0,0)"], ["color", "#ffffff"], ["fontSize", "28rpx"], ["borderTopWidth", "medium"], ["borderRightWidth", "medium"], ["borderBottomWidth", "medium"], ["borderLeftWidth", "medium"], ["borderTopStyle", "none"], ["borderRightStyle", "none"], ["borderBottomStyle", "none"], ["borderLeftStyle", "none"], ["borderTopColor", "#000000"], ["borderRightColor", "#000000"], ["borderBottomColor", "#000000"], ["borderLeftColor", "#000000"], ["borderTopLeftRadius", "20rpx"], ["borderTopRightRadius", "20rpx"], ["borderBottomRightRadius", "20rpx"], ["borderBottomLeftRadius", "20rpx"], ["paddingTop", "30rpx"], ["paddingRight", 0], ["paddingBottom", "30rpx"], ["paddingLeft", 0], ["fontWeight", "bold"], ["display", "flex"], ["alignItems", "center"], ["justifyContent", "center"], ["gap", "16rpx"], ["boxShadow", "0 8rpx 24rpx rgba(220, 20, 60, 0.3)"], ["transitionProperty", "transform"], ["transitionDuration", "0.2s"], ["transform:active", "scale(0.98)"]]))], ["logout-icon", padStyleMapOf(utsMapOf([["fontSize", "24rpx"]]))], ["logout-text", padStyleMapOf(utsMapOf([["fontSize", "28rpx"]]))], ["@TRANSITION", utsMapOf([["logout-btn", utsMapOf([["property", "transform"], ["duration", "0.2s"]])]])]])]
