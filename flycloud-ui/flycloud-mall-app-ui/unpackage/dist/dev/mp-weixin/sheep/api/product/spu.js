"use strict";
const sheep_request_index = require("../../request/index.js");
const SpuApi = {
  // 获得商品 SPU 列表
  getSpuListByIds: (ids) => {
    return sheep_request_index.request({
      url: "/product/spu/list-by-ids",
      method: "GET",
      params: { ids },
      custom: {
        showLoading: false,
        showError: false
      }
    });
  },
  // 获得商品结算信息
  getSettlementProduct: (spuIds) => {
    return sheep_request_index.request({
      url: "/trade/order/settlement-product",
      method: "GET",
      params: { spuIds },
      custom: {
        showLoading: false,
        showError: false
      }
    });
  },
  // 获得商品 SPU 分页
  getSpuPage: (params) => {
    return sheep_request_index.request({
      url: "/product/spu/page",
      method: "GET",
      params,
      custom: {
        showLoading: false,
        showError: false
      }
    });
  },
  // 查询商品
  getSpuDetail: (id) => {
    return sheep_request_index.request({
      url: "/product/spu/get-detail",
      method: "GET",
      params: { id },
      custom: {
        showLoading: false,
        showError: false
      }
    });
  }
};
const __vite_glob_0_19 = /* @__PURE__ */ Object.freeze(/* @__PURE__ */ Object.defineProperty({
  __proto__: null,
  default: SpuApi
}, Symbol.toStringTag, { value: "Module" }));
exports.SpuApi = SpuApi;
exports.__vite_glob_0_19 = __vite_glob_0_19;
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/api/product/spu.js.map
