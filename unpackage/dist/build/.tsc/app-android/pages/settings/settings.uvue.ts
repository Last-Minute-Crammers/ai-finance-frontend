import { ref, onMounted } from 'vue'
import { checkBackendConnection, setBackendEnvironment, getAvailableBackendUrls } from '@/utils/request'


const __sfc__ = defineComponent({
  __name: 'settings',
  setup(__props): any | null {
const __ins = getCurrentInstance()!;
const _ctx = __ins.proxy as InstanceType<typeof __sfc__>;
const _cache = __ins.renderCache;

const nickname = ref('')
const avatarUrl = ref('/static/icons/default-avatar.png')
const userId = ref('')

// API连接状态
const isConnected = ref(false)
const isTesting = ref(false)
const currentApiUrl = ref('http://47.109.194.39/api')
const responseTime = ref(0)
const errorMessage = ref('')

onMounted(() => {
  // 从本地存储获取当前用户信息
  const userStr = uni.getStorageSync('current_user')
  if (userStr) {
    try {
      const user = JSON.parse(userStr)
      nickname.value = user.username || ''
      userId.value = user.id ? String(user.id) : ''
      // 头像可根据后端实际字段调整
      // avatarUrl.value = user.avatarUrl || avatarUrl.value
    } catch (e) {
      // ignore
    }
  }
  
  // 获取当前API URL
  const backendUrl = uni.getStorageSync('backend_url')
  if (backendUrl) {
    currentApiUrl.value = backendUrl
  }
  
  // 自动测试连接
  testConnection()
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

// API连接测试函数
async function testConnection() {
  if (isTesting.value) return
  
  isTesting.value = true
  errorMessage.value = ''
  
  try {
    const result = await checkBackendConnection(currentApiUrl.value)
    
    if (result.connected) {
      isConnected.value = true
      responseTime.value = result.responseTime || 0
      uni.showToast({
        title: '连接成功',
        icon: 'success'
      })
    } else {
      isConnected.value = false
      errorMessage.value = result.error || '连接失败'
      uni.showToast({
        title: '连接失败',
        icon: 'error'
      })
    }
  } catch (error: any) {
    isConnected.value = false
    errorMessage.value = error.message || '测试连接时发生错误'
    uni.showToast({
      title: '连接测试失败',
      icon: 'error'
    })
  } finally {
    isTesting.value = false
  }
}

// 显示API选择器
function showApiSelector() {
  const availableUrls = getAvailableBackendUrls()
  const urlOptions = Object.keys(availableUrls).map(key => ({
    label: `${key}: ${availableUrls[key]}`,
    value: key
  }))
  
  uni.showActionSheet({
    itemList: urlOptions.map(item => item.label),
    success: (res) => {
      const selectedKey = urlOptions[res.tapIndex].value
      const selectedUrl = availableUrls[selectedKey]
      
      setBackendEnvironment(selectedKey)
      currentApiUrl.value = selectedUrl
      
      uni.showToast({
        title: 'API已切换',
        icon: 'success'
      })
      
      // 自动测试新连接
      testConnection()
    }
  })
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
        createElementVNode("text", utsMapOf({ class: "info-content" }), toDisplayString(nickname.value ?? '未设置'), 1 /* TEXT */)
      ]),
      createElementVNode("view", utsMapOf({ class: "info-item" }), [
        createElementVNode("text", utsMapOf({ class: "info-label" }), "用户ID"),
        createElementVNode("text", utsMapOf({ class: "info-content" }), toDisplayString(userId.value ?? '未知'), 1 /* TEXT */)
      ])
    ]),
    createElementVNode("view", utsMapOf({ class: "api-card" }), [
      createElementVNode("view", utsMapOf({ class: "card-header" }), [
        createElementVNode("text", utsMapOf({ class: "card-title" }), "API连接状态"),
        createElementVNode("text", utsMapOf({ class: "card-subtitle" }), "测试后端服务连接")
      ]),
      createElementVNode("view", utsMapOf({ class: "connection-status" }), [
        createElementVNode("view", utsMapOf({
          class: normalizeClass(["status-indicator", utsMapOf({ 'connected': isConnected.value, 'disconnected': !isConnected.value })])
        }), [
          createElementVNode("text", utsMapOf({ class: "status-dot" })),
          createElementVNode("text", utsMapOf({ class: "status-text" }), toDisplayString(isConnected.value ? '已连接' : '未连接'), 1 /* TEXT */)
        ], 2 /* CLASS */),
        createElementVNode("view", utsMapOf({ class: "api-info" }), [
          createElementVNode("text", utsMapOf({ class: "api-url" }), toDisplayString(currentApiUrl.value), 1 /* TEXT */),
          isTrue(responseTime.value)
            ? createElementVNode("text", utsMapOf({
                key: 0,
                class: "response-time"
              }), "响应时间: " + toDisplayString(responseTime.value) + "ms", 1 /* TEXT */)
            : createCommentVNode("v-if", true)
        ])
      ]),
      createElementVNode("view", utsMapOf({ class: "action-buttons" }), [
        createElementVNode("button", utsMapOf({
          class: "test-btn",
          onClick: testConnection,
          disabled: isTesting.value
        }), [
          createElementVNode("text", utsMapOf({ class: "btn-icon" }), "🔄"),
          createElementVNode("text", utsMapOf({ class: "btn-text" }), toDisplayString(isTesting.value ? '测试中...' : '测试连接'), 1 /* TEXT */)
        ], 8 /* PROPS */, ["disabled"]),
        createElementVNode("button", utsMapOf({
          class: "switch-btn",
          onClick: showApiSelector
        }), [
          createElementVNode("text", utsMapOf({ class: "btn-icon" }), "⚙️"),
          createElementVNode("text", utsMapOf({ class: "btn-text" }), "切换API")
        ])
      ]),
      isTrue(errorMessage.value)
        ? createElementVNode("view", utsMapOf({
            key: 0,
            class: "error-info"
          }), [
            createElementVNode("text", utsMapOf({ class: "error-title" }), "连接错误:"),
            createElementVNode("text", utsMapOf({ class: "error-message" }), toDisplayString(errorMessage.value), 1 /* TEXT */)
          ])
        : createCommentVNode("v-if", true)
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
const GenPagesSettingsSettingsStyles = [utsMapOf([["container", padStyleMapOf(utsMapOf([["backgroundImage", "linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%)"], ["backgroundColor", "rgba(0,0,0,0)"], ["paddingBottom", "10%"]]))], ["header", padStyleMapOf(utsMapOf([["backgroundImage", "linear-gradient(135deg, #667eea 0%, #764ba2 100%)"], ["backgroundColor", "rgba(0,0,0,0)"], ["paddingTop", "80rpx"], ["paddingRight", "30rpx"], ["paddingBottom", "40rpx"], ["paddingLeft", "30rpx"], ["borderBottomLeftRadius", "30rpx"], ["borderBottomRightRadius", "30rpx"], ["position", "relative"], ["textAlign", "center"], ["boxShadow", "0 4rpx 20rpx rgba(102, 126, 234, 0.2)"]]))], ["back", padStyleMapOf(utsMapOf([["position", "absolute"], ["left", "30rpx"], ["top", "80rpx"], ["fontSize", "36rpx"], ["color", "#FFFFFF"], ["fontWeight", "bold"]]))], ["title-section", padStyleMapOf(utsMapOf([["textAlign", "center"]]))], ["title", padStyleMapOf(utsMapOf([["fontSize", "36rpx"], ["fontWeight", "bold"], ["color", "#FFFFFF"], ["marginBottom", "8rpx"]]))], ["subtitle", padStyleMapOf(utsMapOf([["fontSize", "24rpx"], ["color", "rgba(255,255,255,0.8)"]]))], ["user-card", padStyleMapOf(utsMapOf([["backgroundImage", "none"], ["backgroundColor", "rgba(255,255,255,0.95)"], ["marginTop", "40rpx"], ["marginRight", "30rpx"], ["marginBottom", "20rpx"], ["marginLeft", "30rpx"], ["borderTopLeftRadius", "20rpx"], ["borderTopRightRadius", "20rpx"], ["borderBottomRightRadius", "20rpx"], ["borderBottomLeftRadius", "20rpx"], ["boxShadow", "0 8rpx 24rpx rgba(0, 0, 0, 0.1)"], ["paddingTop", "30rpx"], ["paddingRight", "30rpx"], ["paddingBottom", "30rpx"], ["paddingLeft", "30rpx"], ["backdropFilter", "blur(10rpx)"]]))], ["info-item", padStyleMapOf(utsMapOf([["marginBottom", "40rpx"], ["marginBottom:last-child", 0]]))], ["info-label", padStyleMapOf(utsMapOf([["fontSize", "26rpx"], ["color", "#666666"], ["marginBottom", "16rpx"]]))], ["info-content", padStyleMapOf(utsMapOf([["fontSize", "30rpx"], ["color", "#333333"], ["paddingTop", "20rpx"], ["paddingRight", 0], ["paddingBottom", "20rpx"], ["paddingLeft", 0], ["borderBottomWidth", "1rpx"], ["borderBottomStyle", "solid"], ["borderBottomColor", "#f0f0f0"], ["display", "flex"], ["alignItems", "center"], ["justifyContent", "center"]]))], ["avatar", padStyleMapOf(utsMapOf([["width", "120rpx"], ["height", "120rpx"], ["backgroundImage", "none"], ["backgroundColor", "#f0f0f0"], ["borderTopWidth", "3rpx"], ["borderRightWidth", "3rpx"], ["borderBottomWidth", "3rpx"], ["borderLeftWidth", "3rpx"], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "#667eea"], ["borderRightColor", "#667eea"], ["borderBottomColor", "#667eea"], ["borderLeftColor", "#667eea"], ["boxShadow", "0 4rpx 16rpx rgba(102, 126, 234, 0.2)"]]))], ["logout-section", padStyleMapOf(utsMapOf([["marginTop", "40rpx"], ["marginRight", "30rpx"], ["marginBottom", 0], ["marginLeft", "30rpx"]]))], ["logout-btn", padStyleMapOf(utsMapOf([["width", "100%"], ["backgroundImage", "linear-gradient(135deg, #dc143c, #b22222)"], ["backgroundColor", "rgba(0,0,0,0)"], ["color", "#ffffff"], ["fontSize", "28rpx"], ["borderTopWidth", "medium"], ["borderRightWidth", "medium"], ["borderBottomWidth", "medium"], ["borderLeftWidth", "medium"], ["borderTopStyle", "none"], ["borderRightStyle", "none"], ["borderBottomStyle", "none"], ["borderLeftStyle", "none"], ["borderTopColor", "#000000"], ["borderRightColor", "#000000"], ["borderBottomColor", "#000000"], ["borderLeftColor", "#000000"], ["borderTopLeftRadius", "20rpx"], ["borderTopRightRadius", "20rpx"], ["borderBottomRightRadius", "20rpx"], ["borderBottomLeftRadius", "20rpx"], ["paddingTop", "30rpx"], ["paddingRight", 0], ["paddingBottom", "30rpx"], ["paddingLeft", 0], ["fontWeight", "bold"], ["display", "flex"], ["alignItems", "center"], ["justifyContent", "center"], ["gap", "16rpx"], ["boxShadow", "0 8rpx 24rpx rgba(220, 20, 60, 0.3)"], ["transitionProperty", "transform"], ["transitionDuration", "0.2s"], ["transform:active", "scale(0.98)"]]))], ["logout-icon", padStyleMapOf(utsMapOf([["fontSize", "24rpx"]]))], ["logout-text", padStyleMapOf(utsMapOf([["fontSize", "28rpx"]]))], ["api-card", padStyleMapOf(utsMapOf([["backgroundImage", "none"], ["backgroundColor", "rgba(255,255,255,0.95)"], ["marginTop", "20rpx"], ["marginRight", "30rpx"], ["marginBottom", "20rpx"], ["marginLeft", "30rpx"], ["borderTopLeftRadius", "20rpx"], ["borderTopRightRadius", "20rpx"], ["borderBottomRightRadius", "20rpx"], ["borderBottomLeftRadius", "20rpx"], ["boxShadow", "0 8rpx 24rpx rgba(0, 0, 0, 0.1)"], ["paddingTop", "30rpx"], ["paddingRight", "30rpx"], ["paddingBottom", "30rpx"], ["paddingLeft", "30rpx"], ["backdropFilter", "blur(10rpx)"]]))], ["card-header", padStyleMapOf(utsMapOf([["marginBottom", "30rpx"]]))], ["card-title", padStyleMapOf(utsMapOf([["fontSize", "28rpx"], ["fontWeight", "bold"], ["color", "#333333"], ["marginBottom", "8rpx"]]))], ["card-subtitle", padStyleMapOf(utsMapOf([["fontSize", "24rpx"], ["color", "#666666"]]))], ["connection-status", padStyleMapOf(utsMapOf([["marginBottom", "30rpx"]]))], ["status-indicator", padStyleMapOf(utsMapOf([["display", "flex"], ["alignItems", "center"], ["marginBottom", "20rpx"]]))], ["status-dot", utsMapOf([["", utsMapOf([["width", "16rpx"], ["height", "16rpx"], ["marginRight", "16rpx"]])], [".status-indicator.connected ", utsMapOf([["backgroundImage", "none"], ["backgroundColor", "#4CAF50"], ["boxShadow", "0 0 8rpx rgba(76, 175, 80, 0.5)"]])], [".status-indicator.disconnected ", utsMapOf([["backgroundImage", "none"], ["backgroundColor", "#f44336"], ["boxShadow", "0 0 8rpx rgba(244, 67, 54, 0.5)"]])]])], ["status-text", utsMapOf([["", utsMapOf([["fontSize", "26rpx"]])], [".status-indicator.connected ", utsMapOf([["color", "#4CAF50"]])], [".status-indicator.disconnected ", utsMapOf([["color", "#f44336"]])]])], ["api-info", padStyleMapOf(utsMapOf([["backgroundImage", "none"], ["backgroundColor", "#f8f9fa"], ["paddingTop", "20rpx"], ["paddingRight", "20rpx"], ["paddingBottom", "20rpx"], ["paddingLeft", "20rpx"], ["borderTopLeftRadius", "12rpx"], ["borderTopRightRadius", "12rpx"], ["borderBottomRightRadius", "12rpx"], ["borderBottomLeftRadius", "12rpx"], ["borderLeftWidth", "4rpx"], ["borderLeftStyle", "solid"], ["borderLeftColor", "#667eea"]]))], ["api-url", padStyleMapOf(utsMapOf([["fontSize", "24rpx"], ["color", "#333333"], ["fontFamily", "monospace"], ["marginBottom", "8rpx"], ["wordBreak", "break-all"]]))], ["response-time", padStyleMapOf(utsMapOf([["fontSize", "22rpx"], ["color", "#666666"]]))], ["action-buttons", padStyleMapOf(utsMapOf([["display", "flex"], ["gap", "20rpx"], ["marginBottom", "20rpx"]]))], ["test-btn", padStyleMapOf(utsMapOf([["flex", 1], ["backgroundImage", "linear-gradient(135deg, #667eea, #764ba2)"], ["backgroundColor", "rgba(0,0,0,0)"], ["color", "#ffffff"], ["fontSize", "24rpx"], ["borderTopWidth", "medium"], ["borderRightWidth", "medium"], ["borderBottomWidth", "medium"], ["borderLeftWidth", "medium"], ["borderTopStyle", "none"], ["borderRightStyle", "none"], ["borderBottomStyle", "none"], ["borderLeftStyle", "none"], ["borderTopColor", "#000000"], ["borderRightColor", "#000000"], ["borderBottomColor", "#000000"], ["borderLeftColor", "#000000"], ["borderTopLeftRadius", "12rpx"], ["borderTopRightRadius", "12rpx"], ["borderBottomRightRadius", "12rpx"], ["borderBottomLeftRadius", "12rpx"], ["paddingTop", "20rpx"], ["paddingRight", 0], ["paddingBottom", "20rpx"], ["paddingLeft", 0], ["display", "flex"], ["alignItems", "center"], ["justifyContent", "center"], ["gap", "12rpx"], ["boxShadow", "0 4rpx 16rpx rgba(102, 126, 234, 0.3)"], ["transitionProperty", "transform"], ["transitionDuration", "0.2s"], ["transform:active", "scale(0.98)"], ["backgroundImage:disabled", "none"], ["backgroundColor:disabled", "#cccccc"], ["boxShadow:disabled", "none"]]))], ["switch-btn", padStyleMapOf(utsMapOf([["flex", 1], ["backgroundImage", "linear-gradient(135deg, #667eea, #764ba2)"], ["backgroundColor", "rgba(0,0,0,0)"], ["color", "#ffffff"], ["fontSize", "24rpx"], ["borderTopWidth", "medium"], ["borderRightWidth", "medium"], ["borderBottomWidth", "medium"], ["borderLeftWidth", "medium"], ["borderTopStyle", "none"], ["borderRightStyle", "none"], ["borderBottomStyle", "none"], ["borderLeftStyle", "none"], ["borderTopColor", "#000000"], ["borderRightColor", "#000000"], ["borderBottomColor", "#000000"], ["borderLeftColor", "#000000"], ["borderTopLeftRadius", "12rpx"], ["borderTopRightRadius", "12rpx"], ["borderBottomRightRadius", "12rpx"], ["borderBottomLeftRadius", "12rpx"], ["paddingTop", "20rpx"], ["paddingRight", 0], ["paddingBottom", "20rpx"], ["paddingLeft", 0], ["display", "flex"], ["alignItems", "center"], ["justifyContent", "center"], ["gap", "12rpx"], ["boxShadow", "0 4rpx 16rpx rgba(102, 126, 234, 0.3)"], ["transitionProperty", "transform"], ["transitionDuration", "0.2s"], ["transform:active:active", "scale(0.98)"]]))], ["btn-icon", padStyleMapOf(utsMapOf([["fontSize", "20rpx"]]))], ["btn-text", padStyleMapOf(utsMapOf([["fontSize", "24rpx"]]))], ["error-info", padStyleMapOf(utsMapOf([["backgroundImage", "none"], ["backgroundColor", "#ffebee"], ["borderTopWidth", "1rpx"], ["borderRightWidth", "1rpx"], ["borderBottomWidth", "1rpx"], ["borderLeftWidth", "1rpx"], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "#ffcdd2"], ["borderRightColor", "#ffcdd2"], ["borderBottomColor", "#ffcdd2"], ["borderLeftColor", "#ffcdd2"], ["borderTopLeftRadius", "12rpx"], ["borderTopRightRadius", "12rpx"], ["borderBottomRightRadius", "12rpx"], ["borderBottomLeftRadius", "12rpx"], ["paddingTop", "20rpx"], ["paddingRight", "20rpx"], ["paddingBottom", "20rpx"], ["paddingLeft", "20rpx"], ["marginTop", "20rpx"]]))], ["error-title", padStyleMapOf(utsMapOf([["fontSize", "24rpx"], ["color", "#d32f2f"], ["marginBottom", "8rpx"]]))], ["error-message", padStyleMapOf(utsMapOf([["fontSize", "22rpx"], ["color", "#c62828"], ["wordBreak", "break-all"], ["lineHeight", 1.4]]))], ["@TRANSITION", utsMapOf([["logout-btn", utsMapOf([["property", "transform"], ["duration", "0.2s"]])], ["test-btn", utsMapOf([["property", "transform"], ["duration", "0.2s"]])], ["switch-btn", utsMapOf([["property", "transform"], ["duration", "0.2s"]])]])]])]
