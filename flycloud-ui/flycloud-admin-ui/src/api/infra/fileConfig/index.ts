import request from '@/config/axios'

export interface FileClientConfig {
  basePath: string
  host?: string
  port?: number
  username?: string
  password?: string
  mode?: string
  endpoint?: string
  bucket?: string
  accessKey?: string
  accessSecret?: string
  domain: string
}

export interface FileConfigVO {
  id: string
  name: string
  storage?: number
  master: boolean
  visible: boolean
  config: FileClientConfig
  remark: string
  createTime: Date
}

// 查询文件配置列表
export const getFileConfigPage = (params: PageParam) => {
  return request.get({ url: '/infra/file-config/page', params })
}

// 查询文件配置详情
export const getFileConfig = (id: string) => {
  return request.get({ url: '/infra/file-config/get/' + id })
}

// 更新文件配置为主配置
export const updateFileConfigMaster = (id: string) => {
  return request.put({ url: '/infra/file-config/update-master/' + id })
}

// 新增文件配置
export const createFileConfig = (data: FileConfigVO) => {
  return request.post({ url: '/infra/file-config/create', data })
}

// 修改文件配置
export const updateFileConfig = (data: FileConfigVO) => {
  return request.put({ url: '/infra/file-config/update', data })
}

// 删除文件配置
export const deleteFileConfig = (id: string) => {
  return request.delete({ url: '/infra/file-config/delete/' + id })
}

// 测试文件配置
export const testFileConfig = (id: string) => {
  return request.get({ url: '/infra/file-config/test/' + id })
}
