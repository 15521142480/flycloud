
import axios from '../util/http'
import base from './base'
import qs from 'qs'

export default {

  // 登录
  loginApi (params) {
    return axios.post(`${base.dev}/login`, qs.stringify(params));
  },

  // 登出
  loginOutApi (params) {
    return axios.post(`${base.dev}/loginOut`, qs.stringify(params));
  }

}
