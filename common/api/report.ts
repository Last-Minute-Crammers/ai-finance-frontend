import { request } from '../../utils/request'

export function getAIReport(data: {
  type: 'week' | 'month' | 'year',
  stats: any
}) {
  return request({
    url: '/api/report/ai',
    method: 'POST',
    data
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