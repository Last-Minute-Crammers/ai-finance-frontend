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
import io.dcloud.uniapp.extapi.showToast as uni_showToast
open class GenComponentsAddCategoryModal : VueComponent, Props {
    constructor(__ins: ComponentInternalInstance) : super(__ins) {}
    override var visible: Boolean by `$props`
    companion object {
        @Suppress("UNUSED_PARAMETER", "UNUSED_VARIABLE")
        var setup: (__props: GenComponentsAddCategoryModal) -> Any? = fun(__props): Any? {
            val __ins = getCurrentInstance()!!
            val _ctx = __ins.proxy as GenComponentsAddCategoryModal
            val _cache = __ins.renderCache
            val props = __props
            fun emit(event: String, vararg do_not_transform_spread: Any?) {
                __ins.emit(event, *do_not_transform_spread)
            }
            val categoryName = ref("")
            val selectedIconId = ref(1)
            val loading = ref(false)
            val currentPageType = ref<String?>(null)
            val availableIcons = getAllIcons()
            val currentTypeDisplay = computed(fun(): String {
                return currentPageType.value ?: "未设置"
            }
            )
            val canSubmit = computed(fun(): Boolean {
                val hasName = categoryName.value.trim().length > 0
                val hasType = hasValidPageType()
                console.log("按钮状态检查:", UTSJSONObject(Map<String, Any?>(utsArrayOf(
                    utsArrayOf(
                        "hasName",
                        hasName
                    ),
                    utsArrayOf(
                        "hasType",
                        hasType
                    ),
                    utsArrayOf(
                        "categoryName",
                        categoryName.value
                    ),
                    utsArrayOf(
                        "currentType",
                        currentPageType.value
                    )
                ))), " at components/AddCategoryModal.uvue:90")
                return hasName && hasType
            }
            )
            val updateCurrentType = fun(){
                currentPageType.value = getCurrentType()
                console.log("AddCategoryModal - 当前页面类型已更新:", currentPageType.value, " at components/AddCategoryModal.uvue:102")
            }
            val selectIcon = fun(icon: IconDefinition){
                selectedIconId.value = icon.id
                console.log("选择图标:", icon.name, "ID:", icon.id, " at components/AddCategoryModal.uvue:107")
            }
            val resetForm = fun(){
                categoryName.value = ""
                selectedIconId.value = 1
                loading.value = false
            }
            val closeModal = fun(){
                emit("update:visible", false)
                resetForm()
            }
            val createCategory = fun(): UTSPromise<Unit> {
                return wrapUTSPromise(suspend w@{
                        if (!canSubmit.value) {
                            uni_showToast(ShowToastOptions(title = "请填写分类名称", icon = "none"))
                            return@w
                        }
                        try {
                            loading.value = true
                            val response = await(createCategoryWithCurrentType(categoryName.value.trim(), selectedIconId.value, "#4e54c8"))
                            if (response && UTSAndroid.`typeof`(response) === "object" && resolveInOperator(response, "code") && response.code === 200) {
                                if (response.message === "success (local only)") {
                                    uni_showToast(ShowToastOptions(title = "分类已保存到本地", icon = "success", duration = 2000))
                                } else {
                                    uni_showToast(ShowToastOptions(title = "分类创建成功！", icon = "success", duration = 2000))
                                }
                                emit("created", response.data)
                                closeModal()
                            } else {
                                throw UTSError(response?.message || "创建失败")
                            }
                        }
                         catch (error: Throwable) {
                            console.error("创建分类失败:", error, " at components/AddCategoryModal.uvue:162")
                            val errorMessage = if (error is UTSError) {
                                (error as UTSError).message
                            } else {
                                String(error)
                            }
                            if (errorMessage.includes("网络") || errorMessage.includes("连接")) {
                                uni_showToast(ShowToastOptions(title = "网络连接失败，请检查网络", icon = "none", duration = 3000))
                            } else if (errorMessage.includes("认证") || errorMessage.includes("token")) {
                                uni_showToast(ShowToastOptions(title = "登录已过期，请重新登录", icon = "none", duration = 3000))
                            } else {
                                uni_showToast(ShowToastOptions(title = "\u521B\u5EFA\u5931\u8D25: " + errorMessage, icon = "none", duration = 3000))
                            }
                        }
                         finally{
                            loading.value = false
                        }
                })
            }
            watch(fun(){
                return props.visible
            }
            , fun(newVal){
                if (newVal) {
                    updateCurrentType()
                    if (!hasValidPageType()) {
                        uni_showToast(ShowToastOptions(title = "请先在记账页面选择类型", icon = "none"))
                        closeModal()
                    }
                }
            }
            )
            var typeCheckInterval: Number? = null
            watch(fun(){
                return props.visible
            }
            , fun(newVal){
                if (newVal) {
                    typeCheckInterval = setInterval(fun(){
                        val newType = getCurrentType()
                        if (newType !== currentPageType.value) {
                            updateCurrentType()
                        }
                    }, 500)
                } else {
                    if (typeCheckInterval) {
                        clearInterval(typeCheckInterval)
                        typeCheckInterval = null
                    }
                }
            }
            )
            onUnmounted(fun(){
                if (typeCheckInterval) {
                    clearInterval(typeCheckInterval)
                    typeCheckInterval = null
                }
            }
            )
            return fun(): Any? {
                return if (isTrue(_ctx.visible)) {
                    createElementVNode("view", utsMapOf("key" to 0, "class" to "modal-overlay", "onClick" to closeModal), utsArrayOf(
                        createElementVNode("view", utsMapOf("class" to "modal-content", "onClick" to withModifiers(fun(){}, utsArrayOf(
                            "stop"
                        ))), utsArrayOf(
                            createElementVNode("view", utsMapOf("class" to "modal-header"), utsArrayOf(
                                createElementVNode("text", utsMapOf("class" to "modal-title"), "添加分类"),
                                createElementVNode("text", utsMapOf("class" to "close-btn", "onClick" to closeModal), "×")
                            )),
                            createElementVNode("view", utsMapOf("class" to "modal-body"), utsArrayOf(
                                createElementVNode("view", utsMapOf("class" to "input-group"), utsArrayOf(
                                    createElementVNode("text", utsMapOf("class" to "input-label"), "分类名称"),
                                    createElementVNode("input", utsMapOf("class" to "input-field", "modelValue" to categoryName.value, "onInput" to fun(`$event`: InputEvent){
                                        categoryName.value = `$event`.detail.value
                                    }, "placeholder" to "请输入分类名称", "maxlength" to "20"), null, 40, utsArrayOf(
                                        "modelValue",
                                        "onInput"
                                    ))
                                )),
                                createElementVNode("view", utsMapOf("class" to "input-group"), utsArrayOf(
                                    createElementVNode("text", utsMapOf("class" to "input-label"), "分类图标"),
                                    createElementVNode("view", utsMapOf("class" to "icon-grid"), utsArrayOf(
                                        createElementVNode(Fragment, null, RenderHelpers.renderList(unref(availableIcons), fun(icon, __key, __index, _cached): Any {
                                            return createElementVNode("view", utsMapOf("key" to icon.id, "class" to normalizeClass(utsArrayOf(
                                                "icon-item",
                                                utsMapOf("selected" to (selectedIconId.value === icon.id))
                                            )), "onClick" to fun(){
                                                selectIcon(icon)
                                            }), utsArrayOf(
                                                createElementVNode("text", utsMapOf("class" to "icon-text"), toDisplayString(icon.emoji), 1),
                                                createElementVNode("text", utsMapOf("class" to "icon-name"), toDisplayString(icon.name), 1)
                                            ), 10, utsArrayOf(
                                                "onClick"
                                            ))
                                        }), 128)
                                    ))
                                )),
                                createElementVNode("view", utsMapOf("class" to "type-info"), utsArrayOf(
                                    createElementVNode("text", utsMapOf("class" to "type-label"), "当前类型："),
                                    createElementVNode("text", utsMapOf("class" to "type-value"), toDisplayString(currentTypeDisplay.value), 1)
                                ))
                            )),
                            createElementVNode("view", utsMapOf("class" to "modal-footer"), utsArrayOf(
                                createElementVNode("button", utsMapOf("class" to "btn-confirm", "onClick" to createCategory, "disabled" to !canSubmit.value), toDisplayString(if (loading.value) {
                                    "创建中..."
                                } else {
                                    "创建分类"
                                }), 9, utsArrayOf(
                                    "disabled"
                                ))
                            ))
                        ), 8, utsArrayOf(
                            "onClick"
                        ))
                    ))
                } else {
                    createCommentVNode("v-if", true)
                }
            }
        }
        val styles: Map<String, Map<String, Map<String, Any>>> by lazy {
            normalizeCssStyles(utsArrayOf(
                styles0
            ))
        }
        val styles0: Map<String, Map<String, Map<String, Any>>>
            get() {
                return utsMapOf("modal-overlay" to padStyleMapOf(utsMapOf("position" to "fixed", "top" to 0, "left" to 0, "right" to 0, "bottom" to 0, "backgroundImage" to "none", "backgroundColor" to "rgba(0,0,0,0.5)", "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "zIndex" to 1000)), "modal-content" to padStyleMapOf(utsMapOf("backgroundImage" to "none", "backgroundColor" to "#FFFFFF", "borderTopLeftRadius" to "20rpx", "borderTopRightRadius" to "20rpx", "borderBottomRightRadius" to "20rpx", "borderBottomLeftRadius" to "20rpx", "width" to "80%", "maxWidth" to "600rpx", "overflow" to "hidden", "position" to "relative", "display" to "flex", "flexDirection" to "column")), "modal-header" to padStyleMapOf(utsMapOf("display" to "flex", "justifyContent" to "space-between", "alignItems" to "center", "paddingTop" to "30rpx", "paddingRight" to "30rpx", "paddingBottom" to "30rpx", "paddingLeft" to "30rpx", "borderBottomWidth" to "2rpx", "borderBottomStyle" to "solid", "borderBottomColor" to "#f0f0f0", "flexShrink" to 0)), "modal-title" to padStyleMapOf(utsMapOf("fontSize" to "32rpx", "fontWeight" to "bold", "color" to "#333333")), "close-btn" to padStyleMapOf(utsMapOf("fontSize" to "40rpx", "color" to "#999999", "paddingTop" to "10rpx", "paddingRight" to "10rpx", "paddingBottom" to "10rpx", "paddingLeft" to "10rpx")), "modal-body" to padStyleMapOf(utsMapOf("paddingTop" to "30rpx", "paddingRight" to "30rpx", "paddingBottom" to "30rpx", "paddingLeft" to "30rpx", "overflowY" to "auto", "flex" to 1, "scrollbarWidth" to "thin", "scrollbarColor" to "#c1c1c1 transparent", "width::-webkit-scrollbar" to "6rpx", "backgroundImage::-webkit-scrollbar-track" to "none", "backgroundColor::-webkit-scrollbar-track" to "rgba(0,0,0,0)", "backgroundImage::-webkit-scrollbar-thumb" to "none", "backgroundColor::-webkit-scrollbar-thumb" to "#c1c1c1", "borderTopLeftRadius::-webkit-scrollbar-thumb" to "3rpx", "borderTopRightRadius::-webkit-scrollbar-thumb" to "3rpx", "borderBottomRightRadius::-webkit-scrollbar-thumb" to "3rpx", "borderBottomLeftRadius::-webkit-scrollbar-thumb" to "3rpx", "backgroundImage::-webkit-scrollbar-thumb:hover" to "none", "backgroundColor::-webkit-scrollbar-thumb:hover" to "#a8a8a8")), "input-group" to padStyleMapOf(utsMapOf("marginBottom" to "30rpx")), "input-label" to padStyleMapOf(utsMapOf("fontSize" to "28rpx", "color" to "#333333", "marginBottom" to "15rpx")), "input-field" to padStyleMapOf(utsMapOf("width" to "100%", "paddingTop" to "20rpx", "paddingRight" to "20rpx", "paddingBottom" to "20rpx", "paddingLeft" to "20rpx", "borderTopWidth" to "2rpx", "borderRightWidth" to "2rpx", "borderBottomWidth" to "2rpx", "borderLeftWidth" to "2rpx", "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#e0e0e0", "borderRightColor" to "#e0e0e0", "borderBottomColor" to "#e0e0e0", "borderLeftColor" to "#e0e0e0", "borderTopLeftRadius" to "10rpx", "borderTopRightRadius" to "10rpx", "borderBottomRightRadius" to "10rpx", "borderBottomLeftRadius" to "10rpx", "fontSize" to "28rpx", "backgroundImage" to "none", "backgroundColor" to "#f9f9f9")), "icon-grid" to padStyleMapOf(utsMapOf("gridTemplateColumns" to "repeat(4, 1fr)", "gap" to "20rpx", "marginTop" to "15rpx")), "icon-item" to utsMapOf("" to utsMapOf("width" to "120rpx", "height" to "80rpx", "borderTopWidth" to "2rpx", "borderRightWidth" to "2rpx", "borderBottomWidth" to "2rpx", "borderLeftWidth" to "2rpx", "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#e0e0e0", "borderRightColor" to "#e0e0e0", "borderBottomColor" to "#e0e0e0", "borderLeftColor" to "#e0e0e0", "borderTopLeftRadius" to "10rpx", "borderTopRightRadius" to "10rpx", "borderBottomRightRadius" to "10rpx", "borderBottomLeftRadius" to "10rpx", "display" to "flex", "flexDirection" to "column", "alignItems" to "center", "justifyContent" to "center", "backgroundImage" to "none", "backgroundColor" to "#f9f9f9", "paddingTop" to "10rpx", "paddingRight" to "10rpx", "paddingBottom" to "10rpx", "paddingLeft" to "10rpx"), ".selected" to utsMapOf("borderTopColor" to "#4e54c8", "borderRightColor" to "#4e54c8", "borderBottomColor" to "#4e54c8", "borderLeftColor" to "#4e54c8", "backgroundImage" to "none", "backgroundColor" to "#e0e3ff")), "icon-text" to padStyleMapOf(utsMapOf("fontSize" to "32rpx", "marginBottom" to "4rpx")), "icon-name" to padStyleMapOf(utsMapOf("fontSize" to "20rpx", "color" to "#666666", "textAlign" to "center", "lineHeight" to 1.2)), "type-info" to padStyleMapOf(utsMapOf("display" to "flex", "alignItems" to "center", "paddingTop" to "20rpx", "paddingRight" to "20rpx", "paddingBottom" to "20rpx", "paddingLeft" to "20rpx", "backgroundImage" to "none", "backgroundColor" to "#f0f2ff", "borderTopLeftRadius" to "10rpx", "borderTopRightRadius" to "10rpx", "borderBottomRightRadius" to "10rpx", "borderBottomLeftRadius" to "10rpx", "marginTop" to "20rpx")), "type-label" to padStyleMapOf(utsMapOf("fontSize" to "26rpx", "color" to "#666666")), "type-value" to padStyleMapOf(utsMapOf("fontSize" to "26rpx", "color" to "#4e54c8", "fontWeight" to "bold", "marginLeft" to "10rpx")), "modal-footer" to padStyleMapOf(utsMapOf("display" to "flex", "paddingTop" to "20rpx", "paddingRight" to "30rpx", "paddingBottom" to "20rpx", "paddingLeft" to "30rpx", "borderTopWidth" to "2rpx", "borderTopStyle" to "solid", "borderTopColor" to "#f0f0f0", "gap" to "15rpx", "backgroundImage" to "none", "backgroundColor" to "#fafafa", "flexShrink" to 0)), "btn-confirm" to padStyleMapOf(utsMapOf("width" to "160rpx", "paddingTop" to "15rpx", "paddingRight" to "18rpx", "paddingBottom" to "15rpx", "paddingLeft" to "18rpx", "borderTopLeftRadius" to "8rpx", "borderTopRightRadius" to "8rpx", "borderBottomRightRadius" to "8rpx", "borderBottomLeftRadius" to "8rpx", "fontSize" to "26rpx", "fontWeight" to "bold", "borderTopWidth" to "medium", "borderRightWidth" to "medium", "borderBottomWidth" to "medium", "borderLeftWidth" to "medium", "borderTopStyle" to "none", "borderRightStyle" to "none", "borderBottomStyle" to "none", "borderLeftStyle" to "none", "borderTopColor" to "#000000", "borderRightColor" to "#000000", "borderBottomColor" to "#000000", "borderLeftColor" to "#000000", "minHeight" to "50rpx", "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "backgroundImage" to "linear-gradient(to right, #4e54c8, #8f94fb)", "backgroundColor" to "rgba(0,0,0,0)", "color" to "#FFFFFF", "boxShadow" to "0 4rpx 12rpx rgba(78, 84, 200, 0.3)", "letterSpacing" to "2rpx", "marginTop" to 0, "marginRight" to "auto", "marginBottom" to 0, "marginLeft" to "auto", "transform:active" to "translateY(2rpx)", "boxShadow:active" to "0 2rpx 8rpx rgba(78, 84, 200, 0.3)", "backgroundImage:disabled" to "none", "backgroundColor:disabled" to "#cccccc", "color:disabled" to "#999999", "boxShadow:disabled" to "none", "transform:disabled" to "none")))
            }
        var inheritAttrs = true
        var inject: Map<String, Map<String, Any?>> = utsMapOf()
        var emits: Map<String, Any?> = utsMapOf("update:visible" to null, "created" to null)
        var props = normalizePropsOptions(utsMapOf("visible" to utsMapOf("type" to "Boolean", "required" to true)))
        var propsNeedCastKeys = utsArrayOf(
            "visible"
        )
        var components: Map<String, CreateVueComponent> = utsMapOf()
    }
}
