"use strict";
const sheep_request_index = require("../../request/index.js");
const BrokerageApi = {
  // 绑定分销用户
  bindBrokerageUser: (data) => {
    return sheep_request_index.request({
      url: "/trade/brokerage-user/bind",
      method: "PUT",
      data
    });
  },
  // 获得个人分销信息
  getBrokerageUser: () => {
    return sheep_request_index.request({
      url: "/trade/brokerage-user/get",
      method: "GET"
    });
  },
  // 获得个人分销统计
  getBrokerageUserSummary: () => {
    return sheep_request_index.request({
      url: "/trade/brokerage-user/get-summary",
      method: "GET"
    });
  },
  // 获得分销记录分页
  getBrokerageRecordPage: (params) => {
    if (params.status === void 0) {
      delete params.status;
    }
    const queryString = Object.keys(params).map((key) => encodeURIComponent(key) + "=" + params[key]).join("&");
    return sheep_request_index.request({
      url: `/trade/brokerage-record/page?${queryString}`,
      method: "GET"
    });
  },
  // 创建分销提现
  createBrokerageWithdraw: (data) => {
    return sheep_request_index.request({
      url: "/trade/brokerage-withdraw/create",
      method: "POST",
      data
    });
  },
  // 获得分销提现分页
  getBrokerageWithdrawPage: (params) => {
    const queryString = Object.keys(params).map((key) => encodeURIComponent(key) + "=" + params[key]).join("&");
    return sheep_request_index.request({
      url: `/trade/brokerage-withdraw/page?${queryString}`,
      method: "GET"
    });
  },
  // 获得分销提现详情
  getBrokerageWithdraw: (id) => {
    return sheep_request_index.request({
      url: `/trade/brokerage-withdraw/get`,
      method: "GET",
      params: { id }
    });
  },
  // 获得商品的分销金额
  getProductBrokeragePrice: (spuId) => {
    return sheep_request_index.request({
      url: "/trade/brokerage-record/get-product-brokerage-price",
      method: "GET",
      params: { spuId }
    });
  },
  // 获得分销用户排行（基于佣金）
  getRankByPrice: (params) => {
    const queryString = `times=${params.times[0]}&times=${params.times[1]}`;
    return sheep_request_index.request({
      url: `/trade/brokerage-user/get-rank-by-price?${queryString}`,
      method: "GET"
    });
  },
  // 获得分销用户排行分页（基于佣金）
  getBrokerageUserChildSummaryPageByPrice: (params) => {
    const queryString = Object.keys(params).map((key) => encodeURIComponent(key) + "=" + params[key]).join("&");
    return sheep_request_index.request({
      url: `/trade/brokerage-user/rank-page-by-price?${queryString}`,
      method: "GET"
    });
  },
  // 获得分销用户排行分页（基于用户量）
  getBrokerageUserRankPageByUserCount: (params) => {
    const queryString = Object.keys(params).map((key) => encodeURIComponent(key) + "=" + params[key]).join("&");
    return sheep_request_index.request({
      url: `/trade/brokerage-user/rank-page-by-user-count?${queryString}`,
      method: "GET"
    });
  },
  // 获得下级分销统计分页
  getBrokerageUserChildSummaryPage: (params) => {
    return sheep_request_index.request({
      url: "/trade/brokerage-user/child-summary-page",
      method: "GET",
      params
    });
  }
};
const __vite_glob_0_32 = /* @__PURE__ */ Object.freeze(/* @__PURE__ */ Object.defineProperty({
  __proto__: null,
  default: BrokerageApi
}, Symbol.toStringTag, { value: "Module" }));
exports.BrokerageApi = BrokerageApi;
exports.__vite_glob_0_32 = __vite_glob_0_32;
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/api/trade/brokerage.js.map
