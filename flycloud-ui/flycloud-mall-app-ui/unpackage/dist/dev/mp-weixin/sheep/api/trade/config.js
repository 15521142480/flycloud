"use strict";
const sheep_request_index = require("../../request/index.js");
const TradeConfigApi = {
  // 获得交易配置
  getTradeConfig: () => {
    return sheep_request_index.request({
      url: `/trade/config/get`,
      method: "GET",
      custom: {
        showLoading: false
      }
    });
  }
};
const __vite_glob_0_34 = /* @__PURE__ */ Object.freeze(/* @__PURE__ */ Object.defineProperty({
  __proto__: null,
  default: TradeConfigApi
}, Symbol.toStringTag, { value: "Module" }));
exports.TradeConfigApi = TradeConfigApi;
exports.__vite_glob_0_34 = __vite_glob_0_34;
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/api/trade/config.js.map
