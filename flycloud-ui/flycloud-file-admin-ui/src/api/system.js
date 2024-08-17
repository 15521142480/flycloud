import axios from '../util/http'
import auth from './auth-base'

export default {

  // 登录
  loginApi (params) {
    return axios.post(`${auth.dev}/oauth/token`, JSON.stringify(params))
  },

  // 登出
  loginOutApi (params) {
    return axios.post(`${auth.dev}/oauth/logOut`, JSON.stringify(params))
  }

}
