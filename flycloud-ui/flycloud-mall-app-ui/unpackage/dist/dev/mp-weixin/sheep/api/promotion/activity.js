"use strict";
const sheep_request_index = require("../../request/index.js");
const ActivityApi = {
  // 获得单个商品，进行中的拼团、秒杀、砍价活动信息
  getActivityListBySpuId: (spuId) => {
    return sheep_request_index.request({
      url: "/promotion/activity/list-by-spu-id",
      method: "GET",
      params: {
        spuId
      }
    });
  }
};
const __vite_glob_0_20 = /* @__PURE__ */ Object.freeze(/* @__PURE__ */ Object.defineProperty({
  __proto__: null,
  default: ActivityApi
}, Symbol.toStringTag, { value: "Module" }));
exports.ActivityApi = ActivityApi;
exports.__vite_glob_0_20 = __vite_glob_0_20;
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/api/promotion/activity.js.map
