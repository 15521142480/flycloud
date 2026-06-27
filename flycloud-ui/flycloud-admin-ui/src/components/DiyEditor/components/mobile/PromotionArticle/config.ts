import { ComponentStyle, DiyComponent } from '@/components/DiyEditor/util'
import { useI18n } from '@/hooks/web/useI18n'
/** 营销文章属性 */
const { t } = useI18n()

export interface PromotionArticleProperty {
  // 文章编号
  id: number
  // 组件样式
  style: ComponentStyle
}

// 定义组件
export const component = {
  id: 'PromotionArticle',
  name: t('auto.components.DiyEditor.components.mobile.PromotionArticle.config.k92a89068'),
  icon: 'ph:article-medium',
  property: {
    style: {
      bgType: 'color',
      bgColor: '',
      marginLeft: 8,
      marginRight: 8,
      marginBottom: 8
    } as ComponentStyle
  }
} as DiyComponent<PromotionArticleProperty>
