"use strict";
const sheep_request_index = require("../../request/index.js");
const FavoriteApi = {
  // 获得商品收藏分页
  getFavoritePage: (data) => {
    return sheep_request_index.request({
      url: "/product/favorite/page",
      method: "GET",
      params: data
    });
  },
  // 检查是否收藏过商品
  isFavoriteExists: (spuId) => {
    return sheep_request_index.request({
      url: "/product/favorite/exits",
      method: "GET",
      params: {
        spuId
      }
    });
  },
  // 添加商品收藏
  createFavorite: (spuId) => {
    return sheep_request_index.request({
      url: "/product/favorite/create",
      method: "POST",
      data: {
        spuId
      },
      custom: {
        auth: true,
        showSuccess: true,
        successMsg: "收藏成功"
      }
    });
  },
  // 取消商品收藏
  deleteFavorite: (spuId) => {
    return sheep_request_index.request({
      url: "/product/favorite/delete",
      method: "DELETE",
      data: {
        spuId
      },
      custom: {
        auth: true,
        showSuccess: true,
        successMsg: "取消成功"
      }
    });
  }
};
const __vite_glob_0_17 = /* @__PURE__ */ Object.freeze(/* @__PURE__ */ Object.defineProperty({
  __proto__: null,
  default: FavoriteApi
}, Symbol.toStringTag, { value: "Module" }));
exports.FavoriteApi = FavoriteApi;
exports.__vite_glob_0_17 = __vite_glob_0_17;
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/api/product/favorite.js.map
