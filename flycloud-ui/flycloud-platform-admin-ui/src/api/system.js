import axios from '../util/http'
import config from '../config'

export default {

  // 登录
  loginApi (params) {
    return axios.post(
      `${config.authBaseUrl.dev}/oauth/token`,
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
    return axios.post(`${config.authBaseUrl.dev}/oauth/logOut`, JSON.stringify(params))
  },

  // 获取验证码
  getCodeApi (params) {
    return axios.get(`${config.authBaseUrl.dev}/auth/code`, JSON.stringify(params))
  }

}
