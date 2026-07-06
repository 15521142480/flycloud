"use strict";
const common_vendor = require("../../common/vendor.js");
const sheep_platform_provider_wechat_index = require("./provider/wechat/index.js");
const sheep_platform_provider_alipay_index = require("./provider/alipay/index.js");
const sheep_platform_provider_apple_index = require("./provider/apple/index.js");
const sheep_platform_share = require("./share.js");
const sheep_platform_pay = require("./pay.js");
const device = common_vendor.index.getWindowInfo();
const os = common_vendor.index.getDeviceInfo().platform;
let name = "";
let provider = "";
let platform = "";
let isWechatInstalled = true;
name = "WechatMiniProgram";
platform = "miniProgram";
provider = "wechat";
if (common_vendor.isEmpty(name)) {
  common_vendor.index.showToast({
    title: "暂不支持该平台",
    icon: "none"
  });
}
const load = () => {
  if (provider === "wechat") {
    sheep_platform_provider_wechat_index.wechat.load();
  } else if (provider === "alipay") {
    sheep_platform_provider_alipay_index.alipay.load();
  }
};
const useProvider = (_provider = "") => {
  if (_provider === "")
    _provider = provider;
  if (_provider === "wechat")
    return sheep_platform_provider_wechat_index.wechat;
  if (_provider === "apple")
    return sheep_platform_provider_apple_index.apple;
  if (_provider === "alipay")
    return sheep_platform_provider_alipay_index.alipay;
};
const pay = (payment, orderType, orderSN) => {
  return new sheep_platform_pay.SheepPay(payment, orderType, orderSN);
};
const checkUpdate = (silence = false) => {
  useProvider().checkUpdate(silence);
};
async function checkNetwork() {
  const networkStatus = await common_vendor.index.getNetworkType();
  if (networkStatus.networkType == "none") {
    return Promise.resolve(false);
  }
  return Promise.resolve(true);
}
const getCapsule = () => {
  let capsule2 = common_vendor.index.getMenuButtonBoundingClientRect();
  if (!capsule2) {
    capsule2 = {
      bottom: 56,
      height: 32,
      left: 278,
      right: 365,
      top: 24,
      width: 87
    };
  }
  return capsule2;
};
const capsule = getCapsule();
const getNavBar = () => {
  return device.statusBarHeight + 44;
};
const navbar = getNavBar();
function getLandingPage() {
  let page = "";
  return page;
}
const landingPage = getLandingPage();
const _platform = {
  name,
  device,
  os,
  provider,
  platform,
  useProvider,
  checkUpdate,
  checkNetwork,
  pay,
  share: sheep_platform_share.$share,
  load,
  capsule,
  navbar,
  landingPage,
  isWechatInstalled
};
exports._platform = _platform;
//# sourceMappingURL=../../../.sourcemap/mp-weixin/sheep/platform/index.js.map
