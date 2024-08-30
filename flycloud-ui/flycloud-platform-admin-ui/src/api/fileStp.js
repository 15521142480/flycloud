import axios from '../util/http'
import config from '../config'
import qs from 'qs'

export default {

  // 登陆到ftp
  ftpLogin (params) {
    return axios.post(`${config.baseUrl.dev}/fileFtp/login`, JSON.stringify(params))
  },

  // 登出ftp
  ftpLoginOut (params) {
    return axios.post(`${config.baseUrl.dev}/fileFtp/loginOut`, qs.stringify(params))
  },

  // 验证连接
  isConnect (params) {
    return axios.post(`${config.baseUrl.dev}/fileFtp/isConnect`, qs.stringify(params))
  },

  // 验证连接
  isConnectApi (params) {
    return axios.post(`${config.baseUrl.dev}/fileFtp/isConnect`, qs.stringify(params))
  },

  // 文件列表
  getFtpFileListApi (params) {
    return axios.post(`${config.baseUrl.dev}/fileFtp/getList`, params)
  },

  // 上传文件
  // uploadFtpFileApi (params) {
  //   return axios.post(`${config.baseUrl.dev}/fileFtp/uploadFile`, qs.stringify(params));
  // },
  uploadFtpFileApiPath: `${config.baseUrl.dev}/fileFtp/uploadFile`,

  // 下载文件
  // downloadFtpFileApi (path, fileName) {
  //   return `${config.baseUrl.dev}/fileFtp/downloadFile` + '?path=' + path + '&fileName=' + fileName
  // },
  downloadFtpFilePath: `${config.baseUrl.dev}/fileFtp/downloadFile`,

  // 执行操作 -> 操作类型(0:新建文件夹, 1:启动服务, 2:关闭服务, 3:文件/夹重命名, 4:删除文件/夹, 5:压缩文件/夹, 6:解压文件/夹, 10:执行文件)
  executeOptionFtpApi (params) {
    return axios.post(`${config.baseUrl.dev}/fileFtp/executeOption`, JSON.stringify(params))
  },

  // 执行操作命令
  executeCommandFtpApi (params) {
    return axios.post(`${config.baseUrl.dev}/fileFtp/executeCommand`, JSON.stringify(params))
  }

}
