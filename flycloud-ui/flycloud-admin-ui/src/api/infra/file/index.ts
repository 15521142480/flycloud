import request from '@/config/axios'
const SYS_BASE_URL = import.meta.env.VITE_SYSTEM_SERVER

export interface FilePageReqVO extends PageParam {
  path?: string
  type?: string
  createTime?: Date[]
}

// 文件预签名地址 Response VO
export interface FilePresignedUrlRespVO {
  // 文件配置编号
  configId: number
  // 文件上传 URL
  uploadUrl: string
  // 文件 URL
  url: string
}

// 查询文件列表
export const getFilePage = (params: FilePageReqVO) => {
  return request.get({ url: `/${SYS_BASE_URL}/file/page`, params })
}

// 删除文件
export const deleteFile = (id: number) => {
  return request.delete({ url: `/${SYS_BASE_URL}/file/delete/` + id })
}

// 获取文件预签名地址
export const getFilePresignedUrl = (path: string) => {
  return request.get<FilePresignedUrlRespVO>({
    url: `/${SYS_BASE_URL}/file/presigned-url`,
    params: { path }
  })
}

// 创建文件
export const createFile = (data: any) => {
  return request.post({ url: `/${SYS_BASE_URL}/file/create`, data })
}

// 上传文件
export const updateFile = (data: any) => {
  return request.upload({ url: `/${SYS_BASE_URL}/admin/file/upload`, data })
}
