"use strict";
const sheep_request_index = require("../../request/index.js");
const CategoryApi = {
  // 查询分类列表
  getCategoryList: () => {
    return sheep_request_index.request({
      url: "/product/category/list",
      method: "GET"
    });
  },
  // 查询分类列表，指定编号
  getCategoryListByIds: (ids) => {
    return sheep_request_index.request({
      url: "/product/category/list-by-ids",
      method: "GET",
      params: { ids }
    });
  }
};
const __vite_glob_0_15 = /* @__PURE__ */ Object.freeze(/* @__PURE__ */ Object.defineProperty({
  __proto__: null,
  default: CategoryApi
}, Symbol.toStringTag, { value: "Module" }));
exports.CategoryApi = CategoryApi;
exports.__vite_glob_0_15 = __vite_glob_0_15;
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/api/product/category.js.map
