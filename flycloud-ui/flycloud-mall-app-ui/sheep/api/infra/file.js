import { apiPath, tenantId } from '@/sheep/config';
import { getSystemBaseUrl } from '@/sheep/config/server';
import request, { buildAuthorization, getAccessToken } from '@/sheep/request';

/**
 * 兼容旧字符串返回和新上传 VO，统一出预览 URL 与业务存储 path。
 */
function normalizeUploadResult(result) {
  const data = result?.data ?? result;
  if (typeof data === 'string') {
    return {
      url: data,
      baseUrl: '',
      path: data,
    };
  }
  return {
    url: data?.url || data?.path || '',
    baseUrl: data?.baseUrl || '',
    path: data?.path || data?.url || '',
  };
}

const FileApi = {
  // 获取文件配置
  getFileConfig: () => {
    return request({
      url: getSystemBaseUrl() + apiPath + '/file/config',
      method: 'GET',
      custom: {
        showLoading: false,
        isToken: false,
      },
    });
  },

  // 上传文件
  uploadFile: (file, directory = '') => {
    uni.showLoading({
      title: '上传中',
    });
    return new Promise((resolve, reject) => {
      uni.uploadFile({
        url: getSystemBaseUrl() + apiPath + '/file/upload',
        filePath: file,
        name: 'file',
        header: {
          Accept: '*/*',
          'tenant-id': tenantId,
          Authorization: buildAuthorization(getAccessToken()),
        },
        formData: {
          directory,
        },
        success: (uploadFileRes) => {
          let result = JSON.parse(uploadFileRes.data);
          if (result.error === 1) {
            uni.showToast({
              icon: 'none',
              title: result.msg,
            });
          } else {
            return resolve(result);
          }
        },
        fail: (error) => {
          console.log('上传失败：', error);
          return resolve(false);
        },
        complete: () => {
          uni.hideLoading();
        },
      });
    });
  },

  // 规范化上传接口返回值
  normalizeUploadResult,

  // 获取文件预签名地址
  getFilePresignedUrl: (name, directory) => {
    return request({
      url: '/infra/file/presigned-url',
      method: 'GET',
      params: {
        name,
        directory,
      },
    });
  },

  // 创建文件
  createFile: (data) => {
    return request({
      url: '/infra/file/create', // 请求的 URL
      method: 'POST', // 请求方法
      data: data, // 要发送的数据
    });
  },
};

export default FileApi;
