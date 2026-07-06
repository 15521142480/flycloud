"use strict";
const sheep_request_index = require("../../request/index.js");
const sheep_config_server = require("../../config/server.js");
const sheep_config_index = require("../../config/index.js");
const AddressApi = {
  // 获得用户收件地址列表
  getAddressList: () => {
    return sheep_request_index.request({
      // url: '/auth/getImageTextClickCaptcha',
      // baseURL: getSystemBaseUrl(),
      url: sheep_config_server.getSystemBaseUrl() + sheep_config_index.apiPath + "/member/address/list",
      method: "GET"
    });
  },
  // 创建用户收件地址
  createAddress: (data) => {
    return sheep_request_index.request({
      url: sheep_config_server.getSystemBaseUrl() + sheep_config_index.apiPath + "/member/address/create",
      method: "POST",
      data,
      custom: {
        showSuccess: true,
        successMsg: "保存成功"
      }
    });
  },
  // 更新用户收件地址
  updateAddress: (data) => {
    return sheep_request_index.request({
      url: sheep_config_server.getSystemBaseUrl() + sheep_config_index.apiPath + "/member/address/update",
      method: "PUT",
      data,
      custom: {
        showSuccess: true,
        successMsg: "更新成功"
      }
    });
  },
  // 获得用户收件地址
  getAddress: (id) => {
    return sheep_request_index.request({
      url: sheep_config_server.getSystemBaseUrl() + sheep_config_index.apiPath + "/member/address/get",
      method: "GET",
      params: { id }
    });
  },
  // 删除用户收件地址
  deleteAddress: (id) => {
    return sheep_request_index.request({
      url: sheep_config_server.getSystemBaseUrl() + sheep_config_index.apiPath + "/member/address/delete",
      method: "DELETE",
      params: { id }
    });
  }
};
const __vite_glob_0_3 = /* @__PURE__ */ Object.freeze(/* @__PURE__ */ Object.defineProperty({
  __proto__: null,
  default: AddressApi
}, Symbol.toStringTag, { value: "Module" }));
exports.AddressApi = AddressApi;
exports.__vite_glob_0_3 = __vite_glob_0_3;
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/api/member/address.js.map
