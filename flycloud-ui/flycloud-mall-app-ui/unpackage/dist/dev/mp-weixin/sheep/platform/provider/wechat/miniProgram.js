"use strict";
const common_vendor = require("../../../../common/vendor.js");
const sheep_api_member_auth = require("../../../api/member/auth.js");
const sheep_api_member_social = require("../../../api/member/social.js");
const sheep_api_member_user = require("../../../api/member/user.js");
const sheep_index = require("../../../index.js");
const socialType = 34;
let subscribeEventList = [];
function load() {
  checkUpdate();
  getSubscribeTemplate();
}
const login = async () => {
  return new Promise(async (resolve, reject) => {
    const codeResult = await common_vendor.index.login();
    if (codeResult.errMsg !== "login:ok") {
      return resolve(false);
    }
    const loginResult = await sheep_api_member_auth.AuthUtil.socialLogin(socialType, codeResult.code, "default");
    if (loginResult.code === 0) {
      setOpenid(loginResult.data.openid);
      return resolve(true);
    } else {
      return resolve(false);
    }
  });
};
const mobileLogin = async (e) => {
  return new Promise(async (resolve, reject) => {
    if (e.errMsg !== "getPhoneNumber:ok") {
      return resolve(false);
    }
    const codeResult = await common_vendor.index.login();
    if (codeResult.errMsg !== "login:ok") {
      return resolve(false);
    }
    const loginResult = await sheep_api_member_auth.AuthUtil.weixinMiniAppLogin(e.code, codeResult.code, "default");
    if (loginResult.code === 0) {
      setOpenid(loginResult.data.openid);
      return resolve(true);
    } else {
      return resolve(false);
    }
  });
};
const bind = () => {
  return new Promise(async (resolve, reject) => {
    const codeResult = await common_vendor.index.login();
    if (codeResult.errMsg !== "login:ok") {
      return resolve(false);
    }
    const bindResult = await sheep_api_member_social.SocialApi.socialBind(socialType, codeResult.code, "default");
    if (bindResult.code === 0) {
      setOpenid(bindResult.data);
      return resolve(true);
    } else {
      return resolve(false);
    }
  });
};
const unbind = async (openid) => {
  const { code } = await sheep_api_member_social.SocialApi.socialUnbind(socialType, openid);
  return code === 0;
};
const bindUserPhoneNumber = (e) => {
  return new Promise(async (resolve, reject) => {
    const { code } = await sheep_api_member_user.UserApi.updateUserMobileByWeixin(e.code);
    if (code === 0) {
      resolve(true);
    }
    resolve(false);
  });
};
function setOpenid(openid) {
  common_vendor.index.setStorageSync("openid", openid);
}
async function getOpenid(force = false) {
  let openid = common_vendor.index.getStorageSync("openid");
  if (!openid && force) {
    const info = await getInfo();
    if (info && info.openid) {
      openid = info.openid;
      setOpenid(openid);
    }
  }
  return openid;
}
async function getInfo() {
  const { code, data } = await sheep_api_member_social.SocialApi.getSocialUser(socialType);
  if (code !== 0) {
    return void 0;
  }
  return data;
}
const checkUpdate = async (silence = true) => {
  if (common_vendor.index.canIUse("getUpdateManager")) {
    const updateManager = common_vendor.index.getUpdateManager();
    updateManager.onCheckForUpdate(function(res) {
      if (res.hasUpdate) {
        updateManager.onUpdateReady(function() {
          common_vendor.index.showModal({
            title: "更新提示",
            content: "新版本已经准备好，是否重启应用？",
            success: function(res2) {
              if (res2.confirm) {
                updateManager.applyUpdate();
              }
            }
          });
        });
        updateManager.onUpdateFailed(function() {
        });
      } else {
        if (!silence) {
          common_vendor.index.showModal({
            title: "当前为最新版本",
            showCancel: false
          });
        }
      }
    });
  }
};
async function getSubscribeTemplate() {
  const { code, data } = await sheep_api_member_social.SocialApi.getSubscribeTemplateList();
  if (code === 0) {
    subscribeEventList = data;
  }
}
function subscribeMessage(event, callback = void 0) {
  let tmplIds = [];
  if (typeof event === "string") {
    const temp = subscribeEventList.find((item) => item.title.includes(event));
    if (temp) {
      tmplIds.push(temp.id);
    }
  }
  if (typeof event === "object") {
    event.forEach((e) => {
      const temp = subscribeEventList.find((item) => item.title.includes(e));
      if (temp) {
        tmplIds.push(temp.id);
      }
    });
  }
  if (tmplIds.length === 0)
    return;
  common_vendor.index.requestSubscribeMessage({
    tmplIds,
    success: () => {
      callback && callback();
    },
    fail: (err) => {
      common_vendor.index.__f__("log", "at sheep/platform/provider/wechat/miniProgram.js:198", err);
    }
  });
}
function requestMerchantTransfer(mchId, packageInfo, successCallback, failCallback) {
  if (!common_vendor.wx$1.canIUse("requestMerchantTransfer")) {
    common_vendor.wx$1.showModal({
      content: "你的微信版本过低，请更新至最新版本。",
      showCancel: false
    });
    return;
  }
  common_vendor.wx$1.requestMerchantTransfer({
    mchId,
    appId: common_vendor.wx$1.getAccountInfoSync().miniProgram.appId,
    package: packageInfo,
    success: (res) => {
      common_vendor.index.__f__("log", "at sheep/platform/provider/wechat/miniProgram.js:218", "success:", res);
      successCallback && successCallback(res);
    },
    fail: (res) => {
      common_vendor.index.__f__("log", "at sheep/platform/provider/wechat/miniProgram.js:222", "fail:", res);
      sheep_index.sheep.$helper.toast(res.errMsg);
      failCallback && failCallback(res);
    }
  });
}
const service = {
  load,
  login,
  bind,
  unbind,
  bindUserPhoneNumber,
  mobileLogin,
  getInfo,
  getOpenid,
  subscribeMessage,
  checkUpdate,
  requestMerchantTransfer
};
exports.service = service;
//# sourceMappingURL=../../../../../.sourcemap/mp-weixin/sheep/platform/provider/wechat/miniProgram.js.map
