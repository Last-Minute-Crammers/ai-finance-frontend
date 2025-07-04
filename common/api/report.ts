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