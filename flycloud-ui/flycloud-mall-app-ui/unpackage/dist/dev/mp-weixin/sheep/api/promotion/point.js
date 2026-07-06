"use strict";
const sheep_request_index = require("../../request/index.js");
const PointApi = {
  // 获得积分商城活动分页
  getPointActivityPage: (params) => {
    return sheep_request_index.request({ url: "promotion/point-activity/page", method: "GET", params });
  },
  // 获得积分商城活动列表，基于活动编号数组
  getPointActivityListByIds: (ids) => {
    return sheep_request_index.request({
      url: "/promotion/point-activity/list-by-ids",
      method: "GET",
      params: {
        ids
      }
    });
  },
  // 获得积分商城活动明细
  getPointActivity: (id) => {
    return sheep_request_index.request({
      url: "promotion/point-activity/get-detail",
      method: "GET",
      params: { id }
    });
  }
};
const __vite_glob_0_26 = /* @__PURE__ */ Object.freeze(/* @__PURE__ */ Object.defineProperty({
  __proto__: null,
  default: PointApi
}, Symbol.toStringTag, { value: "Module" }));
exports.PointApi = PointApi;
exports.__vite_glob_0_26 = __vite_glob_0_26;
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/api/promotion/point.js.map
