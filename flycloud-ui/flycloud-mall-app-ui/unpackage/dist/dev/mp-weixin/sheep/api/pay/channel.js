"use strict";
const sheep_request_index = require("../../request/index.js");
const sheep_config_server = require("../../config/server.js");
const sheep_config_index = require("../../config/index.js");
const PayChannelApi = {
  // 获得指定应用的开启的支付渠道编码列表
  getEnableChannelCodeList: (appId) => {
    return sheep_request_index.request({
      url: sheep_config_server.getSystemBaseUrl() + sheep_config_index.apiPath + "/pay/channel/get-enable-code-list",
      method: "GET",
      params: { appId }
    });
  }
};
const __vite_glob_0_11 = /* @__PURE__ */ Object.freeze(/* @__PURE__ */ Object.defineProperty({
  __proto__: null,
  default: PayChannelApi
}, Symbol.toStringTag, { value: "Module" }));
exports.PayChannelApi = PayChannelApi;
exports.__vite_glob_0_11 = __vite_glob_0_11;
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/api/pay/channel.js.map
