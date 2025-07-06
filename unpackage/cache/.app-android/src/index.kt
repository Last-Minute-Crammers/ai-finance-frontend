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
import io.dcloud.uniapp.extapi.connectSocket as uni_connectSocket
import io.dcloud.uniapp.extapi.getStorageSync as uni_getStorageSync
import io.dcloud.uniapp.extapi.request as uni_request
import io.dcloud.uniapp.extapi.setStorageSync as uni_setStorageSync
import io.dcloud.uniapp.extapi.showToast as uni_showToast
val runBlock1 = run {
    __uniConfig.getAppStyles = fun(): Map<String, Map<String, Map<String, Any>>> {
        return GenApp.styles
    }
}
fun initRuntimeSocket(hosts: String, port: String, id: String): UTSPromise<SocketTask?> {
    if (hosts == "" || port == "" || id == "") {
        return UTSPromise.resolve(null)
    }
    return hosts.split(",").reduce<UTSPromise<SocketTask?>>(fun(promise: UTSPromise<SocketTask?>, host: String): UTSPromise<SocketTask?> {
        return promise.then(fun(socket): UTSPromise<SocketTask?> {
            if (socket != null) {
                return UTSPromise.resolve(socket)
            }
            return tryConnectSocket(host, port, id)
        }
        )
    }
    , UTSPromise.resolve(null))
}
val SOCKET_TIMEOUT: Number = 500
fun tryConnectSocket(host: String, port: String, id: String): UTSPromise<SocketTask?> {
    return UTSPromise(fun(resolve, reject){
        val socket = uni_connectSocket(ConnectSocketOptions(url = "ws://" + host + ":" + port + "/" + id, fail = fun(_) {
            resolve(null)
        }
        ))
        val timer = setTimeout(fun(){
            socket.close(CloseSocketOptions(code = 1006, reason = "connect timeout"))
            resolve(null)
        }
        , SOCKET_TIMEOUT)
        socket.onOpen(fun(e){
            clearTimeout(timer)
            resolve(socket)
        }
        )
        socket.onClose(fun(e){
            clearTimeout(timer)
            resolve(null)
        }
        )
        socket.onError(fun(e){
            clearTimeout(timer)
            resolve(null)
        }
        )
    }
    )
}
fun initRuntimeSocketService(): UTSPromise<Boolean> {
    val hosts: String = "192.168.239.1,192.168.150.1,10.132.231.65,127.0.0.1"
    val port: String = "8090"
    val id: String = "app-android_vnHDNy"
    if (hosts == "" || port == "" || id == "") {
        return UTSPromise.resolve(false)
    }
    var socketTask: SocketTask? = null
    __registerWebViewUniConsole(fun(): String {
        return "!function(){\"use strict\";\"function\"==typeof SuppressedError&&SuppressedError;var e=[\"log\",\"warn\",\"error\",\"info\",\"debug\"],n=e.reduce((function(e,n){return e[n]=console[n].bind(console),e}),{}),t=null,r=new Set,o={};function i(e){if(null!=t){var n=e.map((function(e){if(\"string\"==typeof e)return e;var n=e&&\"promise\"in e&&\"reason\"in e,t=n?\"UnhandledPromiseRejection: \":\"\";if(n&&(e=e.reason),e instanceof Error&&e.stack)return e.message&&!e.stack.includes(e.message)?\"\".concat(t).concat(e.message,\"\\n\").concat(e.stack):\"\".concat(t).concat(e.stack);if(\"object\"==typeof e&&null!==e)try{return t+JSON.stringify(e)}catch(e){return t+String(e)}return t+String(e)})).filter(Boolean);n.length>0&&t(JSON.stringify(Object.assign({type:\"error\",data:n},o)))}else e.forEach((function(e){r.add(e)}))}function a(e,n){try{return{type:e,args:u(n)}}catch(e){}return{type:e,args:[]}}function u(e){return e.map((function(e){return c(e)}))}function c(e,n){if(void 0===n&&(n=0),n>=7)return{type:\"object\",value:\"[Maximum depth reached]\"};switch(typeof e){case\"string\":return{type:\"string\",value:e};case\"number\":return function(e){return{type:\"number\",value:String(e)}}(e);case\"boolean\":return function(e){return{type:\"boolean\",value:String(e)}}(e);case\"object\":try{return function(e,n){if(null===e)return{type:\"null\"};if(function(e){return e.\$&&s(e.\$)}(e))return function(e,n){return{type:\"object\",className:\"ComponentPublicInstance\",value:{properties:Object.entries(e.\$.type).map((function(e){return f(e[0],e[1],n+1)}))}}}(e,n);if(s(e))return function(e,n){return{type:\"object\",className:\"ComponentInternalInstance\",value:{properties:Object.entries(e.type).map((function(e){return f(e[0],e[1],n+1)}))}}}(e,n);if(function(e){return e.style&&null!=e.tagName&&null!=e.nodeName}(e))return function(e,n){return{type:\"object\",value:{properties:Object.entries(e).filter((function(e){var n=e[0];return[\"id\",\"tagName\",\"nodeName\",\"dataset\",\"offsetTop\",\"offsetLeft\",\"style\"].includes(n)})).map((function(e){return f(e[0],e[1],n+1)}))}}}(e,n);if(function(e){return\"function\"==typeof e.getPropertyValue&&\"function\"==typeof e.setProperty&&e.\$styles}(e))return function(e,n){return{type:\"object\",value:{properties:Object.entries(e.\$styles).map((function(e){return f(e[0],e[1],n+1)}))}}}(e,n);if(Array.isArray(e))return{type:\"object\",subType:\"array\",value:{properties:e.map((function(e,t){return function(e,n,t){var r=c(e,t);return r.name=\"\".concat(n),r}(e,t,n+1)}))}};if(e instanceof Set)return{type:\"object\",subType:\"set\",className:\"Set\",description:\"Set(\".concat(e.size,\")\"),value:{entries:Array.from(e).map((function(e){return function(e,n){return{value:c(e,n)}}(e,n+1)}))}};if(e instanceof Map)return{type:\"object\",subType:\"map\",className:\"Map\",description:\"Map(\".concat(e.size,\")\"),value:{entries:Array.from(e.entries()).map((function(e){return function(e,n){return{key:c(e[0],n),value:c(e[1],n)}}(e,n+1)}))}};if(e instanceof Promise)return{type:\"object\",subType:\"promise\",value:{properties:[]}};if(e instanceof RegExp)return{type:\"object\",subType:\"regexp\",value:String(e),className:\"Regexp\"};if(e instanceof Date)return{type:\"object\",subType:\"date\",value:String(e),className:\"Date\"};if(e instanceof Error)return{type:\"object\",subType:\"error\",value:e.message||String(e),className:e.name||\"Error\"};var t=void 0,r=e.constructor;r&&r.get\$UTSMetadata\$&&(t=r.get\$UTSMetadata\$().name);var o=Object.entries(e);(function(e){return e.modifier&&e.modifier._attribute&&e.nodeContent})(e)&&(o=o.filter((function(e){var n=e[0];return\"modifier\"!==n&&\"nodeContent\"!==n})));return{type:\"object\",className:t,value:{properties:o.map((function(e){return f(e[0],e[1],n+1)}))}}}(e,n)}catch(e){return{type:\"object\",value:{properties:[]}}}case\"undefined\":return{type:\"undefined\"};case\"function\":return function(e){return{type:\"function\",value:\"function \".concat(e.name,\"() {}\")}}(e);case\"symbol\":return function(e){return{type:\"symbol\",value:e.description}}(e);case\"bigint\":return function(e){return{type:\"bigint\",value:String(e)}}(e)}}function s(e){return e.type&&null!=e.uid&&e.appContext}function f(e,n,t){var r=c(n,t);return r.name=e,r}var l=null,p=[],y={},g=\"---BEGIN:EXCEPTION---\",d=\"---END:EXCEPTION---\";function v(e){null!=l?l(JSON.stringify(Object.assign({type:\"console\",data:e},y))):p.push.apply(p,e)}var m=/^\\s*at\\s+[\\w/./-]+:\\d+\$/;function b(){function t(e){return function(){for(var t=[],r=0;r<arguments.length;r++)t[r]=arguments[r];var o=function(e,n,t){if(t||2===arguments.length)for(var r,o=0,i=n.length;o<i;o++)!r&&o in n||(r||(r=Array.prototype.slice.call(n,0,o)),r[o]=n[o]);return e.concat(r||Array.prototype.slice.call(n))}([],t,!0);if(o.length){var u=o[o.length-1];\"string\"==typeof u&&m.test(u)&&o.pop()}if(n[e].apply(n,o),\"error\"===e&&1===t.length){var c=t[0];if(\"string\"==typeof c&&c.startsWith(g)){var s=g.length,f=c.length-d.length;return void i([c.slice(s,f)])}if(c instanceof Error)return void i([c])}v([a(e,t)])}}return function(){var e=console.log,n=Symbol();try{console.log=n}catch(e){return!1}var t=console.log===n;return console.log=e,t}()?(e.forEach((function(e){console[e]=t(e)})),function(){e.forEach((function(e){console[e]=n[e]}))}):function(){}}function _(e){var n={type:\"WEB_INVOKE_APPSERVICE\",args:{data:{name:\"console\",arg:e}}};return window.__uniapp_x_postMessageToService?window.__uniapp_x_postMessageToService(n):window.__uniapp_x_.postMessageToService(JSON.stringify(n))}!function(){if(!window.__UNI_CONSOLE_WEBVIEW__){window.__UNI_CONSOLE_WEBVIEW__=!0;var e=\"[web-view]\".concat(window.__UNI_PAGE_ROUTE__?\"[\".concat(window.__UNI_PAGE_ROUTE__,\"]\"):\"\");b(),function(e,n){if(void 0===n&&(n={}),l=e,Object.assign(y,n),null!=e&&p.length>0){var t=p.slice();p.length=0,v(t)}}((function(e){_(e)}),{channel:e}),function(e,n){if(void 0===n&&(n={}),t=e,Object.assign(o,n),null!=e&&r.size>0){var a=Array.from(r);r.clear(),i(a)}}((function(e){_(e)}),{channel:e}),window.addEventListener(\"error\",(function(e){i([e.error])})),window.addEventListener(\"unhandledrejection\",(function(e){i([e])}))}}()}();"
    }
    , fun(data: String){
        socketTask?.send(SendSocketMessageOptions(data = data))
    }
    )
    return UTSPromise.resolve().then(fun(): UTSPromise<Boolean> {
        return initRuntimeSocket(hosts, port, id).then(fun(socket): Boolean {
            if (socket == null) {
                return false
            }
            socketTask = socket
            return true
        }
        )
    }
    ).`catch`(fun(): Boolean {
        return false
    }
    )
}
val runBlock2 = run {
    initRuntimeSocketService()
}
open class GenApp : BaseApp {
    constructor(__ins: ComponentInternalInstance) : super(__ins) {
        onLaunch(fun(_: OnLaunchOptions) {
            console.log("App 启动", " at App.uvue:10")
        }
        , __ins)
        onAppShow(fun(_: OnShowOptions) {
            console.log("App 显示", " at App.uvue:13")
        }
        , __ins)
        onAppHide(fun() {
            console.log("App 隐藏", " at App.uvue:16")
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
val BACKEND_URLS: UTSJSONObject = object : UTSJSONObject(UTSSourceMapPosition("BACKEND_URLS", "utils/request.uts", 3, 7)) {
    var production = "http://47.109.194.39/api"
    var local = "http://localhost:8080"
    var dockerHost = "http://host.docker.internal:8080"
    var ip = "http://127.0.0.1:8080"
    var external = "http://192.168.1.100:8080"
}
val DEFAULT_URL = BACKEND_URLS["production"]
val getBackendUrl = fun(): String {
    val configuredUrl = uni_getStorageSync("backend_url")
    return configuredUrl || DEFAULT_URL
}
val BASE_URL = getBackendUrl()
interface RequestOptions {
    var url: String
    var method: String?
    var data: Any?
    var params: Any?
    var requireAuth: Boolean?
    var timeout: Number?
    var retries: Number?
    var headers: Any?
}
val getToken = fun(): String {
    val token = uni_getStorageSync("token") || ""
    console.log("Retrieved token:", if (token) {
        "" + token.substring(0, 20) + "..."
    } else {
        "No token found"
    }
    , " at utils/request.ts:45")
    return token
}
val uni: Any
val setBackendUrl = fun(url: String): Unit {
    uni_setStorageSync("backend_url", url)
    console.log("Backend URL set to:", url, " at utils/request.ts:55")
}
val setBackendEnvironment = fun(envKey: String): String? {
    if (BACKEND_URLS[envKey]) {
        val url = BACKEND_URLS[envKey]
        setBackendUrl(url)
        return url
    }
    return null
}
val getAvailableBackendUrls = fun(): Any {
    return BACKEND_URLS
}
val checkBackendConnection = fun(customUrl: String?): UTSPromise<Any> {
    return wrapUTSPromise(suspend w@{
            val targetUrl = customUrl || BASE_URL
            try {
                console.log("检查后端连接状态...", targetUrl, " at utils/request.ts:96")
                val startTime = Date.now()
                val requestPromise = UTSPromise(fun(resolve, reject){
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
                val timeoutPromise = UTSPromise(fun(_, reject){
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
                val endTime = Date.now()
                if (!response || response.statusCode === undefined) {
                    throw UTSError("无效的响应格式")
                }
                console.log("后端连接检查结果:", response, "\u54CD\u5E94\u65F6\u95F4: " + (endTime - startTime) + "ms", " at utils/request.ts:131")
                return@w object : UTSJSONObject() {
                    var connected = response.statusCode === 200
                    var statusCode = response.statusCode
                    var responseTime = endTime - startTime
                    var serverInfo = response.data || UTSJSONObject()
                }
            }
             catch (error: Throwable) {
                console.error("后端连接检查失败:", error, " at utils/request.ts:140")
                val errMsg = if (UTSAndroid.`typeof`(error) === "object" && error != null && resolveInOperator(error, "message")) {
                    String((error as Any).message || (error as Any).errMsg)
                } else {
                    "未知错误"
                }
                val isConnectionRefused = UTSAndroid.`typeof`(errMsg) === "string" && errMsg.indexOf("CONNECTION_REFUSED") !== -1
                val isTimeout = UTSAndroid.`typeof`(errMsg) === "string" && (errMsg.indexOf("timeout") !== -1 || errMsg.indexOf("超时") !== -1)
                val diagnostics: UTSJSONObject = UTSJSONObject(Map<String, Any?>(utsArrayOf(
                    utsArrayOf(
                        "__\$originalPosition",
                        UTSSourceMapPosition("diagnostics", "utils/request.uts", 130, 15)
                    ),
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
                        utsArrayOf<String>()
                    )
                )))
                if (isConnectionRefused) {
                    diagnostics["possibleCauses"] = utsArrayOf(
                        "后端服务器未启动",
                        "端口8080可能被其他应用占用",
                        "检查防火墙设置是否允许连接"
                    )
                } else if (isTimeout) {
                    diagnostics["possibleCauses"] = utsArrayOf(
                        "Docker容器端口映射不正确 - 检查docker-compose.yml",
                        "Docker网络配置问题 - 尝试使用127.0.0.1而不是localhost",
                        "防火墙阻止了连接 - 检查防火墙设置",
                        "后端服务响应过慢 - 检查服务器负载",
                        "后端服务未正确监听端口 - 检查后端日志"
                    )
                } else {
                    diagnostics["possibleCauses"] = utsArrayOf(
                        "后端服务未正确启动",
                        "API端点路径可能不正确",
                        "请求处理过程中出现错误",
                        "网络连接问题"
                    )
                }
                console.log("连接诊断:", diagnostics, " at utils/request.ts:184")
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
            val token = getToken()
            val headers: Any = UTSJSONObject.assign(UTSJSONObject(), (options.headers || UTSJSONObject()), object : UTSJSONObject() {
                var `Content-Type` = "application/json"
            })
            if (options.requireAuth !== false && token) {
                headers["Authorization"] = "Bearer " + token
            }
            var url = BASE_URL + options.url
            if (options.method === "GET" && options.params) {
                val params = options.params
                val queryArr: UTSArray<String> = utsArrayOf()
                for(key in resolveUTSKeyIterator(params)){
                    if (params[key] !== undefined && params[key] != null) {
                        queryArr.push(UTSAndroid.consoleDebugError(encodeURIComponent(key), " at utils/request.uts:293") + "=" + UTSAndroid.consoleDebugError(encodeURIComponent(String(params[key])), " at utils/request.uts:293"))
                    }
                }
                val queryString = queryArr.join("&")
                if (queryString) {
                    url += (if (url.indexOf("?") !== -1) {
                        "&"
                    } else {
                        "?"
                    }
                    ) + queryString
                }
            }
            return@w UTSPromise<T>(fun(resolve, reject){
                uni_request<Any>(RequestOptions(url = url, method = options.method || "GET", data = options.data, header = headers, timeout = options.timeout || 30000, success = fun(res: Any){
                    if (!res) {
                        reject(UTSError("未收到响应数据"))
                        return
                    }
                    if (!res.statusCode || res.statusCode !== 200) {
                        reject(UTSError("\u8BF7\u6C42\u5931\u8D25 (" + res.statusCode + ")"))
                        return
                    }
                    resolve(res.data as T)
                }
                , fail = fun(err: Any){
                    reject(err || UTSError("请求失败"))
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
                ))), " at common/api/user.ts:20")
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
                console.log("Login response received:", response, " at common/api/user.ts:28")
                console.log("Response type:", UTSAndroid.`typeof`(response), " at common/api/user.ts:29")
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
                        console.log("Token saved successfully:", token.substring(0, 20) + "...", " at common/api/user.ts:64")
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
                        console.error("登录成功但未找到token，完整响应:", JSON.stringify(response, null, 2), " at common/api/user.ts:83")
                        throw UTSError("登录成功但服务器未返回访问令牌")
                    }
                } else {
                    val errorMessage = response.Msg || response.message || response.error || response.msg || "登录失败"
                    console.error("登录失败:", errorMessage, response, " at common/api/user.ts:89")
                    throw UTSError(errorMessage)
                }
            }
             catch (error: Throwable) {
                console.error("Login error details:", error, " at common/api/user.ts:94")
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
    console.log("createTransaction called with data:", data, " at common/api/transaction.ts:58")
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
val saveCategoriesToLocal = fun(categories: UTSArray<Category>){
    try {
        uni_setStorageSync(CATEGORY_STORAGE_KEY, JSON.stringify(categories))
        console.log("分类数据已保存到本地存储:", categories.length, "个分类", " at common/api/category.ts:58")
    }
     catch (error: Throwable) {
        console.error("保存分类到本地存储失败:", error, " at common/api/category.ts:60")
    }
}
val getCategoriesFromLocal = fun(): UTSArray<Category> {
    try {
        val data = uni_getStorageSync(CATEGORY_STORAGE_KEY)
        if (data) {
            val categories = UTSAndroid.consoleDebugError(JSON.parse(data), " at common/api/category.uts:61")
            console.log("从本地存储获取分类数据:", categories.length, "个分类", " at common/api/category.ts:69")
            return categories
        }
    }
     catch (error: Throwable) {
        console.error("从本地存储获取分类失败:", error, " at common/api/category.ts:73")
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
                val localCategories = getCategoriesFromLocal()
                val filteredCategories = filterCategoriesByType(localCategories, data.income_expense)
                if (filteredCategories.length > 0) {
                    console.log("使用本地分类数据:", filteredCategories.length, "个分类", " at common/api/category.ts:106")
                    return@w object : UTSJSONObject() {
                        var code: Number = 200
                        var message = "success"
                        var data = filteredCategories
                    }
                }
                console.log("本地无数据，从服务器获取分类列表...", " at common/api/category.ts:115")
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
                val response: UTSJSONObject = object : UTSJSONObject(UTSSourceMapPosition("response", "common/api/category.uts", 126, 15)) {
                    var code: Number = 200
                    var message = rawResponse.Msg || "success"
                    var data = transformedData
                }
                console.log("转换后的响应格式:", response, " at common/api/category.ts:146")
                if (response["code"] === 200) {
                    val newCategories = response["data"] || utsArrayOf()
                    if (newCategories.length > 0) {
                        val existingCategories = getCategoriesFromLocal()
                        val existingIds = Set(existingCategories.map(fun(cat): Number {
                            return cat.id
                        }))
                        val uniqueNewCategories = newCategories.filter(fun(cat){
                            return !existingIds.has(cat.id)
                        })
                        val allCategories = existingCategories.concat(uniqueNewCategories)
                        saveCategoriesToLocal(allCategories)
                        console.log("分类数据已同步到本地存储，总计:", allCategories.length, "个分类", " at common/api/category.ts:164")
                    } else {
                        console.log("服务器返回空分类列表，无需更新本地存储", " at common/api/category.ts:166")
                    }
                }
                return@w response
            }
             catch (error: Throwable) {
                console.error("获取分类列表失败:", error, " at common/api/category.ts:172")
                val localCategories = getCategoriesFromLocal()
                val filteredCategories = filterCategoriesByType(localCategories, data.income_expense)
                if (filteredCategories.length > 0) {
                    console.log("网络请求失败，使用本地缓存数据:", filteredCategories.length, "个分类", " at common/api/category.ts:179")
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
            val requestData: UTSJSONObject = object : UTSJSONObject(UTSSourceMapPosition("requestData", "common/api/category.uts", 180, 11)) {
                var name = data.name
                var icon = iconInfo?.emoji || data.icon || "📁"
                var color = data.color
                var income_expense = data.incomeExpense
            }
            console.log("发送创建分类请求:", requestData, " at common/api/category.ts:211")
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
                console.error("无法获取当前页面类型，请确保在记账页面中", " at common/api/category.ts:235")
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
                console.log("分类已保存到本地:", tempCategory, " at common/api/category.ts:263")
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
                        console.log("分类已同步到后端:", response.data, " at common/api/category.ts:280")
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
                    console.error("后端同步失败，但本地已保存:", serverError, " at common/api/category.ts:291")
                    uni_showToast(ShowToastOptions(title = "分类已保存到本地，网络同步失败", icon = "none", duration = 3000))
                    return@w object : UTSJSONObject() {
                        var code: Number = 200
                        var message = "success (local only)"
                        var data = tempCategory
                    }
                }
            }
             catch (localError: Throwable) {
                console.error("本地保存失败:", localError, " at common/api/category.ts:307")
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
                console.log("强制从服务器刷新分类列表...", " at common/api/category.ts:349")
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
                val response: UTSJSONObject = object : UTSJSONObject(UTSSourceMapPosition("response", "common/api/category.uts", 330, 15)) {
                    var code: Number = 200
                    var message = rawResponse.Msg || "success"
                    var data = transformedData
                }
                console.log("转换后的响应格式:", response, " at common/api/category.ts:380")
                if (response["code"] === 200) {
                    val newCategories = response["data"] || utsArrayOf()
                    val existingCategories = getCategoriesFromLocal()
                    val otherTypeCategories = existingCategories.filter(fun(cat): Boolean {
                        return cat.incomeExpense !== data.income_expense
                    }
                    )
                    val allCategories = otherTypeCategories.concat(newCategories)
                    saveCategoriesToLocal(allCategories)
                    console.log("分类数据已强制刷新并保存到本地存储，总计:", allCategories.length, "个分类", " at common/api/category.ts:397")
                }
                return@w response
            }
             catch (error: Throwable) {
                console.error("强制刷新分类列表失败:", error, " at common/api/category.ts:402")
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
val setPageType = fun(type: String){
    pageState.currentType = type
    pageState.incomeExpense = if (type === "支出") {
        "expense"
    } else {
        "income"
    }
    console.log("页面类型已设置:", type, "income_expense:", pageState.incomeExpense, " at utils/pageState.ts:19")
}
val getCurrentType = fun(): String? {
    return pageState.currentType
}
val getCurrentIncomeExpense = fun(): String? {
    return pageState.incomeExpense
}
val setPagePath = fun(path: String){
    pageState.pagePath = path
    console.log("页面路径已设置:", path, " at utils/pageState.ts:35")
}
val getPagePath = fun(): String? {
    return pageState.pagePath
}
val resetPageState = fun(){
    pageState = object : UTSJSONObject() {
        var currentType = null
        var incomeExpense = null
        var pagePath = null
    }
    console.log("页面状态已重置", " at utils/pageState.ts:55")
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
            ))), " at common/api/report.ts:7")
            testConversion()
            try {
                console.log("开始并行获取数据...", " at common/api/report.ts:14")
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
                console.error("获取统计数据失败:", error, " at common/api/report.ts:78")
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
                console.error("\u83B7\u53D6" + type + "\u7EC6\u7C92\u5EA6\u6570\u636E\u5931\u8D25:", error, " at common/api/report.ts:159")
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
    val requestData: UTSJSONObject = object : UTSJSONObject(UTSSourceMapPosition("requestData", "common/api/analyze.uts", 81, 11)) {
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
    ))), " at common/api/analyze.ts:102")
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
    ))), " at common/api/analyze.ts:143")
    return object : UTSJSONObject() {
        var startTime = startTime.toISOString()
        var endTime = endTime.toISOString()
    }
}
val GenComponentsSimpleChartClass = CreateVueComponent(GenComponentsSimpleChart::class.java, fun(): VueComponentOptions {
    return VueComponentOptions(type = "component", name = GenComponentsSimpleChart.name, inheritAttrs = GenComponentsSimpleChart.inheritAttrs, inject = GenComponentsSimpleChart.inject, props = GenComponentsSimpleChart.props, propsNeedCastKeys = GenComponentsSimpleChart.propsNeedCastKeys, emits = GenComponentsSimpleChart.emits, components = GenComponentsSimpleChart.components, styles = GenComponentsSimpleChart.styles)
}
, fun(instance, renderer): GenComponentsSimpleChart {
    return GenComponentsSimpleChart(instance)
}
)
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
