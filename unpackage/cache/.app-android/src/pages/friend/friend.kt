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
import io.dcloud.uniapp.extapi.getStorageSync as uni_getStorageSync
import io.dcloud.uniapp.extapi.navigateBack as uni_navigateBack
import io.dcloud.uniapp.extapi.showToast as uni_showToast
open class GenPagesFriendFriend : BasePage {
    constructor(__ins: ComponentInternalInstance, __renderer: String?) : super(__ins, __renderer) {}
    companion object {
        @Suppress("UNUSED_PARAMETER", "UNUSED_VARIABLE")
        var setup: (__props: GenPagesFriendFriend) -> Any? = fun(__props): Any? {
            val __ins = getCurrentInstance()!!
            val _ctx = __ins.proxy as GenPagesFriendFriend
            val _cache = __ins.renderCache
            val searchEmail = ref("")
            val searchResult = ref<Any>(null)
            val pendingRequests = ref(utsArrayOf<Any>())
            val myFriends = ref(utsArrayOf<Any>())
            val loading = ref(false)
            val currentUser = ref<Any>(null)
            onMounted(fun(){
                getCurrentUserInfo()
                loadFriendInvitations()
                loadFriendList()
            }
            )
            fun gen_getCurrentUserInfo_fn() {
                val userStr = uni_getStorageSync("current_user")
                if (userStr) {
                    try {
                        currentUser.value = UTSAndroid.consoleDebugError(JSON.parse(userStr), " at pages/friend/friend.uvue:112")
                    } catch (e: Throwable) {
                        currentUser.value = null
                    }
                } else {
                    currentUser.value = null
                }
            }
            val getCurrentUserInfo = ::gen_getCurrentUserInfo_fn
            fun gen_searchUser_fn(): UTSPromise<Unit> {
                return wrapUTSPromise(suspend w@{
                        if (!searchEmail.value.trim()) {
                            uni_showToast(ShowToastOptions(title = "请输入邮箱地址", icon = "none"))
                            return@w
                        }
                        if (currentUser.value && searchEmail.value === currentUser.value.email) {
                            uni_showToast(ShowToastOptions(title = "不能添加自己为好友", icon = "none"))
                            searchResult.value = null
                            return@w
                        }
                        loading.value = true
                        try {
                            val response = await(searchUserByEmailDirect(searchEmail.value))
                            if (response.code === 200 && response.data) {
                                val user = (response as {
                                    var code: Number
                                    var message: String
                                    var data: UTSJSONObject
                                }).data
                                val isFriend = myFriends.value.some(fun(friend): Boolean {
                                    return friend.id === user["id"]
                                })
                                val hasPendingInvitation = pendingRequests.value.some(fun(req): Boolean {
                                    return req.inviter.id === user["id"] && req.status === "pending"
                                })
                                searchResult.value = object : UTSJSONObject() {
                                    var id = user["id"]
                                    var email = user["email"]
                                    var username = user["username"]
                                    var added = isFriend
                                    var pending = hasPendingInvitation
                                }
                            } else {
                                searchResult.value = null
                                uni_showToast(ShowToastOptions(title = response.message || "未找到该用户", icon = "none"))
                            }
                        }
                         catch (error: Throwable) {
                            searchResult.value = null
                            uni_showToast(ShowToastOptions(title = "搜索失败，请稍后重试", icon = "none"))
                        }
                         finally{
                            loading.value = false
                        }
                })
            }
            val searchUser = ::gen_searchUser_fn
            fun gen_searchUserByEmailDirect_fn(email: String): UTSPromise<Any> {
                return wrapUTSPromise(suspend w@{
                        val mockUsers = utsArrayOf<UTSJSONObject>(object : UTSJSONObject() {
                            var id: Number = 1
                            var username = "yjj"
                            var email = "3213495082@qq.com"
                        }, object : UTSJSONObject() {
                            var id: Number = 2
                            var username = "yjj"
                            var email = "yjj@qq.com"
                        }, object : UTSJSONObject() {
                            var id: Number = 3
                            var username = "testuser1"
                            var email = "testuser1@example.com"
                        }, object : UTSJSONObject() {
                            var id: Number = 4
                            var username = "111"
                            var email = "111@qq.com"
                        })
                        val user = mockUsers.find(fun(u): Boolean {
                            return u["email"] === email
                        }
                        )
                        if (user) {
                            return@w object : UTSJSONObject() {
                                var code: Number = 200
                                var message = "搜索成功"
                                var data = user
                            }
                        } else {
                            return@w object : UTSJSONObject() {
                                var code: Number = 404
                                var message = "未找到该用户"
                                var data = null
                            }
                        }
                })
            }
            val searchUserByEmailDirect = ::gen_searchUserByEmailDirect_fn
            fun gen_addFriend_fn(user: Any): UTSPromise<Unit> {
                return wrapUTSPromise(suspend w@{
                        if (!currentUser.value) {
                            uni_showToast(ShowToastOptions(title = "请先登录", icon = "none"))
                            return@w
                        }
                        loading.value = true
                        try {
                            val response = await(sendFriendInvitation(object : UTSJSONObject() {
                                var invitee = user.id
                            }))
                            searchResult.value.pending = true
                            uni_showToast(ShowToastOptions(title = "添加好友成功", icon = "success"))
                            loadFriendInvitations()
                        }
                         catch (error: Throwable) {
                            searchResult.value.pending = true
                            uni_showToast(ShowToastOptions(title = "添加好友成功", icon = "success"))
                            loadFriendInvitations()
                        }
                         finally{
                            loading.value = false
                        }
                })
            }
            val addFriend = ::gen_addFriend_fn
            fun gen_loadFriendInvitations_fn(): UTSPromise<Unit> {
                return wrapUTSPromise(suspend {
                        try {
                            val response = await(getFriendInvitations())
                            if (response.code === 200 && response.data) {
                                pendingRequests.value = response.data.filter(fun(invitation: Any): Boolean {
                                    return invitation.status === "pending" && invitation.invitee.id === currentUser.value?.id
                                }
                                )
                            }
                        }
                         catch (error: Throwable) {
                            pendingRequests.value = utsArrayOf()
                        }
                })
            }
            val loadFriendInvitations = ::gen_loadFriendInvitations_fn
            fun gen_loadFriendList_fn(): UTSPromise<Unit> {
                return wrapUTSPromise(suspend {
                        try {
                            val response = await(getFriendList())
                            if (response.code === 200 && response.data) {
                                myFriends.value = response.data.list || utsArrayOf()
                            }
                        }
                         catch (error: Throwable) {
                            myFriends.value = utsArrayOf()
                        }
                })
            }
            val loadFriendList = ::gen_loadFriendList_fn
            fun gen_acceptRequest_fn(invitation: Any): UTSPromise<Unit> {
                return wrapUTSPromise(suspend {
                        loading.value = true
                        try {
                            val response = await(acceptFriendInvitation(invitation.id))
                            if (response.code === 200) {
                                uni_showToast(ShowToastOptions(title = "已添加为好友", icon = "success"))
                                loadFriendInvitations()
                                loadFriendList()
                            } else {
                                uni_showToast(ShowToastOptions(title = response.message || "操作失败", icon = "none"))
                            }
                        }
                         catch (error: Throwable) {
                            uni_showToast(ShowToastOptions(title = "操作失败，请稍后重试", icon = "none"))
                        }
                         finally{
                            loading.value = false
                        }
                })
            }
            val acceptRequest = ::gen_acceptRequest_fn
            fun gen_rejectRequest_fn(invitation: Any): UTSPromise<Unit> {
                return wrapUTSPromise(suspend {
                        loading.value = true
                        try {
                            val response = await(refuseFriendInvitation(invitation.id))
                            if (response.code === 200) {
                                uni_showToast(ShowToastOptions(title = "已拒绝", icon = "success"))
                                loadFriendInvitations()
                            } else {
                                uni_showToast(ShowToastOptions(title = response.message || "操作失败", icon = "none"))
                            }
                        }
                         catch (error: Throwable) {
                            uni_showToast(ShowToastOptions(title = "操作失败，请稍后重试", icon = "none"))
                        }
                         finally{
                            loading.value = false
                        }
                })
            }
            val rejectRequest = ::gen_rejectRequest_fn
            fun gen_onSearchInput_fn() {
                searchResult.value = null
            }
            val onSearchInput = ::gen_onSearchInput_fn
            fun gen_goBack_fn() {
                uni_navigateBack(null)
            }
            val goBack = ::gen_goBack_fn
            return fun(): Any? {
                return createElementVNode("view", utsMapOf("class" to "container"), utsArrayOf(
                    createElementVNode("view", utsMapOf("class" to "header"), utsArrayOf(
                        createElementVNode("text", utsMapOf("class" to "back", "onClick" to goBack), "←"),
                        createElementVNode("text", utsMapOf("class" to "title"), "添加好友")
                    )),
                    createElementVNode("view", utsMapOf("class" to "search-section"), utsArrayOf(
                        createElementVNode("input", utsMapOf("modelValue" to searchEmail.value, "onInput" to utsArrayOf(
                            fun(`$event`: InputEvent){
                                searchEmail.value = `$event`.detail.value
                            }
                            ,
                            onSearchInput
                        ), "placeholder" to "输入Email搜索用户", "class" to "search-input"), null, 40, utsArrayOf(
                            "modelValue",
                            "onInput"
                        )),
                        createElementVNode("button", utsMapOf("class" to "search-btn", "onClick" to searchUser), "搜索")
                    )),
                    if (isTrue(searchResult.value)) {
                        createElementVNode("view", utsMapOf("key" to 0, "class" to "user-result"), utsArrayOf(
                            createElementVNode("view", utsMapOf("class" to "user-info"), utsArrayOf(
                                createElementVNode("text", utsMapOf("class" to "user-email"), toDisplayString(searchResult.value.email), 1),
                                createElementVNode("text", utsMapOf("class" to "user-username"), toDisplayString(searchResult.value.username), 1)
                            )),
                            if (isTrue(!searchResult.value.added && !searchResult.value.pending)) {
                                createElementVNode("button", utsMapOf("key" to 0, "class" to "add-btn", "onClick" to fun(){
                                    addFriend(searchResult.value)
                                }, "disabled" to loading.value), toDisplayString(if (loading.value) {
                                    "发送中..."
                                } else {
                                    "添加"
                                }), 9, utsArrayOf(
                                    "onClick",
                                    "disabled"
                                ))
                            } else {
                                if (isTrue(searchResult.value.pending)) {
                                    createElementVNode("button", utsMapOf("key" to 1, "class" to "pending-btn", "disabled" to ""), " 等待验证 ")
                                } else {
                                    createElementVNode("button", utsMapOf("key" to 2, "class" to "added-btn", "disabled" to ""), " 已添加 ")
                                }
                            }
                        ))
                    } else {
                        createCommentVNode("v-if", true)
                    }
                    ,
                    if (pendingRequests.value.length > 0) {
                        createElementVNode("view", utsMapOf("key" to 1, "class" to "pending-section"), utsArrayOf(
                            createElementVNode("text", utsMapOf("class" to "pending-title"), "好友请求"),
                            createElementVNode(Fragment, null, RenderHelpers.renderList(pendingRequests.value, fun(req, idx, __index, _cached): Any {
                                return createElementVNode("view", utsMapOf("key" to idx, "class" to "pending-item"), utsArrayOf(
                                    createElementVNode("view", utsMapOf("class" to "pending-info"), utsArrayOf(
                                        createElementVNode("text", utsMapOf("class" to "pending-username"), toDisplayString(req.inviter.username), 1),
                                        createElementVNode("text", utsMapOf("class" to "pending-email"), toDisplayString(req.inviter.email), 1)
                                    )),
                                    createElementVNode("view", utsMapOf("class" to "pending-buttons"), utsArrayOf(
                                        createElementVNode("button", utsMapOf("class" to "accept-btn", "onClick" to fun(){
                                            acceptRequest(req)
                                        }, "disabled" to loading.value), "同意", 8, utsArrayOf(
                                            "onClick",
                                            "disabled"
                                        )),
                                        createElementVNode("button", utsMapOf("class" to "reject-btn", "onClick" to fun(){
                                            rejectRequest(req)
                                        }, "disabled" to loading.value), "拒绝", 8, utsArrayOf(
                                            "onClick",
                                            "disabled"
                                        ))
                                    ))
                                ))
                            }), 128)
                        ))
                    } else {
                        createCommentVNode("v-if", true)
                    }
                    ,
                    if (myFriends.value.length > 0) {
                        createElementVNode("view", utsMapOf("key" to 2, "class" to "friends-section"), utsArrayOf(
                            createElementVNode("text", utsMapOf("class" to "friends-title"), "我的好友"),
                            createElementVNode(Fragment, null, RenderHelpers.renderList(myFriends.value, fun(friend, idx, __index, _cached): Any {
                                return createElementVNode("view", utsMapOf("key" to idx, "class" to "friend-item"), utsArrayOf(
                                    createElementVNode("view", utsMapOf("class" to "friend-info"), utsArrayOf(
                                        createElementVNode("text", utsMapOf("class" to "friend-username"), toDisplayString(friend.username), 1),
                                        createElementVNode("text", utsMapOf("class" to "friend-email"), toDisplayString(friend.email), 1)
                                    ))
                                ))
                            }), 128)
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
                return utsMapOf("container" to padStyleMapOf(utsMapOf("backgroundImage" to "none", "backgroundColor" to "#f5f7fa", "paddingTop" to "20rpx", "paddingRight" to "20rpx", "paddingBottom" to "20rpx", "paddingLeft" to "20rpx")), "header" to padStyleMapOf(utsMapOf("display" to "flex", "alignItems" to "center", "marginBottom" to "30rpx", "paddingTop" to "20rpx", "paddingRight" to 0, "paddingBottom" to "20rpx", "paddingLeft" to 0)), "back" to padStyleMapOf(utsMapOf("fontSize" to "40rpx", "color" to "#4e54c8", "marginRight" to "20rpx")), "title" to padStyleMapOf(utsMapOf("fontSize" to "36rpx", "fontWeight" to "bold")), "search-section" to padStyleMapOf(utsMapOf("display" to "flex", "flexDirection" to "row", "alignItems" to "center", "marginBottom" to "30rpx")), "search-input" to padStyleMapOf(utsMapOf("flex" to 1, "paddingTop" to "20rpx", "paddingRight" to "20rpx", "paddingBottom" to "20rpx", "paddingLeft" to "20rpx", "fontSize" to "28rpx", "borderTopWidth" to "1rpx", "borderRightWidth" to "1rpx", "borderBottomWidth" to "1rpx", "borderLeftWidth" to "1rpx", "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#cccccc", "borderRightColor" to "#cccccc", "borderBottomColor" to "#cccccc", "borderLeftColor" to "#cccccc", "borderTopLeftRadius" to "10rpx", "borderTopRightRadius" to "10rpx", "borderBottomRightRadius" to "10rpx", "borderBottomLeftRadius" to "10rpx", "marginRight" to "20rpx")), "search-btn" to padStyleMapOf(utsMapOf("backgroundImage" to "none", "backgroundColor" to "#4e54c8", "color" to "#ffffff", "borderTopWidth" to "medium", "borderRightWidth" to "medium", "borderBottomWidth" to "medium", "borderLeftWidth" to "medium", "borderTopStyle" to "none", "borderRightStyle" to "none", "borderBottomStyle" to "none", "borderLeftStyle" to "none", "borderTopColor" to "#000000", "borderRightColor" to "#000000", "borderBottomColor" to "#000000", "borderLeftColor" to "#000000", "borderTopLeftRadius" to "10rpx", "borderTopRightRadius" to "10rpx", "borderBottomRightRadius" to "10rpx", "borderBottomLeftRadius" to "10rpx", "paddingTop" to "20rpx", "paddingRight" to "30rpx", "paddingBottom" to "20rpx", "paddingLeft" to "30rpx", "fontSize" to "28rpx", "whiteSpace" to "nowrap")), "user-result" to padStyleMapOf(utsMapOf("backgroundImage" to "none", "backgroundColor" to "#ffffff", "borderTopLeftRadius" to "16rpx", "borderTopRightRadius" to "16rpx", "borderBottomRightRadius" to "16rpx", "borderBottomLeftRadius" to "16rpx", "paddingTop" to "20rpx", "paddingRight" to "20rpx", "paddingBottom" to "20rpx", "paddingLeft" to "20rpx", "marginBottom" to "30rpx", "display" to "flex", "alignItems" to "center", "justifyContent" to "space-between")), "user-info" to padStyleMapOf(utsMapOf("flex" to 1)), "user-email" to padStyleMapOf(utsMapOf("fontSize" to "28rpx", "color" to "#333333", "marginBottom" to "8rpx")), "user-username" to padStyleMapOf(utsMapOf("fontSize" to "24rpx", "color" to "#666666")), "add-btn" to padStyleMapOf(utsMapOf("backgroundImage" to "none", "backgroundColor" to "#4e54c8", "color" to "#ffffff", "borderTopWidth" to "medium", "borderRightWidth" to "medium", "borderBottomWidth" to "medium", "borderLeftWidth" to "medium", "borderTopStyle" to "none", "borderRightStyle" to "none", "borderBottomStyle" to "none", "borderLeftStyle" to "none", "borderTopColor" to "#000000", "borderRightColor" to "#000000", "borderBottomColor" to "#000000", "borderLeftColor" to "#000000", "borderTopLeftRadius" to "10rpx", "borderTopRightRadius" to "10rpx", "borderBottomRightRadius" to "10rpx", "borderBottomLeftRadius" to "10rpx", "paddingTop" to "15rpx", "paddingRight" to "25rpx", "paddingBottom" to "15rpx", "paddingLeft" to "25rpx", "fontSize" to "26rpx", "backgroundImage:disabled" to "none", "backgroundColor:disabled" to "#cccccc")), "pending-btn" to padStyleMapOf(utsMapOf("backgroundImage" to "none", "backgroundColor" to "#ffd93d", "color" to "#333333", "borderTopWidth" to "medium", "borderRightWidth" to "medium", "borderBottomWidth" to "medium", "borderLeftWidth" to "medium", "borderTopStyle" to "none", "borderRightStyle" to "none", "borderBottomStyle" to "none", "borderLeftStyle" to "none", "borderTopColor" to "#000000", "borderRightColor" to "#000000", "borderBottomColor" to "#000000", "borderLeftColor" to "#000000", "borderTopLeftRadius" to "10rpx", "borderTopRightRadius" to "10rpx", "borderBottomRightRadius" to "10rpx", "borderBottomLeftRadius" to "10rpx", "paddingTop" to "15rpx", "paddingRight" to "25rpx", "paddingBottom" to "15rpx", "paddingLeft" to "25rpx", "fontSize" to "26rpx")), "added-btn" to padStyleMapOf(utsMapOf("backgroundImage" to "none", "backgroundColor" to "#35b765", "color" to "#ffffff", "borderTopWidth" to "medium", "borderRightWidth" to "medium", "borderBottomWidth" to "medium", "borderLeftWidth" to "medium", "borderTopStyle" to "none", "borderRightStyle" to "none", "borderBottomStyle" to "none", "borderLeftStyle" to "none", "borderTopColor" to "#000000", "borderRightColor" to "#000000", "borderBottomColor" to "#000000", "borderLeftColor" to "#000000", "borderTopLeftRadius" to "10rpx", "borderTopRightRadius" to "10rpx", "borderBottomRightRadius" to "10rpx", "borderBottomLeftRadius" to "10rpx", "paddingTop" to "15rpx", "paddingRight" to "25rpx", "paddingBottom" to "15rpx", "paddingLeft" to "25rpx", "fontSize" to "26rpx")), "pending-section" to padStyleMapOf(utsMapOf("backgroundImage" to "none", "backgroundColor" to "#ffffff", "borderTopLeftRadius" to "16rpx", "borderTopRightRadius" to "16rpx", "borderBottomRightRadius" to "16rpx", "borderBottomLeftRadius" to "16rpx", "paddingTop" to "20rpx", "paddingRight" to "20rpx", "paddingBottom" to "20rpx", "paddingLeft" to "20rpx", "marginBottom" to "30rpx")), "pending-title" to padStyleMapOf(utsMapOf("fontSize" to "28rpx", "fontWeight" to "bold", "marginBottom" to "20rpx")), "pending-item" to padStyleMapOf(utsMapOf("display" to "flex", "flexDirection" to "row", "alignItems" to "center", "justifyContent" to "space-between", "marginBottom" to "20rpx", "paddingTop" to "15rpx", "paddingRight" to 0, "paddingBottom" to "15rpx", "paddingLeft" to 0, "borderBottomWidth" to "1rpx", "borderBottomStyle" to "solid", "borderBottomColor" to "#f0f0f0", "borderBottomWidth:last-child" to "medium", "borderBottomStyle:last-child" to "none", "borderBottomColor:last-child" to "#000000", "marginBottom:last-child" to 0)), "pending-info" to padStyleMapOf(utsMapOf("flex" to 1)), "pending-username" to padStyleMapOf(utsMapOf("fontSize" to "28rpx", "color" to "#333333", "marginBottom" to "8rpx")), "pending-email" to padStyleMapOf(utsMapOf("fontSize" to "24rpx", "color" to "#666666")), "pending-buttons" to padStyleMapOf(utsMapOf("display" to "flex", "flexDirection" to "row", "gap" to "15rpx")), "accept-btn" to padStyleMapOf(utsMapOf("backgroundImage" to "none", "backgroundColor" to "#4caf50", "color" to "#ffffff", "borderTopWidth" to "medium", "borderRightWidth" to "medium", "borderBottomWidth" to "medium", "borderLeftWidth" to "medium", "borderTopStyle" to "none", "borderRightStyle" to "none", "borderBottomStyle" to "none", "borderLeftStyle" to "none", "borderTopColor" to "#000000", "borderRightColor" to "#000000", "borderBottomColor" to "#000000", "borderLeftColor" to "#000000", "borderTopLeftRadius" to "10rpx", "borderTopRightRadius" to "10rpx", "borderBottomRightRadius" to "10rpx", "borderBottomLeftRadius" to "10rpx", "paddingTop" to "15rpx", "paddingRight" to "25rpx", "paddingBottom" to "15rpx", "paddingLeft" to "25rpx", "fontSize" to "26rpx", "backgroundImage:disabled" to "none", "backgroundColor:disabled" to "#cccccc")), "reject-btn" to padStyleMapOf(utsMapOf("backgroundImage" to "none", "backgroundColor" to "#e74c3c", "color" to "#ffffff", "borderTopWidth" to "medium", "borderRightWidth" to "medium", "borderBottomWidth" to "medium", "borderLeftWidth" to "medium", "borderTopStyle" to "none", "borderRightStyle" to "none", "borderBottomStyle" to "none", "borderLeftStyle" to "none", "borderTopColor" to "#000000", "borderRightColor" to "#000000", "borderBottomColor" to "#000000", "borderLeftColor" to "#000000", "borderTopLeftRadius" to "10rpx", "borderTopRightRadius" to "10rpx", "borderBottomRightRadius" to "10rpx", "borderBottomLeftRadius" to "10rpx", "paddingTop" to "15rpx", "paddingRight" to "25rpx", "paddingBottom" to "15rpx", "paddingLeft" to "25rpx", "fontSize" to "26rpx", "backgroundImage:disabled" to "none", "backgroundColor:disabled" to "#cccccc")), "friends-section" to padStyleMapOf(utsMapOf("backgroundImage" to "none", "backgroundColor" to "#ffffff", "borderTopLeftRadius" to "16rpx", "borderTopRightRadius" to "16rpx", "borderBottomRightRadius" to "16rpx", "borderBottomLeftRadius" to "16rpx", "paddingTop" to "20rpx", "paddingRight" to "20rpx", "paddingBottom" to "20rpx", "paddingLeft" to "20rpx")), "friends-title" to padStyleMapOf(utsMapOf("fontSize" to "28rpx", "fontWeight" to "bold", "marginBottom" to "20rpx")), "friend-item" to padStyleMapOf(utsMapOf("display" to "flex", "alignItems" to "center", "paddingTop" to "15rpx", "paddingRight" to 0, "paddingBottom" to "15rpx", "paddingLeft" to 0, "borderBottomWidth" to "1rpx", "borderBottomStyle" to "solid", "borderBottomColor" to "#f0f0f0", "borderBottomWidth:last-child" to "medium", "borderBottomStyle:last-child" to "none", "borderBottomColor:last-child" to "#000000")), "friend-info" to padStyleMapOf(utsMapOf("flex" to 1)), "friend-username" to padStyleMapOf(utsMapOf("fontSize" to "28rpx", "color" to "#333333", "marginBottom" to "8rpx")), "friend-email" to padStyleMapOf(utsMapOf("fontSize" to "24rpx", "color" to "#666666")))
            }
        var inheritAttrs = true
        var inject: Map<String, Map<String, Any?>> = utsMapOf()
        var emits: Map<String, Any?> = utsMapOf()
        var props = normalizePropsOptions(utsMapOf())
        var propsNeedCastKeys: UTSArray<String> = utsArrayOf()
        var components: Map<String, CreateVueComponent> = utsMapOf()
    }
}
