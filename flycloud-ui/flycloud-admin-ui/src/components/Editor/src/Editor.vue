<script lang="ts" setup>
import { PropType } from 'vue'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import { i18nChangeLanguage, IDomEditor, IEditorConfig } from '@wangeditor/editor'
import { propTypes } from '@/utils/propTypes'
import { isNumber } from '@/utils/is'
import { ElMessage } from 'element-plus'
import { useLocaleStore } from '@/store/modules/locale'
import { normalizeUploadResult, useUpload } from '@/components/UploadFile/src/useUpload'
const { t } = useI18n()
defineOptions({ name: 'Editor' })

type InsertFnType = (url: string, alt: string, href: string) => void

const localeStore = useLocaleStore()

const currentLocale = computed(() => localeStore.getCurrentLocale)

i18nChangeLanguage(unref(currentLocale).lang)

const props = defineProps({
  editorId: propTypes.string.def('wangeEditor-1'),
  height: propTypes.oneOfType([Number, String]).def('500px'),
  editorConfig: {
    type: Object as PropType<Partial<IEditorConfig>>,
    default: () => undefined
  },
  readonly: propTypes.bool.def(false),
  modelValue: propTypes.string.def(''),
  directory: propTypes.string.def('editor-default')
})

const emit = defineEmits(['change', 'update:modelValue'])

const { httpRequest: imageHttpRequest } = useUpload(`${props.directory}-image`)
const { httpRequest: videoHttpRequest } = useUpload(`${props.directory}-video`)

// 编辑器实例，必须用 shallowRef
const editorRef = shallowRef<IDomEditor>()

const valueHtml = ref('')
const uploading = ref(false)
const uploadNumber = ref(0)

const startUpload = (file: File) => {
  uploadNumber.value++
  uploading.value = true
  return file
}

const finishUpload = () => {
  uploadNumber.value = Math.max(0, uploadNumber.value - 1)
  uploading.value = uploadNumber.value > 0
}

const uploadByHttpRequest = async (
  file: File,
  insertFn: InsertFnType,
  httpRequest: ReturnType<typeof useUpload>['httpRequest'],
  type: 'image' | 'mp4',
  errorMessage: string
) => {
  startUpload(file)
  try {
    const res = await httpRequest({
      file: file as any,
      onProgress: () => {},
      onSuccess: () => {},
      onError: () => {}
    } as any)
    const uploadResult = normalizeUploadResult(res)
    insertFn(uploadResult.url, type, uploadResult.url)
  } catch (error: any) {
    ElMessage.error(error?.msg || error?.message || errorMessage)
    console.error('Upload error:', error)
  } finally {
    finishUpload()
  }
}

watch(
  () => props.modelValue,
  (val: string) => {
    if (val === unref(valueHtml)) return
    valueHtml.value = val
  },
  {
    immediate: true
  }
)

// 监听
watch(
  () => valueHtml.value,
  (val: string) => {
    emit('update:modelValue', val)
  }
)

const handleCreated = (editor: IDomEditor) => {
  editorRef.value = editor
}

// 编辑器配置
const editorConfig = computed((): IEditorConfig => {
  return Object.assign(
    {
      placeholder: t('auto.components.Editor.src.Editor.ke708d913'),
      readOnly: props.readonly,
      customAlert: (s: string, t: string) => {
        switch (t) {
          case 'success':
            ElMessage.success(s)
            break
          case 'info':
            ElMessage.info(s)
            break
          case 'warning':
            ElMessage.warning(s)
            break
          case 'error':
            ElMessage.error(s)
            break
          default:
            ElMessage.info(s)
            break
        }
      },
      autoFocus: false,
      scroll: true,
      MENU_CONF: {
        ['uploadImage']: {
          // 单个文件的最大体积限制，默认为 2M
          maxFileSize: 20 * 1024 * 1024,
          // 最多可上传几个文件，默认为 100
          maxNumberOfFiles: 10,
          // 选择文件时的类型限制，默认为 ['image/*'] 。如不想限制，则设置为 []
          allowedFileTypes: ['image/*'],

          // 使用 customUpload 实现逐个文件上传，复用项目的 httpRequest
          customUpload(file: File, insertFn: InsertFnType) {
            return uploadByHttpRequest(file, insertFn, imageHttpRequest, 'image', '图片上传失败')
          }
        },
        ['uploadVideo']: {
          // 单个文件的最大体积限制，默认为 10M
          maxFileSize: 100 * 1024 * 1024,
          // 最多可上传几个文件，默认为 100
          maxNumberOfFiles: 10,
          // 选择文件时的类型限制，默认为 ['video/*'] 。如不想限制，则设置为 []
          allowedFileTypes: ['video/*'],

          // 使用 customUpload 实现逐个文件上传，复用项目的 httpRequest
          customUpload(file: File, insertFn: InsertFnType) {
            return uploadByHttpRequest(file, insertFn, videoHttpRequest, 'mp4', '视频上传失败')
          }
        }
      },
      uploadImgShowBase64: true
    },
    props.editorConfig || {}
  )
})

const editorStyle = computed(() => {
  return {
    height: isNumber(props.height) ? `${props.height}px` : props.height
  }
})

// 回调函数
const handleChange = (editor: IDomEditor) => {
  emit('change', editor)
}

// 组件销毁时，及时销毁编辑器
onBeforeUnmount(() => {
  const editor = unref(editorRef.value)

  // 销毁，并移除 editor
  editor?.destroy()
})

const getEditorRef = async (): Promise<IDomEditor> => {
  await nextTick()
  return unref(editorRef.value) as IDomEditor
}

defineExpose({
  getEditorRef
})
</script>

<template>
  <div v-loading="uploading" class="border-1 border-solid border-[var(--tags-view-border-color)] z-10">
    <!-- 工具栏 -->
    <Toolbar
      :editor="editorRef"
      :editorId="editorId"
      class="border-0 b-b-1 border-solid border-[var(--tags-view-border-color)]"
    />
    <!-- 编辑器 -->
    <Editor
      v-model="valueHtml"
      :defaultConfig="editorConfig"
      :editorId="editorId"
      :style="editorStyle"
      @on-change="handleChange"
      @on-created="handleCreated"
    />
  </div>
</template>

<style src="@wangeditor/editor/dist/css/style.css"></style>
