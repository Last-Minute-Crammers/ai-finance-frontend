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
import io.dcloud.uniapp.extapi.navigateTo as uni_navigateTo
import io.dcloud.uniapp.extapi.showToast as uni_showToast
open class GenPagesForgotPasswordForgotPassword : BasePage {
    constructor(__ins: ComponentInternalInstance, __renderer: String?) : super(__ins, __renderer) {}
    companion object {
        @Suppress("UNUSED_PARAMETER", "UNUSED_VARIABLE")
        var setup: (__props: GenPagesForgotPasswordForgotPassword) -> Any? = fun(__props): Any? {
            val __ins = getCurrentInstance()!!
            val _ctx = __ins.proxy as GenPagesForgotPasswordForgotPassword
            val _cache = __ins.renderCache
            val emailOrPhone = ref("")
            val verificationCode = ref("")
            val newPassword = ref("")
            val passwordVisible = ref(false)
            val passwordType = ref("password")
            fun gen_togglePasswordVisibility_fn() {
                passwordVisible.value = !passwordVisible.value
                passwordType.value = if (passwordVisible.value) {
                    "text"
                } else {
                    "password"
                }
            }
            val togglePasswordVisibility = ::gen_togglePasswordVisibility_fn
            fun gen_sendVerificationCode_fn() {
                if (!emailOrPhone.value) {
                    uni_showToast(ShowToastOptions(title = "请输入邮箱或手机号", icon = "none"))
                    return
                }
                uni_showToast(ShowToastOptions(title = "验证码已发送", icon = "success", duration = 2000))
            }
            val sendVerificationCode = ::gen_sendVerificationCode_fn
            fun gen_submit_fn() {
                if (!emailOrPhone.value || !verificationCode.value || !newPassword.value) {
                    uni_showToast(ShowToastOptions(title = "请填写完整信息", icon = "none"))
                    return
                }
                if (newPassword.value.length < 6) {
                    uni_showToast(ShowToastOptions(title = "密码长度至少6个字符", icon = "none"))
                    return
                }
                uni_showToast(ShowToastOptions(title = "密码重置成功", icon = "success", duration = 2000))
                uni_navigateTo(NavigateToOptions(url = "/pages/login/login"))
            }
            val submit = ::gen_submit_fn
            fun gen_goToLogin_fn() {
                uni_navigateTo(NavigateToOptions(url = "/pages/login/login"))
            }
            val goToLogin = ::gen_goToLogin_fn
            return fun(): Any? {
                return createElementVNode("view", utsMapOf("class" to "container"), utsArrayOf(
                    createElementVNode("view", utsMapOf("class" to "header"), utsArrayOf(
                        createElementVNode("text", utsMapOf("class" to "back"), "←"),
                        createElementVNode("text", utsMapOf("class" to "title"), "忘记密码"),
                        createElementVNode("text", utsMapOf("class" to "subtitle"), "请输入您的信息来重置密码")
                    )),
                    createElementVNode("view", utsMapOf("class" to "form"), utsArrayOf(
                        createElementVNode("view", utsMapOf("class" to "input-group"), utsArrayOf(
                            createElementVNode("input", utsMapOf("modelValue" to emailOrPhone.value, "onInput" to fun(`$event`: InputEvent){
                                emailOrPhone.value = `$event`.detail.value
                            }
                            , "class" to "input-field", "placeholder" to "请输入注册时的邮箱或手机号"), null, 40, utsArrayOf(
                                "modelValue",
                                "onInput"
                            ))
                        )),
                        createElementVNode("view", utsMapOf("class" to "input-group"), utsArrayOf(
                            createElementVNode("input", utsMapOf("modelValue" to verificationCode.value, "onInput" to fun(`$event`: InputEvent){
                                verificationCode.value = `$event`.detail.value
                            }
                            , "class" to "input-field", "placeholder" to "请输入验证码"), null, 40, utsArrayOf(
                                "modelValue",
                                "onInput"
                            )),
                            createElementVNode("button", utsMapOf("class" to "send-code-btn", "onClick" to sendVerificationCode), "发送验证码")
                        )),
                        createElementVNode("view", utsMapOf("class" to "input-group"), utsArrayOf(
                            createElementVNode("input", utsMapOf("modelValue" to newPassword.value, "onInput" to fun(`$event`: InputEvent){
                                newPassword.value = `$event`.detail.value
                            }
                            , "class" to "input-field", "type" to passwordType.value, "placeholder" to "设置新密码"), null, 40, utsArrayOf(
                                "modelValue",
                                "onInput",
                                "type"
                            )),
                            createElementVNode("text", utsMapOf("class" to "toggle-password", "onClick" to togglePasswordVisibility), toDisplayString(if (passwordVisible.value) {
                                "隐藏"
                            } else {
                                "显示"
                            }
                            ), 1)
                        )),
                        createElementVNode("view", utsMapOf("class" to "button-group"), utsArrayOf(
                            createElementVNode("button", utsMapOf("class" to "submit-btn", "onClick" to submit), "重置密码")
                        )),
                        createElementVNode("view", utsMapOf("class" to "footer-links"), utsArrayOf(
                            createElementVNode("text", utsMapOf("class" to "have-account", "onClick" to goToLogin), "记得密码? 返回登录")
                        ))
                    ))
                ))
            }
        }
        val styles: Map<String, Map<String, Map<String, Any>>> by lazy {
            normalizeCssStyles(utsArrayOf(
                styles0
            ), utsArrayOf(
                GenApp.styles
            ))
        }
        val styles0: Map<String, Map<String, Map<String, Any>>>
            get() {
                return utsMapOf("container" to padStyleMapOf(utsMapOf("backgroundColor" to "#f5f7fa", "paddingBottom" to "10%")), "header" to padStyleMapOf(utsMapOf("backgroundImage" to "linear-gradient(to right, #4e54c8, #8f94fb)", "backgroundColor" to "rgba(0,0,0,0)", "paddingTop" to "80rpx", "paddingRight" to "30rpx", "paddingBottom" to "90rpx", "paddingLeft" to "30rpx", "textAlign" to "center", "alignItems" to "center", "color" to "#FFFFFF", "borderBottomLeftRadius" to "30rpx", "borderBottomRightRadius" to "30rpx")), "title" to padStyleMapOf(utsMapOf("fontSize" to "36rpx", "fontWeight" to "bold")), "subtitle" to padStyleMapOf(utsMapOf("fontSize" to "24rpx", "marginTop" to "10rpx")), "form" to padStyleMapOf(utsMapOf("paddingTop" to "30rpx", "paddingRight" to "30rpx", "paddingBottom" to "30rpx", "paddingLeft" to "30rpx", "backgroundColor" to "#FFFFFF", "borderTopLeftRadius" to "20rpx", "borderTopRightRadius" to "20rpx", "borderBottomRightRadius" to "20rpx", "borderBottomLeftRadius" to "20rpx", "marginTop" to "-40rpx", "boxShadow" to "0 4rpx 12rpx rgba(0, 0, 0, 0.1)")), "input-group" to padStyleMapOf(utsMapOf("marginBottom" to "30rpx")), "input-field" to padStyleMapOf(utsMapOf("width" to "100%", "paddingTop" to "16rpx", "paddingRight" to "20rpx", "paddingBottom" to "16rpx", "paddingLeft" to "20rpx", "fontSize" to "24rpx", "borderTopLeftRadius" to "12rpx", "borderTopRightRadius" to "12rpx", "borderBottomRightRadius" to "12rpx", "borderBottomLeftRadius" to "12rpx", "borderTopWidth" to 1, "borderRightWidth" to 1, "borderBottomWidth" to 1, "borderLeftWidth" to 1, "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#cccccc", "borderRightColor" to "#cccccc", "borderBottomColor" to "#cccccc", "borderLeftColor" to "#cccccc", "backgroundColor" to "#f9f9f9")), "send-code-btn" to padStyleMapOf(utsMapOf("width" to "40%", "paddingTop" to "12rpx", "paddingRight" to "20rpx", "paddingBottom" to "12rpx", "paddingLeft" to "20rpx", "backgroundColor" to "#4e54c8", "color" to "#FFFFFF", "fontSize" to "20rpx", "borderTopLeftRadius" to "12rpx", "borderTopRightRadius" to "12rpx", "borderBottomRightRadius" to "12rpx", "borderBottomLeftRadius" to "12rpx", "borderTopWidth" to "medium", "borderRightWidth" to "medium", "borderBottomWidth" to "medium", "borderLeftWidth" to "medium", "borderTopStyle" to "none", "borderRightStyle" to "none", "borderBottomStyle" to "none", "borderLeftStyle" to "none", "borderTopColor" to "#000000", "borderRightColor" to "#000000", "borderBottomColor" to "#000000", "borderLeftColor" to "#000000", "cursor" to "pointer")), "toggle-password" to padStyleMapOf(utsMapOf("fontSize" to "20rpx", "color" to "#4e54c8", "cursor" to "pointer")), "button-group" to padStyleMapOf(utsMapOf("marginTop" to "20rpx")), "submit-btn" to padStyleMapOf(utsMapOf("width" to "100%", "paddingTop" to "16rpx", "paddingRight" to "16rpx", "paddingBottom" to "16rpx", "paddingLeft" to "16rpx", "fontSize" to "28rpx", "backgroundColor" to "#4e54c8", "color" to "#FFFFFF", "borderTopLeftRadius" to "30rpx", "borderTopRightRadius" to "30rpx", "borderBottomRightRadius" to "30rpx", "borderBottomLeftRadius" to "30rpx", "borderTopWidth" to "medium", "borderRightWidth" to "medium", "borderBottomWidth" to "medium", "borderLeftWidth" to "medium", "borderTopStyle" to "none", "borderRightStyle" to "none", "borderBottomStyle" to "none", "borderLeftStyle" to "none", "borderTopColor" to "#000000", "borderRightColor" to "#000000", "borderBottomColor" to "#000000", "borderLeftColor" to "#000000")), "footer-links" to padStyleMapOf(utsMapOf("display" to "flex", "justifyContent" to "center", "marginTop" to "20rpx")), "have-account" to padStyleMapOf(utsMapOf("fontSize" to "24rpx", "color" to "#4e54c8", "cursor" to "pointer")))
            }
        var inheritAttrs = true
        var inject: Map<String, Map<String, Any?>> = utsMapOf()
        var emits: Map<String, Any?> = utsMapOf()
        var props = normalizePropsOptions(utsMapOf())
        var propsNeedCastKeys: UTSArray<String> = utsArrayOf()
        var components: Map<String, CreateVueComponent> = utsMapOf()
    }
}
