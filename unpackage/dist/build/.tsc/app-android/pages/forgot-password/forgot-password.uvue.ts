import { ref } from 'vue'


const __sfc__ = defineComponent({
  __name: 'forgot-password',
  setup(__props): any | null {
const __ins = getCurrentInstance()!;
const _ctx = __ins.proxy as InstanceType<typeof __sfc__>;
const _cache = __ins.renderCache;

const emailOrPhone = ref('')
const verificationCode = ref('')
const newPassword = ref('')
const passwordVisible = ref(false)
const passwordType = ref('password')

// 切换密码显示/隐藏
function togglePasswordVisibility() {
  passwordVisible.value = !passwordVisible.value
  passwordType.value = passwordVisible.value ? 'text' : 'password'
}

// 发送验证码
function sendVerificationCode() {
  if (!emailOrPhone.value) {
    uni.showToast({
      title: '请输入邮箱或手机号',
      icon: 'none',
    })
    return
  }

  // 模拟验证码发送操作
  uni.showToast({
    title: '验证码已发送',
    icon: 'success',
    duration: 2000,
  })
}

// 提交重置密码请求
function submit() {
  if (!emailOrPhone.value || !verificationCode.value || !newPassword.value) {
    uni.showToast({
      title: '请填写完整信息',
      icon: 'none',
    })
    return
  }

  if (newPassword.value.length < 6) {
    uni.showToast({
      title: '密码长度至少6个字符',
      icon: 'none',
    })
    return
  }

  // 模拟密码重置成功操作
  uni.showToast({
    title: '密码重置成功',
    icon: 'success',
    duration: 2000,
  })

  // 跳转到登录页面
  uni.navigateTo({
    url: '/pages/login/login',
  })
}

// 跳转到登录页面
function goToLogin() {
  uni.navigateTo({
    url: '/pages/login/login',
  })
}

return (): any | null => {

  return createElementVNode("view", utsMapOf({ class: "container" }), [
    createElementVNode("view", utsMapOf({ class: "header" }), [
      createElementVNode("text", utsMapOf({ class: "back" }), "←"),
      createElementVNode("text", utsMapOf({ class: "title" }), "忘记密码"),
      createElementVNode("text", utsMapOf({ class: "subtitle" }), "请输入您的信息来重置密码")
    ]),
    createElementVNode("view", utsMapOf({ class: "form" }), [
      createElementVNode("view", utsMapOf({ class: "input-group" }), [
        createElementVNode("input", utsMapOf({
          modelValue: emailOrPhone.value,
          onInput: ($event: InputEvent) => {(emailOrPhone).value = $event.detail.value},
          class: "input-field",
          placeholder: "请输入注册时的邮箱或手机号"
        }), null, 40 /* PROPS, NEED_HYDRATION */, ["modelValue", "onInput"])
      ]),
      createElementVNode("view", utsMapOf({ class: "input-group" }), [
        createElementVNode("input", utsMapOf({
          modelValue: verificationCode.value,
          onInput: ($event: InputEvent) => {(verificationCode).value = $event.detail.value},
          class: "input-field",
          placeholder: "请输入验证码"
        }), null, 40 /* PROPS, NEED_HYDRATION */, ["modelValue", "onInput"]),
        createElementVNode("button", utsMapOf({
          class: "send-code-btn",
          onClick: sendVerificationCode
        }), "发送验证码")
      ]),
      createElementVNode("view", utsMapOf({ class: "input-group" }), [
        createElementVNode("input", utsMapOf({
          modelValue: newPassword.value,
          onInput: ($event: InputEvent) => {(newPassword).value = $event.detail.value},
          class: "input-field",
          type: passwordType.value,
          placeholder: "设置新密码"
        }), null, 40 /* PROPS, NEED_HYDRATION */, ["modelValue", "onInput", "type"]),
        createElementVNode("text", utsMapOf({
          class: "toggle-password",
          onClick: togglePasswordVisibility
        }), toDisplayString(passwordVisible.value ? '隐藏' : '显示'), 1 /* TEXT */)
      ]),
      createElementVNode("view", utsMapOf({ class: "button-group" }), [
        createElementVNode("button", utsMapOf({
          class: "submit-btn",
          onClick: submit
        }), "重置密码")
      ]),
      createElementVNode("view", utsMapOf({ class: "footer-links" }), [
        createElementVNode("text", utsMapOf({
          class: "have-account",
          onClick: goToLogin
        }), "记得密码? 返回登录")
      ])
    ])
  ])
}
}

})
export default __sfc__
const GenPagesForgotPasswordForgotPasswordStyles = [utsMapOf([["container", padStyleMapOf(utsMapOf([["backgroundColor", "#f5f7fa"], ["paddingBottom", "10%"]]))], ["header", padStyleMapOf(utsMapOf([["backgroundImage", "linear-gradient(to right, #4e54c8, #8f94fb)"], ["backgroundColor", "rgba(0,0,0,0)"], ["paddingTop", "80rpx"], ["paddingRight", "30rpx"], ["paddingBottom", "90rpx"], ["paddingLeft", "30rpx"], ["textAlign", "center"], ["alignItems", "center"], ["color", "#FFFFFF"], ["borderBottomLeftRadius", "30rpx"], ["borderBottomRightRadius", "30rpx"]]))], ["title", padStyleMapOf(utsMapOf([["fontSize", "36rpx"], ["fontWeight", "bold"]]))], ["subtitle", padStyleMapOf(utsMapOf([["fontSize", "24rpx"], ["marginTop", "10rpx"]]))], ["form", padStyleMapOf(utsMapOf([["paddingTop", "30rpx"], ["paddingRight", "30rpx"], ["paddingBottom", "30rpx"], ["paddingLeft", "30rpx"], ["backgroundColor", "#FFFFFF"], ["borderTopLeftRadius", "20rpx"], ["borderTopRightRadius", "20rpx"], ["borderBottomRightRadius", "20rpx"], ["borderBottomLeftRadius", "20rpx"], ["marginTop", "-40rpx"], ["boxShadow", "0 4rpx 12rpx rgba(0, 0, 0, 0.1)"]]))], ["input-group", padStyleMapOf(utsMapOf([["marginBottom", "30rpx"]]))], ["input-field", padStyleMapOf(utsMapOf([["width", "100%"], ["paddingTop", "16rpx"], ["paddingRight", "20rpx"], ["paddingBottom", "16rpx"], ["paddingLeft", "20rpx"], ["fontSize", "24rpx"], ["borderTopLeftRadius", "12rpx"], ["borderTopRightRadius", "12rpx"], ["borderBottomRightRadius", "12rpx"], ["borderBottomLeftRadius", "12rpx"], ["borderTopWidth", 1], ["borderRightWidth", 1], ["borderBottomWidth", 1], ["borderLeftWidth", 1], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "#cccccc"], ["borderRightColor", "#cccccc"], ["borderBottomColor", "#cccccc"], ["borderLeftColor", "#cccccc"], ["backgroundColor", "#f9f9f9"]]))], ["send-code-btn", padStyleMapOf(utsMapOf([["width", "40%"], ["paddingTop", "12rpx"], ["paddingRight", "20rpx"], ["paddingBottom", "12rpx"], ["paddingLeft", "20rpx"], ["backgroundColor", "#4e54c8"], ["color", "#FFFFFF"], ["fontSize", "20rpx"], ["borderTopLeftRadius", "12rpx"], ["borderTopRightRadius", "12rpx"], ["borderBottomRightRadius", "12rpx"], ["borderBottomLeftRadius", "12rpx"], ["borderTopWidth", "medium"], ["borderRightWidth", "medium"], ["borderBottomWidth", "medium"], ["borderLeftWidth", "medium"], ["borderTopStyle", "none"], ["borderRightStyle", "none"], ["borderBottomStyle", "none"], ["borderLeftStyle", "none"], ["borderTopColor", "#000000"], ["borderRightColor", "#000000"], ["borderBottomColor", "#000000"], ["borderLeftColor", "#000000"], ["cursor", "pointer"]]))], ["toggle-password", padStyleMapOf(utsMapOf([["fontSize", "20rpx"], ["color", "#4e54c8"], ["cursor", "pointer"]]))], ["button-group", padStyleMapOf(utsMapOf([["marginTop", "20rpx"]]))], ["submit-btn", padStyleMapOf(utsMapOf([["width", "100%"], ["paddingTop", "16rpx"], ["paddingRight", "16rpx"], ["paddingBottom", "16rpx"], ["paddingLeft", "16rpx"], ["fontSize", "28rpx"], ["backgroundColor", "#4e54c8"], ["color", "#FFFFFF"], ["borderTopLeftRadius", "30rpx"], ["borderTopRightRadius", "30rpx"], ["borderBottomRightRadius", "30rpx"], ["borderBottomLeftRadius", "30rpx"], ["borderTopWidth", "medium"], ["borderRightWidth", "medium"], ["borderBottomWidth", "medium"], ["borderLeftWidth", "medium"], ["borderTopStyle", "none"], ["borderRightStyle", "none"], ["borderBottomStyle", "none"], ["borderLeftStyle", "none"], ["borderTopColor", "#000000"], ["borderRightColor", "#000000"], ["borderBottomColor", "#000000"], ["borderLeftColor", "#000000"]]))], ["footer-links", padStyleMapOf(utsMapOf([["display", "flex"], ["justifyContent", "center"], ["marginTop", "20rpx"]]))], ["have-account", padStyleMapOf(utsMapOf([["fontSize", "24rpx"], ["color", "#4e54c8"], ["cursor", "pointer"]]))]])]
