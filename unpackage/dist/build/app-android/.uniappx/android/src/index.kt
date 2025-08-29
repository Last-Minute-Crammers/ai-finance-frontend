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
import io.dcloud.uniapp.extapi.createCanvasContext as uni_createCanvasContext
import io.dcloud.uniapp.extapi.getStorageSync as uni_getStorageSync
import io.dcloud.uniapp.extapi.request as uni_request
import io.dcloud.uniapp.extapi.setStorageSync as uni_setStorageSync
import io.dcloud.uniapp.extapi.showToast as uni_showToast
val runBlock1 = run {
    __uniConfig.getAppStyles = fun(): Map<String, Map<String, Map<String, Any>>> {
        return GenApp.styles
    }
}
open class GenApp : BaseApp {
    constructor(__ins: ComponentInternalInstance) : super(__ins) {
        onLaunch(fun(_: OnLaunchOptions) {
            console.log("App 启动")
        }
        , __ins)
        onAppShow(fun(_: OnShowOptions) {
            console.log("App 显示")
        }
        , __ins)
        onAppHide(fun() {
            console.log("App 隐藏")
        }
        , __ins)
    }
    companion object {
        val styles: Map<String, Map<String, Map<String, Any>>> by lazy {
            normalizeCssStyles(utsArrayOf(
                styles0
            ))
        }
        val styles0: Map<String, Map<String, Map<String, Any>>>
            get() {
                return utsMapOf("app-container" to padStyleMapOf(utsMapOf("backgroundColor" to "#f4f4f4", "fontFamily" to "Arial, sans-serif", "color" to "#333333")))
            }
    }
}
val GenAppClass = CreateVueAppComponent(GenApp::class.java, fun(): VueComponentOptions {
    return VueComponentOptions(type = "app", name = "", inheritAttrs = true, inject = Map(), props = Map(), propsNeedCastKeys = utsArrayOf(), emits = Map(), components = Map(), styles = GenApp.styles)
}
, fun(instance): GenApp {
    return GenApp(instance)
}
)
val BACKEND_URLS = Record(production = "http://47.109.194.39/api", local = "http://localhost:8080", dockerHost = "http://host.docker.internal:8080", ip = "http://127.0.0.1:8080", external = "http://192.168.1.100:8080")
val DEFAULT_URL: String = BACKEND_URLS.production
val getBackendUrl = fun(): String {
    val configuredUrl: String? = uni_getStorageSync("backend_url")
    return configuredUrl || DEFAULT_URL
}
val BASE_URL: String = getBackendUrl()
interface RequestOptions {
    var url: String
    var method: String?
    var data: Any?
    var params: Any?
    var requireAuth: Boolean?
    var timeout: Number?
    var retries: Number?
    var headers: Record<String, String>?
}
interface UniRequestError {
    var errMsg: String?
    var message: String?
}
val getToken = fun(): String {
    val token: String = uni_getStorageSync("token") || ""
    console.log("Retrieved token:", if (token) {
        "" + token.substring(0, 20) + "..."
    } else {
        "No token found"
    }
    )
    return token
}
val uni: Any
val setBackendUrl = fun(url: String): Unit {
    uni_setStorageSync("backend_url", url)
    console.log("Backend URL set to:", url)
}
val setBackendEnvironment = fun(envKey: String): String? {
    if (BACKEND_URLS[envKey]) {
        val url: String = BACKEND_URLS[envKey]
        setBackendUrl(url)
        return url
    }
    return null
}
val getAvailableBackendUrls = fun(): Record<String, String> {
    return BACKEND_URLS
}
val checkBackendConnection = fun(customUrl: String?): UTSPromise<Any> {
    return wrapUTSPromise(suspend w@{
            val targetUrl: String = customUrl || BASE_URL
            try {
                console.log("检查后端连接状态...", targetUrl)
                val startTime: Number = Date.now()
                val requestPromise: UTSPromise<Any> = UTSPromise(fun(resolve, reject){
                    uni_request<Any>(RequestOptions(url = targetUrl + "/api/test", method = "GET", timeout = 10000, success = fun(response: Any){
                        if (!response) {
                            reject(UTSError("未收到后端响应"))
                            return
                        }
                        resolve(response)
                    }
                    , fail = fun(error: Any){
                        reject(error || UTSError("请求失败"))
                    }
                    ))
                }
                )
                val timeoutPromise: UTSPromise<Any> = UTSPromise(fun(_, reject){
                    setTimeout(fun(){
                        return reject(UTSError("请求超时(10秒)"))
                    }
                    , 10000)
                }
                )
                val response: Any = await(UTSPromise.race(utsArrayOf(
                    requestPromise,
                    timeoutPromise
                )))
                val endTime: Number = Date.now()
                if (!response || response.statusCode === undefined) {
                    throw UTSError("无效的响应格式")
                }
                console.log("后端连接检查结果:", response, "\u54CD\u5E94\u65F6\u95F4: " + (endTime - startTime) + "ms")
                return@w object : UTSJSONObject() {
                    var connected = response.statusCode === 200
                    var statusCode = response.statusCode
                    var responseTime = endTime - startTime
                    var serverInfo = response.data || UTSJSONObject()
                }
            }
             catch (error: Throwable) {
                console.error("后端连接检查失败:", error)
                val errMsg: String = if (UTSAndroid.`typeof`(error) === "object" && error != null && resolveInOperator(error, "message")) {
                    String((error as Any).message || (error as Any).errMsg)
                } else {
                    "未知错误"
                }
                val isConnectionRefused: Boolean = UTSAndroid.`typeof`(errMsg) === "string" && errMsg.indexOf("CONNECTION_REFUSED") !== -1
                val isTimeout: Boolean = UTSAndroid.`typeof`(errMsg) === "string" && (errMsg.indexOf("timeout") !== -1 || errMsg.indexOf("超时") !== -1)
                val diagnostics: {
                    var serverUrl: String
                    var errorType: String
                    var isConnectionRefused: Boolean
                    var isTimeout: Boolean
                    var possibleCauses: UTSArray<String>
                } = UTSJSONObject(Map<String, Any?>(utsArrayOf(
                    utsArrayOf(
                        "serverUrl",
                        targetUrl
                    ),
                    utsArrayOf(
                        "errorType",
                        errMsg
                    ),
                    utsArrayOf(
                        "isConnectionRefused",
                        isConnectionRefused
                    ),
                    utsArrayOf(
                        "isTimeout",
                        isTimeout
                    ),
                    utsArrayOf(
                        "possibleCauses",
                        utsArrayOf()
                    )
                )))
                if (isConnectionRefused) {
                    diagnostics.possibleCauses = utsArrayOf(
                        "后端服务器未启动",
                        "端口8080可能被其他应用占用",
                        "检查防火墙设置是否允许连接"
                    )
                } else if (isTimeout) {
                    diagnostics.possibleCauses = utsArrayOf(
                        "Docker容器端口映射不正确 - 检查docker-compose.yml",
                        "Docker网络配置问题 - 尝试使用127.0.0.1而不是localhost",
                        "防火墙阻止了连接 - 检查防火墙设置",
                        "后端服务响应过慢 - 检查服务器负载",
                        "后端服务未正确监听端口 - 检查后端日志"
                    )
                } else {
                    diagnostics.possibleCauses = utsArrayOf(
                        "后端服务未正确启动",
                        "API端点路径可能不正确",
                        "请求处理过程中出现错误",
                        "网络连接问题"
                    )
                }
                console.log("连接诊断:", diagnostics)
                return@w UTSJSONObject(Map<String, Any?>(utsArrayOf(
                    utsArrayOf(
                        "connected",
                        false
                    ),
                    utsArrayOf(
                        "error",
                        errMsg
                    ),
                    utsArrayOf(
                        "diagnostics",
                        diagnostics
                    )
                )))
            }
    })
}
val request = fun<T>(options: RequestOptions): UTSPromise<T> {
    return wrapUTSPromise(suspend w@{
            val url = options.url
            val _options_method = options.method
            val method = if (_options_method == null) {
                "GET"
            } else {
                _options_method
            }
            val data = options.data
            val params = options.params
            val _options_requireAuth = options.requireAuth
            val requireAuth = if (_options_requireAuth == null) {
                true
            } else {
                _options_requireAuth
            }
            val _options_timeout = options.timeout
            val timeout = if (_options_timeout == null) {
                10000
            } else {
                _options_timeout
            }
            val _options_headers = options.headers
            val headers = if (_options_headers == null) {
                UTSJSONObject()
            } else {
                _options_headers
            }
            var fullUrl: String = BASE_URL + url
            if (params) {
                val queryString: String = Object.keys(params).map(fun(key): String {
                    return "" + encodeURIComponent(key) + "=" + encodeURIComponent(params[key])
                }
                ).join("&")
                fullUrl += (if (fullUrl.includes("?")) {
                    "&"
                } else {
                    "?"
                }
                ) + queryString
            }
            val requestHeaders: Record<String, String> = UTSJSONObject.assign(object : UTSJSONObject() {
                var `Content-Type` = "application/json"
            }, headers)
            if (requireAuth) {
                val token: String = getToken()
                if (token) {
                    requestHeaders["Authorization"] = "Bearer " + token
                }
            }
            return@w UTSPromise<T>(fun(resolve, reject){
                uni_request<Any>(RequestOptions(url = fullUrl, method = method, data = data, header = requestHeaders, timeout = timeout, success = fun(response: Any){
                    console.log("\u8BF7\u6C42\u6210\u529F [" + method + "] " + fullUrl + ":", response)
                    if (response.statusCode >= 200 && response.statusCode < 300) {
                        resolve(response.data)
                    } else {
                        val error = UniRequestError(message = "HTTP " + response.statusCode + ": " + (response.data?.message || "请求失败"))
                        reject(error)
                    }
                }
                , fail = fun(error: Any){
                    console.error("\u8BF7\u6C42\u5931\u8D25 [" + method + "] " + fullUrl + ":", error)
                    val errorMessage: String = error.errMsg || error.message || "网络请求失败"
                    val uniError = UniRequestError(errMsg = errorMessage, message = errorMessage)
                    reject(uniError)
                }
                ))
            }
            )
    })
}
interface ApiResponse<T> {
    var code: Number
    var data: T?
    var message: String?
    var Code: Number?
    var Data: T?
    var Msg: String?
    var error: String?
}
val uni1: Any
interface LoginResponse {
    var token: String
    var user: {
        var id: Number
        var username: String
        var email: String
    }
}
val userLogin = fun(email: String, password: String): UTSPromise<ApiResponse<LoginResponse>> {
    return wrapUTSPromise(suspend w@{
            try {
                console.log("Attempting login with:", UTSJSONObject(Map<String, Any?>(utsArrayOf(
                    utsArrayOf(
                        "email",
                        email
                    ),
                    utsArrayOf(
                        "password",
                        "***"
                    )
                ))))
                val response = await(request(object : UTSJSONObject() {
                    var url = "/api/public/user/login"
                    var method = "POST"
                    var data = UTSJSONObject(Map<String, Any?>(utsArrayOf(
                        utsArrayOf(
                            "email",
                            email
                        ),
                        utsArrayOf(
                            "password",
                            password
                        )
                    )))
                }))
                console.log("Login response received:", response)
                console.log("Response type:", UTSAndroid.`typeof`(response))
                if (!response) {
                    throw UTSError("服务器未返回有效响应")
                }
                val isSuccess = response.code === 200 || response.code === 0 || (response.statusCode && response.statusCode === 200) || (response.Msg && response.Msg === "登录成功") || (response.message && response.message.includes("成功"))
                if (isSuccess) {
                    var token: String? = null
                    if (response.Data?.Token) {
                        token = response.Data.Token
                    } else if (response.data?.token) {
                        token = response.data.token
                    } else if (response.token) {
                        token = response.token
                    } else if (response.access_token) {
                        token = response.access_token
                    } else if (response.accessToken) {
                        token = response.accessToken
                    }
                    if (token) {
                        uni_setStorageSync("token", token)
                        console.log("Token saved successfully:", token.substring(0, 20) + "...")
                        val user = response.Data?.User || response.data?.user || response.user || UTSJSONObject()
                        return@w object : UTSJSONObject() {
                            var code: Number = 200
                            var message = response.Msg || response.message || "登录成功"
                            var data = UTSJSONObject(Map<String, Any?>(utsArrayOf(
                                utsArrayOf(
                                    "token",
                                    token
                                ),
                                utsArrayOf(
                                    "user",
                                    object : UTSJSONObject() {
                                        var id = user.Id || user.id
                                        var username = user.Username || user.username
                                        var email = user.Email || user.email
                                    }
                                )
                            )))
                        }
                    } else {
                        console.error("登录成功但未找到token，完整响应:", JSON.stringify(response, null, 2))
                        throw UTSError("登录成功但服务器未返回访问令牌")
                    }
                } else {
                    val errorMessage = response.Msg || response.message || response.error || response.msg || "登录失败"
                    console.error("登录失败:", errorMessage, response)
                    throw UTSError(errorMessage)
                }
            }
             catch (error: Throwable) {
                console.error("Login error details:", error)
                if (error is UTSError) {
                    if ((error as UTSError).message.includes("500") || (error as UTSError).message.includes("Internal Server Error") || (error as UTSError).message.includes("服务器错误")) {
                        throw UTSError("服务器内部错误，请检查用户账号是否存在或联系管理员")
                    } else if ((error as UTSError).message.includes("Failed to fetch") || (error as UTSError).message.includes("network")) {
                        throw UTSError("网络连接失败，请检查网络连接")
                    } else if ((error as UTSError).message.includes("timeout")) {
                        throw UTSError("请求超时，请稍后重试")
                    } else if ((error as UTSError).message.includes("用户不存在") || (error as UTSError).message.includes("record not found")) {
                        throw UTSError("用户不存在，请检查邮箱地址或先注册账号")
                    }
                }
                throw error
            }
    })
}
val userRegister = fun(userData: {
    var username: String
    var email: String
    var password: String
    var captcha: String
}): UTSPromise<Any> {
    return request(object : UTSJSONObject() {
        var url = "/api/public/user/register"
        var method = "POST"
        var data = userData
    })
}
val sendFriendInvitation = fun(data: {
    var invitee: Number
}): UTSPromise<ApiResponse<null>> {
    return request(UTSJSONObject(Map<String, Any?>(utsArrayOf(
        utsArrayOf(
            "url",
            "/api/user/friend/invitation"
        ),
        utsArrayOf(
            "method",
            "POST"
        ),
        utsArrayOf(
            "data",
            data
        ),
        utsArrayOf(
            "requireAuth",
            true
        )
    ))))
}
val getFriendInvitations = fun(): UTSPromise<ApiResponse<UTSArray<{
    var id: Number
    var inviter: {
        var id: Number
        var username: String
        var email: String
    }
    var invitee: {
        var id: Number
        var username: String
        var email: String
    }
    var status: String
    var createTime: String
}>>> {
    return request(object : UTSJSONObject() {
        var url = "/api/user/friend/invitation?isInvite=false"
        var method = "GET"
        var requireAuth = true
    })
}
val acceptFriendInvitation = fun(invitationId: Number): UTSPromise<ApiResponse<null>> {
    return request(object : UTSJSONObject() {
        var url = "/api/user/friend/invitation/" + invitationId + "/accept"
        var method = "PUT"
        var requireAuth = true
    })
}
val refuseFriendInvitation = fun(invitationId: Number): UTSPromise<ApiResponse<null>> {
    return request(object : UTSJSONObject() {
        var url = "/api/user/friend/invitation/" + invitationId + "/refuse"
        var method = "PUT"
        var requireAuth = true
    })
}
val getFriendList = fun(): UTSPromise<ApiResponse<{
    var list: UTSArray<{
        var id: Number
        var username: String
        var email: String
    }>
}>> {
    return request(object : UTSJSONObject() {
        var url = "/api/user/friend/list"
        var method = "GET"
        var requireAuth = true
    })
}
interface Transaction {
    var Id: Number
    var UserId: Number
    var Amount: Number
    var CategoryId: Number
    var CategoryName: String?
    var IncomeExpense: String
    var Remark: String
    var TradeTime: String
    var CreateTime: String
    var UpdateTime: String
}
val createTransaction = fun(data: {
    var CategoryId: Number
    var IncomeExpense: String
    var Amount: Number
    var Remark: String
    var TradeTime: String
}): UTSPromise<ApiResponse<Transaction>> {
    console.log("createTransaction called with data:", data)
    return request(UTSJSONObject(Map<String, Any?>(utsArrayOf(
        utsArrayOf(
            "url",
            "/api/user/transaction"
        ),
        utsArrayOf(
            "method",
            "POST"
        ),
        utsArrayOf(
            "data",
            data
        ),
        utsArrayOf(
            "requireAuth",
            true
        )
    ))))
}
val getMonthStatistic = fun(params: {
    var incomeExpense: String?
    var startTime: String?
    var endTime: String?
}?): UTSPromise<ApiResponse<Any>> {
    return request(object : UTSJSONObject() {
        var url = "/api/user/transaction/statistic/month"
        var method = "POST"
        var data = params
        var requireAuth = true
    })
}
val getTotalStatistic = fun(): UTSPromise<ApiResponse<{
    var income: {
        var amount: Number
        var count: Number
    }
    var expense: {
        var amount: Number
        var count: Number
    }
    var total_assets: Number
}>> {
    return request(object : UTSJSONObject() {
        var url = "/api/user/transaction/statistic/total"
        var method = "GET"
        var requireAuth = true
    })
}
val CATEGORY_STORAGE_KEY = "local_categories"
interface IconDefinition {
    var id: Number
    var emoji: String
    var name: String
    var category: String
}
val STANDARD_ICONS = utsArrayOf(
    IconDefinition(id = 1, emoji = "📁", name = "文件夹", category = "general"),
    IconDefinition(id = 2, emoji = "🍔", name = "餐饮", category = "food"),
    IconDefinition(id = 3, emoji = "🚗", name = "交通", category = "transport"),
    IconDefinition(id = 4, emoji = "🏠", name = "住房", category = "home"),
    IconDefinition(id = 5, emoji = "👕", name = "服装", category = "clothing"),
    IconDefinition(id = 6, emoji = "📱", name = "数码", category = "entertainment"),
    IconDefinition(id = 7, emoji = "💊", name = "医疗", category = "health"),
    IconDefinition(id = 8, emoji = "🎬", name = "娱乐", category = "entertainment"),
    IconDefinition(id = 9, emoji = "📚", name = "教育", category = "education"),
    IconDefinition(id = 10, emoji = "🎮", name = "游戏", category = "entertainment"),
    IconDefinition(id = 11, emoji = "💼", name = "工作", category = "business"),
    IconDefinition(id = 12, emoji = "💰", name = "收入", category = "business"),
    IconDefinition(id = 13, emoji = "🎁", name = "礼物", category = "general"),
    IconDefinition(id = 14, emoji = "✈️", name = "旅行", category = "travel"),
    IconDefinition(id = 15, emoji = "🏥", name = "医院", category = "health"),
    IconDefinition(id = 16, emoji = "🎓", name = "学习", category = "education"),
    IconDefinition(id = 17, emoji = "💍", name = "珠宝", category = "clothing"),
    IconDefinition(id = 18, emoji = "🏖️", name = "度假", category = "travel"),
    IconDefinition(id = 19, emoji = "🎯", name = "运动", category = "entertainment"),
    IconDefinition(id = 20, emoji = "🎨", name = "艺术", category = "entertainment")
) as UTSArray<IconDefinition>
val getIconById = fun(id: Number): Any {
    return STANDARD_ICONS.find(fun(icon): Boolean {
        return icon.id === id
    }
    )
}
val getIconByEmoji = fun(emoji: String): Any {
    return STANDARD_ICONS.find(fun(icon): Boolean {
        return icon.emoji === emoji
    }
    )
}
val getAllIcons = fun(): UTSArray<IconDefinition> {
    return STANDARD_ICONS.slice()
}
val saveCategoriesToLocal = fun(categories: UTSArray<Category>): Unit {
    try {
        uni_setStorageSync(CATEGORY_STORAGE_KEY, JSON.stringify(categories))
        console.log("分类数据已保存到本地存储:", categories.length, "个分类")
    }
     catch (error: Throwable) {
        console.error("保存分类到本地存储失败:", error)
    }
}
val getCategoriesFromLocal = fun(): UTSArray<Category> {
    try {
        val data: String? = uni_getStorageSync(CATEGORY_STORAGE_KEY)
        if (data) {
            val categories: UTSArray<Category> = JSON.parse(data)
            console.log("从本地存储获取分类数据:", categories.length, "个分类")
            return categories
        }
    }
     catch (error: Throwable) {
        console.error("从本地存储获取分类失败:", error)
    }
    return utsArrayOf()
}
val filterCategoriesByType = fun(categories: UTSArray<Category>, type: String): UTSArray<Category> {
    return categories.filter(fun(category): Boolean {
        return category.incomeExpense === type
    }
    )
}
interface Category {
    var id: Number
    var name: String
    var iconId: Number?
    var icon: String?
    var color: String?
    var incomeExpense: String
    var createTime: String
    var updateTime: String
}
val getCategoryList = fun(data: {
    var income_expense: String
}): UTSPromise<ApiResponse<UTSArray<Category>>> {
    return wrapUTSPromise(suspend w@{
            try {
                val localCategories: UTSArray<Category> = getCategoriesFromLocal()
                val filteredCategories: UTSArray<Category> = filterCategoriesByType(localCategories, data.income_expense)
                if (filteredCategories.length > 0) {
                    return@w object : UTSJSONObject() {
                        var code: Number = 200
                        var message = "success"
                        var data = filteredCategories
                    }
                }
                val rawResponse: Any = await(request(object : UTSJSONObject() {
                    var url = "/api/user/category/list"
                    var method = "GET"
                    var params = data
                    var requireAuth = true
                }))
                val transformedData: UTSArray<Category> = utsArrayOf()
                if (rawResponse.Data && UTSArray.isArray(rawResponse.Data)) {
                    run {
                        var i: Number = 0
                        while(i < rawResponse.Data.length){
                            val item: Any = rawResponse.Data[i]
                            val iconInfo: Any = if (item.Icon || item.icon) {
                                getIconByEmoji(item.Icon || item.icon)
                            } else {
                                undefined
                            }
                            transformedData.push(object : UTSJSONObject() {
                                var id = item.ID
                                var name = item.Name
                                var iconId = iconInfo?.id || 1
                                var icon = item.Icon || item.icon
                                var color = item.Color
                                var incomeExpense = item.IncomeExpense
                                var createTime = item.CreatedAt
                                var updateTime = item.UpdatedAt
                            })
                            i++
                        }
                    }
                }
                val response = ApiResponse(code = 200, message = rawResponse.Msg || "success", data = transformedData)
                if (response.code === 200) {
                    val newCategories: UTSArray<Category> = response.data || utsArrayOf()
                    if (newCategories.length > 0) {
                        val existingCategories: UTSArray<Category> = getCategoriesFromLocal()
                        val existingIds: Set<Number> = Set<Number>()
                        run {
                            var i: Number = 0
                            while(i < existingCategories.length){
                                existingIds.add(existingCategories[i].id)
                                i++
                            }
                        }
                        val uniqueNewCategories: UTSArray<Category> = utsArrayOf()
                        run {
                            var i: Number = 0
                            while(i < newCategories.length){
                                if (!existingIds.has(newCategories[i].id)) {
                                    uniqueNewCategories.push(newCategories[i])
                                }
                                i++
                            }
                        }
                        val allCategories: UTSArray<Category> = existingCategories.concat(uniqueNewCategories)
                        saveCategoriesToLocal(allCategories)
                    }
                }
                return@w response
            }
             catch (error: Throwable) {
                val localCategories: UTSArray<Category> = getCategoriesFromLocal()
                val filteredCategories: UTSArray<Category> = filterCategoriesByType(localCategories, data.income_expense)
                if (filteredCategories.length > 0) {
                    return@w object : UTSJSONObject() {
                        var code: Number = 200
                        var message = "success (cached)"
                        var data = filteredCategories
                    }
                }
                throw error
            }
    })
}
val createCategory = fun(data: {
    var name: String
    var iconId: Number?
    var icon: String?
    var color: String?
    var incomeExpense: String
}): UTSPromise<ApiResponse<Category>> {
    return wrapUTSPromise(suspend w@{
            val iconInfo = if (data.iconId) {
                getIconById(data.iconId)
            } else {
                undefined
            }
            val requestData: UTSJSONObject = object : UTSJSONObject() {
                var name = data.name
                var icon = iconInfo?.emoji || data.icon || "📁"
                var color = data.color
                var income_expense = data.incomeExpense
            }
            console.log("发送创建分类请求:", requestData)
            val rawResponse = await(request(object : UTSJSONObject() {
                var url = "/api/user/category"
                var method = "POST"
                var data = requestData
                var requireAuth = true
            }))
            return@w object : UTSJSONObject() {
                var code: Number = 200
                var message = rawResponse.Msg || "success"
                var data = rawResponse.Data || rawResponse
            }
    })
}
val createCategoryWithCurrentType = fun(name: String, iconId: Number?, color: String?): UTSPromise<ApiResponse<Category>?> {
    return wrapUTSPromise(suspend w@{
            val getCurrentIncomeExpense = (await(import("../../utils/pageState"))).getCurrentIncomeExpense
            val currentIncomeExpense = getCurrentIncomeExpense()
            if (!currentIncomeExpense) {
                console.error("无法获取当前页面类型，请确保在记账页面中")
                uni_showToast(ShowToastOptions(title = "请先在记账页面选择类型", icon = "none"))
                return@w null
            }
            val iconInfo = if (iconId) {
                getIconById(iconId)
            } else {
                getIconById(1)
            }
            val tempCategory = Category(id = Date.now(), name = name, iconId = iconId || 1, icon = iconInfo?.emoji || "📁", color = color || "#4e54c8", incomeExpense = currentIncomeExpense, createTime = Date().toISOString(), updateTime = Date().toISOString())
            try {
                val localCategories = getCategoriesFromLocal()
                val allCategories = localCategories.concat(utsArrayOf(
                    tempCategory
                ))
                saveCategoriesToLocal(allCategories)
                console.log("分类已保存到本地:", tempCategory)
                try {
                    val response = await(createCategory(UTSJSONObject(Map<String, Any?>(utsArrayOf(
                        utsArrayOf(
                            "name",
                            name
                        ),
                        utsArrayOf(
                            "iconId",
                            iconId
                        ),
                        utsArrayOf(
                            "color",
                            color
                        ),
                        utsArrayOf(
                            "incomeExpense",
                            currentIncomeExpense
                        )
                    )))))
                    if (response.code === 200 && response.data) {
                        val updatedCategories = localCategories.map(fun(cat){
                            return if (cat.id === tempCategory.id) {
                                response.data
                            } else {
                                cat
                            }
                        }).filter(fun(cat): cat is Category {
                            return cat !== undefined
                        })
                        saveCategoriesToLocal(updatedCategories)
                        console.log("分类已同步到后端:", response.data)
                        return@w object : UTSJSONObject() {
                            var code: Number = 200
                            var message = "分类创建成功"
                            var data = response.data
                        }
                    } else {
                        throw UTSError(response.message || "后端创建失败")
                    }
                }
                 catch (serverError: Throwable) {
                    console.error("后端同步失败，但本地已保存:", serverError)
                    uni_showToast(ShowToastOptions(title = "分类已保存到本地，网络同步失败", icon = "none", duration = 3000))
                    return@w object : UTSJSONObject() {
                        var code: Number = 200
                        var message = "success (local only)"
                        var data = tempCategory
                    }
                }
            }
             catch (localError: Throwable) {
                console.error("本地保存失败:", localError)
                uni_showToast(ShowToastOptions(title = "保存失败，请重试", icon = "none"))
                throw localError
            }
    })
}
val refreshCategoryList = fun(data: {
    var income_expense: String
}): UTSPromise<ApiResponse<UTSArray<Category>>> {
    return wrapUTSPromise(suspend w@{
            try {
                console.log("强制从服务器刷新分类列表...")
                val rawResponse = await(request(object : UTSJSONObject() {
                    var url = "/api/user/category/list"
                    var method = "GET"
                    var params = data
                    var requireAuth = true
                }))
                val transformedData = (rawResponse.Data || utsArrayOf()).map(fun(item: Any){
                    val iconInfo = if (item.Icon || item.icon) {
                        getIconByEmoji(item.Icon || item.icon)
                    } else {
                        undefined
                    }
                    return object : UTSJSONObject() {
                        var id = item.ID
                        var name = item.Name
                        var iconId = iconInfo?.id || 1
                        var icon = item.Icon || item.icon
                        var color = item.Color
                        var incomeExpense = item.IncomeExpense
                        var createTime = item.CreatedAt
                        var updateTime = item.UpdatedAt
                    }
                }
                )
                val response: UTSJSONObject = object : UTSJSONObject() {
                    var code: Number = 200
                    var message = rawResponse.Msg || "success"
                    var data = transformedData
                }
                console.log("转换后的响应格式:", response)
                if (response["code"] === 200) {
                    val newCategories = response["data"] || utsArrayOf()
                    val existingCategories = getCategoriesFromLocal()
                    val otherTypeCategories = existingCategories.filter(fun(cat): Boolean {
                        return cat.incomeExpense !== data.income_expense
                    }
                    )
                    val allCategories = otherTypeCategories.concat(newCategories)
                    saveCategoriesToLocal(allCategories)
                    console.log("分类数据已强制刷新并保存到本地存储，总计:", allCategories.length, "个分类")
                }
                return@w response
            }
             catch (error: Throwable) {
                console.error("强制刷新分类列表失败:", error)
                throw error
            }
    })
}
val sendAIMessage = fun(message: String, sessionId: String?): UTSPromise<Any> {
    return request(object : UTSJSONObject() {
        var url = "/api/user/ai/chat"
        var method = "POST"
        var data = if (sessionId) {
            UTSJSONObject(Map<String, Any?>(utsArrayOf(
                utsArrayOf(
                    "message",
                    message
                ),
                utsArrayOf(
                    "sessionId",
                    sessionId
                )
            )))
        } else {
            UTSJSONObject(Map<String, Any?>(utsArrayOf(
                utsArrayOf(
                    "message",
                    message
                )
            )))
        }
        var requireAuth = true
    })
}
val getAIChatHistory = fun(offset: Number = 0, limit: Number = 20): UTSPromise<Any> {
    return request(object : UTSJSONObject() {
        var url = "/api/user/ai/chat/history"
        var method = "GET"
        var params = UTSJSONObject(Map<String, Any?>(utsArrayOf(
            utsArrayOf(
                "offset",
                offset
            ),
            utsArrayOf(
                "limit",
                limit
            )
        )))
        var requireAuth = true
    })
}
val getAIChatSessionDetail = fun(sessionId: String): UTSPromise<Any> {
    return request(object : UTSJSONObject() {
        var url = "/api/user/ai/chat/session"
        var method = "GET"
        var params = UTSJSONObject(Map<String, Any?>(utsArrayOf(
            utsArrayOf(
                "sessionId",
                sessionId
            )
        )))
        var requireAuth = true
    })
}
val GenPagesLoginLoginClass = CreateVueComponent(GenPagesLoginLogin::class.java, fun(): VueComponentOptions {
    return VueComponentOptions(type = "page", name = "", inheritAttrs = GenPagesLoginLogin.inheritAttrs, inject = GenPagesLoginLogin.inject, props = GenPagesLoginLogin.props, propsNeedCastKeys = GenPagesLoginLogin.propsNeedCastKeys, emits = GenPagesLoginLogin.emits, components = GenPagesLoginLogin.components, styles = GenPagesLoginLogin.styles, setup = fun(props: ComponentPublicInstance): Any? {
        return GenPagesLoginLogin.setup(props as GenPagesLoginLogin)
    }
    )
}
, fun(instance, renderer): GenPagesLoginLogin {
    return GenPagesLoginLogin(instance, renderer)
}
)
interface Feature {
    var name: String
    var desc: String
    var icon: String
    var path: String
}
val GenPagesIndexIndexClass = CreateVueComponent(GenPagesIndexIndex::class.java, fun(): VueComponentOptions {
    return VueComponentOptions(type = "page", name = "", inheritAttrs = GenPagesIndexIndex.inheritAttrs, inject = GenPagesIndexIndex.inject, props = GenPagesIndexIndex.props, propsNeedCastKeys = GenPagesIndexIndex.propsNeedCastKeys, emits = GenPagesIndexIndex.emits, components = GenPagesIndexIndex.components, styles = GenPagesIndexIndex.styles, setup = fun(props: ComponentPublicInstance, ctx: SetupContext): Any? {
        return GenPagesIndexIndex.setup(props as GenPagesIndexIndex, ctx)
    }
    )
}
, fun(instance, renderer): GenPagesIndexIndex {
    return GenPagesIndexIndex(instance, renderer)
}
)
interface PageState {
    var currentType: String?
    var incomeExpense: String?
    var pagePath: String?
}
var pageState = PageState(currentType = null, incomeExpense = null, pagePath = null)
val setPageType = fun(type: String): Unit {
    pageState.currentType = type
    pageState.incomeExpense = if (type === "支出") {
        "expense"
    } else {
        "income"
    }
    console.log("页面类型已设置:", type, "income_expense:", pageState.incomeExpense)
}
val getCurrentType = fun(): String? {
    return pageState.currentType
}
val getCurrentIncomeExpense = fun(): String? {
    return pageState.incomeExpense
}
val setPagePath = fun(path: String): Unit {
    pageState.pagePath = path
    console.log("页面路径已设置:", path)
}
val getPagePath = fun(): String? {
    return pageState.pagePath
}
val resetPageState = fun(): Unit {
    pageState = object : UTSJSONObject() {
        var currentType = null
        var incomeExpense = null
        var pagePath = null
    }
    console.log("页面状态已重置")
}
val hasValidPageType = fun(): Boolean {
    return pageState.currentType != null && pageState.incomeExpense != null
}
interface Props {
    var visible: Boolean
}
val GenComponentsAddCategoryModalClass = CreateVueComponent(GenComponentsAddCategoryModal::class.java, fun(): VueComponentOptions {
    return VueComponentOptions(type = "component", name = "", inheritAttrs = GenComponentsAddCategoryModal.inheritAttrs, inject = GenComponentsAddCategoryModal.inject, props = GenComponentsAddCategoryModal.props, propsNeedCastKeys = GenComponentsAddCategoryModal.propsNeedCastKeys, emits = GenComponentsAddCategoryModal.emits, components = GenComponentsAddCategoryModal.components, styles = GenComponentsAddCategoryModal.styles, setup = fun(props: ComponentPublicInstance): Any? {
        return GenComponentsAddCategoryModal.setup(props as GenComponentsAddCategoryModal)
    }
    )
}
, fun(instance, renderer): GenComponentsAddCategoryModal {
    return GenComponentsAddCategoryModal(instance)
}
)
val GenPagesTransactionTransactionClass = CreateVueComponent(GenPagesTransactionTransaction::class.java, fun(): VueComponentOptions {
    return VueComponentOptions(type = "page", name = "", inheritAttrs = GenPagesTransactionTransaction.inheritAttrs, inject = GenPagesTransactionTransaction.inject, props = GenPagesTransactionTransaction.props, propsNeedCastKeys = GenPagesTransactionTransaction.propsNeedCastKeys, emits = GenPagesTransactionTransaction.emits, components = GenPagesTransactionTransaction.components, styles = GenPagesTransactionTransaction.styles, setup = fun(props: ComponentPublicInstance): Any? {
        return GenPagesTransactionTransaction.setup(props as GenPagesTransactionTransaction)
    }
    )
}
, fun(instance, renderer): GenPagesTransactionTransaction {
    return GenPagesTransactionTransaction(instance, renderer)
}
)
val GenPagesPetPetClass = CreateVueComponent(GenPagesPetPet::class.java, fun(): VueComponentOptions {
    return VueComponentOptions(type = "page", name = "", inheritAttrs = GenPagesPetPet.inheritAttrs, inject = GenPagesPetPet.inject, props = GenPagesPetPet.props, propsNeedCastKeys = GenPagesPetPet.propsNeedCastKeys, emits = GenPagesPetPet.emits, components = GenPagesPetPet.components, styles = GenPagesPetPet.styles, setup = fun(props: ComponentPublicInstance): Any? {
        return GenPagesPetPet.setup(props as GenPagesPetPet)
    }
    )
}
, fun(instance, renderer): GenPagesPetPet {
    return GenPagesPetPet(instance, renderer)
}
)
interface ChatMessage {
    var role: String
    var text: String
    var loading: Boolean?
}
interface ChatSessionPreview {
    var sessionId: String
    var firstQuestion: String
    var createdAt: String
}
val GenPagesAIchatAIchatClass = CreateVueComponent(GenPagesAIchatAIchat::class.java, fun(): VueComponentOptions {
    return VueComponentOptions(type = "page", name = "", inheritAttrs = GenPagesAIchatAIchat.inheritAttrs, inject = GenPagesAIchatAIchat.inject, props = GenPagesAIchatAIchat.props, propsNeedCastKeys = GenPagesAIchatAIchat.propsNeedCastKeys, emits = GenPagesAIchatAIchat.emits, components = GenPagesAIchatAIchat.components, styles = GenPagesAIchatAIchat.styles, setup = fun(props: ComponentPublicInstance): Any? {
        return GenPagesAIchatAIchat.setup(props as GenPagesAIchatAIchat)
    }
    )
}
, fun(instance, renderer): GenPagesAIchatAIchat {
    return GenPagesAIchatAIchat(instance, renderer)
}
)
fun getCompleteStatistics(type: Any): UTSPromise<Any> {
    return wrapUTSPromise(suspend w@{
            val _getTimeRange = getTimeRange(type)
            val startTime = _getTimeRange.startTime
            val endTime = _getTimeRange.endTime
            console.log("获取统计数据，类型:", type, "时间范围:", UTSJSONObject(Map<String, Any?>(utsArrayOf(
                utsArrayOf(
                    "startTime",
                    startTime
                ),
                utsArrayOf(
                    "endTime",
                    endTime
                )
            ))))
            testConversion()
            try {
                console.log("开始并行获取数据...")
                val _ref = await(UTSPromise.all(utsArrayOf(
                    request(object : UTSJSONObject() {
                        var url = "/api/user/transaction/statistic/" + type
                        var method = "POST"
                        var data = object : UTSJSONObject() {
                            var StartTime = Date(startTime + "T00:00:00Z").toISOString()
                            var EndTime = Date(endTime + "T23:59:59Z").toISOString()
                        }
                        var requireAuth = true
                    }),
                    request(object : UTSJSONObject() {
                        var url = "/api/user/transaction/statistic/category_rank"
                        var method = "POST"
                        var data = object : UTSJSONObject() {
                            var StartTime = Date(startTime + "T00:00:00Z").toISOString()
                            var EndTime = Date(endTime + "T23:59:59Z").toISOString()
                            var IncomeExpense = "income"
                            var Limit: Number = 10
                        }
                        var requireAuth = true
                    }),
                    request(object : UTSJSONObject() {
                        var url = "/api/user/transaction/statistic/category_rank"
                        var method = "POST"
                        var data = object : UTSJSONObject() {
                            var StartTime = Date(startTime + "T00:00:00Z").toISOString()
                            var EndTime = Date(endTime + "T23:59:59Z").toISOString()
                            var IncomeExpense = "expense"
                            var Limit: Number = 10
                        }
                        var requireAuth = true
                    }),
                    getDetailStatistics(type, startTime, endTime)
                )))
                val baseStats = _ref[0]
                val incomeCategoryStats = _ref[1]
                val expenseCategoryStats = _ref[2]
                val detailStats = _ref[3]
                val baseStatsConverted = convertAmountToYuan(baseStats?.Data || baseStats?.data || baseStats)
                val incomeCategoryStatsConverted = convertCategoryStatsToYuan(incomeCategoryStats?.Data || incomeCategoryStats?.data || incomeCategoryStats)
                val expenseCategoryStatsConverted = convertCategoryStatsToYuan(expenseCategoryStats?.Data || expenseCategoryStats?.data || expenseCategoryStats)
                val detailStatsConverted = convertDetailStatsToYuan(detailStats, type)
                val completeStats: Any = UTSJSONObject.assign(object : UTSJSONObject() {
                    var base_statistics = baseStatsConverted
                    var income_category_stats = incomeCategoryStatsConverted
                    var expense_category_stats = expenseCategoryStatsConverted
                }, detailStatsConverted)
                return@w completeStats
            }
             catch (error: Throwable) {
                console.error("获取统计数据失败:", error)
                return@w UTSJSONObject.assign(object : UTSJSONObject() {
                    var base_statistics = null
                    var income_category_stats = utsArrayOf()
                    var expense_category_stats = utsArrayOf()
                }, getEmptyDetailStats(type))
            }
    })
}
fun getDetailStatistics(type: Any, startTime: Any, endTime: Any): UTSPromise<Any> {
    return wrapUTSPromise(suspend w@{
            try {
                when (type) {
                    "week" -> 
                        {
                            val _ref = await(UTSPromise.all(utsArrayOf(
                                request(object : UTSJSONObject() {
                                    var url = "/api/user/transaction/statistic/day"
                                    var method = "POST"
                                    var data = object : UTSJSONObject() {
                                        var StartTime = Date(startTime + "T00:00:00Z").toISOString()
                                        var EndTime = Date(endTime + "T23:59:59Z").toISOString()
                                        var IncomeExpense = "income"
                                    }
                                    var requireAuth = true
                                }),
                                request(object : UTSJSONObject() {
                                    var url = "/api/user/transaction/statistic/day"
                                    var method = "POST"
                                    var data = object : UTSJSONObject() {
                                        var StartTime = Date(startTime + "T00:00:00Z").toISOString()
                                        var EndTime = Date(endTime + "T23:59:59Z").toISOString()
                                        var IncomeExpense = "expense"
                                    }
                                    var requireAuth = true
                                })
                            )))
                            val incomeDailyStats = _ref[0]
                            val expenseDailyStats = _ref[1]
                            return@w object : UTSJSONObject() {
                                var income_daily_stats = incomeDailyStats?.Data || incomeDailyStats?.data || incomeDailyStats
                                var expense_daily_stats = expenseDailyStats?.Data || expenseDailyStats?.data || expenseDailyStats
                            }
                        }
                    "month" -> 
                        {
                            val weeklyStats = await(request(object : UTSJSONObject() {
                                var url = "/api/user/transaction/statistic/week"
                                var method = "POST"
                                var data = object : UTSJSONObject() {
                                    var StartTime = Date(startTime + "T00:00:00Z").toISOString()
                                    var EndTime = Date(endTime + "T23:59:59Z").toISOString()
                                }
                                var requireAuth = true
                            }))
                            return@w object : UTSJSONObject() {
                                var weekly_stats = weeklyStats?.Data || weeklyStats?.data || weeklyStats
                            }
                        }
                    "year" -> 
                        {
                            val monthlyStats = await(request(object : UTSJSONObject() {
                                var url = "/api/user/transaction/statistic/month"
                                var method = "POST"
                                var data = object : UTSJSONObject() {
                                    var StartTime = Date(startTime + "T00:00:00Z").toISOString()
                                    var EndTime = Date(endTime + "T23:59:59Z").toISOString()
                                }
                                var requireAuth = true
                            }))
                            return@w object : UTSJSONObject() {
                                var monthly_stats = monthlyStats?.Data || monthlyStats?.data || monthlyStats
                            }
                        }
                    else -> 
                        return@w UTSJSONObject()
                }
            }
             catch (error: Throwable) {
                console.error("\u83B7\u53D6" + type + "\u7EC6\u7C92\u5EA6\u6570\u636E\u5931\u8D25:", error)
                return@w getEmptyDetailStats(type)
            }
    })
}
fun getEmptyDetailStats(type: Any): Any {
    when (type) {
        "week" -> 
            return object : UTSJSONObject() {
                var income_daily_stats = utsArrayOf()
                var expense_daily_stats = utsArrayOf()
            }
        "month" -> 
            return object : UTSJSONObject() {
                var weekly_stats = utsArrayOf()
            }
        "year" -> 
            return object : UTSJSONObject() {
                var monthly_stats = utsArrayOf()
            }
        else -> 
            return UTSJSONObject()
    }
}
fun getAIReport(data: Any): UTSPromise<Any> {
    return wrapUTSPromise(suspend w@{
            val completeStats = await(getCompleteStatistics(data.type))
            return@w request(object : UTSJSONObject() {
                var url = "/api/user/report/ai"
                var method = "POST"
                var data = object : UTSJSONObject() {
                    var type = data.type
                    var stats = completeStats
                }
                var requireAuth = true
            })
    })
}
fun getTotalStatistics(): UTSPromise<Any> {
    return request(object : UTSJSONObject() {
        var url = "/api/user/transaction/statistic/total"
        var method = "GET"
        var requireAuth = true
    })
}
fun getTimeRange(type: Any): Any {
    val now = Date()
    var startTime: Any
    var endTime: Any = Date(now)
    when (type) {
        "week" -> 
            {
                val day = now.getDay()
                val diff = now.getDate() - day + (if (day === 0) {
                    -6
                } else {
                    1
                }
                )
                startTime = Date(now.getFullYear(), now.getMonth(), diff)
            }
        "month" -> 
            startTime = Date(now.getFullYear(), now.getMonth(), 1)
        "year" -> 
            startTime = Date(now.getFullYear(), 0, 1)
        else -> 
            startTime = Date(now.getFullYear(), now.getMonth(), 1)
    }
    return object : UTSJSONObject() {
        var startTime = startTime.toISOString()
        var endTime = endTime.toISOString()
    }
}
fun convertAmountToYuan(data: Any): Any {
    if (!data) {
        return data
    }
    if (UTSArray.isArray(data)) {
        return (data as UTSArray<Any>).map(convertAmountToYuan)
    }
    if (UTSAndroid.`typeof`(data) === "object") {
        val result: Any = UTSJSONObject()
        for(key in resolveUTSKeyIterator(data)){
            if (UTSAndroid.`typeof`(data[key]) === "number" && key.toLowerCase().includes("amount")) {
                result[key] = Math.round(data[key]) / 100
            } else {
                result[key] = convertAmountToYuan(data[key])
            }
        }
        return result
    }
    return data
}
fun convertCategoryStatsToYuan(data: Any): Any {
    if (!data) {
        return data
    }
    if (UTSArray.isArray(data)) {
        return (data as UTSArray<Any>).map(convertCategoryStatsToYuan)
    }
    if (UTSAndroid.`typeof`(data) === "object") {
        val result: Any = UTSJSONObject()
        for(key in resolveUTSKeyIterator(data)){
            if (key === "Amount" && UTSAndroid.`typeof`(data[key]) === "number") {
                result[key] = Math.round(data[key]) / 100
            } else {
                result[key] = convertCategoryStatsToYuan(data[key])
            }
        }
        return result
    }
    return data
}
fun convertDetailStatsToYuan(data: Any, type: Any): Any {
    if (!data) {
        return data
    }
    if (type === "week") {
        return object : UTSJSONObject() {
            var income_daily_stats = convertAmountToYuan(data.income_daily_stats)
            var expense_daily_stats = convertAmountToYuan(data.expense_daily_stats)
        }
    } else if (type === "month") {
        return object : UTSJSONObject() {
            var weekly_stats = convertAmountToYuan(data.weekly_stats)
        }
    } else if (type === "year") {
        return object : UTSJSONObject() {
            var monthly_stats = convertAmountToYuan(data.monthly_stats)
        }
    }
    return data
}
fun testConversion(): Unit {}
val GenPagesReportReportClass = CreateVueComponent(GenPagesReportReport::class.java, fun(): VueComponentOptions {
    return VueComponentOptions(type = "page", name = "", inheritAttrs = GenPagesReportReport.inheritAttrs, inject = GenPagesReportReport.inject, props = GenPagesReportReport.props, propsNeedCastKeys = GenPagesReportReport.propsNeedCastKeys, emits = GenPagesReportReport.emits, components = GenPagesReportReport.components, styles = GenPagesReportReport.styles, setup = fun(props: ComponentPublicInstance): Any? {
        return GenPagesReportReport.setup(props as GenPagesReportReport)
    }
    )
}
, fun(instance, renderer): GenPagesReportReport {
    return GenPagesReportReport(instance, renderer)
}
)
fun getTransactionListForCategory(data: {
    var type: String
    var income_expense: String
}): UTSPromise<Any> {
    val _getTimeRange = getTimeRange1(data.type)
    val startTime = _getTimeRange.startTime
    val endTime = _getTimeRange.endTime
    val requestData: UTSJSONObject = object : UTSJSONObject() {
        var StartTime = startTime
        var EndTime = endTime
        var IncomeExpense = data.income_expense
        var Offset: Number = 0
        var Limit: Number = 1000
    }
    console.log("交易列表请求参数:", UTSJSONObject(Map<String, Any?>(utsArrayOf(
        utsArrayOf(
            "type",
            data.type
        ),
        utsArrayOf(
            "income_expense",
            data.income_expense
        ),
        utsArrayOf(
            "startTime",
            startTime
        ),
        utsArrayOf(
            "endTime",
            endTime
        ),
        utsArrayOf(
            "requestData",
            requestData
        )
    ))))
    return request(object : UTSJSONObject() {
        var url = "/api/user/transaction/list"
        var method = "POST"
        var data = requestData
        var requireAuth = true
    })
}
fun getTimeRange1(type: String): {
    var startTime: String
    var endTime: String
} {
    val now = Date()
    var startTime: Date
    var endTime: Date = Date(now)
    when (type) {
        "week" -> 
            {
                val day = now.getDay()
                val diff = now.getDate() - day + (if (day === 0) {
                    -6
                } else {
                    1
                }
                )
                startTime = Date(now.getFullYear(), now.getMonth(), diff)
            }
        "month" -> 
            startTime = Date(now.getFullYear(), now.getMonth(), 1)
        "year" -> 
            startTime = Date(now.getFullYear(), 0, 1)
        else -> 
            startTime = Date(now.getFullYear(), now.getMonth(), 1)
    }
    console.log("时间范围计算:", UTSJSONObject(Map<String, Any?>(utsArrayOf(
        utsArrayOf(
            "type",
            type
        ),
        utsArrayOf(
            "now",
            now.toISOString()
        ),
        utsArrayOf(
            "startTime",
            startTime.toISOString()
        ),
        utsArrayOf(
            "endTime",
            endTime.toISOString()
        ),
        utsArrayOf(
            "startTimeDate",
            startTime
        ),
        utsArrayOf(
            "endTimeDate",
            endTime
        )
    ))))
    return object : UTSJSONObject() {
        var startTime = startTime.toISOString()
        var endTime = endTime.toISOString()
    }
}
val __sfc__ = defineComponent(defineComponent(let {
    object : UTSJSONObject() {
        var name = "SimpleChart"
        var props = object : UTSJSONObject() {
            var type = object : UTSJSONObject() {
                var type = String as PropType<String>
                var `default` = "line"
                var validator = fun(value: String): Boolean {
                    return utsArrayOf(
                        "line",
                        "bar",
                        "pie",
                        "area"
                    ).indexOf(value) !== -1
                }
            }
            var data = object : UTSJSONObject() {
                var type = UTSArray as PropType<UTSArray<Any>>
                var `default` = fun(): UTSArray<Any> {
                    return utsArrayOf()
                }
            }
            var categories = object : UTSJSONObject() {
                var type = UTSArray as PropType<UTSArray<String>>
                var `default` = fun(): UTSArray<String> {
                    return utsArrayOf()
                }
            }
            var series = object : UTSJSONObject() {
                var type = UTSArray as PropType<UTSArray<Any>>
                var `default` = fun(): UTSArray<Any> {
                    return utsArrayOf()
                }
            }
            var width = object : UTSJSONObject() {
                var type = Number
                var `default`: Number = 300
            }
            var height = object : UTSJSONObject() {
                var type = Number
                var `default`: Number = 200
            }
            var colors = object : UTSJSONObject() {
                var type = UTSArray as PropType<UTSArray<String>>
                var `default` = fun(): UTSArray<String> {
                    return utsArrayOf(
                        "#007AFF",
                        "#34C759",
                        "#FF9500",
                        "#FF3B30",
                        "#AF52DE"
                    )
                }
            }
            var showLegend = object : UTSJSONObject() {
                var type = Boolean
                var `default` = true
            }
            var showGrid = object : UTSJSONObject() {
                var type = Boolean
                var `default` = true
            }
        }
        var data = fun(): {
            var chartId: String
            var loading: Boolean
            var error: String?
            var ctx: Any
            var chartData: Any
        } {
            return object : UTSJSONObject() {
                var chartId = ""
                var loading = false
                var error = null
                var ctx = null
                var chartData = null
            }
        }
        var mounted = fun(): Unit {
            it.chartId = "chart_" + Math.random().toString(36).substring(2, 9)
            it.`$nextTick`(fun(){
                it.initChart()
            }
            )
        }
        var methods = let {
            object : UTSJSONObject() {
                var initChart = fun(): UTSPromise<Unit> {
                    return wrapUTSPromise(suspend {
                            try {
                                it.loading = true
                                it.error = null
                                it.ctx = uni_createCanvasContext(it.chartId, it)
                                it.processData()
                                await(it.drawChart())
                                it.loading = false
                            }
                             catch (err: Throwable) {
                                it.error = if (err && err.message) {
                                    err.message
                                } else {
                                    "图表加载失败"
                                }
                                it.loading = false
                            }
                    })
                }
                var processData = fun(): Unit {
                    if (it.data && it.data.length > 0) {
                        it.chartData = it.data
                    } else if (it.series && it.series.length > 0) {
                        it.chartData = let {
                            object : UTSJSONObject() {
                                var categories = it.categories
                                var series = it.series
                            }
                        }
                    } else {
                        throw UTSError("请提供有效的图表数据")
                    }
                }
                var drawChart = fun(): UTSPromise<Unit> {
                    return wrapUTSPromise(suspend w@{
                            if (!it.ctx || !it.chartData) {
                                return@w
                            }
                            val ctx: Any = it.ctx
                            val width: Number = it.width
                            val height: Number = it.height
                            ctx.clearRect(0, 0, width, height)
                            ctx.setFillStyle("#FFFFFF")
                            ctx.fillRect(0, 0, width, height)
                            when (it.type) {
                                "line" -> 
                                    it.drawLineChart(ctx, width, height)
                                "bar" -> 
                                    it.drawBarChart(ctx, width, height)
                                "pie" -> 
                                    it.drawPieChart(ctx, width, height)
                                "area" -> 
                                    it.drawAreaChart(ctx, width, height)
                            }
                            if (it.showLegend) {
                                it.drawLegend(ctx, width, height)
                            }
                            ctx.draw()
                    })
                }
                var drawLineChart = fun(ctx: Any, width: Number, height: Number): Unit {
                    val padding: Number = 40
                    val chartWidth: Number = width - padding * 2
                    val chartHeight: Number = height - padding * 2
                    if (!it.chartData.series || it.chartData.series.length === 0) {
                        return
                    }
                    val series: Any = it.chartData.series[0]
                    val data: UTSArray<Number> = series.data || utsArrayOf()
                    val categories: UTSArray<String> = it.chartData.categories || utsArrayOf()
                    if (data.length === 0) {
                        return
                    }
                    val minValue: Number = Math.min(*data.toTypedArray())
                    val maxValue: Number = Math.max(*data.toTypedArray())
                    val valueRange: Number = maxValue - minValue || 1
                    if (it.showGrid) {
                        it.drawGrid(ctx, padding, chartWidth, chartHeight, categories.length, minValue, maxValue)
                    }
                    ctx.setStrokeStyle(it.colors[0])
                    ctx.setLineWidth(2)
                    ctx.beginPath()
                    run {
                        var i: Number = 0
                        while(i < data.length){
                            val value: Number = data[i]
                            val x: Number = padding + (i / (data.length - 1)) * chartWidth
                            val y: Number = padding + chartHeight - ((value - minValue) / valueRange) * chartHeight
                            if (i === 0) {
                                ctx.moveTo(x, y)
                            } else {
                                ctx.lineTo(x, y)
                            }
                            i++
                        }
                    }
                    ctx.stroke()
                    ctx.setFillStyle(it.colors[0])
                    run {
                        var i: Number = 0
                        while(i < data.length){
                            val value: Number = data[i]
                            val x: Number = padding + (i / (data.length - 1)) * chartWidth
                            val y: Number = padding + chartHeight - ((value - minValue) / valueRange) * chartHeight
                            ctx.beginPath()
                            ctx.arc(x, y, 4, 0, 2 * Math.PI)
                            ctx.fill()
                            i++
                        }
                    }
                }
                var drawBarChart = fun(ctx: Any, width: Number, height: Number): Unit {
                    val padding: Number = 40
                    val chartWidth: Number = width - padding * 2
                    val chartHeight: Number = height - padding * 2
                    if (!it.chartData.series || it.chartData.series.length === 0) {
                        return
                    }
                    val series: Any = it.chartData.series[0]
                    val data: UTSArray<Number> = series.data || utsArrayOf()
                    val categories: UTSArray<String> = it.chartData.categories || utsArrayOf()
                    if (data.length === 0) {
                        return
                    }
                    val minValue: Number = Math.min(*data.toTypedArray())
                    val maxValue: Number = Math.max(*data.toTypedArray())
                    val valueRange: Number = maxValue - minValue || 1
                    if (it.showGrid) {
                        it.drawGrid(ctx, padding, chartWidth, chartHeight, categories.length, minValue, maxValue)
                    }
                    val barWidth: Number = chartWidth / data.length * 0.8
                    val barSpacing: Number = chartWidth / data.length * 0.2
                    run {
                        var i: Number = 0
                        while(i < data.length){
                            val value: Number = data[i]
                            val barHeight: Number = ((value - minValue) / valueRange) * chartHeight
                            val x: Number = padding + i * (barWidth + barSpacing) + barSpacing / 2
                            val y: Number = padding + chartHeight - barHeight
                            ctx.setFillStyle(it.colors[i % it.colors.length])
                            ctx.fillRect(x, y, barWidth, barHeight)
                            i++
                        }
                    }
                }
                var drawPieChart = fun(ctx: Any, width: Number, height: Number): Unit {
                    val centerX: Number = width / 2
                    val centerY: Number = height / 2
                    val radius: Number = Math.min(width, height) / 2 - 40
                    if (!it.chartData.series || it.chartData.series.length === 0) {
                        return
                    }
                    val series: Any = it.chartData.series[0]
                    val data: UTSArray<Number> = series.data || utsArrayOf()
                    if (data.length === 0) {
                        return
                    }
                    val total: Number = data.reduce(fun(sum: Number, value: Number): Number {
                        return sum + value
                    }
                    , 0)
                    var currentAngle: Number = 0
                    run {
                        var i: Number = 0
                        while(i < data.length){
                            val value: Number = data[i]
                            val sliceAngle: Number = (value / total) * 2 * Math.PI
                            ctx.setFillStyle(it.colors[i % it.colors.length])
                            ctx.beginPath()
                            ctx.moveTo(centerX, centerY)
                            ctx.arc(centerX, centerY, radius, currentAngle, currentAngle + sliceAngle)
                            ctx.closePath()
                            ctx.fill()
                            currentAngle += sliceAngle
                            i++
                        }
                    }
                }
                var drawAreaChart = fun(ctx: Any, width: Number, height: Number): Unit {
                    val padding: Number = 40
                    val chartWidth: Number = width - padding * 2
                    val chartHeight: Number = height - padding * 2
                    if (!it.chartData.series || it.chartData.series.length === 0) {
                        return
                    }
                    val series: Any = it.chartData.series[0]
                    val data: UTSArray<Number> = series.data || utsArrayOf()
                    val categories: UTSArray<String> = it.chartData.categories || utsArrayOf()
                    if (data.length === 0) {
                        return
                    }
                    val minValue: Number = Math.min(*data.toTypedArray())
                    val maxValue: Number = Math.max(*data.toTypedArray())
                    val valueRange: Number = maxValue - minValue || 1
                    if (it.showGrid) {
                        it.drawGrid(ctx, padding, chartWidth, chartHeight, categories.length, minValue, maxValue)
                    }
                    ctx.setFillStyle(it.colors[0] + "40")
                    ctx.beginPath()
                    run {
                        var i: Number = 0
                        while(i < data.length){
                            val value: Number = data[i]
                            val x: Number = padding + (i / (data.length - 1)) * chartWidth
                            val y: Number = padding + chartHeight - ((value - minValue) / valueRange) * chartHeight
                            if (i === 0) {
                                ctx.moveTo(x, y)
                            } else {
                                ctx.lineTo(x, y)
                            }
                            i++
                        }
                    }
                    ctx.lineTo(padding + chartWidth, padding + chartHeight)
                    ctx.lineTo(padding, padding + chartHeight)
                    ctx.closePath()
                    ctx.fill()
                    ctx.setStrokeStyle(it.colors[0])
                    ctx.setLineWidth(2)
                    ctx.beginPath()
                    run {
                        var i: Number = 0
                        while(i < data.length){
                            val value: Number = data[i]
                            val x: Number = padding + (i / (data.length - 1)) * chartWidth
                            val y: Number = padding + chartHeight - ((value - minValue) / valueRange) * chartHeight
                            if (i === 0) {
                                ctx.moveTo(x, y)
                            } else {
                                ctx.lineTo(x, y)
                            }
                            i++
                        }
                    }
                    ctx.stroke()
                }
                var drawLegend = fun(ctx: Any, width: Number, height: Number): Unit {
                    if (!it.chartData.series || it.chartData.series.length === 0) {
                        return
                    }
                    val legendY: Number = height - 30
                    val itemWidth: Number = 80
                    val itemHeight: Number = 20
                    run {
                        var i: Number = 0
                        while(i < it.chartData.series.length){
                            val series: Any = it.chartData.series[i]
                            val x: Number = 10 + i * itemWidth
                            ctx.setFillStyle(it.colors[i % it.colors.length])
                            ctx.fillRect(x, legendY, 15, itemHeight)
                            ctx.setFillStyle("#333333")
                            ctx.setFontSize(12)
                            ctx.fillText(series.name || "\u7CFB\u5217" + (i + 1), x + 20, legendY + 15)
                            i++
                        }
                    }
                }
                var drawGrid = fun(ctx: Any, padding: Number, chartWidth: Number, chartHeight: Number, count: Number, minValue: Number, maxValue: Number): Unit {
                    ctx.setStrokeStyle("#E0E0E0")
                    ctx.setLineWidth(1)
                    run {
                        var i: Number = 0
                        while(i <= count){
                            val x: Number = padding + (i / count) * chartWidth
                            ctx.beginPath()
                            ctx.moveTo(x, padding)
                            ctx.lineTo(x, padding + chartHeight)
                            ctx.stroke()
                            i++
                        }
                    }
                    val gridLines: Number = 5
                    run {
                        var i: Number = 0
                        while(i <= gridLines){
                            val y: Number = padding + (i / gridLines) * chartHeight
                            ctx.beginPath()
                            ctx.moveTo(padding, y)
                            ctx.lineTo(padding + chartWidth, y)
                            ctx.stroke()
                            i++
                        }
                    }
                }
                var onError = fun(e: Any): Unit {
                    it.error = if (e && e.message) {
                        e.message
                    } else {
                        "Canvas错误"
                    }
                }
                var retry = fun(): Unit {
                    it.initChart()
                }
                var onTouchStart = fun(e: Any): Unit {}
                var onTouchMove = fun(e: Any): Unit {}
                var onTouchEnd = fun(e: Any): Unit {}
            }
        }
    }
}))
val GenPagesAnalyzeAnalyzeClass = CreateVueComponent(GenPagesAnalyzeAnalyze::class.java, fun(): VueComponentOptions {
    return VueComponentOptions(type = "page", name = "", inheritAttrs = GenPagesAnalyzeAnalyze.inheritAttrs, inject = GenPagesAnalyzeAnalyze.inject, props = GenPagesAnalyzeAnalyze.props, propsNeedCastKeys = GenPagesAnalyzeAnalyze.propsNeedCastKeys, emits = GenPagesAnalyzeAnalyze.emits, components = GenPagesAnalyzeAnalyze.components, styles = GenPagesAnalyzeAnalyze.styles, setup = fun(props: ComponentPublicInstance): Any? {
        return GenPagesAnalyzeAnalyze.setup(props as GenPagesAnalyzeAnalyze)
    }
    )
}
, fun(instance, renderer): GenPagesAnalyzeAnalyze {
    return GenPagesAnalyzeAnalyze(instance, renderer)
}
)
val GenPagesSocialSocialClass = CreateVueComponent(GenPagesSocialSocial::class.java, fun(): VueComponentOptions {
    return VueComponentOptions(type = "page", name = "", inheritAttrs = GenPagesSocialSocial.inheritAttrs, inject = GenPagesSocialSocial.inject, props = GenPagesSocialSocial.props, propsNeedCastKeys = GenPagesSocialSocial.propsNeedCastKeys, emits = GenPagesSocialSocial.emits, components = GenPagesSocialSocial.components, styles = GenPagesSocialSocial.styles, setup = fun(props: ComponentPublicInstance): Any? {
        return GenPagesSocialSocial.setup(props as GenPagesSocialSocial)
    }
    )
}
, fun(instance, renderer): GenPagesSocialSocial {
    return GenPagesSocialSocial(instance, renderer)
}
)
val GenPagesSettingsSettingsClass = CreateVueComponent(GenPagesSettingsSettings::class.java, fun(): VueComponentOptions {
    return VueComponentOptions(type = "page", name = "", inheritAttrs = GenPagesSettingsSettings.inheritAttrs, inject = GenPagesSettingsSettings.inject, props = GenPagesSettingsSettings.props, propsNeedCastKeys = GenPagesSettingsSettings.propsNeedCastKeys, emits = GenPagesSettingsSettings.emits, components = GenPagesSettingsSettings.components, styles = GenPagesSettingsSettings.styles, setup = fun(props: ComponentPublicInstance): Any? {
        return GenPagesSettingsSettings.setup(props as GenPagesSettingsSettings)
    }
    )
}
, fun(instance, renderer): GenPagesSettingsSettings {
    return GenPagesSettingsSettings(instance, renderer)
}
)
val GenPagesRegisterRegisterClass = CreateVueComponent(GenPagesRegisterRegister::class.java, fun(): VueComponentOptions {
    return VueComponentOptions(type = "page", name = "", inheritAttrs = GenPagesRegisterRegister.inheritAttrs, inject = GenPagesRegisterRegister.inject, props = GenPagesRegisterRegister.props, propsNeedCastKeys = GenPagesRegisterRegister.propsNeedCastKeys, emits = GenPagesRegisterRegister.emits, components = GenPagesRegisterRegister.components, styles = GenPagesRegisterRegister.styles, setup = fun(props: ComponentPublicInstance): Any? {
        return GenPagesRegisterRegister.setup(props as GenPagesRegisterRegister)
    }
    )
}
, fun(instance, renderer): GenPagesRegisterRegister {
    return GenPagesRegisterRegister(instance, renderer)
}
)
val GenPagesForgotPasswordForgotPasswordClass = CreateVueComponent(GenPagesForgotPasswordForgotPassword::class.java, fun(): VueComponentOptions {
    return VueComponentOptions(type = "page", name = "", inheritAttrs = GenPagesForgotPasswordForgotPassword.inheritAttrs, inject = GenPagesForgotPasswordForgotPassword.inject, props = GenPagesForgotPasswordForgotPassword.props, propsNeedCastKeys = GenPagesForgotPasswordForgotPassword.propsNeedCastKeys, emits = GenPagesForgotPasswordForgotPassword.emits, components = GenPagesForgotPasswordForgotPassword.components, styles = GenPagesForgotPasswordForgotPassword.styles, setup = fun(props: ComponentPublicInstance): Any? {
        return GenPagesForgotPasswordForgotPassword.setup(props as GenPagesForgotPasswordForgotPassword)
    }
    )
}
, fun(instance, renderer): GenPagesForgotPasswordForgotPassword {
    return GenPagesForgotPasswordForgotPassword(instance, renderer)
}
)
val GenPagesFriendFriendClass = CreateVueComponent(GenPagesFriendFriend::class.java, fun(): VueComponentOptions {
    return VueComponentOptions(type = "page", name = "", inheritAttrs = GenPagesFriendFriend.inheritAttrs, inject = GenPagesFriendFriend.inject, props = GenPagesFriendFriend.props, propsNeedCastKeys = GenPagesFriendFriend.propsNeedCastKeys, emits = GenPagesFriendFriend.emits, components = GenPagesFriendFriend.components, styles = GenPagesFriendFriend.styles, setup = fun(props: ComponentPublicInstance): Any? {
        return GenPagesFriendFriend.setup(props as GenPagesFriendFriend)
    }
    )
}
, fun(instance, renderer): GenPagesFriendFriend {
    return GenPagesFriendFriend(instance, renderer)
}
)
val GenPagesCategoryDemoCategoryDemoClass = CreateVueComponent(GenPagesCategoryDemoCategoryDemo::class.java, fun(): VueComponentOptions {
    return VueComponentOptions(type = "page", name = "", inheritAttrs = GenPagesCategoryDemoCategoryDemo.inheritAttrs, inject = GenPagesCategoryDemoCategoryDemo.inject, props = GenPagesCategoryDemoCategoryDemo.props, propsNeedCastKeys = GenPagesCategoryDemoCategoryDemo.propsNeedCastKeys, emits = GenPagesCategoryDemoCategoryDemo.emits, components = GenPagesCategoryDemoCategoryDemo.components, styles = GenPagesCategoryDemoCategoryDemo.styles, setup = fun(props: ComponentPublicInstance): Any? {
        return GenPagesCategoryDemoCategoryDemo.setup(props as GenPagesCategoryDemoCategoryDemo)
    }
    )
}
, fun(instance, renderer): GenPagesCategoryDemoCategoryDemo {
    return GenPagesCategoryDemoCategoryDemo(instance, renderer)
}
)
fun createApp(): UTSJSONObject {
    val app = createSSRApp(GenAppClass)
    return UTSJSONObject(Map<String, Any?>(utsArrayOf(
        utsArrayOf(
            "app",
            app
        )
    )))
}
interface Transaction {
    var amount: Number
    var category: String
    var date: String
}
val addTransaction = fun(data: Transaction){
    return uni_request<Any>(RequestOptions(url = "/api/transaction", method = "POST", data = data))
}
fun main(app: IApp) {
    definePageRoutes()
    defineAppConfig()
    (createApp()["app"] as VueApp).mount(app, GenUniApp())
}
open class UniAppConfig : io.dcloud.uniapp.appframe.AppConfig {
    override var name: String = "AI Financial Management"
    override var appid: String = "__UNI__5A7011F"
    override var versionName: String = "1.0.0"
    override var versionCode: String = "100"
    override var uniCompilerVersion: String = "4.64"
    constructor() : super() {}
}
fun definePageRoutes() {
    __uniRoutes.push(UniPageRoute(path = "pages/login/login", component = GenPagesLoginLoginClass, meta = UniPageMeta(isQuit = true), style = utsMapOf("navigationBarTitleText" to "")))
    __uniRoutes.push(UniPageRoute(path = "pages/index/index", component = GenPagesIndexIndexClass, meta = UniPageMeta(isQuit = false), style = utsMapOf("navigationBarTitleText" to "uni-app x")))
    __uniRoutes.push(UniPageRoute(path = "pages/transaction/transaction", component = GenPagesTransactionTransactionClass, meta = UniPageMeta(isQuit = false), style = utsMapOf("navigationBarTitleText" to "")))
    __uniRoutes.push(UniPageRoute(path = "pages/pet/pet", component = GenPagesPetPetClass, meta = UniPageMeta(isQuit = false), style = utsMapOf("navigationBarTitleText" to "")))
    __uniRoutes.push(UniPageRoute(path = "pages/AIchat/AIchat", component = GenPagesAIchatAIchatClass, meta = UniPageMeta(isQuit = false), style = utsMapOf("navigationBarTitleText" to "")))
    __uniRoutes.push(UniPageRoute(path = "pages/report/report", component = GenPagesReportReportClass, meta = UniPageMeta(isQuit = false), style = utsMapOf("navigationBarTitleText" to "")))
    __uniRoutes.push(UniPageRoute(path = "pages/analyze/analyze", component = GenPagesAnalyzeAnalyzeClass, meta = UniPageMeta(isQuit = false), style = utsMapOf("navigationBarTitleText" to "", "js" to utsArrayOf(
        "/echarts.min.js"
    ))))
    __uniRoutes.push(UniPageRoute(path = "pages/social/social", component = GenPagesSocialSocialClass, meta = UniPageMeta(isQuit = false), style = utsMapOf("navigationBarTitleText" to "")))
    __uniRoutes.push(UniPageRoute(path = "pages/settings/settings", component = GenPagesSettingsSettingsClass, meta = UniPageMeta(isQuit = false), style = utsMapOf("navigationBarTitleText" to "")))
    __uniRoutes.push(UniPageRoute(path = "pages/register/register", component = GenPagesRegisterRegisterClass, meta = UniPageMeta(isQuit = false), style = utsMapOf("navigationBarTitleText" to "")))
    __uniRoutes.push(UniPageRoute(path = "pages/forgot-password/forgot-password", component = GenPagesForgotPasswordForgotPasswordClass, meta = UniPageMeta(isQuit = false), style = utsMapOf("navigationBarTitleText" to "")))
    __uniRoutes.push(UniPageRoute(path = "pages/friend/friend", component = GenPagesFriendFriendClass, meta = UniPageMeta(isQuit = false), style = utsMapOf("navigationBarTitleText" to "")))
    __uniRoutes.push(UniPageRoute(path = "pages/category-demo/category-demo", component = GenPagesCategoryDemoCategoryDemoClass, meta = UniPageMeta(isQuit = false), style = utsMapOf("navigationBarTitleText" to "分类管理演示")))
}
val __uniLaunchPage: Map<String, Any?> = utsMapOf("url" to "pages/login/login", "style" to utsMapOf("navigationBarTitleText" to ""))
fun defineAppConfig() {
    __uniConfig.entryPagePath = "/pages/login/login"
    __uniConfig.globalStyle = utsMapOf("navigationBarTextStyle" to "black", "navigationBarTitleText" to "uni-app x", "navigationBarBackgroundColor" to "#F8F8F8", "backgroundColor" to "#F8F8F8")
    __uniConfig.getTabBarConfig = fun(): Map<String, Any>? {
        return null
    }
    __uniConfig.tabBar = __uniConfig.getTabBarConfig()
    __uniConfig.conditionUrl = ""
    __uniConfig.uniIdRouter = utsMapOf()
    __uniConfig.ready = true
}
open class GenUniApp : UniAppImpl() {
    open val vm: GenApp?
        get() {
            return getAppVm() as GenApp?
        }
    open val `$vm`: GenApp?
        get() {
            return getAppVm() as GenApp?
        }
}
fun getApp(): GenUniApp {
    return getUniApp() as GenUniApp
}
