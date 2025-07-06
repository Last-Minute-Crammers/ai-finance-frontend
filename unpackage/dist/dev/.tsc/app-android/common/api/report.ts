import { request } from '../../utils/request'

// 获取AI报告所需的完整统计数据
async function getCompleteStatistics(type: any): Promise<any> {
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
    
    const completeStats: any = {
      base_statistics: baseStatsConverted,
      income_category_stats: incomeCategoryStatsConverted,
      expense_category_stats: expenseCategoryStatsConverted,
      ...detailStatsConverted
    }
    
    return completeStats
    
  } catch (error) {
    __f__('error','at common/api/report.ts:78','获取统计数据失败:', error)
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
async function getDetailStatistics(type: any, startTime: any, endTime: any): Promise<any> {
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
    __f__('error','at common/api/report.ts:159',`获取${type}细粒度数据失败:`, error)
    return getEmptyDetailStats(type)
  }
}

// 获取空的细粒度数据
function getEmptyDetailStats(type: any): any {
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

export async function getAIReport(data: any): Promise<any> {
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

export function getHistoryReport(type: any, period: any): Promise<any> {
  return request({
    url: '/api/user/ai/history',
    method: 'GET',
    params: { type, period },
    requireAuth: true
  })
}

// 获取用户财务统计数据
export function getUserStatistics(type: any): Promise<any> {
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

export function getTotalStatistics(): Promise<any> {
  return request({
    url: '/api/user/transaction/statistic/total',
    method: 'GET',
    requireAuth: true
  })
}

function getTimeRange(type: any): any {
  const now = new Date()
  let startTime: any
  let endTime: any = new Date(now)
  
  switch (type) {
    case 'week':
      const day = now.getDay()
      const diff = now.getDate() - day + (day === 0 ? -6 : 1)
      startTime = new Date(now.getFullYear(), now.getMonth(), diff)
      break
    case 'month':
      startTime = new Date(now.getFullYear(), now.getMonth(), 1)
      break
    case 'year':
      startTime = new Date(now.getFullYear(), 0, 1)
      break
    default:
      startTime = new Date(now.getFullYear(), now.getMonth(), 1)
  }
  return {
    startTime: startTime.toISOString(),
    endTime: endTime.toISOString()
  }
}

function convertAmountToYuan(data: any): any {
  if (!data) return data
  if (Array.isArray(data)) {
    return data.map(convertAmountToYuan)
  }
  if (typeof data === 'object') {
    const result: any = {}
    for (const key in data) {
      if (typeof data[key] === 'number' && key.toLowerCase().includes('amount')) {
        result[key] = Math.round(data[key]) / 100
      } else {
        result[key] = convertAmountToYuan(data[key])
      }
    }
    return result
  }
  return data
}

function convertCategoryStatsToYuan(data: any): any {
  if (!data) return data
  if (Array.isArray(data)) {
    return data.map(convertCategoryStatsToYuan)
  }
  if (typeof data === 'object') {
    const result: any = {}
    for (const key in data) {
      if (key === 'Amount' && typeof data[key] === 'number') {
        result[key] = Math.round(data[key]) / 100
      } else {
        result[key] = convertCategoryStatsToYuan(data[key])
      }
    }
    return result
  }
  return data
}

function convertDetailStatsToYuan(data: any, type: any): any {
  if (!data) return data
  if (type === 'week') {
    return {
      income_daily_stats: convertAmountToYuan(data.income_daily_stats),
      expense_daily_stats: convertAmountToYuan(data.expense_daily_stats)
    }
  } else if (type === 'month') {
    return {
      weekly_stats: convertAmountToYuan(data.weekly_stats)
    }
  } else if (type === 'year') {
    return {
      monthly_stats: convertAmountToYuan(data.monthly_stats)
    }
  }
  return data
}

function testConversion(): void {
  // 测试用例略
} 