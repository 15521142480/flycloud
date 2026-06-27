import { isEmpty } from '@/utils/is'
import { useI18n } from '@/hooks/web/useI18n'
const { t } = useI18n()

const emojiList = [
  { name: t('auto.views.mall.promotion.kefu.components.tools.ka39f1f1e'), file: 'xiaodiaoya.png' },
  { name: t('auto.views.mall.promotion.kefu.components.tools.kf8fe8fbc'), file: 'keai.png' },
  { name: t('auto.views.mall.promotion.kefu.components.tools.k38ff7ff1'), file: 'lengku.png' },
  { name: t('auto.views.mall.promotion.kefu.components.tools.k900c7890'), file: 'bizui.png' },
  { name: t('auto.views.mall.promotion.kefu.components.tools.kdf4184a7'), file: 'shengqi.png' },
  { name: t('auto.views.mall.promotion.kefu.components.tools.k18a027e4'), file: 'jingkong.png' },
  { name: t('auto.views.mall.promotion.kefu.components.tools.k443bff27'), file: 'keshui.png' },
  { name: t('auto.views.mall.promotion.kefu.components.tools.k30a89e7d'), file: 'daxiao.png' },
  { name: t('auto.views.mall.promotion.kefu.components.tools.kc107d25f'), file: 'aixin.png' },
  { name: t('auto.views.mall.promotion.kefu.components.tools.kd389d14a'), file: 'huaixiao.png' },
  { name: t('auto.views.mall.promotion.kefu.components.tools.k4bc87f61'), file: 'feiwen.png' },
  { name: t('auto.views.mall.promotion.kefu.components.tools.k1c14a8cd'), file: 'yiwen.png' },
  { name: t('auto.views.mall.promotion.kefu.components.tools.k27a7e338'), file: 'kaixin.png' },
  { name: t('auto.views.mall.promotion.kefu.components.tools.k273067c5'), file: 'fadai.png' },
  { name: t('auto.views.mall.promotion.kefu.components.tools.k15d337f5'), file: 'liulei.png' },
  { name: t('auto.views.mall.promotion.kefu.components.tools.ka8923d89'), file: 'hanyan.png' },
  { name: t('auto.views.mall.promotion.kefu.components.tools.k36772c75'), file: 'jingshu.png' },
  { name: t('auto.views.mall.promotion.kefu.components.tools.k2709a7e7'), file: 'kun.png' },
  { name: t('auto.views.mall.promotion.kefu.components.tools.k7997dac0'), file: 'xinsui.png' },
  { name: t('auto.views.mall.promotion.kefu.components.tools.k4ea891ef'), file: 'tianshi.png' },
  { name: t('auto.views.mall.promotion.kefu.components.tools.k0878ac7e'), file: 'yun.png' },
  { name: t('auto.views.mall.promotion.kefu.components.tools.kfb67796d'), file: 'a.png' },
  { name: t('auto.views.mall.promotion.kefu.components.tools.k49ed1af3'), file: 'fennu.png' },
  { name: t('auto.views.mall.promotion.kefu.components.tools.k2b1304ee'), file: 'shuizhuo.png' },
  {
    name: t('auto.views.mall.promotion.kefu.components.tools.k7a9e1796'),
    file: 'mianwubiaoqing.png'
  },
  { name: t('auto.views.mall.promotion.kefu.components.tools.k1ed44004'), file: 'nanguo.png' },
  { name: t('auto.views.mall.promotion.kefu.components.tools.kc91eaf83'), file: 'fankun.png' },
  { name: t('auto.views.mall.promotion.kefu.components.tools.k33421c7b'), file: 'haochi.png' },
  { name: t('auto.views.mall.promotion.kefu.components.tools.kddee3d93'), file: 'outu.png' },
  { name: t('auto.views.mall.promotion.kefu.components.tools.ke9f53e51'), file: 'ziya.png' },
  { name: t('auto.views.mall.promotion.kefu.components.tools.k41af7bbb'), file: 'mengbi.png' },
  { name: t('auto.views.mall.promotion.kefu.components.tools.kef445d74'), file: 'baiyan.png' },
  { name: t('auto.views.mall.promotion.kefu.components.tools.kc584693c'), file: 'esi.png' },
  { name: t('auto.views.mall.promotion.kefu.components.tools.ka66bcc06'), file: 'xiong.png' },
  { name: t('auto.views.mall.promotion.kefu.components.tools.kaa3dae67'), file: 'ganmao.png' },
  { name: t('auto.views.mall.promotion.kefu.components.tools.k060d2ba4'), file: 'liuhan.png' },
  { name: t('auto.views.mall.promotion.kefu.components.tools.kc3745561'), file: 'xiaoku.png' },
  { name: t('auto.views.mall.promotion.kefu.components.tools.k90139a95'), file: 'liukoushui.png' },
  { name: t('auto.views.mall.promotion.kefu.components.tools.kb195ca10'), file: 'ganga.png' },
  { name: t('auto.views.mall.promotion.kefu.components.tools.ka1667093'), file: 'jingya.png' },
  { name: t('auto.views.mall.promotion.kefu.components.tools.kb960b378'), file: 'dajing.png' },
  { name: t('auto.views.mall.promotion.kefu.components.tools.kcb7687eb'), file: 'buhaoyisi.png' },
  { name: t('auto.views.mall.promotion.kefu.components.tools.kb0f6e1be'), file: 'danao.png' },
  { name: t('auto.views.mall.promotion.kefu.components.tools.k417979ac'), file: 'bukesiyi.png' },
  { name: t('auto.views.mall.promotion.kefu.components.tools.k047d2fce'), file: 'aini.png' },
  { name: t('auto.views.mall.promotion.kefu.components.tools.ka62d5039'), file: 'hongxin.png' },
  { name: t('auto.views.mall.promotion.kefu.components.tools.k53bf189c'), file: 'dianzan.png' },
  { name: t('auto.views.mall.promotion.kefu.components.tools.ke8d88ad3'), file: 'emo.png' }
]

export interface Emoji {
  name: string
  url: string
}

export const useEmoji = () => {
  const emojiPathList = ref<any[]>([])

  /** 加载本地图片 */
  const initStaticEmoji = async () => {
    const pathList = import.meta.glob(
      '@/views/mall/promotion/kefu/components/asserts/*.{png,jpg,jpeg,svg}'
    )
    for (const path in pathList) {
      const imageModule: any = await pathList[path]()
      emojiPathList.value.push({ path: path, src: imageModule.default })
    }
  }

  /** 初始化 */
  onMounted(async () => {
    if (isEmpty(emojiPathList.value)) {
      await initStaticEmoji()
    }
  })

  /**
   * 将文本中的表情替换成图片
   *
   * @return 替换后的文本
   * @param content 消息内容
   */
  const replaceEmoji = (content: string) => {
    let newData = content
    if (typeof newData !== 'object') {
      const reg = /\[(.+?)]/g // [] 中括号
      const zhEmojiName = newData.match(reg)
      if (zhEmojiName) {
        zhEmojiName.forEach((item) => {
          const emojiFile = getEmojiFileByName(item)
          newData = newData.replace(
            item,
            `<img class="chat-img" style="width: 24px;height: 24px;margin: 0 3px;" src="${emojiFile}" alt=""/>`
          )
        })
      }
    }
    return newData
  }

  /**
   * 获得所有表情
   *
   * @return 表情列表
   */
  function getEmojiList(): Emoji[] {
    return emojiList.map((item) => ({
      url: getEmojiFileByName(item.name),
      name: item.name
    })) as Emoji[]
  }

  function getEmojiFileByName(name: string) {
    for (const emoji of emojiList) {
      if (emoji.name === name) {
        const emojiPath = emojiPathList.value.find(
          (item: { path: string; src: string }) => item.path.indexOf(emoji.file) > -1
        )
        return emojiPath ? emojiPath.src : undefined
      }
    }
    return false
  }

  return { replaceEmoji, getEmojiList }
}
