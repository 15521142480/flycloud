"use strict";
const sheep_request_index = require("../../request/index.js");
const RewardActivityApi = {
  // 获得满减送活动
  getRewardActivity: (id) => {
    return sheep_request_index.request({
      url: "/promotion/reward-activity/get",
      method: "GET",
      params: { id }
    });
  }
};
const __vite_glob_0_27 = /* @__PURE__ */ Object.freeze(/* @__PURE__ */ Object.defineProperty({
  __proto__: null,
  default: RewardActivityApi
}, Symbol.toStringTag, { value: "Module" }));
exports.RewardActivityApi = RewardActivityApi;
exports.__vite_glob_0_27 = __vite_glob_0_27;
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/api/promotion/rewardActivity.js.map
