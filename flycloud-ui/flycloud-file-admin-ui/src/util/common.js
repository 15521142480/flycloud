import axios from '../util/http'
// import base from '../api/base'

/**
 * 文件下载
 * @param url
 * @param params 参数对象
 * @param fileName 文件名
 */
export function download (url, params, fileName) {
  axios.post(url, params, {
    responseType: 'blob',
    headers: {
      'Content-Type': 'application/json', // 'application/octet-stream',
      Authorization: 'Bearer ' + localStorage.getItem('userToken')
    }
  })
    .then(res => {
      let blob = res.data
      let reader = new FileReader()
      reader.readAsDataURL(blob)
      reader.onload = (e) => {
        let a = document.createElement('a')
        a.download = fileName // 设置下载文件的名称
        a.href = e.target.result
        document.body.appendChild(a)
        a.click()
        document.body.removeChild(a)
      }
    })
    .catch(err => {
      console.log(err.message)
    })
}
