import { baseInstance } from './request'

export function lookup (table_name) {
  return baseInstance({
    url: '/lookup/' + table_name,
    method: 'get',
    params: {
      range: 'all'
    }
  })
}
