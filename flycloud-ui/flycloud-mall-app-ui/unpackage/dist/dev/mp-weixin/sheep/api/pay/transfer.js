"use strict";
const sheep_request_index = require("../../request/index.js");
const sheep_config_server = require("../../config/server.js");
const sheep_config_index = require("../../config/index.js");
const PayTransferApi = {
  // 同步转账单
  syncTransfer: (id) => {
    return sheep_request_index.request({
      url: sheep_config_server.getSystemBaseUrl() + sheep_config_index.apiPath + "/pay/transfer/sync",
      method: "GET",
      params: { id }
    });
  }
};
const __vite_glob_0_13 = /* @__PURE__ */ Object.freeze(/* @__PURE__ */ Object.defineProperty({
  __proto__: null,
  default: PayTransferApi
}, Symbol.toStringTag, { value: "Module" }));
exports.PayTransferApi = PayTransferApi;
exports.__vite_glob_0_13 = __vite_glob_0_13;
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/api/pay/transfer.js.map
