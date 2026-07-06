"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_config_index = require("../../config/index.js");
const sheep_config_server = require("../../config/server.js");
const sheep_request_index = require("../../request/index.js");
function normalizeUploadResult(result) {
  const data = (result == null ? void 0 : result.data) ?? result;
  if (typeof data === "string") {
    return {
      url: data,
      baseUrl: "",
      path: data
    };
  }
  return {
    url: (data == null ? void 0 : data.url) || (data == null ? void 0 : data.path) || "",
    baseUrl: (data == null ? void 0 : data.baseUrl) || "",
    path: (data == null ? void 0 : data.path) || (data == null ? void 0 : data.url) || ""
  };
}
const FileApi = {
  // 获取文件配置
  getFileConfig: () => {
    return sheep_request_index.request({
      url: sheep_config_server.getSystemBaseUrl() + sheep_config_index.apiPath + "/file/config",
      method: "GET",
      custom: {
        showLoading: false,
        isToken: false
      }
    });
  },
  // 上传文件
  uploadFile: (file, directory = "") => {
    common_vendor.index.showLoading({
      title: "上传中"
    });
    return new Promise((resolve, reject) => {
      common_vendor.index.uploadFile({
        url: sheep_config_server.getSystemBaseUrl() + sheep_config_index.apiPath + "/file/upload",
        filePath: file,
        name: "file",
        header: {
          Accept: "*/*",
          "tenant-id": sheep_config_index.tenantId,
          Authorization: sheep_request_index.buildAuthorization(sheep_request_index.getAccessToken())
        },
        formData: {
          directory
        },
        success: (uploadFileRes) => {
          let result = JSON.parse(uploadFileRes.data);
          if (result.error === 1) {
            common_vendor.index.showToast({
              icon: "none",
              title: result.msg
            });
          } else {
            return resolve(result);
          }
        },
        fail: (error) => {
          common_vendor.index.__f__("log", "at sheep/api/infra/file.js:67", "上传失败：", error);
          return resolve(false);
        },
        complete: () => {
          common_vendor.index.hideLoading();
        }
      });
    });
  },
  // 规范化上传接口返回值
  normalizeUploadResult,
  // 获取文件预签名地址
  getFilePresignedUrl: (name, directory) => {
    return sheep_request_index.request({
      url: "/infra/file/presigned-url",
      method: "GET",
      params: {
        name,
        directory
      }
    });
  },
  // 创建文件
  createFile: (data) => {
    return sheep_request_index.request({
      url: "/infra/file/create",
      // 请求的 URL
      method: "POST",
      // 请求方法
      data
      // 要发送的数据
    });
  }
};
const __vite_glob_0_1 = /* @__PURE__ */ Object.freeze(/* @__PURE__ */ Object.defineProperty({
  __proto__: null,
  default: FileApi
}, Symbol.toStringTag, { value: "Module" }));
exports.FileApi = FileApi;
exports.__vite_glob_0_1 = __vite_glob_0_1;
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/api/infra/file.js.map
