"use strict";
const sheep_request_index = require("../../request/index.js");
const sheep_config_server = require("../../config/server.js");
const sheep_config_index = require("../../config/index.js");
const PayOrderApi = {
  // 获得支付订单
  getOrder: (id, sync, no) => {
    const params = {};
    if (id)
      params.id = id;
    if (no)
      params.no = no;
    if (sync !== void 0)
      params.sync = sync;
    return sheep_request_index.request({
      url: sheep_config_server.getSystemBaseUrl() + sheep_config_index.apiPath + "/pay/order/get",
      method: "GET",
      params
    });
  },
  // 提交支付订单
  submitOrder: (data) => {
    return sheep_request_index.request({
      url: sheep_config_server.getSystemBaseUrl() + sheep_config_index.apiPath + "/pay/order/submit",
      method: "POST",
      data
    });
  }
};
const __vite_glob_0_12 = /* @__PURE__ */ Object.freeze(/* @__PURE__ */ Object.defineProperty({
  __proto__: null,
  default: PayOrderApi
}, Symbol.toStringTag, { value: "Module" }));
exports.PayOrderApi = PayOrderApi;
exports.__vite_glob_0_12 = __vite_glob_0_12;
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/api/pay/order.js.map
