import axios from '../util/http'
import base from './base'
import qs from 'qs'

export default {

  // 文件列表
  getFileListApi (params) {
    return axios.post(`${base.dev}/file/getList`, qs.stringify(params))
  },

  // 文件上传
  uploadFileApi (params) {
    return axios.post(`${base.dev}/file/uploadFile`, qs.stringify(params))
  },

  // 文件上传
  uploadFileApiPath: `${base.dev}/file/uploadFile`,

  // 文件上传进度
  uploadFilePercentApi (params) {
    return axios.post(`${base.dev}/file/uploadPercent`, qs.stringify(params))
  },

  // 文件下载
  downloadFileApi (path, fileName) {
    return `${base.dev}/file/downloadFile` + '?path=' + path + '&fileName=' + fileName
  },

  // 执行操作
  executeOptionApi (params) {
    return axios.post(`${base.dev}/file/executeOption`, qs.stringify(params))
  }

}
