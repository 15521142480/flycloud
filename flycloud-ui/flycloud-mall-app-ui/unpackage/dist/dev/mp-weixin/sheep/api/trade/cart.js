"use strict";
const sheep_request_index = require("../../request/index.js");
const CartApi = {
  addCart: (data) => {
    return sheep_request_index.request({
      url: "/trade/cart/add",
      method: "POST",
      data,
      custom: {
        showSuccess: true,
        successMsg: "已添加到购物车~"
      }
    });
  },
  updateCartCount: (data) => {
    return sheep_request_index.request({
      url: "/trade/cart/update-count",
      method: "PUT",
      data
    });
  },
  updateCartSelected: (data) => {
    return sheep_request_index.request({
      url: "/trade/cart/update-selected",
      method: "PUT",
      data
    });
  },
  deleteCart: (ids) => {
    return sheep_request_index.request({
      url: "/trade/cart/delete",
      method: "DELETE",
      params: {
        ids
      }
    });
  },
  getCartList: () => {
    return sheep_request_index.request({
      url: "/trade/cart/list",
      method: "GET",
      custom: {
        showLoading: false,
        auth: true
      }
    });
  }
};
const __vite_glob_0_33 = /* @__PURE__ */ Object.freeze(/* @__PURE__ */ Object.defineProperty({
  __proto__: null,
  default: CartApi
}, Symbol.toStringTag, { value: "Module" }));
exports.CartApi = CartApi;
exports.__vite_glob_0_33 = __vite_glob_0_33;
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/api/trade/cart.js.map
