import { DiyComponent } from '@/components/DiyEditor/util'
import { useI18n } from '@/hooks/web/useI18n'
/** 页面设置属性 */
const { t } = useI18n()

export interface PageConfigProperty {
  // 页面描述
  description: string
  // 页面背景颜色
  backgroundColor: string
  // 页面背景图片
  backgroundImage: string
}

// 定义页面组件
export const component = {
  id: 'PageConfig',
  name: t('auto.components.DiyEditor.components.mobile.PageConfig.config.k8ee1e316'),
  icon: 'ep:document',
  property: {
    description: '',
    backgroundColor: '#f5f5f5',
    backgroundImage: ''
  }
} as DiyComponent<PageConfigProperty>
