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
import io.dcloud.uniapp.extapi.setStorageSync as uni_setStorageSync
import io.dcloud.uniapp.extapi.showToast as uni_showToast
open class GenPagesLoginLogin : BasePage {
    constructor(__ins: ComponentInternalInstance, __renderer: String?) : super(__ins, __renderer) {}
    companion object {
        @Suppress("UNUSED_PARAMETER", "UNUSED_VARIABLE")
        var setup: (__props: GenPagesLoginLogin) -> Any? = fun(__props): Any? {
            val __ins = getCurrentInstance()!!
            val _ctx = __ins.proxy as GenPagesLoginLogin
            val _cache = __ins.renderCache
            val email = ref("")
            val password = ref("")
            val passwordVisible = ref(false)
            val inputType = ref("password")
            val isLoading = ref(false)
            fun gen_togglePassword_fn() {
                passwordVisible.value = !passwordVisible.value
                inputType.value = if (passwordVisible.value) {
                    "text"
                } else {
                    "password"
                }
            }
            val togglePassword = ::gen_togglePassword_fn
            fun gen_login_fn(): UTSPromise<Unit> {
                return wrapUTSPromise(suspend w@{
                        if (!email.value || !password.value) {
                            uni_showToast(ShowToastOptions(title = "请输入邮箱和密码", icon = "none"))
                            return@w
                        }
                        val emailRegex = UTSRegExp("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+\$", "")
                        if (!emailRegex.test(email.value)) {
                            uni_showToast(ShowToastOptions(title = "请输入正确的邮箱格式", icon = "none"))
                            return@w
                        }
                        isLoading.value = true
                        try {
                            console.log("开始登录:", object : UTSJSONObject() {
                                var email = email.value
                            }, " at pages/login/login.uvue:86")
                            val response = await(userLogin(email.value, password.value))
                            console.log("登录响应:", response, " at pages/login/login.uvue:88")
                            if (response && response.code === 200 && response.data) {
                                val _response_data = response.data
                                val token = _response_data.token
                                val user = _response_data.user
                                console.log("登录成功，token已保存:", token.substring(0, 20) + "...", " at pages/login/login.uvue:94")
                                uni_setStorageSync("current_user", JSON.stringify(user))
                                uni_showToast(ShowToastOptions(title = "登录成功", icon = "success", mask = true, duration = 1500))
                                setTimeout(fun(){
                                    uni_navigateTo(NavigateToOptions(url = "/pages/index/index"))
                                }, 1500)
                            } else {
                                throw UTSError("登录响应格式错误")
                            }
                        }
                         catch (error: Throwable) {
                            console.error("登录失败:", error, " at pages/login/login.uvue:115")
                            val errorMsg = error?.message || "登录失败，请重试"
                            uni_showToast(ShowToastOptions(title = errorMsg, icon = "none"))
                        }
                         finally{
                            isLoading.value = false
                        }
                })
            }
            val login = ::gen_login_fn
            fun gen_goToForgotPassword_fn() {
                uni_navigateTo(NavigateToOptions(url = "/pages/forgot-password/forgot-password"))
            }
            val goToForgotPassword = ::gen_goToForgotPassword_fn
            fun gen_goToRegister_fn() {
                uni_navigateTo(NavigateToOptions(url = "/pages/register/register"))
            }
            val goToRegister = ::gen_goToRegister_fn
            return fun(): Any? {
                return createElementVNode("view", utsMapOf("class" to "container"), utsArrayOf(
                    createElementVNode("view", utsMapOf("class" to "header"), utsArrayOf(
                        createElementVNode("text", utsMapOf("class" to "title"), "登录"),
                        createElementVNode("text", utsMapOf("class" to "subtitle"), "欢迎回来！")
                    )),
                    createElementVNode("view", utsMapOf("class" to "form"), utsArrayOf(
                        createElementVNode("view", utsMapOf("class" to "input-group"), utsArrayOf(
                            createElementVNode("input", utsMapOf("modelValue" to email.value, "onInput" to fun(`$event`: InputEvent){
                                email.value = `$event`.detail.value
                            }
                            , "class" to "input-field", "placeholder" to "请输入邮箱", "type" to "email"), null, 40, utsArrayOf(
                                "modelValue",
                                "onInput"
                            ))
                        )),
                        createElementVNode("view", utsMapOf("class" to "input-group"), utsArrayOf(
                            createElementVNode("input", utsMapOf("modelValue" to password.value, "onInput" to fun(`$event`: InputEvent){
                                password.value = `$event`.detail.value
                            }
                            , "class" to "input-field", "placeholder" to "请输入密码", "type" to inputType.value), null, 40, utsArrayOf(
                                "modelValue",
                                "onInput",
                                "type"
                            )),
                            createElementVNode("text", utsMapOf("class" to "toggle-password", "onClick" to togglePassword), toDisplayString(if (passwordVisible.value) {
                                "隐藏"
                            } else {
                                "显示"
                            }
                            ), 1)
                        )),
                        createElementVNode("view", utsMapOf("class" to "button-group"), utsArrayOf(
                            createElementVNode("button", utsMapOf("class" to "login-btn", "onClick" to login, "disabled" to isLoading.value), toDisplayString(if (isLoading.value) {
                                "登录中..."
                            } else {
                                "登录"
                            }
                            ), 9, utsArrayOf(
                                "disabled"
                            ))
                        )),
                        createElementVNode("view", utsMapOf("class" to "footer-links"), utsArrayOf(
                            createElementVNode("text", utsMapOf("class" to "forgot-password", "onClick" to goToForgotPassword), "忘记密码?"),
                            createElementVNode("text", utsMapOf("class" to "register", "onClick" to goToRegister), "没有账号? 注册")
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
                return utsMapOf("container" to padStyleMapOf(utsMapOf("backgroundColor" to "#f5f7fa", "paddingBottom" to "10%")), "header" to padStyleMapOf(utsMapOf("backgroundImage" to "linear-gradient(to right, #4e54c8, #8f94fb)", "backgroundColor" to "rgba(0,0,0,0)", "paddingTop" to "80rpx", "paddingRight" to "30rpx", "paddingBottom" to "90rpx", "paddingLeft" to "30rpx", "textAlign" to "center", "alignItems" to "center", "color" to "#FFFFFF", "borderBottomLeftRadius" to "30rpx", "borderBottomRightRadius" to "30rpx")), "title" to padStyleMapOf(utsMapOf("fontSize" to "36rpx", "fontWeight" to "bold")), "subtitle" to padStyleMapOf(utsMapOf("fontSize" to "24rpx", "marginTop" to "10rpx")), "form" to padStyleMapOf(utsMapOf("paddingTop" to "30rpx", "paddingRight" to "30rpx", "paddingBottom" to "30rpx", "paddingLeft" to "30rpx", "backgroundColor" to "#FFFFFF", "borderTopLeftRadius" to "20rpx", "borderTopRightRadius" to "20rpx", "borderBottomRightRadius" to "20rpx", "borderBottomLeftRadius" to "20rpx", "marginTop" to "-40rpx", "boxShadow" to "0 4rpx 12rpx rgba(0, 0, 0, 0.1)")), "input-group" to padStyleMapOf(utsMapOf("marginBottom" to "30rpx")), "input-field" to padStyleMapOf(utsMapOf("width" to "100%", "paddingTop" to "16rpx", "paddingRight" to "20rpx", "paddingBottom" to "16rpx", "paddingLeft" to "20rpx", "fontSize" to "24rpx", "borderTopLeftRadius" to "12rpx", "borderTopRightRadius" to "12rpx", "borderBottomRightRadius" to "12rpx", "borderBottomLeftRadius" to "12rpx", "borderTopWidth" to 1, "borderRightWidth" to 1, "borderBottomWidth" to 1, "borderLeftWidth" to 1, "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#cccccc", "borderRightColor" to "#cccccc", "borderBottomColor" to "#cccccc", "borderLeftColor" to "#cccccc", "backgroundColor" to "#f9f9f9")), "toggle-password" to padStyleMapOf(utsMapOf("fontSize" to "20rpx", "color" to "#4e54c8", "cursor" to "pointer")), "button-group" to padStyleMapOf(utsMapOf("marginTop" to "20rpx")), "login-btn" to padStyleMapOf(utsMapOf("width" to "100%", "paddingTop" to "16rpx", "paddingRight" to "16rpx", "paddingBottom" to "16rpx", "paddingLeft" to "16rpx", "fontSize" to "28rpx", "backgroundColor" to "#4e54c8", "color" to "#FFFFFF", "borderTopLeftRadius" to "30rpx", "borderTopRightRadius" to "30rpx", "borderBottomRightRadius" to "30rpx", "borderBottomLeftRadius" to "30rpx", "borderTopWidth" to "medium", "borderRightWidth" to "medium", "borderBottomWidth" to "medium", "borderLeftWidth" to "medium", "borderTopStyle" to "none", "borderRightStyle" to "none", "borderBottomStyle" to "none", "borderLeftStyle" to "none", "borderTopColor" to "#000000", "borderRightColor" to "#000000", "borderBottomColor" to "#000000", "borderLeftColor" to "#000000")), "footer-links" to padStyleMapOf(utsMapOf("display" to "flex", "justifyContent" to "space-between", "marginTop" to "20rpx")), "forgot-password" to padStyleMapOf(utsMapOf("fontSize" to "24rpx", "color" to "#4e54c8", "cursor" to "pointer")), "register" to padStyleMapOf(utsMapOf("fontSize" to "24rpx", "color" to "#4e54c8", "cursor" to "pointer")))
            }
        var inheritAttrs = true
        var inject: Map<String, Map<String, Any?>> = utsMapOf()
        var emits: Map<String, Any?> = utsMapOf()
        var props = normalizePropsOptions(utsMapOf())
        var propsNeedCastKeys: UTSArray<String> = utsArrayOf()
        var components: Map<String, CreateVueComponent> = utsMapOf()
    }
}
