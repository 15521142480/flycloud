"use strict";
const sheep_request_index = require("../../request/index.js");
const SpuHistoryApi = {
  // 删除商品浏览记录
  deleteBrowseHistory: (spuIds) => {
    return sheep_request_index.request({
      url: "/product/browse-history/delete",
      method: "DELETE",
      data: { spuIds },
      custom: {
        showSuccess: true,
        successMsg: "删除成功"
      }
    });
  },
  // 清空商品浏览记录
  cleanBrowseHistory: () => {
    return sheep_request_index.request({
      url: "/product/browse-history/clean",
      method: "DELETE",
      custom: {
        showSuccess: true,
        successMsg: "清空成功"
      }
    });
  },
  // 获得商品浏览记录分页
  getBrowseHistoryPage: (data) => {
    return sheep_request_index.request({
      url: "/product/browse-history/page",
      method: "GET",
      data,
      custom: {
        showLoading: false
      }
    });
  }
};
const __vite_glob_0_18 = /* @__PURE__ */ Object.freeze(/* @__PURE__ */ Object.defineProperty({
  __proto__: null,
  default: SpuHistoryApi
}, Symbol.toStringTag, { value: "Module" }));
exports.SpuHistoryApi = SpuHistoryApi;
exports.__vite_glob_0_18 = __vite_glob_0_18;
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/api/product/history.js.map
