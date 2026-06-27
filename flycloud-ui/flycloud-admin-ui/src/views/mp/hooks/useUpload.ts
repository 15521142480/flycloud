import type { UploadRawFile } from 'element-plus'
import { useI18n } from '@/hooks/web/useI18n'
const { t } = useI18n()
const message = useMessage() // 消息

enum UploadType {
  Image = 'image',
  Voice = 'voice',
  Video = 'video'
}

const useBeforeUpload = (type: UploadType, maxSizeMB: number) => {
  const fn = (rawFile: UploadRawFile): boolean => {
    let allowTypes: string[] = []
    let name = ''

    switch (type) {
      case UploadType.Image:
        allowTypes = ['image/jpeg', 'image/png', 'image/gif', 'image/bmp', 'image/jpg']
        maxSizeMB = 2
        name = t('auto.views.mp.hooks.useUpload.kbe8da62e')
        break
      case UploadType.Voice:
        allowTypes = ['audio/mp3', 'audio/mpeg', 'audio/wma', 'audio/wav', 'audio/amr']
        maxSizeMB = 2
        name = t('auto.views.mp.hooks.useUpload.k7a73e125')
        break
      case UploadType.Video:
        allowTypes = ['video/mp4']
        maxSizeMB = 10
        name = t('auto.views.mp.hooks.useUpload.kfa4e33b6')
        break
    }
    // 格式不正确
    if (!allowTypes.includes(rawFile.type)) {
      message.error(t('extra.ka699eb8d', { p0: name }))
      return false
    }
    // 大小不正确
    if (rawFile.size / 1024 / 1024 > maxSizeMB) {
      message.error(t('extra.k62e74bd3', { p0: name, p1: maxSizeMB }))
      return false
    }

    return true
  }

  return fn
}

export { UploadType, useBeforeUpload }
