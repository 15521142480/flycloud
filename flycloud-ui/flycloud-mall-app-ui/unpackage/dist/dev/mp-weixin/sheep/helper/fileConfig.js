"use strict";
const common_vendor = require("../../common/vendor.js");
const FILE_CONFIG_STORAGE_KEY = "file-config";
function setFileConfig(config = {}) {
  common_vendor.index.setStorageSync(FILE_CONFIG_STORAGE_KEY, config || {});
}
function getFileConfig() {
  return common_vendor.index.getStorageSync(FILE_CONFIG_STORAGE_KEY) || {};
}
function getFileBaseUrl() {
  var _a;
  return String(((_a = getFileConfig()) == null ? void 0 : _a.baseUrl) || "").replace(/\/+$/, "");
}
exports.getFileBaseUrl = getFileBaseUrl;
exports.setFileConfig = setFileConfig;
//# sourceMappingURL=../../../.sourcemap/mp-weixin/sheep/helper/fileConfig.js.map
