"use strict";
const common_vendor = require("../../common/vendor.js");
const sheep_store_index = require("../store/index.js");
const sheep_platform_index = require("./index.js");
const sheep_router_index = require("../router/index.js");
const sheep_url_index = require("../url/index.js");
const sheep_api_trade_brokerage = require("../api/trade/brokerage.js");
const sheep_helper_const = require("../helper/const.js");
const platformMap = ["H5", "WechatOfficialAccount", "WechatMiniProgram", "App"];
const fromMap = ["forward", "poster", "link"];
const getShareInfo = (scene = {
  title: "",
  // 自定义分享标题
  desc: "",
  // 自定义描述
  image: "",
  // 自定义分享图片
  params: {}
  // 自定义分享参数
}, poster = {
  // 自定义海报数据
  type: "user"
}) => {
  const shareInfo = {
    title: "",
    // 分享标题
    desc: "",
    // 描述
    image: "",
    // 分享图片
    path: "",
    // 分享页面+参数
    link: "",
    // 分享Url+参数
    query: "",
    // 分享参数
    poster,
    // 海报所需数据
    forward: {}
    // 转发所需参数
  };
  shareInfo.title = scene.title;
  shareInfo.image = sheep_url_index.$url.cdn(scene.image);
  shareInfo.desc = scene.desc;
  const app = sheep_store_index.$store("app");
  const shareConfig = app.platform.share;
  const query = buildSpmQuery(scene.params);
  shareInfo.query = query;
  shareInfo.link = buildSpmLink(query, shareConfig.linkAddress);
  shareInfo.path = buildSpmPath();
  if (shareConfig.methods.includes("forward")) {
    shareInfo.forward.path = buildSpmPath(query);
  }
  return shareInfo;
};
const buildSpmQuery = (params) => {
  const user = sheep_store_index.$store("user");
  let shareId = "0";
  if (typeof params.shareId === "undefined") {
    if (user.isLogin) {
      shareId = user.userInfo.id;
    }
  }
  let page = sheep_helper_const.SharePageEnum.HOME.value;
  if (typeof params.page !== "undefined") {
    page = params.page;
  }
  let query = "0";
  if (typeof params.query !== "undefined") {
    query = params.query;
  }
  let platform = platformMap.indexOf(sheep_platform_index._platform.name) + 1;
  let from = "1";
  if (typeof params.from !== "undefined") {
    from = platformMap.indexOf(params.from) + 1;
  }
  return `spm=${shareId}.${page}.${query}.${platform}.${from}`;
};
const buildSpmPath = (query) => {
  return typeof query === "undefined" ? `pages/index/index` : `pages/index/index?${query}`;
};
const buildSpmLink = (query, linkAddress = "") => {
  return `${linkAddress}?${query}`;
};
const decryptSpm = async (spm) => {
  const user = sheep_store_index.$store("user");
  let shareParamsArray = spm.split(".");
  let shareParams = {
    spm,
    shareId: 0,
    page: "",
    query: {},
    platform: "",
    from: ""
  };
  shareParams.shareId = shareParamsArray[0];
  switch (shareParamsArray[1]) {
    case sheep_helper_const.SharePageEnum.HOME.value:
      shareParams.page = sheep_helper_const.SharePageEnum.HOME.page;
      break;
    case sheep_helper_const.SharePageEnum.GOODS.value:
      shareParams.page = sheep_helper_const.SharePageEnum.GOODS.page;
      shareParams.query = {
        id: shareParamsArray[2]
        // 设置活动编号
      };
      break;
    case sheep_helper_const.SharePageEnum.GROUPON.value:
      shareParams.page = sheep_helper_const.SharePageEnum.GROUPON.page;
      shareParams.query = {
        id: shareParamsArray[2]
        // 设置活动编号
      };
      break;
    case sheep_helper_const.SharePageEnum.SECKILL.value:
      shareParams.page = sheep_helper_const.SharePageEnum.SECKILL.page;
      shareParams.query = {
        id: shareParamsArray[2]
        // 设置活动编号
      };
      break;
    case sheep_helper_const.SharePageEnum.GROUPON_DETAIL.value:
      shareParams.page = sheep_helper_const.SharePageEnum.GROUPON_DETAIL.page;
      shareParams.query = {
        id: shareParamsArray[2]
        // 设置活动编号
      };
      break;
    case sheep_helper_const.SharePageEnum.POINT.value:
      shareParams.page = sheep_helper_const.SharePageEnum.POINT.page;
      shareParams.query = {
        id: shareParamsArray[2]
        // 设置活动编号
      };
      break;
  }
  shareParams.platform = platformMap[shareParamsArray[3] - 1];
  shareParams.from = fromMap[shareParamsArray[4] - 1];
  if (shareParams.shareId !== 0) {
    common_vendor.index.setStorageSync("shareId", shareParams.shareId);
    if (!!user.isLogin) {
      await bindBrokerageUser(shareParams.shareId);
    }
  }
  if (shareParams.page !== sheep_helper_const.SharePageEnum.HOME.page) {
    sheep_router_index.$router.go(shareParams.page, shareParams.query);
  }
  return shareParams;
};
const bindBrokerageUser = async (val = void 0) => {
  try {
    const shareId = val || common_vendor.index.getStorageSync("shareId");
    if (!shareId) {
      return;
    }
    const { data } = await sheep_api_trade_brokerage.BrokerageApi.bindBrokerageUser({ bindUserId: shareId });
    if (data) {
      common_vendor.index.removeStorageSync("shareId");
    }
  } catch (e) {
    common_vendor.index.__f__("error", "at sheep/platform/share.js:195", e);
  }
};
const updateShareInfo = (shareInfo) => {
};
const $share = {
  getShareInfo,
  updateShareInfo,
  decryptSpm,
  bindBrokerageUser
};
exports.$share = $share;
//# sourceMappingURL=../../../.sourcemap/mp-weixin/sheep/platform/share.js.map
