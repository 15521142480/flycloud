import { store } from '@/store'
import { defineStore } from 'pinia'
import { getAccessToken, removeToken } from '@/utils/auth'
import { CACHE_KEY, useCache, deleteUserCache } from '@/hooks/web/useCache'
import { getUserInfoApi, loginOut } from '@/api/login'
import * as FileApi from '@/api/infra/file'

const { wsCache } = useCache()

interface UserVO {
  id: string
  avatar: string
  name: string
  deptId: string
}

interface UserInfoVO {
  // USER 缓存
  permissionList: string[]
  roles: string[]
  menuTreeList: AppCustomRouteRecordRaw[]
  isSetUser: boolean
  user: UserVO
}

export const useUserStore = defineStore('admin-user', {
  state: (): UserInfoVO => ({
    permissionList: [],
    roles: [],
    menuTreeList: [],
    isSetUser: false,
    user: {
      id: '',
      avatar: '',
      name: '',
      deptId: '0'
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
      this.applyUserInfo(userInfo)
      if (!wsCache.get(CACHE_KEY.BASE_URL)) {
        wsCache.set(CACHE_KEY.BASE_URL, await FileApi.getFileConfig())
      }
    },

    /**
     * 强制从服务端重新获取当前登录用户的权限和菜单。
     *
     * <p>菜单、角色权限或当前用户角色变化后调用本方法，不能继续读取浏览器缓存，
     * 否则左侧导航仍会使用旧菜单。</p>
     */
    async refreshUserInfoAction() {
      if (!getAccessToken()) {
        this.resetState()
        return null
      }
      const userInfo = await getUserInfoApi()
      this.applyUserInfo(userInfo)
      return userInfo
    },

    /**
     * 将服务端返回的当前用户权限、角色和菜单同步至 Pinia 与浏览器缓存。
     *
     * @param userInfo 当前登录用户的完整授权信息
     */
    applyUserInfo(userInfo: UserInfoVO) {
      this.permissionList = userInfo.permissionList
      this.roles = userInfo.roles
      this.user = userInfo.user
      this.isSetUser = true
      wsCache.set(CACHE_KEY.ROLE_ROUTERS, userInfo.menuTreeList)
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
      try {
        await loginOut()
      } finally {
        removeToken()
        deleteUserCache() // 删除用户缓存
        this.resetState()
      }
    },

    /**
     * 重置state
     */
    resetState() {
      this.permissionList = []
      this.roles = []
      this.menuTreeList = []
      this.isSetUser = false
      this.user = {
        id: '',
        avatar: '',
        name: '',
        deptId: '0'
      }
    }
  }
})

export const useUserStoreWithOut = () => {
  return useUserStore(store)
}
