import request from '@/config/axios'

const SYS_BASE_URL = import.meta.env.VITE_SYSTEM_SERVER
const BASE_URL = `/${SYS_BASE_URL}/admin/member/user/search`

/** 会员用户 ES 搜索结果。 */
export interface MemberUserSearchVO {
  id: string
  mobile: string
  email?: string
  status: number
  nickname: string
  name?: string
  avatar?: string
  tagIds?: string[]
  postIds?: string[]
  levelId?: string
  groupId?: string
  mark?: string
  createTime?: string
  updateTime?: string
}

/** 会员用户 ES 分页条件。 */
export interface MemberUserSearchPageParams extends PageParam {
  id?: string
  mobile?: string
  email?: string
  keyword?: string
  status?: number
  groupId?: string
  levelId?: string
  sortField?: 'id' | 'createTime' | 'updateTime'
  sortOrder?: 'ASC' | 'DESC'
}

/** 新增会员并建立 ES 投影的请求。 */
export interface MemberUserSearchInsertReq {
  mobile: string
  email?: string
  nickname: string
  name?: string
  status: number
  mark?: string
  tagIds?: string[]
  postIds?: string[]
  levelId?: string
  groupId?: string
}

/** 修改会员并异步更新 ES 投影的请求。 */
export interface MemberUserSearchUpdateReq {
  id: string
  nickname?: string
  name?: string
  mark?: string
  status?: number
  postIds?: string[]
}

/** 初始化或全量同步会员索引。 */
export const synchronize = () => request.post<string>({ url: `${BASE_URL}/synchronize` })

/** 新增会员，并由 RocketMQ 异步更新 ES。 */
export const insertMemberUser = (data: MemberUserSearchInsertReq) =>
  request.post<string>({ url: `${BASE_URL}/insert`, data })

/** 查询会员 ES 分页列表。 */
export const getPage = (params: MemberUserSearchPageParams) =>
  request.get<PageResult<MemberUserSearchVO[]>>({ url: `${BASE_URL}/page`, params })

/** 修改会员，并由 RocketMQ 异步更新 ES。 */
export const updateMemberUser = (data: MemberUserSearchUpdateReq) =>
  request.put({ url: `${BASE_URL}/update`, data })

/** 创建新版本索引并原子切换 Alias。 */
export const upgradeIndex = () => request.post<string>({ url: `${BASE_URL}/upgrade-index` })

/** 在观察期内回滚指定的索引升级记录。 */
export const rollbackIndex = (recordId: string) =>
  request.post({ url: `${BASE_URL}/rollback-index/${recordId}` })
