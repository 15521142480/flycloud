import { store } from '@/store'
import { defineStore } from 'pinia'
import { getAccessToken, removeToken } from '@/utils/auth'
import { CACHE_KEY, useCache, deleteUserCache } from '@/hooks/web/useCache'
import { getUserInfoApi, loginOut } from '@/api/login'

const { wsCache } = useCache()

interface UserVO {
  id: number
  avatar: string
  name: string
  deptId: number
}

interface UserInfoVO {
  // USER 缓存
  permissionList: string[]
  roles: string[]
  isSetUser: boolean
  user: UserVO
}

export const useUserStore = defineStore('admin-user', {
  state: (): UserInfoVO => ({
    permissionList: [],
    roles: [],
    isSetUser: false,
    user: {
      id: 0,
      avatar: '',
      name: '',
      deptId: 0
    }
  }),
  getters: {
    getPermissions(): string[] {
      return this.permissionList
    },
    getRoles(): string[] {
      return this.roles
    },
    getIsSetUser(): boolean {
      return this.isSetUser
    },
    getUser(): UserVO {
      return this.user
    }
  },

  actions: {

    /**
     * 设置用户信息
     */
    async setUserInfoAction() {

      if (!getAccessToken()) {
        this.resetState()
        return null
      }
      let userInfo = wsCache.get(CACHE_KEY.USER)
      if (!userInfo) {
        userInfo = await getUserInfoApi()
      }
      this.permissionList = userInfo.permissionList
      this.roles = userInfo.roles
      this.user = userInfo.user
      this.isSetUser = true
      wsCache.set(CACHE_KEY.ROLE_ROUTERS, userInfo.menuTreeList) // 菜单
      wsCache.set(CACHE_KEY.USER, userInfo)
    },

    /**
     * 设置用户头像
     */
    async setUserAvatarAction(avatar: string) {
      const userInfo = wsCache.get(CACHE_KEY.USER)
      // NOTE: 是否需要像`setUserInfoAction`一样判断`userInfo != null`
      this.user.avatar = avatar
      userInfo.user.avatar = avatar
      wsCache.set(CACHE_KEY.USER, userInfo)
    },

    /**
     * 设置用户昵称
     */
    async setUserNicknameAction(name: string) {
      const userInfo = wsCache.get(CACHE_KEY.USER)
      // NOTE: 是否需要像`setUserInfoAction`一样判断`userInfo != null`
      this.user.name = name
      userInfo.user.name = name
      wsCache.set(CACHE_KEY.USER, userInfo)
    },

    /**
     * 退出
     */
    async loginOut() {
      await loginOut()
      removeToken()
      deleteUserCache() // 删除用户缓存
      this.resetState()
    },

    /**
     * 重置state
     */
    resetState() {
      this.permissionList = []
      this.roles = []
      this.isSetUser = false
      this.user = {
        id: 0,
        avatar: '',
        name: '',
        deptId: 0
      }
    }
  }
})

export const useUserStoreWithOut = () => {
  return useUserStore(store)
}
