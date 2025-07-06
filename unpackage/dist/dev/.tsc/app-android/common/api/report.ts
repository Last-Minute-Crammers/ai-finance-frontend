import { request } from '../../utils/request'

// 获取AI报告所需的完整统计数据
async function getCompleteStatistics(type: 'week' | 'month' | 'year') {
  const { startTime, endTime } = getTimeRange(type)
  
  __f__('log','at common/api/report.ts:7','获取统计数据，类型:', type, '时间范围:', { startTime, endTime })
  
  // 测试转换函数
  testConversion()
  
  try {
    // 并行获取所有需要的数据
    __f__('log','at common/api/report.ts:14','开始并行获取数据...')
    const [
      baseStats,
      incomeCategoryStats,
      expenseCategoryStats,
      detailStats
    ] = await Promise.all([
      // 1. 基础统计数据
      request({
        url: `/api/user/transaction/statistic/${type}`,
        method: 'POST',
        data: {
          StartTime: new Date(startTime + 'T00:00:00Z').toISOString(),
          EndTime: new Date(endTime + 'T23:59:59Z').toISOString()
        },
        requireAuth: true
      }),
      
      // 2. 收入类别统计数据
      request({
        url: '/api/user/transaction/statistic/category_rank',
        method: 'POST',
        data: {
          StartTime: new Date(startTime + 'T00:00:00Z').toISOString(),
          EndTime: new Date(endTime + 'T23:59:59Z').toISOString(),
          IncomeExpense: 'income',
          Limit: 10
        },
        requireAuth: true
      }),
      
      // 3. 支出类别统计数据
      request({
        url: '/api/user/transaction/statistic/category_rank',
        method: 'POST',
        data: {
          StartTime: new Date(startTime + 'T00:00:00Z').toISOString(),
          EndTime: new Date(endTime + 'T23:59:59Z').toISOString(),
          IncomeExpense: 'expense',
          Limit: 10
        },
        requireAuth: true
      }),
      
      // 4. 根据类型获取细粒度数据
      getDetailStatistics(type, startTime, endTime)
    ])
    
    // 构建完整的统计数据，并将金额从分转换为元
    const baseStatsConverted = convertAmountToYuan(baseStats?.Data || baseStats?.data || baseStats)
    const incomeCategoryStatsConverted = convertCategoryStatsToYuan(incomeCategoryStats?.Data || incomeCategoryStats?.data || incomeCategoryStats)
    const expenseCategoryStatsConverted = convertCategoryStatsToYuan(expenseCategoryStats?.Data || expenseCategoryStats?.data || expenseCategoryStats)
    const detailStatsConverted = convertDetailStatsToYuan(detailStats, type)
    
    __f__('log','at common/api/report.ts:68','转换前基础统计:', baseStats?.Data || baseStats?.data || baseStats)
    __f__('log','at common/api/report.ts:69','转换后基础统计:', baseStatsConverted)
    __f__('log','at common/api/report.ts:70','转换前收入类别统计:', incomeCategoryStats?.Data || incomeCategoryStats?.data || incomeCategoryStats)
    __f__('log','at common/api/report.ts:71','转换后收入类别统计:', incomeCategoryStatsConverted)
    __f__('log','at common/api/report.ts:72','转换前支出类别统计:', expenseCategoryStats?.Data || expenseCategoryStats?.data || expenseCategoryStats)
    __f__('log','at common/api/report.ts:73','转换后支出类别统计:', expenseCategoryStatsConverted)
    
    const completeStats = {
      base_statistics: baseStatsConverted,
      income_category_stats: incomeCategoryStatsConverted,
      expense_category_stats: expenseCategoryStatsConverted,
      ...detailStatsConverted
    }
    
    __f__('log','at common/api/report.ts:82','完整统计数据（已转换为元）:', completeStats)
    __f__('log','at common/api/report.ts:83','原始基础统计数据:', baseStats)
    __f__('log','at common/api/report.ts:84','原始收入类别统计:', incomeCategoryStats)
    __f__('log','at common/api/report.ts:85','原始支出类别统计:', expenseCategoryStats)
    __f__('log','at common/api/report.ts:86','原始细粒度数据:', detailStats)
    
    // 验证转换结果
    if (completeStats.base_statistics) {
      __f__('log','at common/api/report.ts:90','转换后的基础统计:', completeStats.base_statistics)
    }
    if (completeStats.income_category_stats) {
      __f__('log','at common/api/report.ts:93','转换后的收入类别统计:', completeStats.income_category_stats)
    }
    if (completeStats.expense_category_stats) {
      __f__('log','at common/api/report.ts:96','转换后的支出类别统计:', completeStats.expense_category_stats)
    }
    return completeStats
    
  } catch (error) {
    __f__('error','at common/api/report.ts:101','获取统计数据失败:', error)
    // 如果获取失败，返回空数据
    return {
      base_statistics: null,
      income_category_stats: [],
      expense_category_stats: [],
      ...getEmptyDetailStats(type)
    }
  }
}

// 根据报告类型获取细粒度数据
async function getDetailStatistics(type: 'week' | 'month' | 'year', startTime: string, endTime: string) {
  try {
    switch (type) {
      case 'week':
        // 周报：获取单日统计数据
        const [incomeDailyStats, expenseDailyStats] = await Promise.all([
          request({
            url: '/api/user/transaction/statistic/day',
            method: 'POST',
            data: {
              StartTime: new Date(startTime + 'T00:00:00Z').toISOString(),
              EndTime: new Date(endTime + 'T23:59:59Z').toISOString(),
              IncomeExpense: 'income'
            },
            requireAuth: true
          }),
          request({
            url: '/api/user/transaction/statistic/day',
            method: 'POST',
            data: {
              StartTime: new Date(startTime + 'T00:00:00Z').toISOString(),
              EndTime: new Date(endTime + 'T23:59:59Z').toISOString(),
              IncomeExpense: 'expense'
            },
            requireAuth: true
          })
        ])
        
        return {
          income_daily_stats: incomeDailyStats?.Data || incomeDailyStats?.data || incomeDailyStats,
          expense_daily_stats: expenseDailyStats?.Data || expenseDailyStats?.data || expenseDailyStats
        }
        
      case 'month':
        // 月报：获取单周统计数据
        const weeklyStats = await request({
          url: '/api/user/transaction/statistic/week',
          method: 'POST',
          data: {
            StartTime: new Date(startTime + 'T00:00:00Z').toISOString(),
            EndTime: new Date(endTime + 'T23:59:59Z').toISOString()
          },
          requireAuth: true
        })
        
        return {
          weekly_stats: weeklyStats?.Data || weeklyStats?.data || weeklyStats
        }
        
      case 'year':
        // 年报：获取月度统计数据
        const monthlyStats = await request({
          url: '/api/user/transaction/statistic/month',
          method: 'POST',
          data: {
            StartTime: new Date(startTime + 'T00:00:00Z').toISOString(),
            EndTime: new Date(endTime + 'T23:59:59Z').toISOString()
          },
          requireAuth: true
        })
        
        return {
          monthly_stats: monthlyStats?.Data || monthlyStats?.data || monthlyStats
        }
        
      default:
        return {}
    }
  } catch (error) {
    __f__('error','at common/api/report.ts:182',`获取${type}细粒度数据失败:`, error)
    return getEmptyDetailStats(type)
  }
}

// 获取空的细粒度数据
function getEmptyDetailStats(type: 'week' | 'month' | 'year') {
  switch (type) {
    case 'week':
      return {
        income_daily_stats: [],
        expense_daily_stats: []
      }
    case 'month':
      return {
        weekly_stats: []
      }
    case 'year':
      return {
        monthly_stats: []
      }
    default:
      return {}
  }
}

export async function getAIReport(data: {
  type: 'week' | 'month' | 'year',
  stats?: any
}) {
  // 获取完整的统计数据
  const completeStats = await getCompleteStatistics(data.type)
  
  return request({
    url: '/api/user/report/ai',
    method: 'POST',
    data: {
      type: data.type,
      stats: completeStats
    },
    requireAuth: true
  })
}

export function getHistoryReport(type: string, period: string) {
  return request({
    url: '/api/user/ai/history',
    method: 'GET',
    params: { type, period },
    requireAuth: true
  })
}

// 获取用户财务统计数据
export function getUserStatistics(type: 'week' | 'month' | 'year') {
  const { startTime, endTime } = getTimeRange(type)
  
  const requestData = {
    start_time: startTime,
    end_time: endTime,
    income_expense: null // null表示查询收入和支出
  }
  
  const endpoint = type === 'week' ? 'week' : type === 'month' ? 'month' : 'year'
  
  return request({
    url: `/api/user/transaction/statistic/${endpoint}`,
    method: 'POST',
    data: requestData,
    requireAuth: true
  })
}

// 获取总统计数据
export function getTotalStatistics() {
  return request({
    url: '/api/user/transaction/statistic/total',
    method: 'GET',
    requireAuth: true
  })
}

// 根据类型获取时间范围
function getTimeRange(type: 'week' | 'month' | 'year'): { startTime: string, endTime: string } {
  const now = new Date()
  let startTime: Date
  let endTime: Date = now
  
  switch (type) {
    case 'week':
      // 获取最近一周（从7天前到今天）
      startTime = new Date(now.getTime() - 6 * 24 * 60 * 60 * 1000)
      break
    case 'month':
      // 获取本月开始
      startTime = new Date(now.getFullYear(), now.getMonth(), 1)
      break
    case 'year':
      // 获取近12个月（从12个月前到今天）
      startTime = new Date(now.getFullYear(), now.getMonth() - 11, 1)
      // 确保时间范围正确
      if (startTime > endTime) {
        startTime = new Date(now.getFullYear() - 1, now.getMonth(), 1)
      }
      break
    default:
      startTime = new Date(now.getFullYear(), now.getMonth(), 1)
  }
  
  return {
    startTime: startTime.toISOString().split('T')[0],
    endTime: endTime.toISOString().split('T')[0]
  }
}

// 将金额从分转换为元
function convertAmountToYuan(data: any): any {
  if (!data) return data
  
  // 如果是数组，处理每个元素
  if (Array.isArray(data)) {
    return data.map(item => convertAmountToYuan(item))
  }
  
  // 如果是对象，处理金额字段
  if (typeof data === 'object') {
    const converted = { ...data }
    
    // 处理List字段（嵌套数组）
    if (converted.List && Array.isArray(converted.List)) {
      converted.List = converted.List.map(item => convertAmountToYuan(item))
    }
    
    // 处理基础统计数据结构
    if (converted.Income && typeof converted.Income === 'object') {
      if (typeof converted.Income.Amount === 'number') {
        converted.Income.Amount = converted.Income.Amount / 100
      }
    }
    
    if (converted.Expense && typeof converted.Expense === 'object') {
      if (typeof converted.Expense.Amount === 'number') {
        converted.Expense.Amount = converted.Expense.Amount / 100
      }
    }
    
    // 处理直接的Amount字段
    if (typeof converted.Amount === 'number') {
      converted.Amount = converted.Amount / 100
    }
    
    return converted
  }
  
  return data
}

// 将类别统计数据从分转换为元
function convertCategoryStatsToYuan(data: any): any {
  if (!data) return data
  
  // 如果是数组，处理每个元素
  if (Array.isArray(data)) {
    return data.map(item => {
      if (typeof item === 'object' && item !== null) {
        const converted = { ...item }
        if (typeof converted.Amount === 'number') {
          converted.Amount = converted.Amount / 100
        }
        return converted
      }
      return item
    })
  }
  
  // 如果是对象，处理List字段
  if (typeof data === 'object') {
    const converted = { ...data }
    
    // 处理List字段（嵌套数组）
    if (converted.List && Array.isArray(converted.List)) {
      converted.List = converted.List.map(item => {
        if (typeof item === 'object' && item !== null) {
          const convertedItem = { ...item }
          if (typeof convertedItem.Amount === 'number') {
            convertedItem.Amount = convertedItem.Amount / 100
          }
          return convertedItem
        }
        return item
      })
    }
    
    return converted
  }
  
  return data
}

// 将细粒度统计数据从分转换为元
function convertDetailStatsToYuan(data: any, type: 'week' | 'month' | 'year'): any {
  if (!data) return data
  
  const converted = { ...data }
  
  // 根据类型处理不同的细粒度数据
  switch (type) {
    case 'week':
      // 处理单日统计数据
      if (converted.income_daily_stats) {
        converted.income_daily_stats = convertAmountToYuan(converted.income_daily_stats)
      }
      
      if (converted.expense_daily_stats) {
        converted.expense_daily_stats = convertAmountToYuan(converted.expense_daily_stats)
      }
      break
      
    case 'month':
      // 处理单周统计数据
      if (converted.weekly_stats) {
        converted.weekly_stats = convertAmountToYuan(converted.weekly_stats)
      }
      break
      
    case 'year':
      // 处理月度统计数据
      if (converted.monthly_stats) {
        converted.monthly_stats = convertAmountToYuan(converted.monthly_stats)
      }
      break
  }
  
  return converted
}

// 测试转换函数
function testConversion() {
  const testData = {
    List: [
      {
        Income: { Amount: 5500, Count: 1 },
        Expense: { Amount: 31300, Count: 5 }
      }
    ]
  }
  
  const converted = convertAmountToYuan(testData)
  __f__('log','at common/api/report.ts:430','测试转换:', testData, '->', converted)
} 