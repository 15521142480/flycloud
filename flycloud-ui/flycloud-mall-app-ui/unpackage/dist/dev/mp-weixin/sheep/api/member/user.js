"use strict";
const sheep_request_index = require("../../request/index.js");
const sheep_config_server = require("../../config/server.js");
const UserApi = {
  // 获得基本信息
  getUserInfo: () => {
    return sheep_request_index.request({
      url: sheep_config_server.getSystemBaseUrl() + "/app/member/user/get",
      method: "GET",
      custom: {
        showLoading: false,
        auth: true
      }
    });
  },
  // getUserInfo: () => {
  //   return request({
  //     url: '/member/user/get',
  //     method: 'GET',
  //     custom: {
  //       showLoading: false,
  //       auth: true,
  //     },
  //   });
  // },
  // 修改基本信息
  updateUser: (data) => {
    return sheep_request_index.request({
      url: "/member/user/update",
      method: "PUT",
      data,
      custom: {
        auth: true,
        showSuccess: true,
        successMsg: "更新成功"
      }
    });
  },
  // 修改用户手机
  updateUserMobile: (data) => {
    return sheep_request_index.request({
      url: "/member/user/update-mobile",
      method: "PUT",
      data,
      custom: {
        loadingMsg: "验证中",
        showSuccess: true,
        successMsg: "修改成功"
      }
    });
  },
  // 基于微信小程序的授权码，修改用户手机
  updateUserMobileByWeixin: (code) => {
    return sheep_request_index.request({
      url: "/member/user/update-mobile-by-weixin",
      method: "PUT",
      data: {
        code
      },
      custom: {
        showSuccess: true,
        loadingMsg: "获取中",
        successMsg: "修改成功"
      }
    });
  },
  // 修改密码
  updateUserPassword: (data) => {
    return sheep_request_index.request({
      url: "/member/user/update-password",
      method: "PUT",
      data,
      custom: {
        loadingMsg: "验证中",
        showSuccess: true,
        successMsg: "修改成功"
      }
    });
  },
  // 重置密码
  resetUserPassword: (data) => {
    return sheep_request_index.request({
      url: "/member/user/reset-password",
      method: "PUT",
      data,
      custom: {
        loadingMsg: "验证中",
        showSuccess: true,
        successMsg: "修改成功"
      }
    });
  }
};
const __vite_glob_0_8 = /* @__PURE__ */ Object.freeze(/* @__PURE__ */ Object.defineProperty({
  __proto__: null,
  default: UserApi
}, Symbol.toStringTag, { value: "Module" }));
exports.UserApi = UserApi;
exports.__vite_glob_0_8 = __vite_glob_0_8;
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/api/member/user.js.map
