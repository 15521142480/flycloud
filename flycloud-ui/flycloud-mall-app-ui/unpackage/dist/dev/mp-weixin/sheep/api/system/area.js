"use strict";
const sheep_request_index = require("../../request/index.js");
const sheep_config_server = require("../../config/server.js");
const AreaApi = {
  // 获得地区树
  getAreaTree: () => {
    return sheep_request_index.request({
      url: sheep_config_server.getSystemBaseUrl() + "/area/tree",
      method: "GET"
    });
  }
};
const __vite_glob_0_29 = /* @__PURE__ */ Object.freeze(/* @__PURE__ */ Object.defineProperty({
  __proto__: null,
  default: AreaApi
}, Symbol.toStringTag, { value: "Module" }));
exports.AreaApi = AreaApi;
exports.__vite_glob_0_29 = __vite_glob_0_29;
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/api/system/area.js.map
