import axios from '../util/http'
import auth from './auth-base'

export default {

  // 登录
  loginApi (params) {
    return axios.post(
      `${auth.dev}/oauth/token`,
      JSON.stringify(params),
      {
        headers: {
          'Code-Key': params.codeKey,
          'Code-Value': params.codeValue
        }
      }
    )
  },

  // 登出
  loginOutApi (params) {
    return axios.post(`${auth.dev}/oauth/logOut`, JSON.stringify(params))
  },

  // 获取验证码
  getCodeApi (params) {
    return axios.get(`${auth.dev}/auth/code`, JSON.stringify(params))
  }

}
