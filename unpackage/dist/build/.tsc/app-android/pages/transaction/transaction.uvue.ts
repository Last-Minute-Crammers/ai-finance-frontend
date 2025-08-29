import { ref, onMounted, watch, onUnmounted, computed } from 'vue'
import { createTransaction } from '../../common/api/transaction'
import { getCategoryList, refreshCategoryList, type Category, getIconById } from '../../common/api/category'
import { setPageType, setPagePath, resetPageState } from '../../utils/pageState'
import AddCategoryModal from '../../components/AddCategoryModal.uvue'

// 响应式数据

const __sfc__ = defineComponent({
  __name: 'transaction',
  setup(__props): any | null {
const __ins = getCurrentInstance()!;
const _ctx = __ins.proxy as InstanceType<typeof __sfc__>;
const _cache = __ins.renderCache;

const type = ref('支出')
const amount = ref('')
const remark = ref('')
const year = ref('')
const month = ref('')
const day = ref('')
const hour = ref('')
const minute = ref('')
const loading = ref(false)
const categoryLoading = ref(false)
const categories = ref<Category[]>([])
const selectedCategoryId = ref<number | null>(null)
const showAddCategoryModal = ref(false)

// 获取分类列表
const fetchCategoryList = async (forceRefresh = false) => {
  try {
    categoryLoading.value = true
    
    // 根据当前类型获取对应分类
    const incomeExpenseType = type.value === '支出' ? 'expense' : 'income'
    
    let response
    if (forceRefresh) {
      // 强制从服务器刷新数据
      response = await refreshCategoryList({
        income_expense: incomeExpenseType
      })
    } else {
      // 优先使用本地缓存，如果没有则从服务器获取
      response = await getCategoryList({
        income_expense: incomeExpenseType
      })
    }
    
    console.log('分类列表响应:', response)
    console.log('响应数据结构:', {
      code: response.code,
      message: response.message,
      dataType: typeof response.data,
      dataLength: Array.isArray(response.data) ? response.data.length : 'not array',
      data: response.data
    })
    
    if (response.code === 200) {
      // 确保 response.data 是数组，即使是空数组也要处理
      categories.value = response.data || []
      // 重置选中的分类
      selectedCategoryId.value = null
      
      // 显示数据来源提示
      if (response.message === 'success (cached)') {
        console.log('使用本地缓存数据')
      } else {
        console.log('使用服务器数据')
      }
      
      // 如果是空数组，显示提示信息
      if (categories.value.length === 0) {
        console.log('当前类型暂无分类数据')
      } else {
        console.log('成功加载分类数据:', categories.value.length, '个分类')
      }
    } else {
      console.error('API返回错误:', response)
      uni.showToast({
        title: response.message || '获取分类列表失败',
        icon: 'none'
      })
    }
  } catch (error) {
    console.error('获取分类列表失败:', error)
    uni.showToast({
      title: '网络错误，请检查网络连接',
      icon: 'none'
    })
  } finally {
    categoryLoading.value = false
  }
}

// 获取分类图标
const getCategoryIcon = (category: Category): string => {
  // 优先使用iconId获取图标
  if (category.iconId) {
    const iconInfo = getIconById(category.iconId)
    if (iconInfo) {
      return iconInfo.emoji
    }
  }
  
  // 兼容旧数据：直接使用icon字段
  if (category.icon) {
    return category.icon
  }
  
  // 默认图标
  return '📁'
}

// 选择分类
const selectCategory = (category: Category) => {
  selectedCategoryId.value = category.id
}

// 刷新分类数据
const refreshCategories = async () => {
  try {
    uni.showToast({
      title: '刷新分类中...',
      icon: 'loading',
      duration: 1000
    })
    await fetchCategoryList(true) // 强制刷新
    uni.showToast({
      title: '分类已更新',
      icon: 'success',
      duration: 1500
    })
  } catch (error) {
    console.error('刷新分类失败:', error)
    uni.showToast({
      title: '刷新失败',
      icon: 'none'
    })
  }
}

// 显示添加分类模态框
const showAddCategory = () => {
  showAddCategoryModal.value = true
}

// 分类创建成功回调
const onCategoryCreated = (newCategory: Category) => {
  console.log('新分类已创建:', newCategory)
  // 刷新分类列表
  fetchCategoryList(true)
}

// 初始化时间输入为当前时间
const initTimeInput = () => {
  const now = new Date()
  year.value = String(now.getFullYear())
  month.value = String(now.getMonth() + 1).padStart(2, '0')
  day.value = String(now.getDate()).padStart(2, '0')
  hour.value = String(now.getHours()).padStart(2, '0')
  minute.value = String(now.getMinutes()).padStart(2, '0')
}

// 格式化时间输入为ISO格式
const formatTimeToISO = () => {
  try {
    const y = parseInt(year.value)
    const m = parseInt(month.value) - 1
    const d = parseInt(day.value)
    const h = parseInt(hour.value)
    const min = parseInt(minute.value)
    const date = new Date(y, m, d, h, min)
    if (isNaN(date.getTime())) throw new Error('无效的日期')
    return date.toISOString()
  } catch (error) {
    return new Date().toISOString()
  }
}

// 页面加载时获取当前日期和分类列表
onMounted(async () => {
  // 设置页面路径和初始类型
  setPagePath('/pages/transaction/transaction')
  setPageType(type.value)
  
  // 初始化时间输入
  initTimeInput()
  await fetchCategoryList()
})

// 页面卸载时重置状态
onUnmounted(() => {
  resetPageState()
})

// 监听类型变化，重新获取分类列表
watch(type, async () => {
  await fetchCategoryList()
})

function goBack() {
  uni.navigateBack()
}

function changeType(newType: string) {
  type.value = newType
  // 更新页面状态
  setPageType(newType as '支出' | '收入')
}

async function submitTransaction() {
  try {
    loading.value = true
    // 输入验证
    if (!amount.value) {
      uni.showToast({ title: '请输入金额', icon: 'none' })
      return
    }
    const amountNum = parseFloat(amount.value)
    if (isNaN(amountNum) || amountNum <= 0) {
      uni.showToast({ title: '请输入有效的正数金额', icon: 'none' })
      return
    }
    if (!selectedCategoryId.value) {
      uni.showToast({ title: '请选择分类', icon: 'none' })
      return
    }
    // 时间输入验证
    if (!year.value || !month.value || !day.value || !hour.value || !minute.value) {
      uni.showToast({ title: '请完整填写时间', icon: 'none' })
      return
    }
    const formattedTime = formatTimeToISO()
    const transactionData = {
      CategoryId: selectedCategoryId.value,
      IncomeExpense: type.value === '支出' ? 'expense' : 'income',
      Amount: Math.round(amountNum * 100),
      Remark: remark.value.trim(),
      TradeTime: formattedTime
    }
    const response = await createTransaction(transactionData)
    // 判断后端返回Msg
    if (response && (response.Msg === '查询成功' || response.Msg === 'success')) {
      amount.value = ''
      remark.value = ''
      selectedCategoryId.value = null
      initTimeInput()
      uni.showToast({ 
        title: '记录已保存',
        icon: 'success',
        mask: true,
        duration: 1500
      })
      setTimeout(() => {
        uni.navigateBack()
      }, 1500)
    } else {
      throw new Error(response.Msg || '保存失败')
    }
  } catch (error) {
    const errorMessage = error instanceof Error ? error.message : String(error)
    uni.showToast({
      title: `保存失败: ${errorMessage}`,
      icon: 'none',
      duration: 2000
    })
  } finally {
    loading.value = false
  }
}

return (): any | null => {

  return createElementVNode("view", utsMapOf({ class: "transaction-container" }), [
    createElementVNode("view", utsMapOf({ class: "header" }), [
      createElementVNode("text", utsMapOf({
        class: "back-btn",
        onClick: goBack
      }), "←"),
      createElementVNode("text", utsMapOf({ class: "header-title" }), "记账"),
      createElementVNode("text", utsMapOf({ class: "header-sub" }), "记录您的每一笔收支")
    ]),
    createElementVNode("view", utsMapOf({ class: "type-toggle" }), [
      createElementVNode("view", utsMapOf({
        class: normalizeClass(["type-btn", utsMapOf({ active: type.value === '支出' })]),
        onClick: () => {changeType('支出')}
      }), "支出", 10 /* CLASS, PROPS */, ["onClick"]),
      createElementVNode("view", utsMapOf({
        class: normalizeClass(["type-btn", utsMapOf({ active: type.value === '收入' })]),
        onClick: () => {changeType('收入')}
      }), "收入", 10 /* CLASS, PROPS */, ["onClick"])
    ]),
    createElementVNode("view", utsMapOf({ class: "category-card" }), [
      createElementVNode("view", utsMapOf({ class: "section-header" }), [
        createElementVNode("text", utsMapOf({ class: "section-label" }), "选择分类"),
        createElementVNode("view", utsMapOf({ class: "header-actions" }), [
          createElementVNode("text", utsMapOf({
            class: "add-btn",
            onClick: showAddCategory
          }), "➕"),
          createElementVNode("text", utsMapOf({
            class: "refresh-btn",
            onClick: refreshCategories
          }), "🔄")
        ])
      ]),
      isTrue(categoryLoading.value)
        ? createElementVNode("view", utsMapOf({
            key: 0,
            class: "loading-categories"
          }), [
            createElementVNode("text", null, "加载分类中...")
          ])
        : createElementVNode("view", utsMapOf({
            key: 1,
            class: "category-grid"
          }), [
            createElementVNode(Fragment, null, RenderHelpers.renderList(categories.value, (item, index, __index, _cached): any => {
              return createElementVNode("view", utsMapOf({
                key: item.id,
                class: normalizeClass(["category-item", utsMapOf({ selected: selectedCategoryId.value === item.id })]),
                onClick: () => {selectCategory(item)}
              }), [
                createElementVNode("text", utsMapOf({ class: "emoji" }), toDisplayString(getCategoryIcon(item)), 1 /* TEXT */),
                createElementVNode("text", utsMapOf({ class: "name" }), toDisplayString(item.name), 1 /* TEXT */)
              ], 10 /* CLASS, PROPS */, ["onClick"])
            }), 128 /* KEYED_FRAGMENT */)
          ]),
      isTrue(!categoryLoading.value && categories.value.length === 0)
        ? createElementVNode("view", utsMapOf({
            key: 2,
            class: "empty-categories"
          }), [
            createElementVNode("text", null, "暂无分类，请先添加分类")
          ])
        : createCommentVNode("v-if", true)
    ]),
    createElementVNode("view", utsMapOf({ class: "transaction-details-card" }), [
      createElementVNode("view", utsMapOf({ class: "input-group" }), [
        createElementVNode("text", utsMapOf({ class: "input-label" }), "金额"),
        createElementVNode("view", utsMapOf({ class: "amount-input-wrapper" }), [
          createElementVNode("text", utsMapOf({ class: "currency" }), "¥"),
          createElementVNode("input", utsMapOf({
            class: "amount-input",
            type: "digit",
            modelValue: amount.value,
            onInput: ($event: InputEvent) => {(amount).value = $event.detail.value},
            placeholder: "0.00",
            maxlength: "10"
          }), null, 40 /* PROPS, NEED_HYDRATION */, ["modelValue", "onInput"])
        ])
      ]),
      createElementVNode("view", utsMapOf({ class: "input-group" }), [
        createElementVNode("text", utsMapOf({ class: "input-label" }), "备注"),
        createElementVNode("input", utsMapOf({
          class: "remark-input",
          modelValue: remark.value,
          onInput: ($event: InputEvent) => {(remark).value = $event.detail.value},
          placeholder: "添加备注（可选）",
          maxlength: "100"
        }), null, 40 /* PROPS, NEED_HYDRATION */, ["modelValue", "onInput"])
      ]),
      createElementVNode("view", utsMapOf({ class: "input-group" }), [
        createElementVNode("text", utsMapOf({ class: "input-label" }), "交易时间"),
        createElementVNode("view", utsMapOf({
          class: "time-input-row",
          style: normalizeStyle(utsMapOf({"display":"flex","flex-direction":"row","align-items":"center","gap":"12rpx","justify-content":"center"}))
        }), [
          createElementVNode("input", utsMapOf({
            class: "time-input",
            modelValue: year.value,
            onInput: ($event: InputEvent) => {(year).value = $event.detail.value},
            maxlength: "4",
            placeholder: "年",
            style: normalizeStyle(utsMapOf({"width":"130rpx","text-align":"center"}))
          }), null, 44 /* STYLE, PROPS, NEED_HYDRATION */, ["modelValue", "onInput"]),
          createElementVNode("text", null, "年"),
          createElementVNode("input", utsMapOf({
            class: "time-input",
            modelValue: month.value,
            onInput: ($event: InputEvent) => {(month).value = $event.detail.value},
            maxlength: "2",
            placeholder: "月",
            style: normalizeStyle(utsMapOf({"width":"80rpx","text-align":"center"}))
          }), null, 44 /* STYLE, PROPS, NEED_HYDRATION */, ["modelValue", "onInput"]),
          createElementVNode("text", null, "月"),
          createElementVNode("input", utsMapOf({
            class: "time-input",
            modelValue: day.value,
            onInput: ($event: InputEvent) => {(day).value = $event.detail.value},
            maxlength: "2",
            placeholder: "日",
            style: normalizeStyle(utsMapOf({"width":"80rpx","text-align":"center"}))
          }), null, 44 /* STYLE, PROPS, NEED_HYDRATION */, ["modelValue", "onInput"]),
          createElementVNode("text", null, "日"),
          createElementVNode("input", utsMapOf({
            class: "time-input",
            modelValue: hour.value,
            onInput: ($event: InputEvent) => {(hour).value = $event.detail.value},
            maxlength: "2",
            placeholder: "时",
            style: normalizeStyle(utsMapOf({"width":"80rpx","text-align":"center"}))
          }), null, 44 /* STYLE, PROPS, NEED_HYDRATION */, ["modelValue", "onInput"]),
          createElementVNode("text", null, ":"),
          createElementVNode("input", utsMapOf({
            class: "time-input",
            modelValue: minute.value,
            onInput: ($event: InputEvent) => {(minute).value = $event.detail.value},
            maxlength: "2",
            placeholder: "分",
            style: normalizeStyle(utsMapOf({"width":"80rpx","text-align":"center"}))
          }), null, 44 /* STYLE, PROPS, NEED_HYDRATION */, ["modelValue", "onInput"]),
          createElementVNode("text", null, "分")
        ], 4 /* STYLE */)
      ])
    ]),
    createElementVNode("view", utsMapOf({ class: "save-btn-wrapper" }), [
      createElementVNode("button", utsMapOf({
        class: "save-btn",
        onClick: submitTransaction,
        disabled: loading.value
      }), toDisplayString(loading.value ? '保存中...' : '保存记录'), 9 /* TEXT, PROPS */, ["disabled"])
    ]),
    createVNode(unref(AddCategoryModal), utsMapOf({
      visible: showAddCategoryModal.value,
      "onUpdate:visible": $event => {(showAddCategoryModal).value = $event},
      onCreated: onCategoryCreated
    }), null, 8 /* PROPS */, ["visible", "onUpdate:visible"])
  ])
}
}

})
export default __sfc__
const GenPagesTransactionTransactionStyles = [utsMapOf([["transaction-container", padStyleMapOf(utsMapOf([["backgroundColor", "#f5f7fa"], ["paddingBottom", "120rpx"], ["overflowY", "auto"]]))], ["header", padStyleMapOf(utsMapOf([["backgroundImage", "linear-gradient(to right, #4e54c8, #8f94fb)"], ["backgroundColor", "rgba(0,0,0,0)"], ["paddingTop", "80rpx"], ["paddingRight", "30rpx"], ["paddingBottom", "40rpx"], ["paddingLeft", "30rpx"], ["borderBottomLeftRadius", "30rpx"], ["borderBottomRightRadius", "30rpx"], ["textAlign", "center"], ["position", "relative"]]))], ["back-btn", padStyleMapOf(utsMapOf([["position", "absolute"], ["left", "30rpx"], ["top", "80rpx"], ["fontSize", "36rpx"], ["color", "#FFFFFF"]]))], ["header-title", padStyleMapOf(utsMapOf([["fontSize", "36rpx"], ["fontWeight", "bold"], ["color", "#FFFFFF"]]))], ["header-sub", padStyleMapOf(utsMapOf([["fontSize", "24rpx"], ["color", "#FFFFFF"], ["marginTop", "10rpx"]]))], ["type-toggle", padStyleMapOf(utsMapOf([["display", "flex"], ["justifyContent", "center"], ["backgroundImage", "none"], ["backgroundColor", "#e0e3f0"], ["borderTopLeftRadius", "40rpx"], ["borderTopRightRadius", "40rpx"], ["borderBottomRightRadius", "40rpx"], ["borderBottomLeftRadius", "40rpx"], ["marginTop", "30rpx"], ["marginRight", "auto"], ["marginBottom", "20rpx"], ["marginLeft", "auto"], ["width", "60%"]]))], ["type-btn", utsMapOf([["", utsMapOf([["flex", 1], ["textAlign", "center"], ["paddingTop", "20rpx"], ["paddingRight", 0], ["paddingBottom", "20rpx"], ["paddingLeft", 0], ["fontSize", "28rpx"], ["color", "#555555"], ["fontWeight", "bold"], ["borderTopLeftRadius", "40rpx"], ["borderTopRightRadius", "40rpx"], ["borderBottomRightRadius", "40rpx"], ["borderBottomLeftRadius", "40rpx"]])], [".active", utsMapOf([["backgroundImage", "none"], ["backgroundColor", "#FFFFFF"], ["color", "#4e54c8"], ["boxShadow", "0 2rpx 6rpx rgba(0, 0, 0, 0.1)"]])]])], ["category-card", padStyleMapOf(utsMapOf([["backgroundImage", "none"], ["backgroundColor", "#ffffff"], ["marginTop", 0], ["marginRight", "20rpx"], ["marginBottom", "20rpx"], ["marginLeft", "20rpx"], ["paddingTop", "30rpx"], ["paddingRight", "30rpx"], ["paddingBottom", "30rpx"], ["paddingLeft", "30rpx"], ["borderTopLeftRadius", "20rpx"], ["borderTopRightRadius", "20rpx"], ["borderBottomRightRadius", "20rpx"], ["borderBottomLeftRadius", "20rpx"], ["boxShadow", "0 4rpx 12rpx rgba(0, 0, 0, 0.04)"]]))], ["transaction-details-card", padStyleMapOf(utsMapOf([["backgroundImage", "none"], ["backgroundColor", "#ffffff"], ["marginTop", 0], ["marginRight", "20rpx"], ["marginBottom", "20rpx"], ["marginLeft", "20rpx"], ["paddingTop", "30rpx"], ["paddingRight", "30rpx"], ["paddingBottom", "30rpx"], ["paddingLeft", "30rpx"], ["borderTopLeftRadius", "20rpx"], ["borderTopRightRadius", "20rpx"], ["borderBottomRightRadius", "20rpx"], ["borderBottomLeftRadius", "20rpx"], ["boxShadow", "0 4rpx 12rpx rgba(0, 0, 0, 0.04)"]]))], ["input-group", padStyleMapOf(utsMapOf([["marginBottom", "30rpx"], ["marginBottom:last-child", 0]]))], ["input-label", padStyleMapOf(utsMapOf([["fontSize", "28rpx"], ["color", "#333333"], ["marginBottom", "15rpx"], ["fontWeight", "bold"]]))], ["amount-input-wrapper", padStyleMapOf(utsMapOf([["display", "flex"], ["alignItems", "center"], ["borderTopWidth", "2rpx"], ["borderRightWidth", "2rpx"], ["borderBottomWidth", "2rpx"], ["borderLeftWidth", "2rpx"], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "#e0e0e0"], ["borderRightColor", "#e0e0e0"], ["borderBottomColor", "#e0e0e0"], ["borderLeftColor", "#e0e0e0"], ["borderTopLeftRadius", "12rpx"], ["borderTopRightRadius", "12rpx"], ["borderBottomRightRadius", "12rpx"], ["borderBottomLeftRadius", "12rpx"], ["paddingTop", "20rpx"], ["paddingRight", "20rpx"], ["paddingBottom", "20rpx"], ["paddingLeft", "20rpx"], ["backgroundImage", "none"], ["backgroundColor", "#f9f9f9"]]))], ["currency", padStyleMapOf(utsMapOf([["color", "#333333"], ["marginRight", "15rpx"], ["fontSize", "36rpx"], ["fontWeight", "bold"]]))], ["amount-input", padStyleMapOf(utsMapOf([["flex", 1], ["borderTopWidth", "medium"], ["borderRightWidth", "medium"], ["borderBottomWidth", "medium"], ["borderLeftWidth", "medium"], ["borderTopStyle", "none"], ["borderRightStyle", "none"], ["borderBottomStyle", "none"], ["borderLeftStyle", "none"], ["borderTopColor", "#000000"], ["borderRightColor", "#000000"], ["borderBottomColor", "#000000"], ["borderLeftColor", "#000000"], ["backgroundImage", "none"], ["backgroundColor", "rgba(0,0,0,0)"], ["fontSize", "36rpx"], ["fontWeight", "bold"]]))], ["remark-input", padStyleMapOf(utsMapOf([["width", "100%"], ["paddingTop", "20rpx"], ["paddingRight", "20rpx"], ["paddingBottom", "20rpx"], ["paddingLeft", "20rpx"], ["borderTopWidth", "2rpx"], ["borderRightWidth", "2rpx"], ["borderBottomWidth", "2rpx"], ["borderLeftWidth", "2rpx"], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "#e0e0e0"], ["borderRightColor", "#e0e0e0"], ["borderBottomColor", "#e0e0e0"], ["borderLeftColor", "#e0e0e0"], ["borderTopLeftRadius", "12rpx"], ["borderTopRightRadius", "12rpx"], ["borderBottomRightRadius", "12rpx"], ["borderBottomLeftRadius", "12rpx"], ["fontSize", "28rpx"], ["backgroundImage", "none"], ["backgroundColor", "#f9f9f9"]]))], ["time-input-row", padStyleMapOf(utsMapOf([["display", "flex"], ["flexDirection", "row"], ["alignItems", "center"], ["gap", "12rpx"], ["justifyContent", "center"]]))], ["time-input", padStyleMapOf(utsMapOf([["width", "100%"], ["paddingTop", "20rpx"], ["paddingRight", "20rpx"], ["paddingBottom", "20rpx"], ["paddingLeft", "20rpx"], ["borderTopWidth", "2rpx"], ["borderRightWidth", "2rpx"], ["borderBottomWidth", "2rpx"], ["borderLeftWidth", "2rpx"], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "#e0e0e0"], ["borderRightColor", "#e0e0e0"], ["borderBottomColor", "#e0e0e0"], ["borderLeftColor", "#e0e0e0"], ["borderTopLeftRadius", "12rpx"], ["borderTopRightRadius", "12rpx"], ["borderBottomRightRadius", "12rpx"], ["borderBottomLeftRadius", "12rpx"], ["fontSize", "28rpx"], ["backgroundImage", "none"], ["backgroundColor", "#f9f9f9"], ["fontFamily", "monospace"], ["letterSpacing", "2rpx"]]))], ["time-format-hint", padStyleMapOf(utsMapOf([["fontSize", "22rpx"], ["color", "#999999"], ["marginTop", "10rpx"], ["lineHeight", 1.4]]))], ["section-header", padStyleMapOf(utsMapOf([["display", "flex"], ["justifyContent", "space-between"], ["alignItems", "center"], ["marginBottom", "20rpx"]]))], ["section-label", padStyleMapOf(utsMapOf([["fontSize", "24rpx"], ["color", "#333333"]]))], ["header-actions", padStyleMapOf(utsMapOf([["display", "flex"], ["gap", "15rpx"]]))], ["add-btn", padStyleMapOf(utsMapOf([["fontSize", "28rpx"], ["color", "#4e54c8"], ["paddingTop", "10rpx"], ["paddingRight", "10rpx"], ["paddingBottom", "10rpx"], ["paddingLeft", "10rpx"], ["backgroundImage", "none"], ["backgroundColor", "#f0f2ff"], ["width", "40rpx"], ["height", "40rpx"], ["display", "flex"], ["alignItems", "center"], ["justifyContent", "center"], ["cursor", "pointer"], ["backgroundImage:active", "none"], ["backgroundColor:active", "#e0e3ff"]]))], ["refresh-btn", padStyleMapOf(utsMapOf([["fontSize", "28rpx"], ["color", "#4e54c8"], ["paddingTop", "10rpx"], ["paddingRight", "10rpx"], ["paddingBottom", "10rpx"], ["paddingLeft", "10rpx"], ["backgroundImage", "none"], ["backgroundColor", "#f0f2ff"], ["width", "40rpx"], ["height", "40rpx"], ["display", "flex"], ["alignItems", "center"], ["justifyContent", "center"], ["cursor", "pointer"], ["backgroundImage:active:active", "none"], ["backgroundColor:active:active", "#e0e3ff"]]))], ["category-grid", padStyleMapOf(utsMapOf([["gridTemplateColumns", "repeat(4, 1fr)"], ["gap", "20rpx"], ["marginBottom", "30rpx"]]))], ["category-item", utsMapOf([["", utsMapOf([["backgroundImage", "none"], ["backgroundColor", "#f4f4f4"], ["borderTopLeftRadius", "16rpx"], ["borderTopRightRadius", "16rpx"], ["borderBottomRightRadius", "16rpx"], ["borderBottomLeftRadius", "16rpx"], ["paddingTop", "20rpx"], ["paddingRight", 0], ["paddingBottom", "20rpx"], ["paddingLeft", 0], ["textAlign", "center"]])], [".selected", utsMapOf([["backgroundImage", "none"], ["backgroundColor", "#e0e3ff"], ["borderTopWidth", "2rpx"], ["borderRightWidth", "2rpx"], ["borderBottomWidth", "2rpx"], ["borderLeftWidth", "2rpx"], ["borderTopStyle", "solid"], ["borderRightStyle", "solid"], ["borderBottomStyle", "solid"], ["borderLeftStyle", "solid"], ["borderTopColor", "#4e54c8"], ["borderRightColor", "#4e54c8"], ["borderBottomColor", "#4e54c8"], ["borderLeftColor", "#4e54c8"]])]])], ["emoji", padStyleMapOf(utsMapOf([["fontSize", "36rpx"], ["marginBottom", "10rpx"]]))], ["name", padStyleMapOf(utsMapOf([["fontSize", "22rpx"]]))], ["date-row", padStyleMapOf(utsMapOf([["display", "flex"], ["justifyContent", "space-between"], ["alignItems", "center"], ["marginTop", "30rpx"], ["paddingTop", "20rpx"], ["borderTopWidth", "2rpx"], ["borderTopStyle", "solid"], ["borderTopColor", "#f0f0f0"]]))], ["date-label", padStyleMapOf(utsMapOf([["fontSize", "24rpx"], ["color", "#666666"]]))], ["date-value", padStyleMapOf(utsMapOf([["fontSize", "24rpx"], ["color", "#333333"]]))], ["save-btn-wrapper", padStyleMapOf(utsMapOf([["paddingTop", "30rpx"], ["paddingRight", "40rpx"], ["paddingBottom", "30rpx"], ["paddingLeft", "40rpx"], ["position", "fixed"], ["bottom", 0], ["left", 0], ["right", 0], ["backgroundImage", "linear-gradient(to top, #f5f7fa 80%, transparent)"], ["backgroundColor", "rgba(0,0,0,0)"], ["zIndex", 100]]))], ["save-btn", padStyleMapOf(utsMapOf([["width", "100%"], ["backgroundImage", "linear-gradient(to right, #4e54c8, #8f94fb)"], ["backgroundColor", "rgba(0,0,0,0)"], ["color", "#FFFFFF"], ["paddingTop", "20rpx"], ["paddingRight", "20rpx"], ["paddingBottom", "20rpx"], ["paddingLeft", "20rpx"], ["fontSize", "30rpx"], ["borderTopLeftRadius", "35rpx"], ["borderTopRightRadius", "35rpx"], ["borderBottomRightRadius", "35rpx"], ["borderBottomLeftRadius", "35rpx"], ["borderTopWidth", "medium"], ["borderRightWidth", "medium"], ["borderBottomWidth", "medium"], ["borderLeftWidth", "medium"], ["borderTopStyle", "none"], ["borderRightStyle", "none"], ["borderBottomStyle", "none"], ["borderLeftStyle", "none"], ["borderTopColor", "#000000"], ["borderRightColor", "#000000"], ["borderBottomColor", "#000000"], ["borderLeftColor", "#000000"], ["fontWeight", "bold"], ["transitionDuration", "0.3s"], ["transitionTimingFunction", "ease"], ["transform:active", "scale(0.98)"], ["opacity:active", 0.9], ["backgroundImage:disabled", "none"], ["backgroundColor:disabled", "#cccccc"], ["color:disabled", "#999999"], ["transform:disabled", "none"]]))], ["loading-categories", padStyleMapOf(utsMapOf([["textAlign", "center"], ["paddingTop", "40rpx"], ["paddingRight", 0], ["paddingBottom", "40rpx"], ["paddingLeft", 0], ["color", "#666666"]]))], ["empty-categories", padStyleMapOf(utsMapOf([["textAlign", "center"], ["paddingTop", "40rpx"], ["paddingRight", 0], ["paddingBottom", "40rpx"], ["paddingLeft", 0], ["color", "#999999"], ["fontSize", "24rpx"]]))], ["@TRANSITION", utsMapOf([["save-btn", utsMapOf([["duration", "0.3s"], ["timingFunction", "ease"]])]])]])]
