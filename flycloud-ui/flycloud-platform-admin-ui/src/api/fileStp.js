import axios from '../util/http'
import base from './base'
import qs from 'qs'

export default {

  // 登陆到ftp
  ftpLogin (params) {
    return axios.post(`${base.dev}/fileFtp/login`, JSON.stringify(params))
  },

  // 登出ftp
  ftpLoginOut (params) {
    return axios.post(`${base.dev}/fileFtp/loginOut`, qs.stringify(params))
  },

  // 验证连接
  isConnect (params) {
    return axios.post(`${base.dev}/fileFtp/isConnect`, qs.stringify(params))
  },

  // 验证连接
  isConnectApi (params) {
    return axios.post(`${base.dev}/fileFtp/isConnect`, qs.stringify(params))
  },

  // 文件列表
  getFtpFileListApi (params) {
    return axios.post(`${base.dev}/fileFtp/getList`, params)
  },

  // 上传文件
  // uploadFtpFileApi (params) {
  //   return axios.post(`${base.dev}/fileFtp/uploadFile`, qs.stringify(params));
  // },
  uploadFtpFileApiPath: `${base.dev}/fileFtp/uploadFile`,

  // 下载文件
  // downloadFtpFileApi (path, fileName) {
  //   return `${base.dev}/fileFtp/downloadFile` + '?path=' + path + '&fileName=' + fileName
  // },
  downloadFtpFilePath: `${base.dev}/fileFtp/downloadFile`,

  // 执行操作 -> 操作类型(0:新建文件夹, 1:启动服务, 2:关闭服务, 3:文件/夹重命名, 4:删除文件/夹, 5:压缩文件/夹, 6:解压文件/夹, 10:执行文件)
  executeOptionFtpApi (params) {
    return axios.post(`${base.dev}/fileFtp/executeOption`, JSON.stringify(params))
  },

  // 执行操作命令
  executeCommandFtpApi (params) {
    return axios.post(`${base.dev}/fileFtp/executeCommand`, JSON.stringify(params))
  }

}
