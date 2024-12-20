import request from '@/config/axios'

export interface TagVO {
  id?: number
  name: string
  accountId: number
  createTime: Date
}

// 创建公众号标签
export const createTag = (data: TagVO) => {
  return request.post({
    url: '/mp/tag/create',
    data: data
  })
}

// 更新公众号标签
export const updateTag = (data: TagVO) => {
  return request.put({
    url: '/mp/tag/update',
    data: data
  })
}

// 删除公众号标签
export const deleteTag = (id: number) => {
  return request.delete({
    url: '/mp/tag/delete/' + id
  })
}

// 获得公众号标签
export const getTag = (id: number) => {
  return request.get({
    url: '/mp/tag/get/' + id
  })
}

// 获得公众号标签分页
export const getTagPage = (query: PageParam) => {
  return request.get({
    url: '/mp/tag/page',
    params: query
  })
}

// 获取公众号标签精简信息列表
export const getSimpleTagList = () => {
  return request.get({
    url: '/mp/tag/list-all-simple'
  })
}

// 同步公众号标签
export const syncTag = (accountId: number) => {
  return request.post({
    url: '/mp/tag/sync?accountId=' + accountId
  })
}
