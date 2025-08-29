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
import io.dcloud.uniapp.extapi.navigateBack as uni_navigateBack
import io.dcloud.uniapp.extapi.showToast as uni_showToast
open class GenPagesTransactionTransaction : BasePage {
    constructor(__ins: ComponentInternalInstance, __renderer: String?) : super(__ins, __renderer) {}
    companion object {
        @Suppress("UNUSED_PARAMETER", "UNUSED_VARIABLE")
        var setup: (__props: GenPagesTransactionTransaction) -> Any? = fun(__props): Any? {
            val __ins = getCurrentInstance()!!
            val _ctx = __ins.proxy as GenPagesTransactionTransaction
            val _cache = __ins.renderCache
            val type = ref("支出")
            val amount = ref("")
            val remark = ref("")
            val year = ref("")
            val month = ref("")
            val day = ref("")
            val hour = ref("")
            val minute = ref("")
            val loading = ref(false)
            val categoryLoading = ref(false)
            val categories = ref(utsArrayOf<Category>())
            val selectedCategoryId = ref<Number?>(null)
            val showAddCategoryModal = ref(false)
            val fetchCategoryList = fun(forceRefresh: Boolean = false): UTSPromise<Unit> {
                return wrapUTSPromise(suspend {
                        try {
                            categoryLoading.value = true
                            val incomeExpenseType = if (type.value === "支出") {
                                "expense"
                            } else {
                                "income"
                            }
                            var response
                            if (forceRefresh) {
                                response = await(refreshCategoryList(object : UTSJSONObject() {
                                    var income_expense = incomeExpenseType
                                }))
                            } else {
                                response = await(getCategoryList(object : UTSJSONObject() {
                                    var income_expense = incomeExpenseType
                                }))
                            }
                            console.log("分类列表响应:", response)
                            console.log("响应数据结构:", object : UTSJSONObject() {
                                var code = response.code
                                var message = response.message
                                var dataType = UTSAndroid.`typeof`(response.data)
                                var dataLength = if (UTSArray.isArray(response.data)) {
                                    response.data.length
                                } else {
                                    "not array"
                                }
                                var data = response.data
                            })
                            if (response.code === 200) {
                                categories.value = response.data || utsArrayOf()
                                selectedCategoryId.value = null
                                if (response.message === "success (cached)") {
                                    console.log("使用本地缓存数据")
                                } else {
                                    console.log("使用服务器数据")
                                }
                                if (categories.value.length === 0) {
                                    console.log("当前类型暂无分类数据")
                                } else {
                                    console.log("成功加载分类数据:", categories.value.length, "个分类")
                                }
                            } else {
                                console.error("API返回错误:", response)
                                uni_showToast(ShowToastOptions(title = response.message || "获取分类列表失败", icon = "none"))
                            }
                        }
                         catch (error: Throwable) {
                            console.error("获取分类列表失败:", error)
                            uni_showToast(ShowToastOptions(title = "网络错误，请检查网络连接", icon = "none"))
                        }
                         finally{
                            categoryLoading.value = false
                        }
                })
            }
            val getCategoryIcon = fun(category: Category): String {
                if (category.iconId) {
                    val iconInfo = getIconById(category.iconId)
                    if (iconInfo) {
                        return iconInfo.emoji
                    }
                }
                if (category.icon) {
                    return category.icon
                }
                return "📁"
            }
            val selectCategory = fun(category: Category){
                selectedCategoryId.value = category.id
            }
            val refreshCategories = fun(): UTSPromise<Unit> {
                return wrapUTSPromise(suspend {
                        try {
                            uni_showToast(ShowToastOptions(title = "刷新分类中...", icon = "loading", duration = 1000))
                            await(fetchCategoryList(true))
                            uni_showToast(ShowToastOptions(title = "分类已更新", icon = "success", duration = 1500))
                        }
                         catch (error: Throwable) {
                            console.error("刷新分类失败:", error)
                            uni_showToast(ShowToastOptions(title = "刷新失败", icon = "none"))
                        }
                })
            }
            val showAddCategory = fun(){
                showAddCategoryModal.value = true
            }
            val onCategoryCreated = fun(newCategory: Category){
                console.log("新分类已创建:", newCategory)
                fetchCategoryList(true)
            }
            val initTimeInput = fun(){
                val now = Date()
                year.value = String(now.getFullYear())
                month.value = String(now.getMonth() + 1).padStart(2, "0")
                day.value = String(now.getDate()).padStart(2, "0")
                hour.value = String(now.getHours()).padStart(2, "0")
                minute.value = String(now.getMinutes()).padStart(2, "0")
            }
            val formatTimeToISO = fun(): String {
                try {
                    val y = parseInt(year.value)
                    val m = parseInt(month.value) - 1
                    val d = parseInt(day.value)
                    val h = parseInt(hour.value)
                    val min = parseInt(minute.value)
                    val date = Date(y, m, d, h, min)
                    if (isNaN(date.getTime())) {
                        throw UTSError("无效的日期")
                    }
                    return date.toISOString()
                }
                 catch (error: Throwable) {
                    return Date().toISOString()
                }
            }
            onMounted(fun(): UTSPromise<Unit> {
                return wrapUTSPromise(suspend {
                        setPagePath("/pages/transaction/transaction")
                        setPageType(type.value)
                        initTimeInput()
                        await(fetchCategoryList())
                })
            }
            )
            onUnmounted(fun(){
                resetPageState()
            }
            )
            watch(type, fun(): UTSPromise<Unit> {
                return wrapUTSPromise(suspend {
                        await(fetchCategoryList())
                })
            }
            )
            fun gen_goBack_fn() {
                uni_navigateBack(null)
            }
            val goBack = ::gen_goBack_fn
            fun gen_changeType_fn(newType: String) {
                type.value = newType
                setPageType(newType as String)
            }
            val changeType = ::gen_changeType_fn
            fun gen_submitTransaction_fn(): UTSPromise<Unit> {
                return wrapUTSPromise(suspend w@{
                        try {
                            loading.value = true
                            if (!amount.value) {
                                uni_showToast(ShowToastOptions(title = "请输入金额", icon = "none"))
                                return@w
                            }
                            val amountNum = parseFloat(amount.value)
                            if (isNaN(amountNum) || amountNum <= 0) {
                                uni_showToast(ShowToastOptions(title = "请输入有效的正数金额", icon = "none"))
                                return@w
                            }
                            if (!selectedCategoryId.value) {
                                uni_showToast(ShowToastOptions(title = "请选择分类", icon = "none"))
                                return@w
                            }
                            if (!year.value || !month.value || !day.value || !hour.value || !minute.value) {
                                uni_showToast(ShowToastOptions(title = "请完整填写时间", icon = "none"))
                                return@w
                            }
                            val formattedTime = formatTimeToISO()
                            val transactionData: UTSJSONObject = object : UTSJSONObject() {
                                var CategoryId = selectedCategoryId.value
                                var IncomeExpense = if (type.value === "支出") {
                                    "expense"
                                } else {
                                    "income"
                                }
                                var Amount = Math.round(amountNum * 100)
                                var Remark = remark.value.trim()
                                var TradeTime = formattedTime
                            }
                            val response = await(createTransaction(transactionData))
                            if (response && (response.Msg === "查询成功" || response.Msg === "success")) {
                                amount.value = ""
                                remark.value = ""
                                selectedCategoryId.value = null
                                initTimeInput()
                                uni_showToast(ShowToastOptions(title = "记录已保存", icon = "success", mask = true, duration = 1500))
                                setTimeout(fun(){
                                    uni_navigateBack(null)
                                }, 1500)
                            } else {
                                throw UTSError(response.Msg || "保存失败")
                            }
                        }
                         catch (error: Throwable) {
                            val errorMessage = if (error is UTSError) {
                                (error as UTSError).message
                            } else {
                                String(error)
                            }
                            uni_showToast(ShowToastOptions(title = "\u4FDD\u5B58\u5931\u8D25: " + errorMessage, icon = "none", duration = 2000))
                        }
                         finally{
                            loading.value = false
                        }
                })
            }
            val submitTransaction = ::gen_submitTransaction_fn
            return fun(): Any? {
                return createElementVNode("view", utsMapOf("class" to "transaction-container"), utsArrayOf(
                    createElementVNode("view", utsMapOf("class" to "header"), utsArrayOf(
                        createElementVNode("text", utsMapOf("class" to "back-btn", "onClick" to goBack), "←"),
                        createElementVNode("text", utsMapOf("class" to "header-title"), "记账"),
                        createElementVNode("text", utsMapOf("class" to "header-sub"), "记录您的每一笔收支")
                    )),
                    createElementVNode("view", utsMapOf("class" to "type-toggle"), utsArrayOf(
                        createElementVNode("view", utsMapOf("class" to normalizeClass(utsArrayOf(
                            "type-btn",
                            utsMapOf("active" to (type.value === "支出"))
                        )), "onClick" to fun(){
                            changeType("支出")
                        }
                        ), "支出", 10, utsArrayOf(
                            "onClick"
                        )),
                        createElementVNode("view", utsMapOf("class" to normalizeClass(utsArrayOf(
                            "type-btn",
                            utsMapOf("active" to (type.value === "收入"))
                        )), "onClick" to fun(){
                            changeType("收入")
                        }
                        ), "收入", 10, utsArrayOf(
                            "onClick"
                        ))
                    )),
                    createElementVNode("view", utsMapOf("class" to "category-card"), utsArrayOf(
                        createElementVNode("view", utsMapOf("class" to "section-header"), utsArrayOf(
                            createElementVNode("text", utsMapOf("class" to "section-label"), "选择分类"),
                            createElementVNode("view", utsMapOf("class" to "header-actions"), utsArrayOf(
                                createElementVNode("text", utsMapOf("class" to "add-btn", "onClick" to showAddCategory), "➕"),
                                createElementVNode("text", utsMapOf("class" to "refresh-btn", "onClick" to refreshCategories), "🔄")
                            ))
                        )),
                        if (isTrue(categoryLoading.value)) {
                            createElementVNode("view", utsMapOf("key" to 0, "class" to "loading-categories"), utsArrayOf(
                                createElementVNode("text", null, "加载分类中...")
                            ))
                        } else {
                            createElementVNode("view", utsMapOf("key" to 1, "class" to "category-grid"), utsArrayOf(
                                createElementVNode(Fragment, null, RenderHelpers.renderList(categories.value, fun(item, index, __index, _cached): Any {
                                    return createElementVNode("view", utsMapOf("key" to item.id, "class" to normalizeClass(utsArrayOf(
                                        "category-item",
                                        utsMapOf("selected" to (selectedCategoryId.value === item.id))
                                    )), "onClick" to fun(){
                                        selectCategory(item)
                                    }
                                    ), utsArrayOf(
                                        createElementVNode("text", utsMapOf("class" to "emoji"), toDisplayString(getCategoryIcon(item)), 1),
                                        createElementVNode("text", utsMapOf("class" to "name"), toDisplayString(item.name), 1)
                                    ), 10, utsArrayOf(
                                        "onClick"
                                    ))
                                }
                                ), 128)
                            ))
                        }
                        ,
                        if (isTrue(!categoryLoading.value && categories.value.length === 0)) {
                            createElementVNode("view", utsMapOf("key" to 2, "class" to "empty-categories"), utsArrayOf(
                                createElementVNode("text", null, "暂无分类，请先添加分类")
                            ))
                        } else {
                            createCommentVNode("v-if", true)
                        }
                    )),
                    createElementVNode("view", utsMapOf("class" to "transaction-details-card"), utsArrayOf(
                        createElementVNode("view", utsMapOf("class" to "input-group"), utsArrayOf(
                            createElementVNode("text", utsMapOf("class" to "input-label"), "金额"),
                            createElementVNode("view", utsMapOf("class" to "amount-input-wrapper"), utsArrayOf(
                                createElementVNode("text", utsMapOf("class" to "currency"), "¥"),
                                createElementVNode("input", utsMapOf("class" to "amount-input", "type" to "digit", "modelValue" to amount.value, "onInput" to fun(`$event`: InputEvent){
                                    amount.value = `$event`.detail.value
                                }
                                , "placeholder" to "0.00", "maxlength" to "10"), null, 40, utsArrayOf(
                                    "modelValue",
                                    "onInput"
                                ))
                            ))
                        )),
                        createElementVNode("view", utsMapOf("class" to "input-group"), utsArrayOf(
                            createElementVNode("text", utsMapOf("class" to "input-label"), "备注"),
                            createElementVNode("input", utsMapOf("class" to "remark-input", "modelValue" to remark.value, "onInput" to fun(`$event`: InputEvent){
                                remark.value = `$event`.detail.value
                            }
                            , "placeholder" to "添加备注（可选）", "maxlength" to "100"), null, 40, utsArrayOf(
                                "modelValue",
                                "onInput"
                            ))
                        )),
                        createElementVNode("view", utsMapOf("class" to "input-group"), utsArrayOf(
                            createElementVNode("text", utsMapOf("class" to "input-label"), "交易时间"),
                            createElementVNode("view", utsMapOf("class" to "time-input-row", "style" to normalizeStyle(utsMapOf("display" to "flex", "flex-direction" to "row", "align-items" to "center", "gap" to "12rpx", "justify-content" to "center"))), utsArrayOf(
                                createElementVNode("input", utsMapOf("class" to "time-input", "modelValue" to year.value, "onInput" to fun(`$event`: InputEvent){
                                    year.value = `$event`.detail.value
                                }
                                , "maxlength" to "4", "placeholder" to "年", "style" to normalizeStyle(utsMapOf("width" to "130rpx", "text-align" to "center"))), null, 44, utsArrayOf(
                                    "modelValue",
                                    "onInput"
                                )),
                                createElementVNode("text", null, "年"),
                                createElementVNode("input", utsMapOf("class" to "time-input", "modelValue" to month.value, "onInput" to fun(`$event`: InputEvent){
                                    month.value = `$event`.detail.value
                                }
                                , "maxlength" to "2", "placeholder" to "月", "style" to normalizeStyle(utsMapOf("width" to "80rpx", "text-align" to "center"))), null, 44, utsArrayOf(
                                    "modelValue",
                                    "onInput"
                                )),
                                createElementVNode("text", null, "月"),
                                createElementVNode("input", utsMapOf("class" to "time-input", "modelValue" to day.value, "onInput" to fun(`$event`: InputEvent){
                                    day.value = `$event`.detail.value
                                }
                                , "maxlength" to "2", "placeholder" to "日", "style" to normalizeStyle(utsMapOf("width" to "80rpx", "text-align" to "center"))), null, 44, utsArrayOf(
                                    "modelValue",
                                    "onInput"
                                )),
                                createElementVNode("text", null, "日"),
                                createElementVNode("input", utsMapOf("class" to "time-input", "modelValue" to hour.value, "onInput" to fun(`$event`: InputEvent){
                                    hour.value = `$event`.detail.value
                                }
                                , "maxlength" to "2", "placeholder" to "时", "style" to normalizeStyle(utsMapOf("width" to "80rpx", "text-align" to "center"))), null, 44, utsArrayOf(
                                    "modelValue",
                                    "onInput"
                                )),
                                createElementVNode("text", null, ":"),
                                createElementVNode("input", utsMapOf("class" to "time-input", "modelValue" to minute.value, "onInput" to fun(`$event`: InputEvent){
                                    minute.value = `$event`.detail.value
                                }
                                , "maxlength" to "2", "placeholder" to "分", "style" to normalizeStyle(utsMapOf("width" to "80rpx", "text-align" to "center"))), null, 44, utsArrayOf(
                                    "modelValue",
                                    "onInput"
                                )),
                                createElementVNode("text", null, "分")
                            ), 4)
                        ))
                    )),
                    createElementVNode("view", utsMapOf("class" to "save-btn-wrapper"), utsArrayOf(
                        createElementVNode("button", utsMapOf("class" to "save-btn", "onClick" to submitTransaction, "disabled" to loading.value), toDisplayString(if (loading.value) {
                            "保存中..."
                        } else {
                            "保存记录"
                        }
                        ), 9, utsArrayOf(
                            "disabled"
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
                return utsMapOf("transaction-container" to padStyleMapOf(utsMapOf("backgroundColor" to "#f5f7fa", "paddingBottom" to "120rpx", "overflowY" to "auto")), "header" to padStyleMapOf(utsMapOf("backgroundImage" to "linear-gradient(to right, #4e54c8, #8f94fb)", "backgroundColor" to "rgba(0,0,0,0)", "paddingTop" to "80rpx", "paddingRight" to "30rpx", "paddingBottom" to "40rpx", "paddingLeft" to "30rpx", "borderBottomLeftRadius" to "30rpx", "borderBottomRightRadius" to "30rpx", "textAlign" to "center", "position" to "relative")), "back-btn" to padStyleMapOf(utsMapOf("position" to "absolute", "left" to "30rpx", "top" to "80rpx", "fontSize" to "36rpx", "color" to "#FFFFFF")), "header-title" to padStyleMapOf(utsMapOf("fontSize" to "36rpx", "fontWeight" to "bold", "color" to "#FFFFFF")), "header-sub" to padStyleMapOf(utsMapOf("fontSize" to "24rpx", "color" to "#FFFFFF", "marginTop" to "10rpx")), "type-toggle" to padStyleMapOf(utsMapOf("display" to "flex", "justifyContent" to "center", "backgroundImage" to "none", "backgroundColor" to "#e0e3f0", "borderTopLeftRadius" to "40rpx", "borderTopRightRadius" to "40rpx", "borderBottomRightRadius" to "40rpx", "borderBottomLeftRadius" to "40rpx", "marginTop" to "30rpx", "marginRight" to "auto", "marginBottom" to "20rpx", "marginLeft" to "auto", "width" to "60%")), "type-btn" to utsMapOf("" to utsMapOf("flex" to 1, "textAlign" to "center", "paddingTop" to "20rpx", "paddingRight" to 0, "paddingBottom" to "20rpx", "paddingLeft" to 0, "fontSize" to "28rpx", "color" to "#555555", "fontWeight" to "bold", "borderTopLeftRadius" to "40rpx", "borderTopRightRadius" to "40rpx", "borderBottomRightRadius" to "40rpx", "borderBottomLeftRadius" to "40rpx"), ".active" to utsMapOf("backgroundImage" to "none", "backgroundColor" to "#FFFFFF", "color" to "#4e54c8", "boxShadow" to "0 2rpx 6rpx rgba(0, 0, 0, 0.1)")), "category-card" to padStyleMapOf(utsMapOf("backgroundImage" to "none", "backgroundColor" to "#ffffff", "marginTop" to 0, "marginRight" to "20rpx", "marginBottom" to "20rpx", "marginLeft" to "20rpx", "paddingTop" to "30rpx", "paddingRight" to "30rpx", "paddingBottom" to "30rpx", "paddingLeft" to "30rpx", "borderTopLeftRadius" to "20rpx", "borderTopRightRadius" to "20rpx", "borderBottomRightRadius" to "20rpx", "borderBottomLeftRadius" to "20rpx", "boxShadow" to "0 4rpx 12rpx rgba(0, 0, 0, 0.04)")), "transaction-details-card" to padStyleMapOf(utsMapOf("backgroundImage" to "none", "backgroundColor" to "#ffffff", "marginTop" to 0, "marginRight" to "20rpx", "marginBottom" to "20rpx", "marginLeft" to "20rpx", "paddingTop" to "30rpx", "paddingRight" to "30rpx", "paddingBottom" to "30rpx", "paddingLeft" to "30rpx", "borderTopLeftRadius" to "20rpx", "borderTopRightRadius" to "20rpx", "borderBottomRightRadius" to "20rpx", "borderBottomLeftRadius" to "20rpx", "boxShadow" to "0 4rpx 12rpx rgba(0, 0, 0, 0.04)")), "input-group" to padStyleMapOf(utsMapOf("marginBottom" to "30rpx", "marginBottom:last-child" to 0)), "input-label" to padStyleMapOf(utsMapOf("fontSize" to "28rpx", "color" to "#333333", "marginBottom" to "15rpx", "fontWeight" to "bold")), "amount-input-wrapper" to padStyleMapOf(utsMapOf("display" to "flex", "alignItems" to "center", "borderTopWidth" to "2rpx", "borderRightWidth" to "2rpx", "borderBottomWidth" to "2rpx", "borderLeftWidth" to "2rpx", "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#e0e0e0", "borderRightColor" to "#e0e0e0", "borderBottomColor" to "#e0e0e0", "borderLeftColor" to "#e0e0e0", "borderTopLeftRadius" to "12rpx", "borderTopRightRadius" to "12rpx", "borderBottomRightRadius" to "12rpx", "borderBottomLeftRadius" to "12rpx", "paddingTop" to "20rpx", "paddingRight" to "20rpx", "paddingBottom" to "20rpx", "paddingLeft" to "20rpx", "backgroundImage" to "none", "backgroundColor" to "#f9f9f9")), "currency" to padStyleMapOf(utsMapOf("color" to "#333333", "marginRight" to "15rpx", "fontSize" to "36rpx", "fontWeight" to "bold")), "amount-input" to padStyleMapOf(utsMapOf("flex" to 1, "borderTopWidth" to "medium", "borderRightWidth" to "medium", "borderBottomWidth" to "medium", "borderLeftWidth" to "medium", "borderTopStyle" to "none", "borderRightStyle" to "none", "borderBottomStyle" to "none", "borderLeftStyle" to "none", "borderTopColor" to "#000000", "borderRightColor" to "#000000", "borderBottomColor" to "#000000", "borderLeftColor" to "#000000", "backgroundImage" to "none", "backgroundColor" to "rgba(0,0,0,0)", "fontSize" to "36rpx", "fontWeight" to "bold")), "remark-input" to padStyleMapOf(utsMapOf("width" to "100%", "paddingTop" to "20rpx", "paddingRight" to "20rpx", "paddingBottom" to "20rpx", "paddingLeft" to "20rpx", "borderTopWidth" to "2rpx", "borderRightWidth" to "2rpx", "borderBottomWidth" to "2rpx", "borderLeftWidth" to "2rpx", "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#e0e0e0", "borderRightColor" to "#e0e0e0", "borderBottomColor" to "#e0e0e0", "borderLeftColor" to "#e0e0e0", "borderTopLeftRadius" to "12rpx", "borderTopRightRadius" to "12rpx", "borderBottomRightRadius" to "12rpx", "borderBottomLeftRadius" to "12rpx", "fontSize" to "28rpx", "backgroundImage" to "none", "backgroundColor" to "#f9f9f9")), "time-input-row" to padStyleMapOf(utsMapOf("display" to "flex", "flexDirection" to "row", "alignItems" to "center", "gap" to "12rpx", "justifyContent" to "center")), "time-input" to padStyleMapOf(utsMapOf("width" to "100%", "paddingTop" to "20rpx", "paddingRight" to "20rpx", "paddingBottom" to "20rpx", "paddingLeft" to "20rpx", "borderTopWidth" to "2rpx", "borderRightWidth" to "2rpx", "borderBottomWidth" to "2rpx", "borderLeftWidth" to "2rpx", "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#e0e0e0", "borderRightColor" to "#e0e0e0", "borderBottomColor" to "#e0e0e0", "borderLeftColor" to "#e0e0e0", "borderTopLeftRadius" to "12rpx", "borderTopRightRadius" to "12rpx", "borderBottomRightRadius" to "12rpx", "borderBottomLeftRadius" to "12rpx", "fontSize" to "28rpx", "backgroundImage" to "none", "backgroundColor" to "#f9f9f9", "fontFamily" to "monospace", "letterSpacing" to "2rpx")), "time-format-hint" to padStyleMapOf(utsMapOf("fontSize" to "22rpx", "color" to "#999999", "marginTop" to "10rpx", "lineHeight" to 1.4)), "section-header" to padStyleMapOf(utsMapOf("display" to "flex", "justifyContent" to "space-between", "alignItems" to "center", "marginBottom" to "20rpx")), "section-label" to padStyleMapOf(utsMapOf("fontSize" to "24rpx", "color" to "#333333")), "header-actions" to padStyleMapOf(utsMapOf("display" to "flex", "gap" to "15rpx")), "add-btn" to padStyleMapOf(utsMapOf("fontSize" to "28rpx", "color" to "#4e54c8", "paddingTop" to "10rpx", "paddingRight" to "10rpx", "paddingBottom" to "10rpx", "paddingLeft" to "10rpx", "backgroundImage" to "none", "backgroundColor" to "#f0f2ff", "width" to "40rpx", "height" to "40rpx", "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "cursor" to "pointer", "backgroundImage:active" to "none", "backgroundColor:active" to "#e0e3ff")), "refresh-btn" to padStyleMapOf(utsMapOf("fontSize" to "28rpx", "color" to "#4e54c8", "paddingTop" to "10rpx", "paddingRight" to "10rpx", "paddingBottom" to "10rpx", "paddingLeft" to "10rpx", "backgroundImage" to "none", "backgroundColor" to "#f0f2ff", "width" to "40rpx", "height" to "40rpx", "display" to "flex", "alignItems" to "center", "justifyContent" to "center", "cursor" to "pointer", "backgroundImage:active:active" to "none", "backgroundColor:active:active" to "#e0e3ff")), "category-grid" to padStyleMapOf(utsMapOf("gridTemplateColumns" to "repeat(4, 1fr)", "gap" to "20rpx", "marginBottom" to "30rpx")), "category-item" to utsMapOf("" to utsMapOf("backgroundImage" to "none", "backgroundColor" to "#f4f4f4", "borderTopLeftRadius" to "16rpx", "borderTopRightRadius" to "16rpx", "borderBottomRightRadius" to "16rpx", "borderBottomLeftRadius" to "16rpx", "paddingTop" to "20rpx", "paddingRight" to 0, "paddingBottom" to "20rpx", "paddingLeft" to 0, "textAlign" to "center"), ".selected" to utsMapOf("backgroundImage" to "none", "backgroundColor" to "#e0e3ff", "borderTopWidth" to "2rpx", "borderRightWidth" to "2rpx", "borderBottomWidth" to "2rpx", "borderLeftWidth" to "2rpx", "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#4e54c8", "borderRightColor" to "#4e54c8", "borderBottomColor" to "#4e54c8", "borderLeftColor" to "#4e54c8")), "emoji" to padStyleMapOf(utsMapOf("fontSize" to "36rpx", "marginBottom" to "10rpx")), "name" to padStyleMapOf(utsMapOf("fontSize" to "22rpx")), "date-row" to padStyleMapOf(utsMapOf("display" to "flex", "justifyContent" to "space-between", "alignItems" to "center", "marginTop" to "30rpx", "paddingTop" to "20rpx", "borderTopWidth" to "2rpx", "borderTopStyle" to "solid", "borderTopColor" to "#f0f0f0")), "date-label" to padStyleMapOf(utsMapOf("fontSize" to "24rpx", "color" to "#666666")), "date-value" to padStyleMapOf(utsMapOf("fontSize" to "24rpx", "color" to "#333333")), "save-btn-wrapper" to padStyleMapOf(utsMapOf("paddingTop" to "30rpx", "paddingRight" to "40rpx", "paddingBottom" to "30rpx", "paddingLeft" to "40rpx", "position" to "fixed", "bottom" to 0, "left" to 0, "right" to 0, "backgroundImage" to "linear-gradient(to top, #f5f7fa 80%, transparent)", "backgroundColor" to "rgba(0,0,0,0)", "zIndex" to 100)), "save-btn" to padStyleMapOf(utsMapOf("width" to "100%", "backgroundImage" to "linear-gradient(to right, #4e54c8, #8f94fb)", "backgroundColor" to "rgba(0,0,0,0)", "color" to "#FFFFFF", "paddingTop" to "20rpx", "paddingRight" to "20rpx", "paddingBottom" to "20rpx", "paddingLeft" to "20rpx", "fontSize" to "30rpx", "borderTopLeftRadius" to "35rpx", "borderTopRightRadius" to "35rpx", "borderBottomRightRadius" to "35rpx", "borderBottomLeftRadius" to "35rpx", "borderTopWidth" to "medium", "borderRightWidth" to "medium", "borderBottomWidth" to "medium", "borderLeftWidth" to "medium", "borderTopStyle" to "none", "borderRightStyle" to "none", "borderBottomStyle" to "none", "borderLeftStyle" to "none", "borderTopColor" to "#000000", "borderRightColor" to "#000000", "borderBottomColor" to "#000000", "borderLeftColor" to "#000000", "fontWeight" to "bold", "transitionDuration" to "0.3s", "transitionTimingFunction" to "ease", "transform:active" to "scale(0.98)", "opacity:active" to 0.9, "backgroundImage:disabled" to "none", "backgroundColor:disabled" to "#cccccc", "color:disabled" to "#999999", "transform:disabled" to "none")), "loading-categories" to padStyleMapOf(utsMapOf("textAlign" to "center", "paddingTop" to "40rpx", "paddingRight" to 0, "paddingBottom" to "40rpx", "paddingLeft" to 0, "color" to "#666666")), "empty-categories" to padStyleMapOf(utsMapOf("textAlign" to "center", "paddingTop" to "40rpx", "paddingRight" to 0, "paddingBottom" to "40rpx", "paddingLeft" to 0, "color" to "#999999", "fontSize" to "24rpx")), "@TRANSITION" to utsMapOf("save-btn" to utsMapOf("duration" to "0.3s", "timingFunction" to "ease")))
            }
        var inheritAttrs = true
        var inject: Map<String, Map<String, Any?>> = utsMapOf()
        var emits: Map<String, Any?> = utsMapOf()
        var props = normalizePropsOptions(utsMapOf())
        var propsNeedCastKeys: UTSArray<String> = utsArrayOf()
        var components: Map<String, CreateVueComponent> = utsMapOf()
    }
}
