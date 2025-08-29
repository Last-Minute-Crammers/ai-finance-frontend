@file:Suppress("UNCHECKED_CAST", "USELESS_CAST", "INAPPLICABLE_JVM_NAME", "UNUSED_ANONYMOUS_PARAMETER", "NAME_SHADOWING", "UNNECESSARY_NOT_NULL_ASSERTION")
package uni.UNI5A7011F
import io.dcloud.uniapp.*
import io.dcloud.uniapp.extapi.*
import io.dcloud.uniapp.framework.*
import io.dcloud.uniapp.runtime.*
import io.dcloud.uniapp.vue.*
import io.dcloud.uniapp.vue.shared.*
import io.dcloud.uts.*
import io.dcloud.uts.Map
import io.dcloud.uts.Set
import io.dcloud.uts.UTSAndroid
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import io.dcloud.uniapp.extapi.navigateTo as uni_navigateTo
import io.dcloud.uniapp.extapi.setStorageSync as uni_setStorageSync
import io.dcloud.uniapp.extapi.showToast as uni_showToast
open class GenPagesRegisterRegister : BasePage {
    constructor(__ins: ComponentInternalInstance, __renderer: String?) : super(__ins, __renderer) {}
    companion object {
        @Suppress("UNUSED_PARAMETER", "UNUSED_VARIABLE")
        var setup: (__props: GenPagesRegisterRegister) -> Any? = fun(__props): Any? {
            val __ins = getCurrentInstance()!!
            val _ctx = __ins.proxy as GenPagesRegisterRegister
            val _cache = __ins.renderCache
            val username = ref("")
            val email = ref("")
            val password = ref("")
            val confirmPassword = ref("")
            val captcha = ref("")
            val isLoading = ref(false)
            fun gen_register_fn(): UTSPromise<Unit> {
                return wrapUTSPromise(suspend w@{
                        if (!username.value || !email.value || !password.value || !confirmPassword.value || !captcha.value) {
                            uni_showToast(ShowToastOptions(title = "请填写完整信息", icon = "none"))
                            return@w
                        }
                        if (username.value.length < 2) {
                            uni_showToast(ShowToastOptions(title = "用户名至少2个字符", icon = "none"))
                            return@w
                        }
                        val emailRegex = UTSRegExp("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+\$", "")
                        if (!emailRegex.test(email.value)) {
                            uni_showToast(ShowToastOptions(title = "请输入正确的邮箱格式", icon = "none"))
                            return@w
                        }
                        if (password.value !== confirmPassword.value) {
                            uni_showToast(ShowToastOptions(title = "两次密码不一致", icon = "none"))
                            return@w
                        }
                        if (password.value.length < 6) {
                            uni_showToast(ShowToastOptions(title = "密码长度至少6位", icon = "none"))
                            return@w
                        }
                        isLoading.value = true
                        try {
                            console.log("开始注册:", object : UTSJSONObject() {
                                var username = username.value
                                var email = email.value
                            })
                            val response = await(userRegister(object : UTSJSONObject() {
                                var username = username.value
                                var email = email.value
                                var password = password.value
                                var captcha = captcha.value
                            }))
                            console.log("注册响应:", response)
                            if (response && (response.Data || response.data)) {
                                val data = response.Data || response.data
                                uni_setStorageSync("token", data.token)
                                uni_setStorageSync("current_user", data.user)
                                uni_showToast(ShowToastOptions(title = "注册成功", icon = "success", mask = true, duration = 1500))
                                setTimeout(fun(){
                                    uni_navigateTo(NavigateToOptions(url = "/pages/index/index"))
                                }, 1500)
                            } else {
                                throw UTSError("注册响应格式错误")
                            }
                        }
                         catch (error: Throwable) {
                            console.error("注册失败:", error)
                            val errorMsg = error?.message || "注册失败，请重试"
                            uni_showToast(ShowToastOptions(title = errorMsg, icon = "none"))
                        }
                         finally{
                            isLoading.value = false
                        }
                })
            }
            val register = ::gen_register_fn
            fun gen_goToLogin_fn() {
                uni_navigateTo(NavigateToOptions(url = "/pages/login/login"))
            }
            val goToLogin = ::gen_goToLogin_fn
            return fun(): Any? {
                return createElementVNode("view", utsMapOf("class" to "register-container"), utsArrayOf(
                    createElementVNode("view", utsMapOf("class" to "header"), utsArrayOf(
                        createElementVNode("text", utsMapOf("class" to "title"), "注册"),
                        createElementVNode("text", utsMapOf("class" to "subtitle"), "创建新账户")
                    )),
                    createElementVNode("view", utsMapOf("class" to "form"), utsArrayOf(
                        createElementVNode("view", utsMapOf("class" to "form-item"), utsArrayOf(
                            createElementVNode("input", utsMapOf("type" to "text", "modelValue" to username.value, "onInput" to fun(`$event`: InputEvent){
                                username.value = `$event`.detail.value
                            }
                            , "placeholder" to "请输入用户名", "class" to "input-field"), null, 40, utsArrayOf(
                                "modelValue",
                                "onInput"
                            ))
                        )),
                        createElementVNode("view", utsMapOf("class" to "form-item"), utsArrayOf(
                            createElementVNode("input", utsMapOf("type" to "email", "modelValue" to email.value, "onInput" to fun(`$event`: InputEvent){
                                email.value = `$event`.detail.value
                            }
                            , "placeholder" to "请输入邮箱", "class" to "input-field"), null, 40, utsArrayOf(
                                "modelValue",
                                "onInput"
                            ))
                        )),
                        createElementVNode("view", utsMapOf("class" to "form-item"), utsArrayOf(
                            createElementVNode("input", utsMapOf("type" to "password", "modelValue" to password.value, "onInput" to fun(`$event`: InputEvent){
                                password.value = `$event`.detail.value
                            }
                            , "placeholder" to "请输入密码", "class" to "input-field"), null, 40, utsArrayOf(
                                "modelValue",
                                "onInput"
                            ))
                        )),
                        createElementVNode("view", utsMapOf("class" to "form-item"), utsArrayOf(
                            createElementVNode("input", utsMapOf("type" to "password", "modelValue" to confirmPassword.value, "onInput" to fun(`$event`: InputEvent){
                                confirmPassword.value = `$event`.detail.value
                            }
                            , "placeholder" to "请确认密码", "class" to "input-field"), null, 40, utsArrayOf(
                                "modelValue",
                                "onInput"
                            ))
                        )),
                        createElementVNode("view", utsMapOf("class" to "form-item"), utsArrayOf(
                            createElementVNode("input", utsMapOf("type" to "text", "modelValue" to captcha.value, "onInput" to fun(`$event`: InputEvent){
                                captcha.value = `$event`.detail.value
                            }
                            , "placeholder" to "请输入验证码(任意值)", "class" to "input-field"), null, 40, utsArrayOf(
                                "modelValue",
                                "onInput"
                            ))
                        )),
                        createElementVNode("button", utsMapOf("class" to "register-btn", "onClick" to register, "disabled" to isLoading.value), toDisplayString(if (isLoading.value) {
                            "注册中..."
                        } else {
                            "注册"
                        }
                        ), 9, utsArrayOf(
                            "disabled"
                        )),
                        createElementVNode("view", utsMapOf("class" to "login-link", "onClick" to goToLogin), "已有账号？去登录")
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
                return utsMapOf("register-container" to padStyleMapOf(utsMapOf("backgroundColor" to "#f5f7fa", "paddingBottom" to "10%")), "header" to padStyleMapOf(utsMapOf("backgroundImage" to "linear-gradient(to right, #4e54c8, #8f94fb)", "backgroundColor" to "rgba(0,0,0,0)", "paddingTop" to "80rpx", "paddingRight" to "30rpx", "paddingBottom" to "60rpx", "paddingLeft" to "30rpx", "textAlign" to "center", "color" to "#FFFFFF", "borderBottomLeftRadius" to "30rpx", "borderBottomRightRadius" to "30rpx")), "title" to padStyleMapOf(utsMapOf("fontSize" to "36rpx", "fontWeight" to "bold")), "subtitle" to padStyleMapOf(utsMapOf("fontSize" to "24rpx", "marginTop" to "10rpx")), "form" to padStyleMapOf(utsMapOf("paddingTop" to "30rpx", "paddingRight" to "30rpx", "paddingBottom" to "30rpx", "paddingLeft" to "30rpx", "backgroundColor" to "#FFFFFF", "borderTopLeftRadius" to "20rpx", "borderTopRightRadius" to "20rpx", "borderBottomRightRadius" to "20rpx", "borderBottomLeftRadius" to "20rpx", "marginTop" to "-40rpx", "marginRight" to "20rpx", "marginBottom" to 0, "marginLeft" to "20rpx", "boxShadow" to "0 4rpx 12rpx rgba(0, 0, 0, 0.1)")), "form-item" to padStyleMapOf(utsMapOf("marginBottom" to "30rpx")), "input-field" to padStyleMapOf(utsMapOf("width" to "100%", "paddingTop" to "16rpx", "paddingRight" to "20rpx", "paddingBottom" to "16rpx", "paddingLeft" to "20rpx", "fontSize" to "24rpx", "borderTopLeftRadius" to "12rpx", "borderTopRightRadius" to "12rpx", "borderBottomRightRadius" to "12rpx", "borderBottomLeftRadius" to "12rpx", "borderTopWidth" to 1, "borderRightWidth" to 1, "borderBottomWidth" to 1, "borderLeftWidth" to 1, "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#cccccc", "borderRightColor" to "#cccccc", "borderBottomColor" to "#cccccc", "borderLeftColor" to "#cccccc", "backgroundColor" to "#f9f9f9")), "register-btn" to padStyleMapOf(utsMapOf("width" to "100%", "paddingTop" to "16rpx", "paddingRight" to "16rpx", "paddingBottom" to "16rpx", "paddingLeft" to "16rpx", "fontSize" to "28rpx", "backgroundColor" to "#4e54c8", "color" to "#FFFFFF", "borderTopLeftRadius" to "30rpx", "borderTopRightRadius" to "30rpx", "borderBottomRightRadius" to "30rpx", "borderBottomLeftRadius" to "30rpx", "borderTopWidth" to "medium", "borderRightWidth" to "medium", "borderBottomWidth" to "medium", "borderLeftWidth" to "medium", "borderTopStyle" to "none", "borderRightStyle" to "none", "borderBottomStyle" to "none", "borderLeftStyle" to "none", "borderTopColor" to "#000000", "borderRightColor" to "#000000", "borderBottomColor" to "#000000", "borderLeftColor" to "#000000", "marginTop" to "20rpx", "marginRight" to 0, "marginBottom" to "20rpx", "marginLeft" to 0)), "login-link" to padStyleMapOf(utsMapOf("textAlign" to "center", "color" to "#4e54c8", "fontSize" to "24rpx")))
            }
        var inheritAttrs = true
        var inject: Map<String, Map<String, Any?>> = utsMapOf()
        var emits: Map<String, Any?> = utsMapOf()
        var props = normalizePropsOptions(utsMapOf())
        var propsNeedCastKeys: UTSArray<String> = utsArrayOf()
        var components: Map<String, CreateVueComponent> = utsMapOf()
    }
}
