"use strict";
const common_vendor = require("../../common/vendor.js");
const sheep_store_index = require("../store/index.js");
const sheep_hooks_useModal = require("../hooks/useModal.js");
const sheep_helper_throttle = require("../helper/throttle.js");
var define_ROUTES_MAP_default = { "/pages/index/index": { path: "/pages/index/index", aliasPath: "/", meta: { auth: false, sync: true, title: "首页", group: "商城" } }, "/pages/index/user": { path: "/pages/index/user", meta: { sync: true, title: "个人中心", group: "商城" } }, "/pages/index/category": { path: "/pages/index/category", meta: { sync: true, title: "商品分类", group: "商城" } }, "/pages/index/cart": { path: "/pages/index/cart", meta: { sync: true, title: "购物车", group: "商城" } }, "/pages/index/login": { path: "/pages/index/login" }, "/pages/index/search": { path: "/pages/index/search", meta: { sync: true, title: "搜索", group: "商城" } }, "/pages/index/page": { path: "/pages/index/page", meta: { auth: false, sync: true, title: "自定义页面", group: "商城" } }, "/pages/goods/index": { path: "/pages/goods/index", meta: { sync: true, title: "普通商品", group: "商品" } }, "/pages/goods/groupon": { path: "/pages/goods/groupon", meta: { sync: true, title: "拼团商品", group: "商品" } }, "/pages/goods/seckill": { path: "/pages/goods/seckill", meta: { sync: true, title: "秒杀商品", group: "商品" } }, "/pages/goods/point": { path: "/pages/goods/point", meta: { sync: true, title: "积分商品", group: "商品" } }, "/pages/goods/list": { path: "/pages/goods/list", meta: { sync: true, title: "商品列表", group: "商品" } }, "/pages/goods/comment/add": { path: "/pages/goods/comment/add", meta: { auth: true } }, "/pages/goods/comment/list": { path: "/pages/goods/comment/list" }, "/pages/order/detail": { path: "/pages/order/detail", meta: { auth: true, title: "订单详情" } }, "/pages/order/confirm": { path: "/pages/order/confirm", meta: { auth: true, title: "确认订单" } }, "/pages/order/list": { path: "/pages/order/list", meta: { auth: true, sync: true, title: "用户订单", group: "订单中心" } }, "/pages/order/aftersale/apply": { path: "/pages/order/aftersale/apply", meta: { auth: true, title: "申请售后" } }, "/pages/order/aftersale/return-delivery": { path: "/pages/order/aftersale/return-delivery", meta: { auth: true, title: "退货物流" } }, "/pages/order/aftersale/list": { path: "/pages/order/aftersale/list", meta: { auth: true, sync: true, title: "售后订单", group: "订单中心" } }, "/pages/order/aftersale/detail": { path: "/pages/order/aftersale/detail", meta: { auth: true, title: "售后详情" } }, "/pages/order/aftersale/log": { path: "/pages/order/aftersale/log", meta: { auth: true, title: "售后进度" } }, "/pages/order/express/log": { path: "/pages/order/express/log", meta: { auth: true, title: "物流轨迹" } }, "/pages/user/info": { path: "/pages/user/info", meta: { auth: true, sync: true, title: "用户信息", group: "用户中心" } }, "/pages/user/goods-collect": { path: "/pages/user/goods-collect", meta: { auth: true, sync: true, title: "商品收藏", group: "用户中心" } }, "/pages/user/goods-log": { path: "/pages/user/goods-log", meta: { auth: true, sync: true, title: "浏览记录", group: "用户中心" } }, "/pages/user/address/list": { path: "/pages/user/address/list", meta: { auth: true, sync: true, title: "地址管理", group: "用户中心" } }, "/pages/user/address/edit": { path: "/pages/user/address/edit", meta: { auth: true, title: "编辑地址" } }, "/pages/user/goods_details_store/index": { path: "/pages/user/goods_details_store/index", meta: { auth: true, sync: true, title: "地址管理", group: "用户中心" } }, "/pages/user/wallet/money": { path: "/pages/user/wallet/money", meta: { auth: true, sync: true, title: "用户余额", group: "用户中心" } }, "/pages/user/wallet/score": { path: "/pages/user/wallet/score", meta: { auth: true, sync: true, title: "用户积分", group: "用户中心" } }, "/pages/commission/index": { path: "/pages/commission/index", meta: { auth: true, sync: true, title: "分销中心", group: "分销商城" } }, "/pages/commission/wallet": { path: "/pages/commission/wallet", meta: { auth: true, sync: true, title: "用户佣金", group: "分销中心" } }, "/pages/commission/goods": { path: "/pages/commission/goods", meta: { auth: true, sync: true, title: "推广商品", group: "分销商城" } }, "/pages/commission/order": { path: "/pages/commission/order", meta: { auth: true, sync: true, title: "分销订单", group: "分销商城" } }, "/pages/commission/team": { path: "/pages/commission/team", meta: { auth: true, sync: true, title: "我的团队", group: "分销商城" } }, "/pages/commission/promoter": { path: "/pages/commission/promoter", meta: { auth: true, sync: true, title: "推广人排行榜", group: "分销商城" } }, "/pages/commission/commission-ranking": { path: "/pages/commission/commission-ranking", meta: { auth: true, sync: true, title: "佣金排行榜", group: "分销商城" } }, "/pages/commission/withdraw": { path: "/pages/commission/withdraw", meta: { auth: true, sync: true, title: "申请提现", group: "分销商城" } }, "/pages/app/sign": { path: "/pages/app/sign", meta: { auth: true, sync: true, title: "签到中心", group: "应用" } }, "/pages/public/setting": { path: "/pages/public/setting", meta: { sync: true, title: "系统设置", group: "通用" } }, "/pages/public/richtext": { path: "/pages/public/richtext", meta: { sync: true, title: "富文本", group: "通用" } }, "/pages/public/faq": { path: "/pages/public/faq", meta: { sync: true, title: "常见问题", group: "通用" } }, "/pages/public/error": { path: "/pages/public/error" }, "/pages/public/webview": { path: "/pages/public/webview" }, "/pages/coupon/list": { path: "/pages/coupon/list", meta: { sync: true, title: "领券中心", group: "优惠券" } }, "/pages/coupon/detail": { path: "/pages/coupon/detail", meta: { auth: false, sync: true, title: "优惠券详情", group: "优惠券" } }, "/pages/chat/index": { path: "/pages/chat/index", meta: { auth: true, sync: true, title: "客服", group: "客服" } }, "/pages/pay/index": { path: "/pages/pay/index" }, "/pages/pay/result": { path: "/pages/pay/result" }, "/pages/pay/recharge": { path: "/pages/pay/recharge", meta: { auth: true, sync: true, title: "充值余额", group: "支付" } }, "/pages/pay/recharge-log": { path: "/pages/pay/recharge-log", meta: { auth: true, sync: true, title: "充值记录", group: "支付" } }, "/pages/activity/groupon/detail": { path: "/pages/activity/groupon/detail" }, "/pages/activity/groupon/order": { path: "/pages/activity/groupon/order", meta: { auth: true, sync: true, title: "拼团订单", group: "营销活动" } }, "/pages/activity/index": { path: "/pages/activity/index", meta: { sync: true, title: "营销商品", group: "营销活动" } }, "/pages/activity/groupon/list": { path: "/pages/activity/groupon/list", meta: { sync: true, title: "拼团活动", group: "营销活动" } }, "/pages/activity/seckill/list": { path: "/pages/activity/seckill/list", meta: { sync: true, title: "秒杀活动", group: "营销活动" } }, "/pages/activity/point/list": { path: "/pages/activity/point/list", meta: { sync: true, title: "积分商城", group: "营销活动" } } };
var define_TABBAR_default = ["/pages/index/index", "/pages/index/category", "/pages/index/cart", "/pages/index/user"];
const _go = (path, params = {}, options = {
  redirect: false
}) => {
  var _a;
  let page = "";
  let query = "";
  let url = "";
  if (common_vendor.isString(path)) {
    if (common_vendor.startsWith(path, "http")) {
      page = `/pages/public/webview`;
      query = `url=${encodeURIComponent(path)}`;
    } else if (common_vendor.startsWith(path, "action:")) {
      handleAction(path);
      return;
    } else {
      [page, query] = path.split("?");
    }
    if (!common_vendor.isEmpty(params)) {
      let query2 = paramsToQuery(params);
      if (common_vendor.isEmpty(query)) {
        query = query2;
      } else {
        query += "&" + query2;
      }
    }
  }
  if (common_vendor.isObject(path)) {
    page = path.url;
    if (!common_vendor.isNil(path.params)) {
      query = paramsToQuery(path.params);
    }
  }
  const nextRoute = define_ROUTES_MAP_default[page];
  if (!nextRoute) {
    common_vendor.index.__f__("log", "at sheep/router/index.js:56", `%c跳转路径参数错误<${page || "EMPTY"}>`, "color:red;background:yellow");
    return;
  }
  if (((_a = nextRoute.meta) == null ? void 0 : _a.auth) && !sheep_store_index.$store("user").isLogin) {
    sheep_hooks_useModal.showAuthModal();
    return;
  }
  url = page;
  if (!common_vendor.isEmpty(query)) {
    url += `?${query}`;
  }
  if (define_TABBAR_default.includes(page)) {
    const params2 = queryToParams(query);
    sheep_store_index.$store("app").setParamsForTabbar(params2);
    common_vendor.index.switchTab({
      url: page
    });
    return;
  }
  if (options.redirect) {
    common_vendor.index.redirectTo({
      url
    });
    return;
  }
  common_vendor.index.navigateTo({
    url
  });
};
function go(...args) {
  sheep_helper_throttle.throttle(() => {
    _go(...args);
  });
}
function paramsToQuery(params) {
  if (common_vendor.isEmpty(params)) {
    return "";
  }
  let query = [];
  for (let key in params) {
    query.push(key + "=" + params[key]);
  }
  return query.join("&");
}
function queryToParams(query) {
  if (common_vendor.isEmpty(query)) {
    return {};
  }
  let params = {};
  let pairs = query.split("&");
  for (let i = 0; i < pairs.length; i++) {
    let pair = pairs[i].split("=");
    params[decodeURIComponent(pair[0])] = decodeURIComponent(pair[1] || "");
  }
  return params;
}
function back() {
  common_vendor.index.navigateBack();
}
function redirect(path, params = {}) {
  go(path, params, {
    redirect: true
  });
}
function hasHistory() {
  const pages = getCurrentPages();
  if (pages.length > 1) {
    return true;
  }
  return false;
}
function getCurrentRoute(field = "") {
  let currentPage = getCurrentPage();
  currentPage.$page["route"] = currentPage.route;
  currentPage.$page["options"] = currentPage.options;
  if (field !== "") {
    return currentPage.$page[field];
  } else {
    return currentPage.$page;
  }
}
function getCurrentPage() {
  let pages = getCurrentPages();
  return pages[pages.length - 1];
}
function handleAction(path) {
  const action = path.split(":");
  switch (action[1]) {
    case "showShareModal":
      sheep_hooks_useModal.showShareModal();
      break;
  }
}
function error(errCode, errMsg = "") {
  redirect("/pages/public/error", {
    errCode,
    errMsg
  });
}
const $router = {
  go,
  back,
  hasHistory,
  redirect,
  getCurrentPage,
  getCurrentRoute,
  error
};
exports.$router = $router;
//# sourceMappingURL=../../../.sourcemap/mp-weixin/sheep/router/index.js.map
