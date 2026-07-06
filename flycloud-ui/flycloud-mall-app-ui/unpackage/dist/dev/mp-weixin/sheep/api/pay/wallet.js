"use strict";
const sheep_request_index = require("../../request/index.js");
const sheep_config_server = require("../../config/server.js");
const sheep_config_index = require("../../config/index.js");
const PayWalletApi = {
  // 获取钱包
  getPayWallet() {
    return sheep_request_index.request({
      url: sheep_config_server.getSystemBaseUrl() + sheep_config_index.apiPath + "/pay/wallet/get",
      method: "GET",
      custom: {
        showLoading: false,
        auth: true
      }
    });
  },
  // 获得钱包流水分页
  getWalletTransactionPage: (params) => {
    const queryString = Object.keys(params).map((key) => encodeURIComponent(key) + "=" + params[key]).join("&");
    return sheep_request_index.request({
      url: sheep_config_server.getSystemBaseUrl() + sheep_config_index.apiPath + `/pay/wallet-transaction/page?${queryString}`,
      method: "GET"
    });
  },
  // 获得钱包流水统计
  getWalletTransactionSummary: (params) => {
    const queryString = `createTime=${params.createTime[0]}&createTime=${params.createTime[1]}`;
    return sheep_request_index.request({
      url: sheep_config_server.getSystemBaseUrl() + sheep_config_index.apiPath + `/pay/wallet-transaction/get-summary?${queryString}`,
      // url: `/pay/wallet-transaction/get-summary`,
      method: "GET"
      // params: params
    });
  },
  // 获得钱包充值套餐列表
  getWalletRechargePackageList: () => {
    return sheep_request_index.request({
      url: sheep_config_server.getSystemBaseUrl() + sheep_config_index.apiPath + "/pay/wallet-recharge-package/list",
      method: "GET",
      custom: {
        showError: false,
        showLoading: false
      }
    });
  },
  // 创建钱包充值记录（发起充值）
  createWalletRecharge: (data) => {
    return sheep_request_index.request({
      url: sheep_config_server.getSystemBaseUrl() + sheep_config_index.apiPath + "/pay/wallet-recharge/create",
      method: "POST",
      data
    });
  },
  // 获得钱包充值记录分页
  getWalletRechargePage: (params) => {
    return sheep_request_index.request({
      url: sheep_config_server.getSystemBaseUrl() + sheep_config_index.apiPath + "/pay/wallet-recharge/page",
      method: "GET",
      params,
      custom: {
        showError: false,
        showLoading: false
      }
    });
  }
};
const __vite_glob_0_14 = /* @__PURE__ */ Object.freeze(/* @__PURE__ */ Object.defineProperty({
  __proto__: null,
  default: PayWalletApi
}, Symbol.toStringTag, { value: "Module" }));
exports.PayWalletApi = PayWalletApi;
exports.__vite_glob_0_14 = __vite_glob_0_14;
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/api/pay/wallet.js.map
