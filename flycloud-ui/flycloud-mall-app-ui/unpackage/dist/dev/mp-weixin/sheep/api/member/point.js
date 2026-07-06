"use strict";
const sheep_request_index = require("../../request/index.js");
const sheep_config_server = require("../../config/server.js");
const sheep_config_index = require("../../config/index.js");
const PointApi = {
  // 获得用户积分记录分页
  getPointRecordPage: (params) => {
    if (params.addStatus === void 0) {
      delete params.addStatus;
    }
    const queryString = Object.keys(params).map((key) => encodeURIComponent(key) + "=" + params[key]).join("&");
    return sheep_request_index.request({
      url: sheep_config_server.getSystemBaseUrl() + sheep_config_index.apiPath + `/member/point/record/page?${queryString}`,
      method: "GET"
    });
  }
};
const __vite_glob_0_5 = /* @__PURE__ */ Object.freeze(/* @__PURE__ */ Object.defineProperty({
  __proto__: null,
  default: PointApi
}, Symbol.toStringTag, { value: "Module" }));
exports.PointApi = PointApi;
exports.__vite_glob_0_5 = __vite_glob_0_5;
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/api/member/point.js.map
