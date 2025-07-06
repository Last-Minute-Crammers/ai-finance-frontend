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
import io.dcloud.uniapp.extapi.showToast as uni_showToast
open class GenPagesAIchatAIchat : BasePage {
    constructor(__ins: ComponentInternalInstance, __renderer: String?) : super(__ins, __renderer) {}
    companion object {
        @Suppress("UNUSED_PARAMETER", "UNUSED_VARIABLE")
        var setup: (__props: GenPagesAIchatAIchat) -> Any? = fun(__props): Any? {
            val __ins = getCurrentInstance()!!
            val _ctx = __ins.proxy as GenPagesAIchatAIchat
            val _cache = __ins.renderCache
            val aiAvatar = "/static/icons/pet.png"
            val userAvatar = "/static/icons/user.png"
            val inputText = ref("")
            val isLoading = ref(false)
            val messages = ref(utsArrayOf<ChatMessage>())
            val bottomAnchor = ref("bottom-anchor")
            val scrollIntoView = ref("bottomAnchor")
            val showDrawer = ref(false)
            val historyLoading = ref(false)
            val chatHistory = ref(utsArrayOf<ChatSessionPreview>())
            val currentSessionId = ref<String?>(null)
            val sessionLoading = ref(false)
            onMounted(fun(){
                console.log("AI聊天页面初始化")
                val pages = getCurrentPages()
                val currentPage = pages[pages.length - 1]
                console.log("当前页面信息:", currentPage)
                if (currentPage && currentPage.options && currentPage.options["sessionId"]) {
                    val sessionId = currentPage.options["sessionId"]
                    console.log("检测到传入的sessionId:", sessionId)
                    loadSession(sessionId)
                } else {
                    console.log("没有检测到传入的sessionId，显示默认欢迎语")
                    messages.value = utsArrayOf(
                        object : UTSJSONObject() {
                            var role = "ai"
                            var text = "你好，我是理财小汪，你的智能理财助手，有什么可以帮您？"
                        }
                    )
                }
            }
            )
            fun gen_openDrawer_fn() {
                showDrawer.value = true
                loadHistory()
            }
            val openDrawer = ::gen_openDrawer_fn
            fun gen_closeDrawer_fn() {
                showDrawer.value = false
            }
            val closeDrawer = ::gen_closeDrawer_fn
            fun gen_loadHistory_fn(): UTSPromise<Unit> {
                return wrapUTSPromise(suspend {
                        historyLoading.value = true
                        try {
                            val res = await(getAIChatHistory())
                            console.log("历史记录API响应:", res)
                            if (res.success && UTSArray.isArray(res.data)) {
                                chatHistory.value = res.data.map(fun(item: Any){
                                    return (object : UTSJSONObject() {
                                        var sessionId = item.sessionId
                                        var firstQuestion = item.firstQuestion || "未知对话"
                                        var createdAt = item.createdAt || ""
                                    })
                                })
                                console.log("处理后的历史记录:", chatHistory.value)
                            } else {
                                chatHistory.value = utsArrayOf()
                                console.warn("历史记录响应格式错误:", res)
                            }
                        }
                         catch (e: Throwable) {
                            console.error("加载历史记录失败:", e)
                            chatHistory.value = utsArrayOf()
                        }
                         finally{
                            historyLoading.value = false
                        }
                })
            }
            val loadHistory = ::gen_loadHistory_fn
            fun gen_loadSession_fn(sessionId: String): UTSPromise<Unit> {
                return wrapUTSPromise(suspend {
                        closeDrawer()
                        currentSessionId.value = sessionId
                        sessionLoading.value = true
                        console.log("开始加载会话:", sessionId)
                        try {
                            val res = await(getAIChatSessionDetail(sessionId))
                            console.log("会话详情API响应:", res)
                            if (res.success && UTSArray.isArray(res.data)) {
                                val sorted = res.data.slice().sort(fun(a, b){
                                    return Date(a.createdAt).getTime() - Date(b.createdAt).getTime()
                                })
                                console.log("排序后的会话数据:", sorted)
                                messages.value = utsArrayOf()
                                sorted.forEach(fun(item: Any, index: Number){
                                    console.log("\u5904\u7406\u7B2C" + (index + 1) + "\u6761\u8BB0\u5F55:", item)
                                    console.log("- input: \"" + item.Input + "\"")
                                    console.log("- response: \"" + item.Response + "\"")
                                    if (item.Input && item.Input.trim() !== "") {
                                        messages.value.push(object : UTSJSONObject() {
                                            var role = "user"
                                            var text = item.Input
                                        })
                                        console.log("\u6DFB\u52A0\u7528\u6237\u6D88\u606F: \"" + item.Input + "\"")
                                    }
                                    if (item.Response && item.Response.trim() !== "") {
                                        messages.value.push(object : UTSJSONObject() {
                                            var role = "ai"
                                            var text = item.Response
                                        })
                                        console.log("\u6DFB\u52A0AI\u6D88\u606F: \"" + item.Response + "\"")
                                    }
                                })
                                console.log("处理完成后的消息数组:", messages.value)
                                if (messages.value.length === 0) {
                                    console.log("没有找到历史消息，显示默认欢迎语")
                                    messages.value.push(object : UTSJSONObject() {
                                        var role = "ai"
                                        var text = "你好，我是理财小汪，你的智能理财助手，有什么可以帮您？"
                                    })
                                } else {
                                    console.log("成功加载历史消息，消息数量:", messages.value.length)
                                }
                                scrollToBottom()
                                console.log("成功加载会话:", sessionId, "消息数量:", messages.value.length)
                            } else {
                                messages.value = utsArrayOf(
                                    object : UTSJSONObject() {
                                        var role = "ai"
                                        var text = "加载历史对话失败，请稍后重试。"
                                    }
                                )
                                console.error("加载会话失败: 响应数据格式错误", res)
                            }
                        }
                         catch (e: Throwable) {
                            console.error("加载会话失败:", e)
                            messages.value = utsArrayOf(
                                object : UTSJSONObject() {
                                    var role = "ai"
                                    var text = "加载历史对话失败，请稍后重试。"
                                }
                            )
                        }
                         finally{
                            sessionLoading.value = false
                        }
                })
            }
            val loadSession = ::gen_loadSession_fn
            fun gen_sendMessage_fn(): UTSPromise<Unit> {
                return wrapUTSPromise(suspend w@{
                        val content = inputText.value.trim()
                        if (!content || isLoading.value) {
                            return@w
                        }
                        messages.value.push(object : UTSJSONObject() {
                            var role = "user"
                            var text = content
                        })
                        inputText.value = ""
                        scrollToBottom()
                        val loadingMsgIndex = messages.value.length
                        messages.value.push(object : UTSJSONObject() {
                            var role = "ai"
                            var text = "正在思考中..."
                            var loading = true
                        })
                        scrollToBottom()
                        isLoading.value = true
                        try {
                            console.log("发送消息，当前sessionId:", currentSessionId.value)
                            val response = await(sendAIMessage(content, currentSessionId.value || undefined))
                            messages.value.splice(loadingMsgIndex, 1)
                            if (response.success && response.data) {
                                messages.value.push(object : UTSJSONObject() {
                                    var role = "ai"
                                    var text = response.data
                                })
                                if (!currentSessionId.value) {
                                    await(loadHistory())
                                    if (chatHistory.value.length > 0) {
                                        currentSessionId.value = chatHistory.value[0].sessionId
                                        console.log("获取到新的sessionId:", currentSessionId.value)
                                    }
                                }
                            } else {
                                val errorMsg = response.error || "抱歉，我暂时无法回答这个问题。"
                                messages.value.push(object : UTSJSONObject() {
                                    var role = "ai"
                                    var text = errorMsg
                                })
                            }
                        }
                         catch (error: Throwable) {
                            console.error("发送消息失败:", error)
                            messages.value.splice(loadingMsgIndex, 1)
                            messages.value.push(object : UTSJSONObject() {
                                var role = "ai"
                                var text = "抱歉，我暂时无法连接到AI服务。请稍后再试或联系技术支持。"
                            })
                            uni_showToast(ShowToastOptions(title = "AI服务暂时不可用", icon = "none"))
                        }
                         finally{
                            isLoading.value = false
                            scrollToBottom()
                        }
                })
            }
            val sendMessage = ::gen_sendMessage_fn
            fun gen_scrollToBottom_fn() {
                nextTick(fun(){
                    scrollIntoView.value = bottomAnchor.value
                }
                )
            }
            val scrollToBottom = ::gen_scrollToBottom_fn
            fun gen_formatDate_fn(dateString: String): String {
                if (!dateString) {
                    return ""
                }
                try {
                    val date = Date(dateString)
                    val now = Date()
                    val diffTime = now.getTime() - date.getTime()
                    val diffDays = Math.floor(diffTime / 86400000)
                    if (diffDays === 0) {
                        return date.toLocaleTimeString("zh-CN", object : UTSJSONObject() {
                            var hour = "2-digit"
                            var minute = "2-digit"
                        })
                    } else if (diffDays === 1) {
                        return "昨天"
                    } else if (diffDays < 7) {
                        return "" + diffDays + "\u5929\u524D"
                    } else {
                        return date.toLocaleDateString("zh-CN", object : UTSJSONObject() {
                            var month = "2-digit"
                            var day = "2-digit"
                        })
                    }
                }
                 catch (e: Throwable) {
                    return dateString
                }
            }
            val formatDate = ::gen_formatDate_fn
            return fun(): Any? {
                return createElementVNode("view", utsMapOf("class" to "chat-wrapper"), utsArrayOf(
                    createElementVNode("view", utsMapOf("class" to "chat-header"), utsArrayOf(
                        createElementVNode("text", utsMapOf("class" to "chat-title"), "AI 理财助手"),
                        createElementVNode("text", utsMapOf("class" to "chat-subtitle"), "您的智能理财小伙伴"),
                        createElementVNode("button", utsMapOf("class" to "history-btn", "onClick" to openDrawer), "历史")
                    )),
                    createElementVNode("scroll-view", utsMapOf("class" to "chat-body", "scroll-y" to "", "scroll-into-view" to scrollIntoView.value, "scroll-with-animation" to ""), utsArrayOf(
                        if (isTrue(sessionLoading.value)) {
                            createElementVNode("view", utsMapOf("key" to 0, "class" to "loading-container"), utsArrayOf(
                                createElementVNode("text", utsMapOf("class" to "loading-text"), "正在加载历史对话...")
                            ))
                        } else {
                            createElementVNode("view", utsMapOf("key" to 1), utsArrayOf(
                                createElementVNode(Fragment, null, RenderHelpers.renderList(messages.value, fun(msg, index, __index, _cached): Any {
                                    return createElementVNode("view", utsMapOf("key" to index, "id" to ("msg-" + index), "class" to normalizeClass(utsArrayOf(
                                        "chat-message",
                                        msg.role
                                    ))), utsArrayOf(
                                        createElementVNode("image", utsMapOf("class" to "avatar", "src" to if (msg.role === "ai") {
                                            aiAvatar
                                        } else {
                                            userAvatar
                                        }
                                        ), null, 8, utsArrayOf(
                                            "src"
                                        )),
                                        createElementVNode("view", utsMapOf("class" to "bubble"), utsArrayOf(
                                            createElementVNode("text", null, toDisplayString(msg.text), 1)
                                        ))
                                    ), 10, utsArrayOf(
                                        "id"
                                    ))
                                }
                                ), 128)
                            ))
                        }
                        ,
                        createElementVNode("view", utsMapOf("id" to bottomAnchor.value, "class" to "scroll-anchor"), null, 8, utsArrayOf(
                            "id"
                        ))
                    ), 8, utsArrayOf(
                        "scroll-into-view"
                    )),
                    createElementVNode("view", utsMapOf("class" to "input-area"), utsArrayOf(
                        createElementVNode("input", utsMapOf("class" to "input-box", "modelValue" to inputText.value, "onInput" to fun(`$event`: InputEvent){
                            inputText.value = `$event`.detail.value
                        }
                        , "placeholder" to "请输入内容...", "onConfirm" to sendMessage), null, 40, utsArrayOf(
                            "modelValue",
                            "onInput"
                        )),
                        createElementVNode("button", utsMapOf("class" to "send-button", "onClick" to sendMessage), "发送")
                    )),
                    if (isTrue(showDrawer.value)) {
                        createElementVNode("view", utsMapOf("key" to 0, "class" to "drawer-mask", "onClick" to closeDrawer))
                    } else {
                        createCommentVNode("v-if", true)
                    }
                    ,
                    if (isTrue(showDrawer.value)) {
                        createElementVNode("view", utsMapOf("key" to 1, "class" to "drawer"), utsArrayOf(
                            createElementVNode("view", utsMapOf("class" to "drawer-header"), utsArrayOf(
                                createElementVNode("text", null, "历史会话"),
                                createElementVNode("button", utsMapOf("class" to "drawer-close", "onClick" to closeDrawer), "关闭")
                            )),
                            createElementVNode("view", utsMapOf("class" to "drawer-body"), utsArrayOf(
                                if (isTrue(historyLoading.value)) {
                                    createElementVNode("view", utsMapOf("key" to 0, "class" to "drawer-loading"), "加载中...")
                                } else {
                                    if (chatHistory.value.length === 0) {
                                        createElementVNode("view", utsMapOf("key" to 1, "class" to "drawer-empty"), "暂无历史")
                                    } else {
                                        createElementVNode("view", utsMapOf("key" to 2), utsArrayOf(
                                            createElementVNode(Fragment, null, RenderHelpers.renderList(chatHistory.value, fun(item, __key, __index, _cached): Any {
                                                return createElementVNode("view", utsMapOf("key" to item.sessionId, "class" to "drawer-item", "onClick" to fun(){
                                                    loadSession(item.sessionId)
                                                }), utsArrayOf(
                                                    createElementVNode("view", utsMapOf("class" to "drawer-item-content"), utsArrayOf(
                                                        createElementVNode("text", utsMapOf("class" to "drawer-title"), toDisplayString(item.firstQuestion), 1),
                                                        createElementVNode("text", utsMapOf("class" to "drawer-date"), toDisplayString(formatDate(item.createdAt)), 1)
                                                    ))
                                                ), 8, utsArrayOf(
                                                    "onClick"
                                                ))
                                            }), 128)
                                        ))
                                    }
                                }
                            ))
                        ))
                    } else {
                        createCommentVNode("v-if", true)
                    }
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
                return utsMapOf("chat-wrapper" to padStyleMapOf(utsMapOf("display" to "flex", "flexDirection" to "column", "backgroundColor" to "#f5f7fa")), "chat-header" to padStyleMapOf(utsMapOf("backgroundImage" to "linear-gradient(to right,#4e54c8, #8f94fb)", "backgroundColor" to "rgba(0,0,0,0)", "color" to "#FFFFFF", "paddingTop" to "40rpx", "paddingRight" to "32rpx", "paddingBottom" to "24rpx", "paddingLeft" to "32rpx", "borderBottomLeftRadius" to "30rpx", "borderBottomRightRadius" to "30rpx", "textAlign" to "center")), "chat-title" to padStyleMapOf(utsMapOf("fontSize" to "36rpx", "fontWeight" to "bold")), "chat-subtitle" to padStyleMapOf(utsMapOf("fontSize" to "24rpx", "opacity" to 0.9, "marginTop" to "10rpx")), "chat-body" to padStyleMapOf(utsMapOf("flex" to 1, "paddingTop" to "30rpx", "paddingRight" to "24rpx", "paddingBottom" to "30rpx", "paddingLeft" to "24rpx")), "loading-container" to padStyleMapOf(utsMapOf("display" to "flex", "justifyContent" to "center", "alignItems" to "center", "height" to "200rpx")), "loading-text" to padStyleMapOf(utsMapOf("color" to "#888888", "fontSize" to "28rpx")), "chat-message" to utsMapOf("" to utsMapOf("display" to "flex", "alignItems" to "flex-start", "marginBottom" to "30rpx"), ".ai" to utsMapOf("flexDirection" to "row"), ".user" to utsMapOf("flexDirection" to "row-reverse")), "avatar" to padStyleMapOf(utsMapOf("width" to "64rpx", "height" to "64rpx", "marginTop" to 0, "marginRight" to "16rpx", "marginBottom" to 0, "marginLeft" to "16rpx")), "bubble" to utsMapOf("" to utsMapOf("backgroundColor" to "#ffffff", "borderTopLeftRadius" to "20rpx", "borderTopRightRadius" to "20rpx", "borderBottomRightRadius" to "20rpx", "borderBottomLeftRadius" to "20rpx", "paddingTop" to "20rpx", "paddingRight" to "20rpx", "paddingBottom" to "20rpx", "paddingLeft" to "20rpx", "fontSize" to "28rpx", "lineHeight" to 1.6, "boxShadow" to "0 4rpx 8rpx rgba(0, 0, 0, 0.05)", "color" to "#333333", "wordWrap" to "break-word"), ".chat-message.user " to utsMapOf("backgroundColor" to "#dcefff")), "input-area" to padStyleMapOf(utsMapOf("paddingTop" to "20rpx", "paddingRight" to "20rpx", "paddingBottom" to "20rpx", "paddingLeft" to "20rpx", "display" to "flex", "borderTopWidth" to "1rpx", "borderTopStyle" to "solid", "borderTopColor" to "#eeeeee", "backgroundColor" to "#ffffff")), "input-box" to padStyleMapOf(utsMapOf("flex" to 1, "borderTopWidth" to 1, "borderRightWidth" to 1, "borderBottomWidth" to 1, "borderLeftWidth" to 1, "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#cccccc", "borderRightColor" to "#cccccc", "borderBottomColor" to "#cccccc", "borderLeftColor" to "#cccccc", "borderTopLeftRadius" to "30rpx", "borderTopRightRadius" to "30rpx", "borderBottomRightRadius" to "30rpx", "borderBottomLeftRadius" to "30rpx", "paddingTop" to "16rpx", "paddingRight" to "24rpx", "paddingBottom" to "16rpx", "paddingLeft" to "24rpx", "fontSize" to "28rpx", "backgroundColor" to "#f9f9f9")), "send-button" to padStyleMapOf(utsMapOf("marginLeft" to "20rpx", "height" to "70rpx", "paddingTop" to 0, "paddingRight" to "32rpx", "paddingBottom" to 0, "paddingLeft" to "32rpx", "lineHeight" to "70rpx", "backgroundColor" to "#4e54c8", "color" to "#ffffff", "fontSize" to "28rpx", "borderTopLeftRadius" to "32rpx", "borderTopRightRadius" to "32rpx", "borderBottomRightRadius" to "32rpx", "borderBottomLeftRadius" to "32rpx")), "scroll-anchor" to padStyleMapOf(utsMapOf("height" to "1rpx")), "history-btn" to padStyleMapOf(utsMapOf("position" to "absolute", "right" to "32rpx", "top" to "40rpx", "backgroundImage" to "none", "backgroundColor" to "#ffffff", "color" to "#4e54c8", "borderTopLeftRadius" to "20rpx", "borderTopRightRadius" to "20rpx", "borderBottomRightRadius" to "20rpx", "borderBottomLeftRadius" to "20rpx", "paddingTop" to "8rpx", "paddingRight" to "24rpx", "paddingBottom" to "8rpx", "paddingLeft" to "24rpx", "fontSize" to "24rpx", "borderTopWidth" to "1rpx", "borderRightWidth" to "1rpx", "borderBottomWidth" to "1rpx", "borderLeftWidth" to "1rpx", "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#4e54c8", "borderRightColor" to "#4e54c8", "borderBottomColor" to "#4e54c8", "borderLeftColor" to "#4e54c8")), "drawer-mask" to padStyleMapOf(utsMapOf("position" to "fixed", "top" to 0, "left" to 0, "right" to 0, "bottom" to 0, "backgroundImage" to "none", "backgroundColor" to "rgba(0,0,0,0.2)", "zIndex" to 99)), "drawer" to padStyleMapOf(utsMapOf("position" to "fixed", "top" to 0, "right" to 0, "bottom" to 0, "maxWidth" to "500rpx", "backgroundImage" to "none", "backgroundColor" to "#ffffff", "zIndex" to 100, "boxShadow" to "-4rpx 0 16rpx rgba(0,0,0,0.08)", "display" to "flex", "flexDirection" to "column")), "drawer-header" to padStyleMapOf(utsMapOf("display" to "flex", "justifyContent" to "space-between", "alignItems" to "center", "paddingTop" to "32rpx", "paddingRight" to "24rpx", "paddingBottom" to "16rpx", "paddingLeft" to "24rpx", "borderBottomWidth" to "1rpx", "borderBottomStyle" to "solid", "borderBottomColor" to "#eeeeee", "fontSize" to "30rpx", "fontWeight" to "bold")), "drawer-close" to padStyleMapOf(utsMapOf("backgroundImage" to "none", "backgroundColor" to "#f5f7fa", "color" to "#888888", "borderTopLeftRadius" to "16rpx", "borderTopRightRadius" to "16rpx", "borderBottomRightRadius" to "16rpx", "borderBottomLeftRadius" to "16rpx", "paddingTop" to "4rpx", "paddingRight" to "16rpx", "paddingBottom" to "4rpx", "paddingLeft" to "16rpx", "fontSize" to "24rpx", "borderTopWidth" to "medium", "borderRightWidth" to "medium", "borderBottomWidth" to "medium", "borderLeftWidth" to "medium", "borderTopStyle" to "none", "borderRightStyle" to "none", "borderBottomStyle" to "none", "borderLeftStyle" to "none", "borderTopColor" to "#000000", "borderRightColor" to "#000000", "borderBottomColor" to "#000000", "borderLeftColor" to "#000000")), "drawer-body" to padStyleMapOf(utsMapOf("flex" to 1, "overflowY" to "auto", "paddingTop" to "24rpx", "paddingRight" to "24rpx", "paddingBottom" to "24rpx", "paddingLeft" to "24rpx", "textAlign" to "left")), "drawer-item" to padStyleMapOf(utsMapOf("paddingTop" to "18rpx", "paddingRight" to 0, "paddingBottom" to "18rpx", "paddingLeft" to 0, "borderBottomWidth" to "1rpx", "borderBottomStyle" to "solid", "borderBottomColor" to "#f0f0f0", "cursor" to "pointer", "display" to "flex", "alignItems" to "center", "justifyContent" to "space-between")), "drawer-item-content" to padStyleMapOf(utsMapOf("flex" to 1, "textAlign" to "left", "width" to "100%")), "drawer-item-arrow" to padStyleMapOf(utsMapOf("color" to "#cccccc", "fontSize" to "24rpx", "marginLeft" to "16rpx")), "drawer-title" to padStyleMapOf(utsMapOf("fontSize" to "28rpx", "color" to "#333333", "textAlign" to "left")), "drawer-date" to padStyleMapOf(utsMapOf("fontSize" to "22rpx", "color" to "#aaaaaa", "marginLeft" to "12rpx", "textAlign" to "left")), "drawer-loading" to padStyleMapOf(utsMapOf("color" to "#888888", "textAlign" to "center", "marginTop" to "40rpx", "fontSize" to "26rpx")), "drawer-empty" to padStyleMapOf(utsMapOf("color" to "#888888", "textAlign" to "center", "marginTop" to "40rpx", "fontSize" to "26rpx")), "debug-info" to padStyleMapOf(utsMapOf("paddingTop" to "10rpx", "paddingRight" to "20rpx", "paddingBottom" to "10rpx", "paddingLeft" to "20rpx", "backgroundColor" to "#f0f0f0", "borderTopLeftRadius" to "10rpx", "borderTopRightRadius" to "10rpx", "borderBottomRightRadius" to "10rpx", "borderBottomLeftRadius" to "10rpx", "marginBottom" to "20rpx", "fontSize" to "24rpx", "color" to "#555555")))
            }
        var inheritAttrs = true
        var inject: Map<String, Map<String, Any?>> = utsMapOf()
        var emits: Map<String, Any?> = utsMapOf()
        var props = normalizePropsOptions(utsMapOf())
        var propsNeedCastKeys: UTSArray<String> = utsArrayOf()
        var components: Map<String, CreateVueComponent> = utsMapOf()
    }
}
