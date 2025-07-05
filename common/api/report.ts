import { request } from '../../utils/request'

export function getAIReport(data: {
  type: 'week' | 'month' | 'year',
  stats: any
}) {
  // 如果stats为空，则使用正确的时间范围
  if (!data.stats || Object.keys(data.stats).length === 0) {
    const { startTime, endTime } = getTimeRange(data.type)
    data.stats = {
      start_time: startTime,
      end_time: endTime,
      type: data.type
    }
  }
  
  return request({
    url: '/api/user/report/ai',
    method: 'POST',
    data,
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
      // 获取本周开始（周一）
      const day = now.getDay()
      const diff = now.getDate() - day + (day === 0 ? -6 : 1) // 如果是周日，则减6天；否则减(day-1)天
      startTime = new Date(now.getFullYear(), now.getMonth(), diff)
      break
    case 'month':
      // 获取本月开始
      startTime = new Date(now.getFullYear(), now.getMonth(), 1)
      break
    case 'year':
      // 获取本年开始
      startTime = new Date(now.getFullYear(), 0, 1)
      break
    default:
      startTime = new Date(now.getFullYear(), now.getMonth(), 1)
  }
  
  return {
    startTime: startTime.toISOString().split('T')[0],
    endTime: endTime.toISOString().split('T')[0]
  }
} 