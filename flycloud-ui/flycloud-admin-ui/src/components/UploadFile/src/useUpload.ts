import * as FileApi from '@/api/infra/file'
import {
  UploadRawFile,
  UploadRequestOptions,
  UploadProgressEvent
} from 'element-plus/es/components/upload/src/upload'
import axios, { AxiosProgressEvent } from 'axios'
import { CACHE_KEY, useCache } from '@/hooks/web/useCache'

const SYS_BASE_URL = import.meta.env.VITE_SYSTEM_SERVER
const { wsCache } = useCache()

export interface UploadResult {
  url: string
  baseUrl: string
  path: string
}

const filePreviewUrlMap = new Map<string, string>()

export const normalizeUploadResult = (res: any): UploadResult => {
  const data = res?.data ?? res
  if (typeof data === 'string') {
    return { url: data, baseUrl: '', path: data }
  }
  return {
    url: data?.url || data?.path || '',
    baseUrl: data?.baseUrl || '',
    path: data?.path || data?.url || ''
  }
}

/**
 * 缓存上传接口返回的真实预览 URL。
 *
 * path 是业务字段的存储值，url 是当前环境下可直接访问的地址；
 * 刚上传后优先使用缓存 URL，可避免前端环境变量和后端 base-url 不一致导致预览失败。
 */
export const cacheFilePreviewUrl = (path?: string, url?: string) => {
  if (path && url) {
    filePreviewUrlMap.set(path, url)
  }
}

/**
 * 将文件 path 转为前端可预览的 URL。
 *
 * 上传组件保存给业务表单的是 path；当表单回显或 DIY 编辑器直接预览 path 时，
 * 需要临时拼接静态资源前缀用于展示，避免把完整 URL 写回业务字段。
 */
export const getFilePreviewUrl = (pathOrUrl?: unknown): string => {
  if (!pathOrUrl) {
    return ''
  }
  if (typeof pathOrUrl !== 'string') {
    const value = (pathOrUrl as { url?: unknown; path?: unknown })?.url ??
      (pathOrUrl as { url?: unknown; path?: unknown })?.path
    return typeof value === 'string' ? getFilePreviewUrl(value) : ''
  }
  if (/^[a-z][a-z0-9-]*:[\w-]+$/i.test(pathOrUrl)) {
    return pathOrUrl
  }
  const cachedUrl = filePreviewUrlMap.get(pathOrUrl)
  if (cachedUrl) {
    return cachedUrl
  }
  const lowerValue = pathOrUrl.toLowerCase()
  if (
    lowerValue.startsWith('http://') ||
    lowerValue.startsWith('https://') ||
    lowerValue.startsWith('//') ||
    lowerValue.startsWith('data:') ||
    lowerValue.startsWith('blob:')
  ) {
    return pathOrUrl
  }
  const fileConfig = wsCache.get(CACHE_KEY.BASE_URL) as FileApi.FileConfigRespVO | undefined
  const baseUrl = (fileConfig?.baseUrl || `${import.meta.env.VITE_BASE_URL}/static/`).replace(
    /\/$/,
    ''
  )
  return `${baseUrl}/${pathOrUrl.replace(/^\//, '')}`
}

/**
 * 将文件 path 数组转为前端可预览的 URL 数组。
 */
export const getFilePreviewUrls = (pathOrUrls?: string[]): string[] => {
  return pathOrUrls?.map((item) => getFilePreviewUrl(item)).filter(Boolean) || []
}

/**
 * 获得上传 URL
 */
export const getUploadUrl = (): string => {
  return import.meta.env.VITE_BASE_URL + `/${SYS_BASE_URL}/admin/file/upload`
}

export const useUpload = (directory?: string) => {
  // 后端上传地址
  const uploadUrl = getUploadUrl()
  // 是否使用前端直连上传
  const isClientUpload = UPLOAD_TYPE.CLIENT === import.meta.env.VITE_UPLOAD_TYPE
  // 重写ElUpload上传方法
  const httpRequest = async (options: UploadRequestOptions) => {
    // 文件上传进度监听
    const uploadProgressHandler = (evt: AxiosProgressEvent) => {
      const upEvt: UploadProgressEvent = Object.assign(evt.event)
      upEvt.percent = evt.progress ? evt.progress * 100 : 0
      options.onProgress?.(upEvt) // 触发 el-upload 的 on-progress
    }

    // 模式一：前端上传
    if (isClientUpload) {
      // 1.1 生成文件名称
      const fileName = options.file.name || options.filename
      // 1.2 获取文件预签名地址
      const presignedInfo = await FileApi.getFilePresignedUrl(fileName, directory)
      // 1.3 上传文件（不能使用 ElUpload 的 ajaxUpload 方法的原因：其使用的是 FormData 上传，Minio 不支持）
      return axios
        .put(presignedInfo.uploadUrl, options.file, {
          headers: {
            'Content-Type': options.file.type || 'application/octet-stream'
          },
          onUploadProgress: uploadProgressHandler
        })
        .then(() => {
          // 1.4. 记录文件信息到后端（异步）
          createFile(presignedInfo, options.file, fileName)
          // 通知成功，数据格式保持与后端上传的返回结果一致
          return {
            data: {
              url: presignedInfo.url,
              baseUrl: '',
              path: presignedInfo.path || presignedInfo.url
            }
          }
        })
    } else {
      // 模式二：后端上传
      // 重写 el-upload httpRequest 文件上传成功会走成功的钩子，失败走失败的钩子
      return new Promise((resolve, reject) => {
        FileApi.updateFile({ file: options.file, directory }, uploadProgressHandler)
          .then((res) => {
            if (res.code === 0) {
              resolve(res)
            } else {
              reject(res)
            }
          })
          .catch((res) => {
            reject(res)
          })
      })
    }
  }

  return {
    uploadUrl,
    httpRequest
  }
}

/**
 * 创建文件信息
 * @param vo 文件预签名信息
 * @param file 文件
 * @param fileName
 */
function createFile(vo: FileApi.FilePresignedUrlRespVO, file: UploadRawFile, fileName: string) {
  const fileVo = {
    configId: vo.configId,
    url: vo.url,
    path: vo.path,
    name: fileName,
    type: file.type || 'application/octet-stream',
    size: file.size
  }
  FileApi.createFile(fileVo)
  return fileVo
}

/**
 * 上传类型
 */
enum UPLOAD_TYPE {
  // 客户端直接上传（只支持S3服务）
  CLIENT = 'client',
  // 客户端发送到后端上传
  SERVER = 'server'
}
