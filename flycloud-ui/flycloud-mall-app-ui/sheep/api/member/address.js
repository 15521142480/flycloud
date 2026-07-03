import request from '@/sheep/request';
import { getSystemBaseUrl } from '@/sheep/config/server';
import { apiPath } from '@/sheep/config';

const AddressApi = {
  // 获得用户收件地址列表
  getAddressList: () => {
    return request({
      // url: '/auth/getImageTextClickCaptcha',
      // baseURL: getSystemBaseUrl(),
      url: getSystemBaseUrl() + apiPath + '/member/address/list',
      method: 'GET',
    });
  },
  // 创建用户收件地址
  createAddress: (data) => {
    return request({
      url: getSystemBaseUrl() + apiPath + '/member/address/create',
      method: 'POST',
      data,
      custom: {
        showSuccess: true,
        successMsg: '保存成功',
      },
    });
  },
  // 更新用户收件地址
  updateAddress: (data) => {
    return request({
      url: getSystemBaseUrl() + apiPath + '/member/address/update',
      method: 'PUT',
      data,
      custom: {
        showSuccess: true,
        successMsg: '更新成功',
      },
    });
  },
  // 获得用户收件地址
  getAddress: (id) => {
    return request({
      url: getSystemBaseUrl() + apiPath + '/member/address/get',
      method: 'GET',
      params: { id },
    });
  },
  // 删除用户收件地址
  deleteAddress: (id) => {
    return request({
      url: getSystemBaseUrl() + apiPath + '/member/address/delete',
      method: 'DELETE',
      params: { id },
    });
  },
};

export default AddressApi;
