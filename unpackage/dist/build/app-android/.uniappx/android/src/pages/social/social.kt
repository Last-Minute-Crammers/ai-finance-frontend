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
open class GenPagesSocialSocial : BasePage {
    constructor(__ins: ComponentInternalInstance, __renderer: String?) : super(__ins, __renderer) {}
    companion object {
        @Suppress("UNUSED_PARAMETER", "UNUSED_VARIABLE")
        var setup: (__props: GenPagesSocialSocial) -> Any? = fun(__props): Any? {
            val __ins = getCurrentInstance()!!
            val _ctx = __ins.proxy as GenPagesSocialSocial
            val _cache = __ins.renderCache
            val searchQuery = ref("")
            val rankingData = utsArrayOf<UTSJSONObject>(object : UTSJSONObject() {
                var name = "张小萌"
                var progress: Number = 98
            }, object : UTSJSONObject() {
                var name = "李小明"
                var progress: Number = 85
            }, object : UTSJSONObject() {
                var name = "王小红"
                var progress: Number = 78
            }, object : UTSJSONObject() {
                var name = "你"
                var progress: Number = 65
            })
            val friendsData = utsArrayOf<UTSJSONObject>(object : UTSJSONObject() {
                var name = "赵小刚"
                var spendReduction: Number = 15
                var linkText = "PK"
            }, object : UTSJSONObject() {
                var name = "钱小丽"
                var spendReduction: Number = 45
                var linkText = "查看"
            })
            val goToAddFriend = fun(){
                uni_navigateTo(NavigateToOptions(url = "/pages/friend/friend"))
            }
            return fun(): Any? {
                return createElementVNode("view", utsMapOf("class" to "container"), utsArrayOf(
                    createElementVNode("view", utsMapOf("class" to "header"), utsArrayOf(
                        createElementVNode("text", utsMapOf("class" to "back"), "←"),
                        createElementVNode("text", utsMapOf("class" to "title"), "社交互动"),
                        createElementVNode("text", utsMapOf("class" to "subtitle"), "与好友一起理财更有趣"),
                        createElementVNode("view", utsMapOf("class" to "search-bar"), utsArrayOf(
                            createElementVNode("button", utsMapOf("class" to "search-friend-btn", "onClick" to goToAddFriend), "搜索好友")
                        ))
                    )),
                    createElementVNode("view", utsMapOf("class" to "ranking-card"), utsArrayOf(
                        createElementVNode("text", utsMapOf("class" to "ranking-title"), "本月理财排名"),
                        createElementVNode(Fragment, null, RenderHelpers.renderList(rankingData, fun(item, index, __index, _cached): Any {
                            return createElementVNode("view", utsMapOf("key" to index, "class" to "ranking-item"), utsArrayOf(
                                createElementVNode("text", utsMapOf("class" to "rank-number"), toDisplayString(index + 1) + ".", 1),
                                createElementVNode("text", utsMapOf("class" to "rank-name"), toDisplayString(item["name"]), 1),
                                createElementVNode("view", utsMapOf("class" to "progress-bar"), utsArrayOf(
                                    createElementVNode("view", utsMapOf("class" to "progress", "style" to normalizeStyle(utsMapOf("width" to (item["progress"] + "%")))), null, 4)
                                )),
                                createElementVNode("text", utsMapOf("class" to "rank-progress"), toDisplayString(item["progress"]) + "% 完成", 1)
                            ))
                        }
                        ), 64),
                        createElementVNode("text", utsMapOf("class" to "view-all"), "查看全部")
                    )),
                    createElementVNode("view", utsMapOf("class" to "friends-card"), utsArrayOf(
                        createElementVNode("text", utsMapOf("class" to "friends-title"), "我的好友"),
                        createElementVNode(Fragment, null, RenderHelpers.renderList(friendsData, fun(friend, index, __index, _cached): Any {
                            return createElementVNode("view", utsMapOf("key" to index, "class" to "friend-item"), utsArrayOf(
                                createElementVNode("text", utsMapOf("class" to "friend-name"), toDisplayString(friend["name"]), 1),
                                createElementVNode("text", utsMapOf("class" to "friend-status"), "本周消费减少 " + toDisplayString(friend["spendReduction"]) + "%", 1),
                                createElementVNode("text", utsMapOf("class" to "friend-link"), toDisplayString(friend["linkText"]), 1)
                            ))
                        }
                        ), 64)
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
                return utsMapOf("container" to padStyleMapOf(utsMapOf("backgroundColor" to "#f5f7fa", "paddingBottom" to "10%")), "header" to padStyleMapOf(utsMapOf("backgroundImage" to "linear-gradient(to right, #4e54c8, #8f94fb)", "backgroundColor" to "rgba(0,0,0,0)", "paddingTop" to "80rpx", "paddingRight" to "30rpx", "paddingBottom" to "40rpx", "paddingLeft" to "30rpx", "borderBottomLeftRadius" to "30rpx", "borderBottomRightRadius" to "30rpx", "position" to "relative", "textAlign" to "center")), "back" to padStyleMapOf(utsMapOf("position" to "absolute", "left" to "30rpx", "top" to "80rpx", "fontSize" to "36rpx", "color" to "#FFFFFF")), "title" to padStyleMapOf(utsMapOf("width" to "100%", "textAlign" to "center", "fontSize" to "36rpx", "fontWeight" to "bold", "color" to "#FFFFFF")), "subtitle" to padStyleMapOf(utsMapOf("width" to "100%", "textAlign" to "center", "fontSize" to "24rpx", "color" to "#f0f0f0", "marginTop" to "10rpx")), "search-bar" to padStyleMapOf(utsMapOf("marginTop" to "40rpx", "paddingTop" to "6rpx", "paddingRight" to "6rpx", "paddingBottom" to "6rpx", "paddingLeft" to "6rpx", "backgroundImage" to "none", "backgroundColor" to "#FFFFFF", "borderTopLeftRadius" to "30rpx", "borderTopRightRadius" to "30rpx", "borderBottomRightRadius" to "30rpx", "borderBottomLeftRadius" to "30rpx", "boxShadow" to "0 4rpx 8rpx rgba(0, 0, 0, 0.1)", "display" to "flex", "justifyContent" to "center")), "search-friend-btn" to padStyleMapOf(utsMapOf("width" to "85%", "paddingTop" to "15rpx", "paddingRight" to "20rpx", "paddingBottom" to "15rpx", "paddingLeft" to "20rpx", "fontSize" to "28rpx", "borderTopWidth" to "medium", "borderRightWidth" to "medium", "borderBottomWidth" to "medium", "borderLeftWidth" to "medium", "borderTopStyle" to "none", "borderRightStyle" to "none", "borderBottomStyle" to "none", "borderLeftStyle" to "none", "borderTopColor" to "#000000", "borderRightColor" to "#000000", "borderBottomColor" to "#000000", "borderLeftColor" to "#000000", "backgroundImage" to "none", "backgroundColor" to "rgba(255,255,255,0.9)", "borderTopLeftRadius" to "30rpx", "borderTopRightRadius" to "30rpx", "borderBottomRightRadius" to "30rpx", "borderBottomLeftRadius" to "30rpx", "color" to "#4e54c8", "boxShadow" to "0 4rpx 8rpx rgba(0, 0, 0, 0.1)", "marginTop" to 0, "marginRight" to "auto", "marginBottom" to 0, "marginLeft" to "auto")), "ranking-card" to padStyleMapOf(utsMapOf("backgroundImage" to "none", "backgroundColor" to "#ffffff", "marginTop" to "30rpx", "marginRight" to "30rpx", "marginBottom" to "30rpx", "marginLeft" to "30rpx", "borderTopLeftRadius" to "20rpx", "borderTopRightRadius" to "20rpx", "borderBottomRightRadius" to "20rpx", "borderBottomLeftRadius" to "20rpx", "paddingTop" to "30rpx", "paddingRight" to "30rpx", "paddingBottom" to "30rpx", "paddingLeft" to "30rpx", "boxShadow" to "0 6rpx 12rpx rgba(0, 0, 0, 0.06)")), "ranking-title" to padStyleMapOf(utsMapOf("fontSize" to "28rpx", "fontWeight" to "bold", "marginBottom" to "20rpx")), "ranking-item" to padStyleMapOf(utsMapOf("display" to "flex", "alignItems" to "center", "marginBottom" to "20rpx")), "rank-number" to padStyleMapOf(utsMapOf("fontSize" to "26rpx", "fontWeight" to "bold", "width" to "30rpx")), "rank-name" to padStyleMapOf(utsMapOf("fontSize" to "26rpx", "marginRight" to "12rpx", "width" to "100rpx")), "progress-bar" to padStyleMapOf(utsMapOf("width" to "100%", "height" to "10rpx", "backgroundImage" to "none", "backgroundColor" to "#e0e0e0", "borderTopLeftRadius" to "5rpx", "borderTopRightRadius" to "5rpx", "borderBottomRightRadius" to "5rpx", "borderBottomLeftRadius" to "5rpx", "marginTop" to "10rpx", "marginRight" to 0, "marginBottom" to "10rpx", "marginLeft" to 0, "position" to "relative")), "progress" to padStyleMapOf(utsMapOf("height" to "100%", "backgroundImage" to "none", "backgroundColor" to "#4caf50", "borderTopLeftRadius" to "5rpx", "borderTopRightRadius" to "5rpx", "borderBottomRightRadius" to "5rpx", "borderBottomLeftRadius" to "5rpx")), "rank-progress" to padStyleMapOf(utsMapOf("fontSize" to "22rpx", "color" to "#666666")), "friends-card" to padStyleMapOf(utsMapOf("backgroundImage" to "none", "backgroundColor" to "#ffffff", "marginTop" to "30rpx", "marginRight" to "30rpx", "marginBottom" to "30rpx", "marginLeft" to "30rpx", "borderTopLeftRadius" to "20rpx", "borderTopRightRadius" to "20rpx", "borderBottomRightRadius" to "20rpx", "borderBottomLeftRadius" to "20rpx", "paddingTop" to "30rpx", "paddingRight" to "30rpx", "paddingBottom" to "30rpx", "paddingLeft" to "30rpx", "boxShadow" to "0 6rpx 12rpx rgba(0, 0, 0, 0.06)")), "friends-title" to padStyleMapOf(utsMapOf("fontSize" to "28rpx", "fontWeight" to "bold", "marginBottom" to "20rpx")), "friend-item" to padStyleMapOf(utsMapOf("display" to "flex", "justifyContent" to "space-between", "marginBottom" to "20rpx")), "friend-name" to padStyleMapOf(utsMapOf("fontSize" to "26rpx", "fontWeight" to "bold")), "friend-status" to padStyleMapOf(utsMapOf("fontSize" to "22rpx", "color" to "#666666")), "friend-link" to padStyleMapOf(utsMapOf("fontSize" to "24rpx", "color" to "#4e54c8", "cursor" to "pointer")))
            }
        var inheritAttrs = true
        var inject: Map<String, Map<String, Any?>> = utsMapOf()
        var emits: Map<String, Any?> = utsMapOf()
        var props = normalizePropsOptions(utsMapOf())
        var propsNeedCastKeys: UTSArray<String> = utsArrayOf()
        var components: Map<String, CreateVueComponent> = utsMapOf()
    }
}
