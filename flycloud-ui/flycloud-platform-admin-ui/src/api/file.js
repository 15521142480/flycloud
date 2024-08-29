import axios from '../util/http'
import base from './base'

export default {

  // 文件列表
  getFileListApi (params) {
    return axios.post(`${base.dev}/file/getList`, params)
  },

  // 文件上传
  uploadFileApiPath: `${base.dev}/file/uploadFile`,

  // 文件上传进度
  uploadFilePercentApi (params) {
    return axios.post(`${base.dev}/file/uploadPercent`, JSON.stringify(params))
  },

  // 文件下载
  downloadFilePath: `${base.dev}/file/downloadFile`,

  // 执行操作
  executeOptionApi (params) {
    return axios.post(`${base.dev}/file/executeOption`, JSON.stringify(params))
  },

  // 执行操作命令
  executeCommandApi (params) {
    return axios.post(`${base.dev}/file/executeCommand`, JSON.stringify(params))
  }

}
