// 页面状态管理工具
interface PageState {
  currentType: '支出' | '收入' | null;
  incomeExpense: 'income' | 'expense' | null;
  pagePath: string | null;
}

// 全局状态
let pageState: PageState = {
  currentType: null,
  incomeExpense: null,
  pagePath: null
}

// 设置页面类型
export const setPageType = (type: '支出' | '收入'): void => {
  pageState.currentType = type
  pageState.incomeExpense = type === '支出' ? 'expense' : 'income'
  console.log('页面类型已设置:', type, 'income_expense:', pageState.incomeExpense)
}

// 获取当前页面类型
export const getCurrentType = (): '支出' | '收入' | null => {
  return pageState.currentType
}

// 获取当前income_expense
export const getCurrentIncomeExpense = (): 'income' | 'expense' | null => {
  return pageState.incomeExpense
}

// 设置页面路径
export const setPagePath = (path: string): void => {
  pageState.pagePath = path
  console.log('页面路径已设置:', path)
}

// 获取页面路径
export const getPagePath = (): string | null => {
  return pageState.pagePath
}

// 获取完整状态
export const getPageState = (): PageState => {
  return { ...pageState }
}

// 重置状态
export const resetPageState = (): void => {
  pageState = {
    currentType: null,
    incomeExpense: null,
    pagePath: null
  }
  console.log('页面状态已重置')
}

// 检查是否在记账页面
export const isInTransactionPage = (): boolean => {
  return pageState.pagePath === '/pages/transaction/transaction'
}

// 检查是否有有效的页面类型
export const hasValidPageType = (): boolean => {
  return pageState.currentType !== null && pageState.incomeExpense !== null
} 