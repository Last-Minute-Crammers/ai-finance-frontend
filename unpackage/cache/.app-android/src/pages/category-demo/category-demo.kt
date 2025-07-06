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
import io.dcloud.uniapp.extapi.navigateBack as uni_navigateBack
import io.dcloud.uniapp.extapi.showToast as uni_showToast
open class GenPagesCategoryDemoCategoryDemo : BasePage {
    constructor(__ins: ComponentInternalInstance, __renderer: String?) : super(__ins, __renderer) {}
    companion object {
        @Suppress("UNUSED_PARAMETER", "UNUSED_VARIABLE")
        var setup: (__props: GenPagesCategoryDemoCategoryDemo) -> Any? = fun(__props): Any? {
            val __ins = getCurrentInstance()!!
            val _ctx = __ins.proxy as GenPagesCategoryDemoCategoryDemo
            val _cache = __ins.renderCache
            val showAddCategoryModal = ref(false)
            val currentType = computed(fun(): String? {
                return getCurrentType()
            }
            )
            val currentIncomeExpense = computed(fun(): String? {
                return getCurrentIncomeExpense()
            }
            )
            val currentPath = computed(fun(): String? {
                return getPagePath()
            }
            )
            val hasValidType = computed(fun(): Boolean {
                return hasValidPageType()
            }
            )
            val goBack = fun(){
                uni_navigateBack(null)
            }
            val showAddCategory = fun(){
                if (!hasValidType.value) {
                    uni_showToast(ShowToastOptions(title = "请先进入记账页面选择类型", icon = "none"))
                    return
                }
                showAddCategoryModal.value = true
            }
            val onCategoryCreated = fun(newCategory: Any){
                console.log("新分类已创建:", newCategory, " at pages/category-demo/category-demo.uvue:80")
                uni_showToast(ShowToastOptions(title = "分类创建成功！", icon = "success"))
            }
            onMounted(fun(){
                console.log("当前页面状态:", object : UTSJSONObject() {
                    var type = currentType.value
                    var incomeExpense = currentIncomeExpense.value
                    var path = currentPath.value
                    var hasValidType = hasValidType.value
                }, " at pages/category-demo/category-demo.uvue:88")
            }
            )
            return fun(): Any? {
                return createElementVNode("view", utsMapOf("class" to "demo-container"), utsArrayOf(
                    createElementVNode("view", utsMapOf("class" to "header"), utsArrayOf(
                        createElementVNode("text", utsMapOf("class" to "back-btn", "onClick" to goBack), "←"),
                        createElementVNode("text", utsMapOf("class" to "header-title"), "分类管理演示")
                    )),
                    createElementVNode("view", utsMapOf("class" to "content"), utsArrayOf(
                        createElementVNode("view", utsMapOf("class" to "info-card"), utsArrayOf(
                            createElementVNode("text", utsMapOf("class" to "info-title"), "当前页面状态"),
                            createElementVNode("view", utsMapOf("class" to "info-item"), utsArrayOf(
                                createElementVNode("text", utsMapOf("class" to "info-label"), "页面类型："),
                                createElementVNode("text", utsMapOf("class" to "info-value"), toDisplayString(currentType.value || "未设置"), 1)
                            )),
                            createElementVNode("view", utsMapOf("class" to "info-item"), utsArrayOf(
                                createElementVNode("text", utsMapOf("class" to "info-label"), "income_expense："),
                                createElementVNode("text", utsMapOf("class" to "info-value"), toDisplayString(currentIncomeExpense.value || "未设置"), 1)
                            )),
                            createElementVNode("view", utsMapOf("class" to "info-item"), utsArrayOf(
                                createElementVNode("text", utsMapOf("class" to "info-label"), "页面路径："),
                                createElementVNode("text", utsMapOf("class" to "info-value"), toDisplayString(currentPath.value || "未设置"), 1)
                            ))
                        )),
                        createElementVNode("view", utsMapOf("class" to "action-card"), utsArrayOf(
                            createElementVNode("text", utsMapOf("class" to "action-title"), "操作演示"),
                            createElementVNode("button", utsMapOf("class" to "action-btn", "onClick" to showAddCategory, "disabled" to !hasValidType.value), toDisplayString(if (hasValidType.value) {
                                "添加分类"
                            } else {
                                "请先进入记账页面选择类型"
                            }
                            ), 9, utsArrayOf(
                                "disabled"
                            )),
                            createElementVNode("text", utsMapOf("class" to "action-tip"), "点击按钮可以添加分类，会自动使用当前页面的类型")
                        )),
                        createElementVNode("view", utsMapOf("class" to "tip-card"), utsArrayOf(
                            createElementVNode("text", utsMapOf("class" to "tip-title"), "使用说明"),
                            createElementVNode("text", utsMapOf("class" to "tip-text"), "1. 先进入记账页面，选择\"支出\"或\"收入\"类型"),
                            createElementVNode("text", utsMapOf("class" to "tip-text"), "2. 然后进入此页面，就可以添加对应类型的分类"),
                            createElementVNode("text", utsMapOf("class" to "tip-text"), "3. 分类会自动同步到本地存储")
                        ))
                    )),
                    createVNode(unref(GenComponentsAddCategoryModalClass), utsMapOf("visible" to showAddCategoryModal.value, "onUpdate:visible" to fun(`$event`){
                        showAddCategoryModal.value = `$event`
                    }
                    , "onCreated" to onCategoryCreated), null, 8, utsArrayOf(
                        "visible",
                        "onUpdate:visible"
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
                return utsMapOf("demo-container" to padStyleMapOf(utsMapOf("backgroundColor" to "#f5f7fa", "paddingBottom" to "40rpx")), "header" to padStyleMapOf(utsMapOf("backgroundImage" to "linear-gradient(to right, #4e54c8, #8f94fb)", "backgroundColor" to "rgba(0,0,0,0)", "paddingTop" to "80rpx", "paddingRight" to "30rpx", "paddingBottom" to "40rpx", "paddingLeft" to "30rpx", "borderBottomLeftRadius" to "30rpx", "borderBottomRightRadius" to "30rpx", "textAlign" to "center", "position" to "relative")), "back-btn" to padStyleMapOf(utsMapOf("position" to "absolute", "left" to "30rpx", "top" to "80rpx", "fontSize" to "36rpx", "color" to "#FFFFFF")), "header-title" to padStyleMapOf(utsMapOf("fontSize" to "36rpx", "fontWeight" to "bold", "color" to "#FFFFFF")), "content" to padStyleMapOf(utsMapOf("paddingTop" to "30rpx", "paddingRight" to "30rpx", "paddingBottom" to "30rpx", "paddingLeft" to "30rpx")), "info-card" to padStyleMapOf(utsMapOf("backgroundImage" to "none", "backgroundColor" to "#FFFFFF", "borderTopLeftRadius" to "20rpx", "borderTopRightRadius" to "20rpx", "borderBottomRightRadius" to "20rpx", "borderBottomLeftRadius" to "20rpx", "paddingTop" to "30rpx", "paddingRight" to "30rpx", "paddingBottom" to "30rpx", "paddingLeft" to "30rpx", "marginBottom" to "30rpx", "boxShadow" to "0 4rpx 12rpx rgba(0, 0, 0, 0.04)")), "action-card" to padStyleMapOf(utsMapOf("backgroundImage" to "none", "backgroundColor" to "#FFFFFF", "borderTopLeftRadius" to "20rpx", "borderTopRightRadius" to "20rpx", "borderBottomRightRadius" to "20rpx", "borderBottomLeftRadius" to "20rpx", "paddingTop" to "30rpx", "paddingRight" to "30rpx", "paddingBottom" to "30rpx", "paddingLeft" to "30rpx", "marginBottom" to "30rpx", "boxShadow" to "0 4rpx 12rpx rgba(0, 0, 0, 0.04)")), "tip-card" to padStyleMapOf(utsMapOf("backgroundImage" to "none", "backgroundColor" to "#FFFFFF", "borderTopLeftRadius" to "20rpx", "borderTopRightRadius" to "20rpx", "borderBottomRightRadius" to "20rpx", "borderBottomLeftRadius" to "20rpx", "paddingTop" to "30rpx", "paddingRight" to "30rpx", "paddingBottom" to "30rpx", "paddingLeft" to "30rpx", "marginBottom" to "30rpx", "boxShadow" to "0 4rpx 12rpx rgba(0, 0, 0, 0.04)")), "info-title" to padStyleMapOf(utsMapOf("fontSize" to "32rpx", "fontWeight" to "bold", "color" to "#333333", "marginBottom" to "20rpx")), "action-title" to padStyleMapOf(utsMapOf("fontSize" to "32rpx", "fontWeight" to "bold", "color" to "#333333", "marginBottom" to "20rpx")), "tip-title" to padStyleMapOf(utsMapOf("fontSize" to "32rpx", "fontWeight" to "bold", "color" to "#333333", "marginBottom" to "20rpx")), "info-item" to padStyleMapOf(utsMapOf("display" to "flex", "justifyContent" to "space-between", "alignItems" to "center", "paddingTop" to "15rpx", "paddingRight" to 0, "paddingBottom" to "15rpx", "paddingLeft" to 0, "borderBottomWidth" to "1rpx", "borderBottomStyle" to "solid", "borderBottomColor" to "#f0f0f0", "borderBottomWidth:last-child" to "medium", "borderBottomStyle:last-child" to "none", "borderBottomColor:last-child" to "#000000")), "info-label" to padStyleMapOf(utsMapOf("fontSize" to "28rpx", "color" to "#666666")), "info-value" to padStyleMapOf(utsMapOf("fontSize" to "28rpx", "color" to "#4e54c8", "fontWeight" to "bold")), "action-btn" to padStyleMapOf(utsMapOf("width" to "100%", "backgroundImage" to "linear-gradient(to right, #4e54c8, #8f94fb)", "backgroundColor" to "rgba(0,0,0,0)", "color" to "#FFFFFF", "paddingTop" to "24rpx", "paddingRight" to "24rpx", "paddingBottom" to "24rpx", "paddingLeft" to "24rpx", "fontSize" to "32rpx", "borderTopLeftRadius" to "40rpx", "borderTopRightRadius" to "40rpx", "borderBottomRightRadius" to "40rpx", "borderBottomLeftRadius" to "40rpx", "borderTopWidth" to "medium", "borderRightWidth" to "medium", "borderBottomWidth" to "medium", "borderLeftWidth" to "medium", "borderTopStyle" to "none", "borderRightStyle" to "none", "borderBottomStyle" to "none", "borderLeftStyle" to "none", "borderTopColor" to "#000000", "borderRightColor" to "#000000", "borderBottomColor" to "#000000", "borderLeftColor" to "#000000", "marginBottom" to "20rpx", "backgroundImage:disabled" to "none", "backgroundColor:disabled" to "#cccccc", "color:disabled" to "#999999")), "action-tip" to padStyleMapOf(utsMapOf("fontSize" to "24rpx", "color" to "#999999", "textAlign" to "center")), "tip-text" to padStyleMapOf(utsMapOf("fontSize" to "26rpx", "color" to "#666666", "lineHeight" to 1.6, "marginBottom" to "10rpx")))
            }
        var inheritAttrs = true
        var inject: Map<String, Map<String, Any?>> = utsMapOf()
        var emits: Map<String, Any?> = utsMapOf()
        var props = normalizePropsOptions(utsMapOf())
        var propsNeedCastKeys: UTSArray<String> = utsArrayOf()
        var components: Map<String, CreateVueComponent> = utsMapOf()
    }
}
