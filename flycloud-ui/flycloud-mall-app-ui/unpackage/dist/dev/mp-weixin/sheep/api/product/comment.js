"use strict";
const sheep_request_index = require("../../request/index.js");
const CommentApi = {
  // 获得商品评价分页
  getCommentPage: (spuId, pageNum, pageSize, type) => {
    return sheep_request_index.request({
      url: "/product/comment/page",
      method: "GET",
      params: {
        spuId,
        pageNum,
        pageSize,
        type
      },
      custom: {
        showLoading: false,
        showError: false
      }
    });
  }
};
const __vite_glob_0_16 = /* @__PURE__ */ Object.freeze(/* @__PURE__ */ Object.defineProperty({
  __proto__: null,
  default: CommentApi
}, Symbol.toStringTag, { value: "Module" }));
exports.CommentApi = CommentApi;
exports.__vite_glob_0_16 = __vite_glob_0_16;
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/api/product/comment.js.map
