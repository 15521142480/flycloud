"use strict";
const sheep_request_index = require("../../request/index.js");
const SeckillApi = {
  // 获得秒杀时间段列表
  getSeckillConfigList: () => {
    return sheep_request_index.request({ url: "promotion/seckill-config/list", method: "GET" });
  },
  // 获得当前秒杀活动
  getNowSeckillActivity: () => {
    return sheep_request_index.request({ url: "promotion/seckill-activity/get-now", method: "GET" });
  },
  // 获得秒杀活动分页
  getSeckillActivityPage: (params) => {
    return sheep_request_index.request({ url: "promotion/seckill-activity/page", method: "GET", params });
  },
  // 获得秒杀活动列表，基于活动编号数组
  getSeckillActivityListByIds: (ids) => {
    return sheep_request_index.request({
      url: "/promotion/seckill-activity/list-by-ids",
      method: "GET",
      params: {
        ids
      }
    });
  },
  /**
   * 获得秒杀活动明细
   * @param {number} id 秒杀活动编号
   * @return {*}
   */
  getSeckillActivity: (id) => {
    return sheep_request_index.request({
      url: "promotion/seckill-activity/get-detail",
      method: "GET",
      params: { id }
    });
  }
};
const __vite_glob_0_28 = /* @__PURE__ */ Object.freeze(/* @__PURE__ */ Object.defineProperty({
  __proto__: null,
  default: SeckillApi
}, Symbol.toStringTag, { value: "Module" }));
exports.SeckillApi = SeckillApi;
exports.__vite_glob_0_28 = __vite_glob_0_28;
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/api/promotion/seckill.js.map
