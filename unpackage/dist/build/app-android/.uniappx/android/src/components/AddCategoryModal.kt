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
open class GenComponentsAddCategoryModal : VueComponent, Props {
    constructor(__ins: ComponentInternalInstance) : super(__ins) {}
    override var visible: Boolean by `$props`
    companion object {
        @Suppress("UNUSED_PARAMETER", "UNUSED_VARIABLE")
        var setup: (__props: GenComponentsAddCategoryModal) -> Any? = fun(__props): Any? {
            val __ins = getCurrentInstance()!!
            val _ctx = __ins.proxy as GenComponentsAddCategoryModal
            val _cache = __ins.renderCache
            val props: Props = __props
            fun emit(event: String, vararg do_not_transform_spread: Any?) {
                __ins.emit(event, *do_not_transform_spread)
            }
            val categoryName = ref<String>("")
            val selectedIconId = ref<Number>(1)
            val loading = ref<Boolean>(false)
            val currentPageType = ref<String?>(null)
            val availableIcons: UTSArray<IconDefinition> = getAllIcons()
            val currentTypeDisplay = computed<String>(fun(): String {
                return currentPageType.value ?: "未设置"
            }
            )
            val canSubmit = computed<Boolean>(fun(): Boolean {
                val hasName: Boolean = categoryName.value.trim().length > 0
                val hasType: Boolean = hasValidPageType()
                return hasName && hasType
            }
            )
            fun gen_updateCurrentType_fn(): Unit {
                currentPageType.value = getCurrentType()
            }
            val updateCurrentType = ::gen_updateCurrentType_fn
            fun gen_selectIcon_fn(icon: IconDefinition): Unit {
                selectedIconId.value = icon.id
            }
            val selectIcon = ::gen_selectIcon_fn
            fun gen_resetForm_fn(): Unit {
                categoryName.value = ""
                selectedIconId.value = 1
                loading.value = false
            }
            val resetForm = ::gen_resetForm_fn
            fun gen_closeModal_fn(): Unit {
                emit("update:visible", false)
                resetForm()
            }
            val closeModal = ::gen_closeModal_fn
            fun gen_createCategory_fn(): UTSPromise<Unit> {
                return wrapUTSPromise(suspend w@{
                        if (!canSubmit.value) {
                            uni_showToast(ShowToastOptions(title = "请填写分类名称", icon = "none"))
                            return@w
                        }
                        try {
                            loading.value = true
                            val response: ApiResponse<Category>? = await(createCategoryWithCurrentType(categoryName.value.trim(), selectedIconId.value, "#4e54c8"))
                            if (response && response.code === 200) {
                                emit("created", response.data as Category)
                                closeModal()
                            } else {
                                val errorMsg: String = response?.message ?: "创建失败"
                                throw UTSError(errorMsg)
                            }
                        }
                         catch (error: Throwable) {
                            val errorMessage: String = if (error is UTSError) {
                                (error as UTSError).message
                            } else {
                                if (UTSAndroid.`typeof`(error) === "string") {
                                    error as String
                                } else {
                                    JSON.stringify(error)
                                }
                            }
                            uni_showToast(ShowToastOptions(title = "\u521B\u5EFA\u5931\u8D25: " + errorMessage, icon = "none", duration = 3000))
                        }
                         finally{
                            loading.value = false
                        }
                })
            }
            val createCategory = ::gen_createCategory_fn
            watch(fun(){
                return props.visible
            }
            , fun(newVal: Boolean): Unit {
                if (newVal) {
                    updateCurrentType()
                    if (!hasValidPageType()) {
                        uni_showToast(ShowToastOptions(title = "请先在记账页面选择类型", icon = "none"))
                    }
                }
            }
            )
            onUnmounted(fun(){
                resetForm()
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
                return utsMapOf("modal-overlay" to padStyleMapOf(utsMapOf("position" to "fixed", "top" to 0, "left" to 0, "right" to 0, "bottom" to 0, "backgroundColor" to "rgba(0,0,0,0.5)", "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "zIndex" to 1000)), "modal-content" to padStyleMapOf(utsMapOf("backgroundColor" to "#FFFFFF", "borderTopLeftRadius" to "20rpx", "borderTopRightRadius" to "20rpx", "borderBottomRightRadius" to "20rpx", "borderBottomLeftRadius" to "20rpx", "width" to "80%", "maxWidth" to "600rpx", "overflow" to "hidden")), "modal-header" to padStyleMapOf(utsMapOf("display" to "flex", "justifyContent" to "space-between", "alignItems" to "center", "paddingTop" to "30rpx", "paddingRight" to "30rpx", "paddingBottom" to "30rpx", "paddingLeft" to "30rpx", "borderBottomWidth" to "1rpx", "borderBottomStyle" to "solid", "borderBottomColor" to "#eeeeee")), "modal-title" to padStyleMapOf(utsMapOf("fontSize" to "32rpx", "fontWeight" to "bold", "color" to "#333333")), "close-btn" to padStyleMapOf(utsMapOf("fontSize" to "40rpx", "color" to "#999999", "paddingTop" to "10rpx", "paddingRight" to "10rpx", "paddingBottom" to "10rpx", "paddingLeft" to "10rpx")), "modal-body" to padStyleMapOf(utsMapOf("paddingTop" to "30rpx", "paddingRight" to "30rpx", "paddingBottom" to "30rpx", "paddingLeft" to "30rpx", "overflowY" to "auto")), "input-group" to padStyleMapOf(utsMapOf("marginBottom" to "30rpx")), "input-label" to padStyleMapOf(utsMapOf("fontSize" to "28rpx", "color" to "#333333", "marginBottom" to "15rpx")), "input-field" to padStyleMapOf(utsMapOf("width" to "100%", "height" to "80rpx", "borderTopWidth" to "2rpx", "borderRightWidth" to "2rpx", "borderBottomWidth" to "2rpx", "borderLeftWidth" to "2rpx", "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#dddddd", "borderRightColor" to "#dddddd", "borderBottomColor" to "#dddddd", "borderLeftColor" to "#dddddd", "borderTopLeftRadius" to "10rpx", "borderTopRightRadius" to "10rpx", "borderBottomRightRadius" to "10rpx", "borderBottomLeftRadius" to "10rpx", "paddingTop" to 0, "paddingRight" to "20rpx", "paddingBottom" to 0, "paddingLeft" to "20rpx", "fontSize" to "28rpx", "backgroundColor" to "#f9f9f9")), "icon-grid" to padStyleMapOf(utsMapOf("gridTemplateColumns" to "repeat(4, 1fr)", "gap" to "20rpx", "marginTop" to "15rpx")), "icon-item" to utsMapOf("" to utsMapOf("display" to "flex", "flexDirection" to "column", "alignItems" to "center", "paddingTop" to "20rpx", "paddingRight" to "20rpx", "paddingBottom" to "20rpx", "paddingLeft" to "20rpx", "borderTopWidth" to "2rpx", "borderRightWidth" to "2rpx", "borderBottomWidth" to "2rpx", "borderLeftWidth" to "2rpx", "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#eeeeee", "borderRightColor" to "#eeeeee", "borderBottomColor" to "#eeeeee", "borderLeftColor" to "#eeeeee", "borderTopLeftRadius" to "10rpx", "borderTopRightRadius" to "10rpx", "borderBottomRightRadius" to "10rpx", "borderBottomLeftRadius" to "10rpx", "backgroundColor" to "#f9f9f9", "transitionDuration" to "0.3s", "transitionTimingFunction" to "ease"), ".selected" to utsMapOf("borderTopColor" to "#4e54c8", "borderRightColor" to "#4e54c8", "borderBottomColor" to "#4e54c8", "borderLeftColor" to "#4e54c8", "backgroundColor" to "#e8eaff")), "icon-text" to padStyleMapOf(utsMapOf("fontSize" to "40rpx", "marginBottom" to "10rpx")), "icon-name" to padStyleMapOf(utsMapOf("fontSize" to "24rpx", "color" to "#666666", "textAlign" to "center")), "type-info" to padStyleMapOf(utsMapOf("display" to "flex", "alignItems" to "center", "paddingTop" to "20rpx", "paddingRight" to "20rpx", "paddingBottom" to "20rpx", "paddingLeft" to "20rpx", "backgroundColor" to "#f0f8ff", "borderTopLeftRadius" to "10rpx", "borderTopRightRadius" to "10rpx", "borderBottomRightRadius" to "10rpx", "borderBottomLeftRadius" to "10rpx", "marginTop" to "20rpx")), "type-label" to padStyleMapOf(utsMapOf("fontSize" to "28rpx", "color" to "#666666", "marginRight" to "10rpx")), "type-value" to padStyleMapOf(utsMapOf("fontSize" to "28rpx", "color" to "#4e54c8")), "modal-footer" to padStyleMapOf(utsMapOf("paddingTop" to "30rpx", "paddingRight" to "30rpx", "paddingBottom" to "30rpx", "paddingLeft" to "30rpx", "borderTopWidth" to "1rpx", "borderTopStyle" to "solid", "borderTopColor" to "#eeeeee")), "btn-confirm" to padStyleMapOf(utsMapOf("width" to "100%", "height" to "80rpx", "backgroundImage" to "linear-gradient(to right, #4e54c8, #8f94fb)", "backgroundColor" to "rgba(0,0,0,0)", "color" to "#FFFFFF", "borderTopWidth" to "medium", "borderRightWidth" to "medium", "borderBottomWidth" to "medium", "borderLeftWidth" to "medium", "borderTopStyle" to "none", "borderRightStyle" to "none", "borderBottomStyle" to "none", "borderLeftStyle" to "none", "borderTopColor" to "#000000", "borderRightColor" to "#000000", "borderBottomColor" to "#000000", "borderLeftColor" to "#000000", "borderTopLeftRadius" to "10rpx", "borderTopRightRadius" to "10rpx", "borderBottomRightRadius" to "10rpx", "borderBottomLeftRadius" to "10rpx", "fontSize" to "28rpx", "backgroundImage:disabled" to "none", "backgroundColor:disabled" to "#cccccc", "color:disabled" to "#999999")), "@TRANSITION" to utsMapOf("icon-item" to utsMapOf("duration" to "0.3s", "timingFunction" to "ease")))
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
