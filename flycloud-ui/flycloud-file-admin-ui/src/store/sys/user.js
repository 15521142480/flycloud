import { getToken, setAccess } from '@/libs/util'

export default {
  state: {
    userName: '',
    userId: '',
    avatorImgPath: '',
    token: getToken(),
    access: [],
    hasGetInfo: false,
    messageUnreadList: [],
    messageReadedList: [],
    messageTrashList: [],
    messageContentStore: {}
  },
  mutations: {
    setAvator (state, avatorPath) {
      state.avatorImgPath = avatorPath
    },
    setUserId (state, id) {
      state.userId = id
    },
    setUserName (state, name) {
      state.userName = name
    },
    setAccess (state, access) {
      state.access = access
      setAccess(access)
    }
  },
  getters: {
    messageUnreadCount: state => state.messageUnreadList.length,
    messageReadedCount: state => state.messageReadedList.length,
    messageTrashCount: state => state.messageTrashList.length
  },
  actions: {
    // 获取用户相关信息
    getUserInfo ({ state, commit }) {
      return new Promise((resolve, reject) => {
        try {
          this.$api.user.getUserInfoApi().then(res => {
            const data = res.data
            if (data.httpCode === 200) {
              // commit('setAvator', minLogo)
              commit('setUserName', data.data.managerName)
              commit('setUserId', data.data.id)
              commit('setAccess', data.data.permissionList)
              commit('setHasGetInfo', true)
            }
            resolve(data)
          }).catch(err => {
            reject(err)
          })
        } catch (error) {
          reject(error)
        }
      })
    }
  }
}
