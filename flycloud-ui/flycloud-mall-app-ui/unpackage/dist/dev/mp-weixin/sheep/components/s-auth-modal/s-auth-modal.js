"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_index = require("../../index.js");
const sheep_hooks_useModal = require("../../hooks/useModal.js");
if (!Array) {
  const _easycom_su_popup2 = common_vendor.resolveComponent("su-popup");
  _easycom_su_popup2();
}
const _easycom_su_popup = () => "../../ui/su-popup/su-popup.js";
if (!Math) {
  (accountLogin + smsLogin + resetPassword + changeMobile + changePassword + mpAuthorization + _easycom_su_popup)();
}
const accountLogin = () => "./components/account-login.js";
const smsLogin = () => "./components/sms-login.js";
const resetPassword = () => "./components/reset-password.js";
const changeMobile = () => "./components/change-mobile.js";
const changePassword = () => "./components/change-password.js";
const mpAuthorization = () => "./components/mp-authorization.js";
const _sfc_main = {
  __name: "s-auth-modal",
  setup(__props) {
    const modalStore = sheep_index.sheep.$store("modal");
    const authType = common_vendor.computed(() => modalStore.auth);
    const state = common_vendor.reactive({
      protocol: null
      // null表示未选择，true表示同意，false表示拒绝
    });
    const currentProtocol = common_vendor.ref(false);
    function onAgree() {
      state.protocol = true;
    }
    function onRefuse() {
      state.protocol = false;
    }
    function onProtocol(title) {
      sheep_hooks_useModal.closeAuthModal();
      sheep_index.sheep.$router.go("/pages/public/richtext", {
        title
      });
    }
    function onConfirm(e) {
      currentProtocol.value = e;
      setTimeout(() => {
        currentProtocol.value = false;
      }, 1e3);
    }
    const thirdLogin = async (provider) => {
      if (state.protocol !== true) {
        currentProtocol.value = true;
        setTimeout(() => {
          currentProtocol.value = false;
        }, 1e3);
        if (state.protocol === false) {
          sheep_index.sheep.$helper.toast("您已拒绝协议，无法继续登录");
        } else {
          sheep_index.sheep.$helper.toast("请选择是否同意协议");
        }
        return;
      }
      const loginRes = await sheep_index.sheep.$platform.useProvider(provider).login();
      if (loginRes) {
        const userInfo = await sheep_index.sheep.$store("user").getInfo();
        sheep_hooks_useModal.closeAuthModal();
        if (userInfo.avatar && userInfo.nickname) {
          return;
        }
        sheep_hooks_useModal.showAuthModal("mpAuthorization");
      }
    };
    const getPhoneNumber = async (e) => {
      if (e.detail.errMsg !== "getPhoneNumber:ok") {
        sheep_index.sheep.$helper.toast("快捷登录失败");
        return;
      }
      let result = await sheep_index.sheep.$platform.useProvider().mobileLogin(e.detail);
      if (result) {
        sheep_hooks_useModal.closeAuthModal();
      }
    };
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: authType.value === "accountLogin"
      }, authType.value === "accountLogin" ? {
        b: common_vendor.o(onConfirm, "70"),
        c: common_vendor.p({
          agreeStatus: state.protocol
        })
      } : {}, {
        d: authType.value === "smsLogin"
      }, authType.value === "smsLogin" ? {
        e: common_vendor.o(onConfirm, "e7"),
        f: common_vendor.p({
          agreeStatus: state.protocol
        })
      } : {}, {
        g: authType.value === "resetPassword"
      }, authType.value === "resetPassword" ? {} : {}, {
        h: authType.value === "changeMobile"
      }, authType.value === "changeMobile" ? {} : {}, {
        i: authType.value === "changePassword"
      }, authType.value === "changePassword" ? {} : {}, {
        j: authType.value === "mpAuthorization"
      }, authType.value === "mpAuthorization" ? {} : {}, {
        k: ["accountLogin", "smsLogin"].includes(authType.value)
      }, ["accountLogin", "smsLogin"].includes(authType.value) ? common_vendor.e({
        l: common_vendor.unref(sheep_index.sheep).$platform.name === "WechatMiniProgram"
      }, common_vendor.unref(sheep_index.sheep).$platform.name === "WechatMiniProgram" ? {
        m: common_vendor.o(getPhoneNumber, "86")
      } : {}, {
        n: ["WechatOfficialAccount", "WechatMiniProgram", "App"].includes(common_vendor.unref(sheep_index.sheep).$platform.name) && common_vendor.unref(sheep_index.sheep).$platform.isWechatInstalled
      }, ["WechatOfficialAccount", "WechatMiniProgram", "App"].includes(common_vendor.unref(sheep_index.sheep).$platform.name) && common_vendor.unref(sheep_index.sheep).$platform.isWechatInstalled ? {
        o: common_vendor.unref(sheep_index.sheep).$url.static("/static/img/shop/platform/wechat.png"),
        p: common_vendor.o(($event) => thirdLogin("wechat"), "c6")
      } : {}, {
        q: common_vendor.unref(sheep_index.sheep).$platform.os === "ios" && common_vendor.unref(sheep_index.sheep).$platform.name === "App"
      }, common_vendor.unref(sheep_index.sheep).$platform.os === "ios" && common_vendor.unref(sheep_index.sheep).$platform.name === "App" ? {
        r: common_vendor.unref(sheep_index.sheep).$url.static("/static/img/shop/platform/apple.png"),
        s: common_vendor.o(($event) => thirdLogin("apple"), "b2")
      } : {}, {
        t: common_vendor.unref(sheep_index.sheep).$platform.name === "alipayMiniProgram"
      }, common_vendor.unref(sheep_index.sheep).$platform.name === "alipayMiniProgram" ? {
        v: common_vendor.unref(sheep_index.sheep).$url.static("/static/img/shop/pay/alipay.png"),
        w: common_vendor.o(($event) => thirdLogin("alipay"), "46")
      } : {}) : {}, {
        x: ["accountLogin", "smsLogin"].includes(authType.value)
      }, ["accountLogin", "smsLogin"].includes(authType.value) ? {
        y: state.protocol === true,
        z: common_vendor.o(onAgree, "24"),
        A: common_vendor.o(($event) => onProtocol("用户协议"), "66"),
        B: common_vendor.o(($event) => onProtocol("隐私协议"), "92"),
        C: common_vendor.o(onAgree, "4c"),
        D: state.protocol === false,
        E: common_vendor.o(onRefuse, "5a"),
        F: common_vendor.o(($event) => onProtocol("用户协议"), "56"),
        G: common_vendor.o(($event) => onProtocol("隐私协议"), "30"),
        H: common_vendor.o(onRefuse, "ff"),
        I: currentProtocol.value ? 1 : ""
      } : {}, {
        J: common_vendor.o(common_vendor.unref(sheep_hooks_useModal.closeAuthModal), "73"),
        K: common_vendor.p({
          show: authType.value !== "",
          round: "10",
          showClose: true
        })
      });
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-21181321"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/components/s-auth-modal/s-auth-modal.js.map
