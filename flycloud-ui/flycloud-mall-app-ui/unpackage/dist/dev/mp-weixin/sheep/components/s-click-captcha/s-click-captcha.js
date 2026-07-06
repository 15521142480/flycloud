"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_index = require("../../index.js");
const sheep_api_auth_captcha = require("../../api/auth/captcha.js");
if (!Array) {
  const _easycom_su_popup2 = common_vendor.resolveComponent("su-popup");
  _easycom_su_popup2();
}
const _easycom_su_popup = () => "../../ui/su-popup/su-popup.js";
if (!Math) {
  _easycom_su_popup();
}
const _sfc_main = {
  __name: "s-click-captcha",
  props: {
    show: {
      type: Boolean,
      default: false
    }
  },
  emits: ["update:show", "success", "close"],
  setup(__props, { emit: __emit }) {
    const props = __props;
    const emits = __emit;
    const instance = common_vendor.getCurrentInstance();
    const state = common_vendor.reactive({
      loading: false,
      challenge: null,
      points: []
    });
    const targetWords = common_vendor.computed(() => {
      var _a, _b;
      return ((_b = (_a = state.challenge) == null ? void 0 : _a.targetText) == null ? void 0 : _b.split("")) || [];
    });
    common_vendor.watch(
      () => props.show,
      async (value) => {
        if (value) {
          await loadCaptcha();
        }
      }
    );
    async function loadCaptcha() {
      state.loading = true;
      state.points = [];
      try {
        const res = await sheep_api_auth_captcha.CaptchaApi.getImageTextClickCaptcha();
        if ((res == null ? void 0 : res.code) === 0) {
          state.challenge = res.data;
          return;
        }
        sheep_index.sheep.$helper.toast((res == null ? void 0 : res.msg) || "验证码加载失败");
        state.challenge = null;
      } finally {
        state.loading = false;
      }
    }
    async function handleImageTap(event) {
      if (!state.challenge || state.loading || state.points.length >= 3) {
        return;
      }
      const rect = await getImageRect();
      const clientPoint = getClientPoint(event);
      if (!rect || !clientPoint) {
        sheep_index.sheep.$helper.toast("验证码定位失败，请重试");
        await loadCaptcha();
        return;
      }
      const offsetX = Math.max(0, Math.min(clientPoint.clientX - rect.left, rect.width));
      const offsetY = Math.max(0, Math.min(clientPoint.clientY - rect.top, rect.height));
      const x = Math.round(offsetX / rect.width * sheep_api_auth_captcha.CaptchaApi.imageWidth);
      const y = Math.round(offsetY / rect.height * sheep_api_auth_captcha.CaptchaApi.imageHeight);
      state.points.push({
        x,
        y,
        displayX: offsetX / rect.width * 100,
        displayY: offsetY / rect.height * 100
      });
      if (state.points.length === 3) {
        await verifyCaptcha();
      }
    }
    async function verifyCaptcha() {
      var _a;
      state.loading = true;
      try {
        const res = await sheep_api_auth_captcha.CaptchaApi.checkImageTextClickCaptcha({
          captchaId: state.challenge.captchaId,
          points: state.points.map(({ x, y }) => ({ x, y }))
        });
        if ((res == null ? void 0 : res.code) === 0 && ((_a = res.data) == null ? void 0 : _a.captchaVerification)) {
          emits("success", res.data.captchaVerification);
          emits("update:show", false);
          resetCaptcha();
          return;
        }
        sheep_index.sheep.$helper.toast((res == null ? void 0 : res.msg) || "验证码错误，请重新验证");
        await loadCaptcha();
      } finally {
        state.loading = false;
      }
    }
    function getImageRect() {
      return new Promise((resolve) => {
        common_vendor.index.createSelectorQuery().in(instance == null ? void 0 : instance.proxy).select(".captcha-image").boundingClientRect((rect) => resolve(rect)).exec();
      });
    }
    function getClientPoint(event) {
      var _a, _b, _c, _d;
      const touch = ((_a = event.changedTouches) == null ? void 0 : _a[0]) || ((_b = event.touches) == null ? void 0 : _b[0]);
      if (touch) {
        return { clientX: touch.clientX, clientY: touch.clientY };
      }
      if (((_c = event.detail) == null ? void 0 : _c.x) !== void 0 && ((_d = event.detail) == null ? void 0 : _d.y) !== void 0) {
        return { clientX: event.detail.x, clientY: event.detail.y };
      }
      if (event.clientX !== void 0 && event.clientY !== void 0) {
        return { clientX: event.clientX, clientY: event.clientY };
      }
      return null;
    }
    function handleClose() {
      emits("update:show", false);
      emits("close");
      common_vendor.nextTick$1(resetCaptcha);
    }
    function resetCaptcha() {
      state.challenge = null;
      state.points = [];
    }
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.o(handleClose, "6e"),
        b: common_vendor.f(targetWords.value, (word, index, i0) => {
          return {
            a: common_vendor.t(word),
            b: `${word}-${index}`
          };
        }),
        c: state.loading,
        d: common_vendor.o(loadCaptcha, "7c"),
        e: state.challenge
      }, state.challenge ? {
        f: state.challenge.imageBase64,
        g: common_vendor.o(handleImageTap, "4a")
      } : {}, {
        h: common_vendor.f(state.points, (point, index, i0) => {
          return {
            a: common_vendor.t(index + 1),
            b: `${point.x}-${point.y}-${index}`,
            c: `${point.displayX}%`,
            d: `${point.displayY}%`
          };
        }),
        i: state.loading ? 1 : "",
        j: common_vendor.o(handleClose, "0c"),
        k: common_vendor.p({
          show: __props.show,
          type: "center",
          round: "10",
          isMaskClick: false
        })
      });
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-4a220b27"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/components/s-click-captcha/s-click-captcha.js.map
