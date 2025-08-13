import axios from '../util/http'
import config from '../config'
// import qs from 'qs'

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
  },

  // ============================================== 用户/菜单/角色 start
  /**
   * 用户详情信息
   */
  getUserDetailInfoApi (userId) {
    return axios.request({
      url: `${config.baseUrl.dev}/user/getDetailInfo/${userId}`,
      method: 'get'
    })
  },

  /**
   * 用户列表
   */
  getUserListApi (params) {
    return axios.request({
      url: `${config.baseUrl.dev}/user/list`,
      method: 'get',
      params: params
    })
  },

  /**
   * 用户详情
   */
  getUserDetailApi (userId) {
    return axios.request({
      url: `${config.baseUrl.dev}/user/${userId}`,
      method: 'get'
    })
  },

  /**
   * 用户新增/修改
   */
  saveOrUpdateUserApi (params) {
    return axios.request({
      url: `${config.baseUrl.dev}/user/saveOrUpdate`,
      method: 'post',
      data: params
    })
  },

  /**
   * 用户禁用启用
   */
  updateUserEnableApi (params) {
    return axios.request({
      url: `${config.baseUrl.dev}/user/enable`,
      method: 'post',
      params: params
    })
  },

  /**
   * 重置密码
   */
  resetPasswordUserApi (userId) {
    return axios.request({
      url: `${config.baseUrl.dev}/user/resetPassword${userId}`,
      method: 'get'
    })
  },

  /**
   * 菜单列表
   */
  getMenuTreeListApi (params) {
    return axios.get(`${config.baseUrl.dev}/menu/getTreeList`, JSON.stringify(params))
  },

  /**
   * 菜单详情
   */
  getMenuDetailApi (id) {
    return axios.get(`${config.baseUrl.dev}/menu/${id}`)
  },

  /**
   * 菜单新增/修改
   */
  saveOrUpdateByMenuApi (params) {
    return axios.post(`${config.baseUrl.dev}/menu/saveOrUpdate`, JSON.stringify(params))
  },

  /**
   * 角色列表
   */
  // getRoleListApi (params) {
  //   // return axios.post(`${config.baseUrl.dev}/role/list`, qs.stringify(params))
  //   return axios.post(`${config.baseUrl.dev}/role/list`, JSON.stringify(params))
  //   // return axios.post(`${config.baseUrl.dev}/role/list`, params)
  // },
  // return axios.request({
  //   url: `/enter/archiveStatistic/exportList`,
  //   method: 'get',
  //   params:data,
  //   responseType: "blob" // 响应类型是下载且文件类型是 blob
  // });
  getRoleListApi (params) {
    return axios.request({
      url: `${config.baseUrl.dev}/role/list`,
      method: 'get',
      params: params
    })
  },

  /**
   * 角色下拉列表
   */
  getRoleSelectListApi (params) {
    return axios.request({
      url: `${config.baseUrl.dev}/role/getList`,
      method: 'post',
      data: params
    })
  },

  /**
   * 角色菜单权限列表
   */
  getRoleMenuTreeListApi (id) {
    return axios.request({
      url: `${config.baseUrl.dev}/role/getRoleTreeList`,
      method: 'get',
      params: {id}
    })
  },

  /**
   * 修改角色信息
   */
  updateRoleByIdApi (params) {
    return axios.request({
      url: `${config.baseUrl.dev}/role/updateById`,
      method: 'post',
      data: JSON.stringify(params)
    })
  },

  /**
   * 角色新增/修改
   */
  saveOrUpdateByRoleApi (params) {
    return axios.post(`${config.baseUrl.dev}/role/saveOrUpdate`, JSON.stringify(params))
  }

  // ============================================== 用户/菜单/角色 end

}
