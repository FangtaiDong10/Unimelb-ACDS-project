import { baseInstance } from './request'

export function getKs (table_name) {
  return baseInstance({
    url: '/audit/beamInfo/' + table_name,
    method: 'get'
  })
}
