"use strict";
const sheep_request_index = require("../../request/index.js");
const AfterSaleApi = {
  // 获得售后分页
  getAfterSalePage: (params) => {
    return sheep_request_index.request({
      url: `/trade/after-sale/page`,
      method: "GET",
      params,
      custom: {
        showLoading: false
      }
    });
  },
  // 创建售后
  createAfterSale: (data) => {
    return sheep_request_index.request({
      url: `/trade/after-sale/create`,
      method: "POST",
      data
    });
  },
  // 获得售后
  getAfterSale: (id) => {
    return sheep_request_index.request({
      url: `/trade/after-sale/get`,
      method: "GET",
      params: {
        id
      }
    });
  },
  // 取消售后
  cancelAfterSale: (id) => {
    return sheep_request_index.request({
      url: `/trade/after-sale/cancel`,
      method: "DELETE",
      params: {
        id
      }
    });
  },
  // 获得售后日志列表
  getAfterSaleLogList: (afterSaleId) => {
    return sheep_request_index.request({
      url: `/trade/after-sale-log/list`,
      method: "GET",
      params: {
        afterSaleId
      }
    });
  },
  // 退回货物
  deliveryAfterSale: (data) => {
    return sheep_request_index.request({
      url: `/trade/after-sale/delivery`,
      method: "PUT",
      data
    });
  }
};
const __vite_glob_0_31 = /* @__PURE__ */ Object.freeze(/* @__PURE__ */ Object.defineProperty({
  __proto__: null,
  default: AfterSaleApi
}, Symbol.toStringTag, { value: "Module" }));
exports.AfterSaleApi = AfterSaleApi;
exports.__vite_glob_0_31 = __vite_glob_0_31;
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/api/trade/afterSale.js.map
