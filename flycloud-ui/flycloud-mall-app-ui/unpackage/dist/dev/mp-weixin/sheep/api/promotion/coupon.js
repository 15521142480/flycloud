"use strict";
const sheep_request_index = require("../../request/index.js");
const CouponApi = {
  // 获得优惠劵模板列表
  getCouponTemplateListByIds: (ids) => {
    return sheep_request_index.request({
      url: "/promotion/coupon-template/list-by-ids",
      method: "GET",
      params: { ids },
      custom: {
        showLoading: false,
        // 不展示 Loading，避免领取优惠劵时，不成功提示
        showError: false
      }
    });
  },
  // 获得优惠劵模版列表
  getCouponTemplateList: (spuId, productScope, count) => {
    return sheep_request_index.request({
      url: "/promotion/coupon-template/list",
      method: "GET",
      params: { spuId, productScope, count }
    });
  },
  // 获得优惠劵模版分页
  getCouponTemplatePage: (params) => {
    return sheep_request_index.request({
      url: "/promotion/coupon-template/page",
      method: "GET",
      params
    });
  },
  // 获得优惠劵模版
  getCouponTemplate: (id) => {
    return sheep_request_index.request({
      url: "/promotion/coupon-template/get",
      method: "GET",
      params: { id }
    });
  },
  // 我的优惠劵列表
  getCouponPage: (params) => {
    return sheep_request_index.request({
      url: "/promotion/coupon/page",
      method: "GET",
      params
    });
  },
  // 领取优惠券
  takeCoupon: (templateId) => {
    return sheep_request_index.request({
      url: "/promotion/coupon/take",
      method: "POST",
      data: { templateId },
      custom: {
        auth: true,
        showLoading: true,
        loadingMsg: "领取中",
        showSuccess: true,
        successMsg: "领取成功"
      }
    });
  },
  // 获得优惠劵
  getCoupon: (id) => {
    return sheep_request_index.request({
      url: "/promotion/coupon/get",
      method: "GET",
      params: { id }
    });
  },
  // 获得未使用的优惠劵数量
  getUnusedCouponCount: () => {
    return sheep_request_index.request({
      url: "/promotion/coupon/get-unused-count",
      method: "GET",
      custom: {
        showLoading: false,
        auth: true
      }
    });
  }
};
const __vite_glob_0_23 = /* @__PURE__ */ Object.freeze(/* @__PURE__ */ Object.defineProperty({
  __proto__: null,
  default: CouponApi
}, Symbol.toStringTag, { value: "Module" }));
exports.CouponApi = CouponApi;
exports.__vite_glob_0_23 = __vite_glob_0_23;
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/api/promotion/coupon.js.map
