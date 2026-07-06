"use strict";
const sheep_request_index = require("../../request/index.js");
const DeliveryApi = {
  // 获得快递公司列表
  getDeliveryExpressList: () => {
    return sheep_request_index.request({
      url: `/trade/delivery/express/list`,
      method: "get"
    });
  },
  // 获得自提门店列表
  getDeliveryPickUpStoreList: (params) => {
    return sheep_request_index.request({
      url: `/trade/delivery/pick-up-store/list`,
      method: "GET",
      params
    });
  },
  // 获得自提门店
  getDeliveryPickUpStore: (id) => {
    return sheep_request_index.request({
      url: `/trade/delivery/pick-up-store/get`,
      method: "GET",
      params: {
        id
      }
    });
  }
};
const __vite_glob_0_35 = /* @__PURE__ */ Object.freeze(/* @__PURE__ */ Object.defineProperty({
  __proto__: null,
  default: DeliveryApi
}, Symbol.toStringTag, { value: "Module" }));
exports.DeliveryApi = DeliveryApi;
exports.__vite_glob_0_35 = __vite_glob_0_35;
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/api/trade/delivery.js.map
