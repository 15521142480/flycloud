export interface PageParam {
  pageNum: number
  pageSize: number
  [key: string]: unknown
}

export interface PageResult<T> {
  list: T[]
  total: number | string
  pageNum: number | string
  pageSize: number | string
}

export interface BaseEntity {
  id?: number
  status?: number
  createTime?: string
  remark?: string
}
