"use strict";
const sheep_store_index = require("../store/index.js");
const sheep_helper_index = require("../helper/index.js");
const common_vendor = require("../../common/vendor.js");
const sheep_helper_test = require("../helper/test.js");
const sheep_api_member_auth = require("../api/member/auth.js");
function showAuthModal(type = "accountLogin") {
  const modal = sheep_store_index.$store("modal");
  modal.$patch((state) => {
    state.auth = type;
  });
}
function closeAuthModal() {
  sheep_store_index.$store("modal").$patch((state) => {
    state.auth = "";
  });
}
function showShareModal() {
  sheep_store_index.$store("modal").$patch((state) => {
    state.share = true;
  });
}
function closeShareModal() {
  sheep_store_index.$store("modal").$patch((state) => {
    state.share = false;
  });
}
function showMenuTools() {
  sheep_store_index.$store("modal").$patch((state) => {
    state.menu = true;
  });
}
function closeMenuTools() {
  sheep_store_index.$store("modal").$patch((state) => {
    state.menu = false;
  });
}
function getSmsCode(event, mobile) {
  const modalStore = sheep_store_index.$store("modal");
  const lastSendTimer = modalStore.lastTimer[event];
  if (typeof lastSendTimer === "undefined") {
    sheep_helper_index.$helper.toast("短信发送事件错误");
    return;
  }
  const duration = common_vendor.dayjs().unix() - lastSendTimer;
  const canSend = duration >= 60;
  if (!canSend) {
    sheep_helper_index.$helper.toast("请稍后再试");
    return;
  }
  if (mobile && !sheep_helper_test.test.mobile(mobile)) {
    sheep_helper_index.$helper.toast("手机号码格式不正确");
    return;
  }
  let scene = -1;
  switch (event) {
    case "resetPassword":
      scene = 4;
      break;
    case "changePassword":
      scene = 3;
      break;
    case "changeMobile":
      scene = 2;
      break;
    case "smsLogin":
      scene = 1;
      break;
  }
  sheep_api_member_auth.AuthUtil.sendSmsCode(mobile, scene).then((res) => {
    if (res.code === 0) {
      modalStore.$patch((state) => {
        state.lastTimer[event] = common_vendor.dayjs().unix();
      });
    }
  });
}
function getSmsTimer(event, mobile = "") {
  const modalStore = sheep_store_index.$store("modal");
  const lastSendTimer = modalStore.lastTimer[event];
  if (typeof lastSendTimer === "undefined") {
    sheep_helper_index.$helper.toast("短信发送事件错误");
    return;
  }
  const duration = common_vendor.ref(common_vendor.dayjs().unix() - lastSendTimer - 60);
  const canSend = duration.value >= 0;
  if (canSend) {
    return "获取验证码";
  }
  if (!canSend) {
    setTimeout(() => {
      duration.value++;
    }, 1e3);
    return -duration.value.toString() + " 秒";
  }
}
function saveAdvHistory(adv) {
  const modal = sheep_store_index.$store("modal");
  modal.$patch((state) => {
    if (!state.advHistory.includes(adv.imgUrl)) {
      state.advHistory.push(adv.imgUrl);
    }
  });
}
exports.closeAuthModal = closeAuthModal;
exports.closeMenuTools = closeMenuTools;
exports.closeShareModal = closeShareModal;
exports.getSmsCode = getSmsCode;
exports.getSmsTimer = getSmsTimer;
exports.saveAdvHistory = saveAdvHistory;
exports.showAuthModal = showAuthModal;
exports.showMenuTools = showMenuTools;
exports.showShareModal = showShareModal;
//# sourceMappingURL=../../../.sourcemap/mp-weixin/sheep/hooks/useModal.js.map
