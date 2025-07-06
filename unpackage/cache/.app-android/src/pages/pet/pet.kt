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
open class GenPagesPetPet : BasePage {
    constructor(__ins: ComponentInternalInstance, __renderer: String?) : super(__ins, __renderer) {}
    companion object {
        @Suppress("UNUSED_PARAMETER", "UNUSED_VARIABLE")
        var setup: (__props: GenPagesPetPet) -> Any? = fun(__props): Any? {
            val __ins = getCurrentInstance()!!
            val _ctx = __ins.proxy as GenPagesPetPet
            val _cache = __ins.renderCache
            val showAllBadges = ref(false)
            val petMood = ref("很开心")
            val statusClass = ref("status-green")
            val allBadges = utsArrayOf<UTSJSONObject>(object : UTSJSONObject() {
                var name = "目标达人"
                var icon = "🎯"
            }, object : UTSJSONObject() {
                var name = "省钱小能手"
                var icon = "💰"
            }, object : UTSJSONObject() {
                var name = "储蓄先锋"
                var icon = "📈"
            }, object : UTSJSONObject() {
                var name = "社交达人"
                var icon = "🗣️"
            }, object : UTSJSONObject() {
                var name = "理财王者"
                var icon = "👑"
            }, object : UTSJSONObject() {
                var name = "早起打卡"
                var icon = "⏰"
            }, object : UTSJSONObject() {
                var name = "账单清零"
                var icon = "🧾"
            })
            val displayedBadges = computed(fun(): UTSArray<UTSJSONObject> {
                return if (showAllBadges.value) {
                    allBadges
                } else {
                    allBadges.slice(0, 3)
                }
            }
            )
            val toggleShowAll = fun(){
                showAllBadges.value = !showAllBadges.value
            }
            val goToChat = fun(){
                uni_navigateTo(NavigateToOptions(url = "/pages/AIchat/AIchat"))
            }
            val updatePetMood = fun(): UTSPromise<Unit> {
                return wrapUTSPromise(suspend {
                        try {
                            val response = await(getTotalStatistic())
                            console.log("宠物页面 - 总统计响应:", response, " at pages/pet/pet.uvue:77")
                            if (response?.Data) {
                                val _response_Data = response.Data
                                val Income = _response_Data.Income
                                val Expense = _response_Data.Expense
                                console.log("宠物页面 - 收入数据:", Income, " at pages/pet/pet.uvue:81")
                                console.log("宠物页面 - 支出数据:", Expense, " at pages/pet/pet.uvue:82")
                                val balance = (Income?.Amount || 0) - (Expense?.Amount || 0)
                                console.log("宠物页面 - 计算余额:", balance, " at pages/pet/pet.uvue:85")
                                if (balance <= 0) {
                                    petMood.value = "很担心"
                                    statusClass.value = "status-red"
                                } else if (balance <= 1000) {
                                    petMood.value = "很欣慰"
                                    statusClass.value = "status-yellow"
                                } else {
                                    petMood.value = "很开心"
                                    statusClass.value = "status-green"
                                }
                                console.log("宠物页面 - 最终心情:", petMood.value, " at pages/pet/pet.uvue:98")
                            }
                        }
                         catch (error: Throwable) {
                            console.error("获取收支数据失败:", error, " at pages/pet/pet.uvue:101")
                            petMood.value = "很开心"
                            statusClass.value = "status-green"
                        }
                })
            }
            onMounted(fun(){
                updatePetMood()
            }
            )
            return fun(): Any? {
                return createElementVNode("view", utsMapOf("class" to "container"), utsArrayOf(
                    createElementVNode("view", utsMapOf("class" to "header"), utsArrayOf(
                        createElementVNode("text", utsMapOf("class" to "back-btn"), "←"),
                        createElementVNode("text", utsMapOf("class" to "title"), "AI理财宠物"),
                        createElementVNode("text", utsMapOf("class" to "subtitle"), "您的智能理财小伙伴")
                    )),
                    createElementVNode("view", utsMapOf("class" to "card"), utsArrayOf(
                        createElementVNode("view", utsMapOf("class" to "avatar-wrapper"), utsArrayOf(
                            createElementVNode("image", utsMapOf("src" to "/static/icons/pet.png", "class" to "avatar", "mode" to "aspectFit"))
                        )),
                        createElementVNode("text", utsMapOf("class" to "pet-name"), "理财小汪"),
                        createElementVNode("text", utsMapOf("class" to "pet-status"), utsArrayOf(
                            "状态：",
                            createElementVNode("text", utsMapOf("class" to normalizeClass(statusClass.value)), toDisplayString(petMood.value), 3)
                        ))
                    )),
                    createElementVNode("view", utsMapOf("class" to "action-column"), utsArrayOf(
                        createElementVNode("view", utsMapOf("class" to "action-item", "onClick" to goToChat), utsArrayOf(
                            createElementVNode("text", utsMapOf("class" to "icon"), "💬"),
                            createElementVNode("text", utsMapOf("class" to "label"), "聊天")
                        ))
                    )),
                    createElementVNode("view", utsMapOf("class" to "badge-section"), utsArrayOf(
                        createElementVNode("view", utsMapOf("class" to "badge-header"), utsArrayOf(
                            createElementVNode("text", utsMapOf("class" to "badge-title"), "我的勋章"),
                            createElementVNode("text", utsMapOf("class" to "badge-link", "onClick" to toggleShowAll), toDisplayString(if (showAllBadges.value) {
                                "收起"
                            } else {
                                "查看全部"
                            }
                            ), 1)
                        )),
                        createElementVNode("view", utsMapOf("class" to "badge-row"), utsArrayOf(
                            createElementVNode(Fragment, null, RenderHelpers.renderList(displayedBadges.value, fun(badge, __key, __index, _cached): Any {
                                return createElementVNode("view", utsMapOf("class" to "badge-item", "key" to badge["name"]), utsArrayOf(
                                    createElementVNode("text", utsMapOf("class" to "badge-icon"), toDisplayString(badge["icon"]), 1),
                                    createElementVNode("text", utsMapOf("class" to "badge-name"), toDisplayString(badge["name"]), 1)
                                ))
                            }
                            ), 128)
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
                return utsMapOf("container" to padStyleMapOf(utsMapOf("backgroundColor" to "#f5f7fa", "paddingBottom" to "10%")), "header" to padStyleMapOf(utsMapOf("backgroundImage" to "linear-gradient(to right, #4e54c8, #8f94fb)", "backgroundColor" to "rgba(0,0,0,0)", "paddingTop" to "80rpx", "paddingRight" to "30rpx", "paddingBottom" to "40rpx", "paddingLeft" to "30rpx", "borderBottomLeftRadius" to "30rpx", "borderBottomRightRadius" to "30rpx", "position" to "relative", "alignItems" to "center")), "back-btn" to padStyleMapOf(utsMapOf("position" to "absolute", "left" to "30rpx", "top" to "80rpx", "fontSize" to "36rpx", "color" to "#FFFFFF")), "title" to padStyleMapOf(utsMapOf("fontSize" to "36rpx", "fontWeight" to "bold", "color" to "#FFFFFF")), "subtitle" to padStyleMapOf(utsMapOf("fontSize" to "24rpx", "color" to "#f0f0f0", "marginTop" to "10rpx")), "card" to padStyleMapOf(utsMapOf("backgroundImage" to "none", "backgroundColor" to "#ffffff", "marginTop" to "30rpx", "marginRight" to "30rpx", "marginBottom" to "30rpx", "marginLeft" to "30rpx", "paddingTop" to "30rpx", "paddingRight" to "30rpx", "paddingBottom" to "30rpx", "paddingLeft" to "30rpx", "borderTopLeftRadius" to "20rpx", "borderTopRightRadius" to "20rpx", "borderBottomRightRadius" to "20rpx", "borderBottomLeftRadius" to "20rpx", "boxShadow" to "0 6rpx 16rpx rgba(0, 0, 0, 0.06)", "textAlign" to "center")), "avatar-wrapper" to padStyleMapOf(utsMapOf("display" to "flex", "justifyContent" to "center", "position" to "relative", "alignItems" to "center", "width" to "100%")), "avatar" to padStyleMapOf(utsMapOf("width" to "140rpx", "height" to "140rpx")), "level" to padStyleMapOf(utsMapOf("position" to "absolute", "right" to "100rpx", "bottom" to 0, "width" to "36rpx", "height" to "36rpx", "backgroundImage" to "none", "backgroundColor" to "#FFA500", "color" to "#ffffff", "fontSize" to "22rpx", "fontWeight" to "bold", "textAlign" to "center", "lineHeight" to "36rpx")), "pet-name" to padStyleMapOf(utsMapOf("fontSize" to "30rpx", "fontWeight" to "bold", "marginTop" to "12rpx")), "pet-status" to padStyleMapOf(utsMapOf("fontSize" to "24rpx", "marginTop" to "6rpx")), "status-green" to padStyleMapOf(utsMapOf("color" to "#35b765")), "status-yellow" to padStyleMapOf(utsMapOf("color" to "#f39c12")), "status-red" to padStyleMapOf(utsMapOf("color" to "#e74c3c")), "action-column" to padStyleMapOf(utsMapOf("display" to "flex", "alignItems" to "center", "marginTop" to "40rpx", "marginRight" to 0, "marginBottom" to "40rpx", "marginLeft" to 0)), "action-item" to padStyleMapOf(utsMapOf("width" to "90%", "backgroundImage" to "none", "backgroundColor" to "#ffffff", "borderTopLeftRadius" to "16rpx", "borderTopRightRadius" to "16rpx", "borderBottomRightRadius" to "16rpx", "borderBottomLeftRadius" to "16rpx", "paddingTop" to "24rpx", "paddingRight" to 0, "paddingBottom" to "24rpx", "paddingLeft" to 0, "boxShadow" to "0 2rpx 8rpx rgba(0, 0, 0, 0.06)", "display" to "flex", "flexDirection" to "column", "alignItems" to "center", "justifyContent" to "center")), "icon" to padStyleMapOf(utsMapOf("fontSize" to "40rpx")), "label" to padStyleMapOf(utsMapOf("fontSize" to "24rpx", "marginTop" to "8rpx")), "badge-section" to padStyleMapOf(utsMapOf("marginTop" to "40rpx", "marginRight" to "30rpx", "marginBottom" to "40rpx", "marginLeft" to "30rpx")), "badge-header" to padStyleMapOf(utsMapOf("display" to "flex", "justifyContent" to "space-between", "alignItems" to "center", "marginBottom" to "20rpx")), "badge-title" to padStyleMapOf(utsMapOf("fontSize" to "28rpx", "fontWeight" to "bold", "color" to "#333333")), "badge-link" to padStyleMapOf(utsMapOf("fontSize" to "24rpx", "color" to "#4e54c8")), "badge-row" to padStyleMapOf(utsMapOf("display" to "flex", "flexDirection" to "row", "flexWrap" to "wrap", "justifyContent" to "space-between", "gap" to "20rpx 10rpx")), "badge-item" to padStyleMapOf(utsMapOf("width" to "30%", "backgroundImage" to "none", "backgroundColor" to "#FFFFFF", "borderTopLeftRadius" to "16rpx", "borderTopRightRadius" to "16rpx", "borderBottomRightRadius" to "16rpx", "borderBottomLeftRadius" to "16rpx", "paddingTop" to "20rpx", "paddingRight" to 0, "paddingBottom" to "20rpx", "paddingLeft" to 0, "boxShadow" to "0 2rpx 6rpx rgba(0, 0, 0, 0.05)", "display" to "flex", "flexDirection" to "column", "alignItems" to "center", "justifyContent" to "center")), "badge-icon" to padStyleMapOf(utsMapOf("fontSize" to "36rpx")), "badge-name" to padStyleMapOf(utsMapOf("fontSize" to "22rpx", "marginTop" to "6rpx")))
            }
        var inheritAttrs = true
        var inject: Map<String, Map<String, Any?>> = utsMapOf()
        var emits: Map<String, Any?> = utsMapOf()
        var props = normalizePropsOptions(utsMapOf())
        var propsNeedCastKeys: UTSArray<String> = utsArrayOf()
        var components: Map<String, CreateVueComponent> = utsMapOf()
    }
}
