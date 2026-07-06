"use strict";
const sheep_request_index = require("../../request/index.js");
const ArticleApi = {
  // 获得文章详情
  getArticle: (id, title) => {
    return sheep_request_index.request({
      url: "/promotion/article/get",
      method: "GET",
      params: { id, title }
    });
  }
};
const __vite_glob_0_21 = /* @__PURE__ */ Object.freeze(/* @__PURE__ */ Object.defineProperty({
  __proto__: null,
  default: ArticleApi
}, Symbol.toStringTag, { value: "Module" }));
exports.ArticleApi = ArticleApi;
exports.__vite_glob_0_21 = __vite_glob_0_21;
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/api/promotion/article.js.map
