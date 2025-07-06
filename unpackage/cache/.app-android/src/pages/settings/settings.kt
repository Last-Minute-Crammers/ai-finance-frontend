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
import io.dcloud.uniapp.extapi.chooseImage as uni_chooseImage
import io.dcloud.uniapp.extapi.getStorageSync as uni_getStorageSync
import io.dcloud.uniapp.extapi.hideLoading as uni_hideLoading
import io.dcloud.uniapp.extapi.navigateBack as uni_navigateBack
import io.dcloud.uniapp.extapi.reLaunch as uni_reLaunch
import io.dcloud.uniapp.extapi.removeStorageSync as uni_removeStorageSync
import io.dcloud.uniapp.extapi.showActionSheet as uni_showActionSheet
import io.dcloud.uniapp.extapi.showLoading as uni_showLoading
import io.dcloud.uniapp.extapi.showModal as uni_showModal
import io.dcloud.uniapp.extapi.showToast as uni_showToast
open class GenPagesSettingsSettings : BasePage {
    constructor(__ins: ComponentInternalInstance, __renderer: String?) : super(__ins, __renderer) {}
    companion object {
        @Suppress("UNUSED_PARAMETER", "UNUSED_VARIABLE")
        var setup: (__props: GenPagesSettingsSettings) -> Any? = fun(__props): Any? {
            val __ins = getCurrentInstance()!!
            val _ctx = __ins.proxy as GenPagesSettingsSettings
            val _cache = __ins.renderCache
            val nickname = ref("")
            val avatarUrl = ref("/static/icons/default-avatar.png")
            val userId = ref("")
            val isConnected = ref(false)
            val isTesting = ref(false)
            val currentApiUrl = ref("http://47.109.194.39/api")
            val responseTime = ref(0)
            val errorMessage = ref("")
            onMounted(fun(){
                val userStr = uni_getStorageSync("current_user")
                if (userStr) {
                    try {
                        val user = UTSAndroid.consoleDebugError(JSON.parse(userStr), " at pages/settings/settings.uvue:99")
                        nickname.value = user.username || ""
                        userId.value = if (user.id) {
                            String(user.id)
                        } else {
                            ""
                        }
                    }
                     catch (e: Throwable) {}
                }
                val backendUrl = uni_getStorageSync("backend_url")
                if (backendUrl) {
                    currentApiUrl.value = backendUrl
                }
                testConnection()
            }
            )
            fun gen_goBack_fn() {
                uni_navigateBack(null)
            }
            val goBack = ::gen_goBack_fn
            fun gen_chooseAvatar_fn() {
                uni_chooseImage(ChooseImageOptions(count = 1, success = fun(res){
                    avatarUrl.value = res.tempFilePaths[0]
                    uni_showToast(ShowToastOptions(title = "头像更新成功", icon = "success"))
                }
                ))
            }
            val chooseAvatar = ::gen_chooseAvatar_fn
            fun gen_showLogoutConfirm_fn() {
                uni_showModal(ShowModalOptions(title = "确认退出", content = "确定要退出登录吗？", confirmText = "退出", cancelText = "取消", confirmColor = "#dc143c", success = fun(res){
                    if (res.confirm) {
                        logout()
                    }
                }
                ))
            }
            val showLogoutConfirm = ::gen_showLogoutConfirm_fn
            fun gen_logout_fn() {
                uni_showLoading(ShowLoadingOptions(title = "退出中..."))
                setTimeout(fun(){
                    uni_hideLoading()
                    uni_showToast(ShowToastOptions(title = "已退出登录", icon = "success", duration = 2000))
                    uni_removeStorageSync("current_user")
                    uni_removeStorageSync("token")
                    uni_reLaunch(ReLaunchOptions(url = "/pages/login/login"))
                }
                , 1000)
            }
            val logout = ::gen_logout_fn
            fun gen_testConnection_fn(): UTSPromise<Unit> {
                return wrapUTSPromise(suspend w@{
                        if (isTesting.value) {
                            return@w
                        }
                        isTesting.value = true
                        errorMessage.value = ""
                        try {
                            val result = await(checkBackendConnection(currentApiUrl.value))
                            if (result.connected) {
                                isConnected.value = true
                                responseTime.value = result.responseTime || 0
                                uni_showToast(ShowToastOptions(title = "连接成功", icon = "success"))
                            } else {
                                isConnected.value = false
                                errorMessage.value = result.error || "连接失败"
                                uni_showToast(ShowToastOptions(title = "连接失败", icon = "error"))
                            }
                        }
                         catch (error: Throwable) {
                            isConnected.value = false
                            errorMessage.value = error.message || "测试连接时发生错误"
                            uni_showToast(ShowToastOptions(title = "连接测试失败", icon = "error"))
                        }
                         finally{
                            isTesting.value = false
                        }
                })
            }
            val testConnection = ::gen_testConnection_fn
            fun gen_showApiSelector_fn() {
                val availableUrls = getAvailableBackendUrls()
                val urlOptions = Object.keys(availableUrls).map(fun(key): UTSJSONObject {
                    return (object : UTSJSONObject() {
                        var label = "" + key + ": " + availableUrls[key]
                        var value = key
                    })
                }
                )
                uni_showActionSheet(ShowActionSheetOptions(itemList = urlOptions.map(fun(item): String {
                    return item.label
                }
                ), success = fun(res){
                    val selectedKey = urlOptions[res.tapIndex].value
                    val selectedUrl = availableUrls[selectedKey]
                    setBackendEnvironment(selectedKey)
                    currentApiUrl.value = selectedUrl
                    uni_showToast(ShowToastOptions(title = "API已切换", icon = "success"))
                    testConnection()
                }
                ))
            }
            val showApiSelector = ::gen_showApiSelector_fn
            return fun(): Any? {
                return createElementVNode("view", utsMapOf("class" to "container"), utsArrayOf(
                    createElementVNode("view", utsMapOf("class" to "header"), utsArrayOf(
                        createElementVNode("text", utsMapOf("class" to "back", "onClick" to goBack), "←"),
                        createElementVNode("view", utsMapOf("class" to "title-section"), utsArrayOf(
                            createElementVNode("text", utsMapOf("class" to "title"), "设置"),
                            createElementVNode("text", utsMapOf("class" to "subtitle"), "个人信息管理")
                        ))
                    )),
                    createElementVNode("view", utsMapOf("class" to "user-card"), utsArrayOf(
                        createElementVNode("view", utsMapOf("class" to "info-item"), utsArrayOf(
                            createElementVNode("text", utsMapOf("class" to "info-label"), "头像"),
                            createElementVNode("view", utsMapOf("class" to "info-content", "onClick" to chooseAvatar), utsArrayOf(
                                createElementVNode("image", utsMapOf("src" to avatarUrl.value, "class" to "avatar", "mode" to "aspectFill"), null, 8, utsArrayOf(
                                    "src"
                                ))
                            ))
                        )),
                        createElementVNode("view", utsMapOf("class" to "info-item"), utsArrayOf(
                            createElementVNode("text", utsMapOf("class" to "info-label"), "昵称"),
                            createElementVNode("text", utsMapOf("class" to "info-content"), toDisplayString(nickname.value || "未设置"), 1)
                        )),
                        createElementVNode("view", utsMapOf("class" to "info-item"), utsArrayOf(
                            createElementVNode("text", utsMapOf("class" to "info-label"), "用户ID"),
                            createElementVNode("text", utsMapOf("class" to "info-content"), toDisplayString(userId.value || "未知"), 1)
                        ))
                    )),
                    createElementVNode("view", utsMapOf("class" to "api-card"), utsArrayOf(
                        createElementVNode("view", utsMapOf("class" to "card-header"), utsArrayOf(
                            createElementVNode("text", utsMapOf("class" to "card-title"), "API连接状态"),
                            createElementVNode("text", utsMapOf("class" to "card-subtitle"), "测试后端服务连接")
                        )),
                        createElementVNode("view", utsMapOf("class" to "connection-status"), utsArrayOf(
                            createElementVNode("view", utsMapOf("class" to normalizeClass(utsArrayOf(
                                "status-indicator",
                                utsMapOf("connected" to isConnected.value, "disconnected" to !isConnected.value)
                            ))), utsArrayOf(
                                createElementVNode("text", utsMapOf("class" to "status-dot")),
                                createElementVNode("text", utsMapOf("class" to "status-text"), toDisplayString(if (isConnected.value) {
                                    "已连接"
                                } else {
                                    "未连接"
                                }
                                ), 1)
                            ), 2),
                            createElementVNode("view", utsMapOf("class" to "api-info"), utsArrayOf(
                                createElementVNode("text", utsMapOf("class" to "api-url"), toDisplayString(currentApiUrl.value), 1),
                                if (isTrue(responseTime.value)) {
                                    createElementVNode("text", utsMapOf("key" to 0, "class" to "response-time"), "响应时间: " + toDisplayString(responseTime.value) + "ms", 1)
                                } else {
                                    createCommentVNode("v-if", true)
                                }
                            ))
                        )),
                        createElementVNode("view", utsMapOf("class" to "action-buttons"), utsArrayOf(
                            createElementVNode("button", utsMapOf("class" to "test-btn", "onClick" to testConnection, "disabled" to isTesting.value), utsArrayOf(
                                createElementVNode("text", utsMapOf("class" to "btn-icon"), "🔄"),
                                createElementVNode("text", utsMapOf("class" to "btn-text"), toDisplayString(if (isTesting.value) {
                                    "测试中..."
                                } else {
                                    "测试连接"
                                }
                                ), 1)
                            ), 8, utsArrayOf(
                                "disabled"
                            )),
                            createElementVNode("button", utsMapOf("class" to "switch-btn", "onClick" to showApiSelector), utsArrayOf(
                                createElementVNode("text", utsMapOf("class" to "btn-icon"), "⚙️"),
                                createElementVNode("text", utsMapOf("class" to "btn-text"), "切换API")
                            ))
                        )),
                        if (isTrue(errorMessage.value)) {
                            createElementVNode("view", utsMapOf("key" to 0, "class" to "error-info"), utsArrayOf(
                                createElementVNode("text", utsMapOf("class" to "error-title"), "连接错误:"),
                                createElementVNode("text", utsMapOf("class" to "error-message"), toDisplayString(errorMessage.value), 1)
                            ))
                        } else {
                            createCommentVNode("v-if", true)
                        }
                    )),
                    createElementVNode("view", utsMapOf("class" to "logout-section"), utsArrayOf(
                        createElementVNode("button", utsMapOf("class" to "logout-btn", "onClick" to showLogoutConfirm), utsArrayOf(
                            createElementVNode("text", utsMapOf("class" to "logout-icon"), "🚪"),
                            createElementVNode("text", utsMapOf("class" to "logout-text"), "退出登录")
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
                return utsMapOf("container" to padStyleMapOf(utsMapOf("backgroundImage" to "linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%)", "backgroundColor" to "rgba(0,0,0,0)", "paddingBottom" to "10%")), "header" to padStyleMapOf(utsMapOf("backgroundImage" to "linear-gradient(135deg, #667eea 0%, #764ba2 100%)", "backgroundColor" to "rgba(0,0,0,0)", "paddingTop" to "80rpx", "paddingRight" to "30rpx", "paddingBottom" to "40rpx", "paddingLeft" to "30rpx", "borderBottomLeftRadius" to "30rpx", "borderBottomRightRadius" to "30rpx", "position" to "relative", "textAlign" to "center", "boxShadow" to "0 4rpx 20rpx rgba(102, 126, 234, 0.2)")), "back" to padStyleMapOf(utsMapOf("position" to "absolute", "left" to "30rpx", "top" to "80rpx", "fontSize" to "36rpx", "color" to "#FFFFFF", "fontWeight" to "bold")), "title-section" to padStyleMapOf(utsMapOf("textAlign" to "center")), "title" to padStyleMapOf(utsMapOf("fontSize" to "36rpx", "fontWeight" to "bold", "color" to "#FFFFFF", "marginBottom" to "8rpx")), "subtitle" to padStyleMapOf(utsMapOf("fontSize" to "24rpx", "color" to "rgba(255,255,255,0.8)")), "user-card" to padStyleMapOf(utsMapOf("backgroundImage" to "none", "backgroundColor" to "rgba(255,255,255,0.95)", "marginTop" to "40rpx", "marginRight" to "30rpx", "marginBottom" to "20rpx", "marginLeft" to "30rpx", "borderTopLeftRadius" to "20rpx", "borderTopRightRadius" to "20rpx", "borderBottomRightRadius" to "20rpx", "borderBottomLeftRadius" to "20rpx", "boxShadow" to "0 8rpx 24rpx rgba(0, 0, 0, 0.1)", "paddingTop" to "30rpx", "paddingRight" to "30rpx", "paddingBottom" to "30rpx", "paddingLeft" to "30rpx", "backdropFilter" to "blur(10rpx)")), "info-item" to padStyleMapOf(utsMapOf("marginBottom" to "40rpx", "marginBottom:last-child" to 0)), "info-label" to padStyleMapOf(utsMapOf("fontSize" to "26rpx", "color" to "#666666", "marginBottom" to "16rpx")), "info-content" to padStyleMapOf(utsMapOf("fontSize" to "30rpx", "color" to "#333333", "paddingTop" to "20rpx", "paddingRight" to 0, "paddingBottom" to "20rpx", "paddingLeft" to 0, "borderBottomWidth" to "1rpx", "borderBottomStyle" to "solid", "borderBottomColor" to "#f0f0f0", "display" to "flex", "alignItems" to "center", "justifyContent" to "center")), "avatar" to padStyleMapOf(utsMapOf("width" to "120rpx", "height" to "120rpx", "backgroundImage" to "none", "backgroundColor" to "#f0f0f0", "borderTopWidth" to "3rpx", "borderRightWidth" to "3rpx", "borderBottomWidth" to "3rpx", "borderLeftWidth" to "3rpx", "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#667eea", "borderRightColor" to "#667eea", "borderBottomColor" to "#667eea", "borderLeftColor" to "#667eea", "boxShadow" to "0 4rpx 16rpx rgba(102, 126, 234, 0.2)")), "logout-section" to padStyleMapOf(utsMapOf("marginTop" to "40rpx", "marginRight" to "30rpx", "marginBottom" to 0, "marginLeft" to "30rpx")), "logout-btn" to padStyleMapOf(utsMapOf("width" to "100%", "backgroundImage" to "linear-gradient(135deg, #dc143c, #b22222)", "backgroundColor" to "rgba(0,0,0,0)", "color" to "#ffffff", "fontSize" to "28rpx", "borderTopWidth" to "medium", "borderRightWidth" to "medium", "borderBottomWidth" to "medium", "borderLeftWidth" to "medium", "borderTopStyle" to "none", "borderRightStyle" to "none", "borderBottomStyle" to "none", "borderLeftStyle" to "none", "borderTopColor" to "#000000", "borderRightColor" to "#000000", "borderBottomColor" to "#000000", "borderLeftColor" to "#000000", "borderTopLeftRadius" to "20rpx", "borderTopRightRadius" to "20rpx", "borderBottomRightRadius" to "20rpx", "borderBottomLeftRadius" to "20rpx", "paddingTop" to "30rpx", "paddingRight" to 0, "paddingBottom" to "30rpx", "paddingLeft" to 0, "fontWeight" to "bold", "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "gap" to "16rpx", "boxShadow" to "0 8rpx 24rpx rgba(220, 20, 60, 0.3)", "transitionProperty" to "transform", "transitionDuration" to "0.2s", "transform:active" to "scale(0.98)")), "logout-icon" to padStyleMapOf(utsMapOf("fontSize" to "24rpx")), "logout-text" to padStyleMapOf(utsMapOf("fontSize" to "28rpx")), "api-card" to padStyleMapOf(utsMapOf("backgroundImage" to "none", "backgroundColor" to "rgba(255,255,255,0.95)", "marginTop" to "20rpx", "marginRight" to "30rpx", "marginBottom" to "20rpx", "marginLeft" to "30rpx", "borderTopLeftRadius" to "20rpx", "borderTopRightRadius" to "20rpx", "borderBottomRightRadius" to "20rpx", "borderBottomLeftRadius" to "20rpx", "boxShadow" to "0 8rpx 24rpx rgba(0, 0, 0, 0.1)", "paddingTop" to "30rpx", "paddingRight" to "30rpx", "paddingBottom" to "30rpx", "paddingLeft" to "30rpx", "backdropFilter" to "blur(10rpx)")), "card-header" to padStyleMapOf(utsMapOf("marginBottom" to "30rpx")), "card-title" to padStyleMapOf(utsMapOf("fontSize" to "28rpx", "fontWeight" to "bold", "color" to "#333333", "marginBottom" to "8rpx")), "card-subtitle" to padStyleMapOf(utsMapOf("fontSize" to "24rpx", "color" to "#666666")), "connection-status" to padStyleMapOf(utsMapOf("marginBottom" to "30rpx")), "status-indicator" to padStyleMapOf(utsMapOf("display" to "flex", "alignItems" to "center", "marginBottom" to "20rpx")), "status-dot" to utsMapOf("" to utsMapOf("width" to "16rpx", "height" to "16rpx", "marginRight" to "16rpx"), ".status-indicator.connected " to utsMapOf("backgroundImage" to "none", "backgroundColor" to "#4CAF50", "boxShadow" to "0 0 8rpx rgba(76, 175, 80, 0.5)"), ".status-indicator.disconnected " to utsMapOf("backgroundImage" to "none", "backgroundColor" to "#f44336", "boxShadow" to "0 0 8rpx rgba(244, 67, 54, 0.5)")), "status-text" to utsMapOf("" to utsMapOf("fontSize" to "26rpx"), ".status-indicator.connected " to utsMapOf("color" to "#4CAF50"), ".status-indicator.disconnected " to utsMapOf("color" to "#f44336")), "api-info" to padStyleMapOf(utsMapOf("backgroundImage" to "none", "backgroundColor" to "#f8f9fa", "paddingTop" to "20rpx", "paddingRight" to "20rpx", "paddingBottom" to "20rpx", "paddingLeft" to "20rpx", "borderTopLeftRadius" to "12rpx", "borderTopRightRadius" to "12rpx", "borderBottomRightRadius" to "12rpx", "borderBottomLeftRadius" to "12rpx", "borderLeftWidth" to "4rpx", "borderLeftStyle" to "solid", "borderLeftColor" to "#667eea")), "api-url" to padStyleMapOf(utsMapOf("fontSize" to "24rpx", "color" to "#333333", "fontFamily" to "monospace", "marginBottom" to "8rpx", "wordBreak" to "break-all")), "response-time" to padStyleMapOf(utsMapOf("fontSize" to "22rpx", "color" to "#666666")), "action-buttons" to padStyleMapOf(utsMapOf("display" to "flex", "gap" to "20rpx", "marginBottom" to "20rpx")), "test-btn" to padStyleMapOf(utsMapOf("flex" to 1, "backgroundImage" to "linear-gradient(135deg, #667eea, #764ba2)", "backgroundColor" to "rgba(0,0,0,0)", "color" to "#ffffff", "fontSize" to "24rpx", "borderTopWidth" to "medium", "borderRightWidth" to "medium", "borderBottomWidth" to "medium", "borderLeftWidth" to "medium", "borderTopStyle" to "none", "borderRightStyle" to "none", "borderBottomStyle" to "none", "borderLeftStyle" to "none", "borderTopColor" to "#000000", "borderRightColor" to "#000000", "borderBottomColor" to "#000000", "borderLeftColor" to "#000000", "borderTopLeftRadius" to "12rpx", "borderTopRightRadius" to "12rpx", "borderBottomRightRadius" to "12rpx", "borderBottomLeftRadius" to "12rpx", "paddingTop" to "20rpx", "paddingRight" to 0, "paddingBottom" to "20rpx", "paddingLeft" to 0, "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "gap" to "12rpx", "boxShadow" to "0 4rpx 16rpx rgba(102, 126, 234, 0.3)", "transitionProperty" to "transform", "transitionDuration" to "0.2s", "transform:active" to "scale(0.98)", "backgroundImage:disabled" to "none", "backgroundColor:disabled" to "#cccccc", "boxShadow:disabled" to "none")), "switch-btn" to padStyleMapOf(utsMapOf("flex" to 1, "backgroundImage" to "linear-gradient(135deg, #667eea, #764ba2)", "backgroundColor" to "rgba(0,0,0,0)", "color" to "#ffffff", "fontSize" to "24rpx", "borderTopWidth" to "medium", "borderRightWidth" to "medium", "borderBottomWidth" to "medium", "borderLeftWidth" to "medium", "borderTopStyle" to "none", "borderRightStyle" to "none", "borderBottomStyle" to "none", "borderLeftStyle" to "none", "borderTopColor" to "#000000", "borderRightColor" to "#000000", "borderBottomColor" to "#000000", "borderLeftColor" to "#000000", "borderTopLeftRadius" to "12rpx", "borderTopRightRadius" to "12rpx", "borderBottomRightRadius" to "12rpx", "borderBottomLeftRadius" to "12rpx", "paddingTop" to "20rpx", "paddingRight" to 0, "paddingBottom" to "20rpx", "paddingLeft" to 0, "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "gap" to "12rpx", "boxShadow" to "0 4rpx 16rpx rgba(102, 126, 234, 0.3)", "transitionProperty" to "transform", "transitionDuration" to "0.2s", "transform:active:active" to "scale(0.98)")), "btn-icon" to padStyleMapOf(utsMapOf("fontSize" to "20rpx")), "btn-text" to padStyleMapOf(utsMapOf("fontSize" to "24rpx")), "error-info" to padStyleMapOf(utsMapOf("backgroundImage" to "none", "backgroundColor" to "#ffebee", "borderTopWidth" to "1rpx", "borderRightWidth" to "1rpx", "borderBottomWidth" to "1rpx", "borderLeftWidth" to "1rpx", "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#ffcdd2", "borderRightColor" to "#ffcdd2", "borderBottomColor" to "#ffcdd2", "borderLeftColor" to "#ffcdd2", "borderTopLeftRadius" to "12rpx", "borderTopRightRadius" to "12rpx", "borderBottomRightRadius" to "12rpx", "borderBottomLeftRadius" to "12rpx", "paddingTop" to "20rpx", "paddingRight" to "20rpx", "paddingBottom" to "20rpx", "paddingLeft" to "20rpx", "marginTop" to "20rpx")), "error-title" to padStyleMapOf(utsMapOf("fontSize" to "24rpx", "color" to "#d32f2f", "marginBottom" to "8rpx")), "error-message" to padStyleMapOf(utsMapOf("fontSize" to "22rpx", "color" to "#c62828", "wordBreak" to "break-all", "lineHeight" to 1.4)), "@TRANSITION" to utsMapOf("logout-btn" to utsMapOf("property" to "transform", "duration" to "0.2s"), "test-btn" to utsMapOf("property" to "transform", "duration" to "0.2s"), "switch-btn" to utsMapOf("property" to "transform", "duration" to "0.2s")))
            }
        var inheritAttrs = true
        var inject: Map<String, Map<String, Any?>> = utsMapOf()
        var emits: Map<String, Any?> = utsMapOf()
        var props = normalizePropsOptions(utsMapOf())
        var propsNeedCastKeys: UTSArray<String> = utsArrayOf()
        var components: Map<String, CreateVueComponent> = utsMapOf()
    }
}
